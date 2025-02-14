/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.upload;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.ServletInputStreamAdapter;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProgressTracker;
import com.liferay.portal.util.PropsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Brian Myunghun Kim
 * @author Brian Wing Shun Chan
 * @author Harry Mark
 */
public class LiferayInputStream extends ServletInputStreamAdapter {

	public static final String COPY_MULTIPART_STREAM_TO_FILE =
		LiferayInputStream.class.getName() + "#COPY_MULTIPART_STREAM_TO_FILE";

	public static final long THRESHOLD_SIZE = GetterUtil.getLong(
		PropsUtil.get(LiferayInputStream.class.getName() + ".threshold.size"));

	public LiferayInputStream(HttpServletRequest httpServletRequest)
		throws IOException {

		super(httpServletRequest.getInputStream());

		_httpSession = httpServletRequest.getSession();

		long totalSize = httpServletRequest.getContentLength();

		if (totalSize < 0) {
			totalSize = GetterUtil.getLong(
				httpServletRequest.getHeader(HttpHeaders.CONTENT_LENGTH),
				totalSize);
		}

		_totalSize = totalSize;

		boolean createTempFile = GetterUtil.getBoolean(
			httpServletRequest.getAttribute(COPY_MULTIPART_STREAM_TO_FILE),
			Boolean.TRUE);

		if ((_totalSize >= THRESHOLD_SIZE) && createTempFile) {
			_tempFile = FileUtil.createTempFile();
		}
		else {
			_tempFile = null;

			httpServletRequest.removeAttribute(COPY_MULTIPART_STREAM_TO_FILE);
		}
	}

	public void cleanUp() {
		if (_tempFile != null) {
			if (_tempFileOutputStream != null) {
				try {
					_tempFileOutputStream.close();
				}
				catch (IOException ioException) {
					if (_log.isWarnEnabled()) {
						_log.warn(ioException, ioException);
					}
				}
			}

			_tempFile.delete();
		}
	}

	@Override
	public void close() throws IOException {
		super.close();

		if (_tempFileOutputStream != null) {
			_tempFileOutputStream.close();
		}
	}

	public ServletInputStream getCachedInputStream() throws IOException {
		if (_totalSize < THRESHOLD_SIZE) {
			return new ServletInputStreamAdapter(
				new UnsyncByteArrayInputStream(
					_unsyncByteArrayOutputStream.unsafeGetByteArray(), 0,
					_unsyncByteArrayOutputStream.size()));
		}
		else if (_tempFile != null) {
			return new ServletInputStreamAdapter(
				new FileInputStream(_tempFile));
		}

		return this;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int bytesRead = super.read(b, off, len);

		if (bytesRead > 0) {
			_totalRead += bytesRead;
		}
		else {
			return bytesRead;
		}

		int percent = (int)((_totalRead * 100L) / _totalSize);

		if (_log.isDebugEnabled()) {
			_log.debug(
				StringBundler.concat(bytesRead, "/", _totalRead, "=", percent));
		}

		if (_totalSize > 0) {
			if (_totalSize < THRESHOLD_SIZE) {
				_unsyncByteArrayOutputStream.write(b, off, bytesRead);
			}
			else {
				_writeToTempFile(b, off, bytesRead);
			}
		}

		ProgressTracker progressTracker =
			(ProgressTracker)_httpSession.getAttribute(ProgressTracker.PERCENT);

		Integer curPercent = null;

		if (progressTracker != null) {
			curPercent = progressTracker.getPercent();
		}

		if ((curPercent == null) || ((percent - curPercent.intValue()) >= 1)) {
			if (progressTracker == null) {
				progressTracker = new ProgressTracker(StringPool.BLANK);

				progressTracker.initialize(_httpSession);
			}

			progressTracker.setPercent(percent);
		}

		return bytesRead;
	}

	private void _writeToTempFile(byte[] b, int off, int bytesRead)
		throws IOException {

		if ((_tempFile != null) && (bytesRead > 0)) {
			if (_tempFileOutputStream == null) {
				_tempFileOutputStream = new FileOutputStream(_tempFile, true);
			}

			_tempFileOutputStream.write(b, off, bytesRead);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayInputStream.class);

	private final HttpSession _httpSession;
	private final File _tempFile;
	private OutputStream _tempFileOutputStream;
	private long _totalRead;
	private final long _totalSize;
	private final UnsyncByteArrayOutputStream _unsyncByteArrayOutputStream =
		new UnsyncByteArrayOutputStream();

}