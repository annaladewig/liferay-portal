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

package com.liferay.commerce.term.service.http;

import com.liferay.commerce.term.service.CommerceTermEntryRelServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.http.TunnelUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * Provides the HTTP utility for the
 * <code>CommerceTermEntryRelServiceUtil</code> service
 * utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it requires an additional
 * <code>HttpPrincipal</code> parameter.
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <b>tunnel.servlet.hosts.allowed</b> in portal.properties to
 * configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Luca Pellizzon
 * @see CommerceTermEntryRelServiceSoap
 * @generated
 */
public class CommerceTermEntryRelServiceHttp {

	public static com.liferay.commerce.term.model.CommerceTermEntryRel
			addCommerceTermEntryRel(
				HttpPrincipal httpPrincipal, String className, long classPK,
				long commerceTermEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommerceTermEntryRelServiceUtil.class,
				"addCommerceTermEntryRel",
				_addCommerceTermEntryRelParameterTypes0);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, className, classPK, commerceTermEntryId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.commerce.term.model.CommerceTermEntryRel)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static void deleteCommerceTermEntryRel(
			HttpPrincipal httpPrincipal, long commerceTermEntryRelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommerceTermEntryRelServiceUtil.class,
				"deleteCommerceTermEntryRel",
				_deleteCommerceTermEntryRelParameterTypes1);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commerceTermEntryRelId);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static void deleteCommerceTermEntryRels(
			HttpPrincipal httpPrincipal, String className,
			long commerceTermEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommerceTermEntryRelServiceUtil.class,
				"deleteCommerceTermEntryRels",
				_deleteCommerceTermEntryRelsParameterTypes2);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, className, commerceTermEntryId);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static void deleteCommerceTermEntryRelsByCommerceTermEntryId(
			HttpPrincipal httpPrincipal, long commerceTermEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommerceTermEntryRelServiceUtil.class,
				"deleteCommerceTermEntryRelsByCommerceTermEntryId",
				_deleteCommerceTermEntryRelsByCommerceTermEntryIdParameterTypes3);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commerceTermEntryId);

			try {
				TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.commerce.term.model.CommerceTermEntryRel
			fetchCommerceTermEntryRel(
				HttpPrincipal httpPrincipal, String className, long classPK,
				long commerceTermEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommerceTermEntryRelServiceUtil.class,
				"fetchCommerceTermEntryRel",
				_fetchCommerceTermEntryRelParameterTypes4);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, className, classPK, commerceTermEntryId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.commerce.term.model.CommerceTermEntryRel)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static com.liferay.commerce.term.model.CommerceTermEntryRel
			getCommerceTermEntryRel(
				HttpPrincipal httpPrincipal, long commerceTermEntryRelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommerceTermEntryRelServiceUtil.class,
				"getCommerceTermEntryRel",
				_getCommerceTermEntryRelParameterTypes5);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commerceTermEntryRelId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (com.liferay.commerce.term.model.CommerceTermEntryRel)
				returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.commerce.term.model.CommerceTermEntryRel>
				getCommerceTermEntryRels(
					HttpPrincipal httpPrincipal, long commerceTermEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommerceTermEntryRelServiceUtil.class,
				"getCommerceTermEntryRels",
				_getCommerceTermEntryRelsParameterTypes6);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commerceTermEntryId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.commerce.term.model.CommerceTermEntryRel>)
					returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static java.util.List
		<com.liferay.commerce.term.model.CommerceTermEntryRel>
				getCommerceTermEntryRels(
					HttpPrincipal httpPrincipal, long commerceTermEntryId,
					int start, int end,
					com.liferay.portal.kernel.util.OrderByComparator
						<com.liferay.commerce.term.model.CommerceTermEntryRel>
							orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommerceTermEntryRelServiceUtil.class,
				"getCommerceTermEntryRels",
				_getCommerceTermEntryRelsParameterTypes7);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commerceTermEntryId, start, end, orderByComparator);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return (java.util.List
				<com.liferay.commerce.term.model.CommerceTermEntryRel>)
					returnObj;
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	public static int getCommerceTermEntryRelsCount(
			HttpPrincipal httpPrincipal, long commerceTermEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		try {
			MethodKey methodKey = new MethodKey(
				CommerceTermEntryRelServiceUtil.class,
				"getCommerceTermEntryRelsCount",
				_getCommerceTermEntryRelsCountParameterTypes8);

			MethodHandler methodHandler = new MethodHandler(
				methodKey, commerceTermEntryId);

			Object returnObj = null;

			try {
				returnObj = TunnelUtil.invoke(httpPrincipal, methodHandler);
			}
			catch (Exception exception) {
				if (exception instanceof
						com.liferay.portal.kernel.exception.PortalException) {

					throw (com.liferay.portal.kernel.exception.PortalException)
						exception;
				}

				throw new com.liferay.portal.kernel.exception.SystemException(
					exception);
			}

			return ((Integer)returnObj).intValue();
		}
		catch (com.liferay.portal.kernel.exception.SystemException
					systemException) {

			_log.error(systemException, systemException);

			throw systemException;
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		CommerceTermEntryRelServiceHttp.class);

	private static final Class<?>[] _addCommerceTermEntryRelParameterTypes0 =
		new Class[] {String.class, long.class, long.class};
	private static final Class<?>[] _deleteCommerceTermEntryRelParameterTypes1 =
		new Class[] {long.class};
	private static final Class<?>[]
		_deleteCommerceTermEntryRelsParameterTypes2 = new Class[] {
			String.class, long.class
		};
	private static final Class<?>[]
		_deleteCommerceTermEntryRelsByCommerceTermEntryIdParameterTypes3 =
			new Class[] {long.class};
	private static final Class<?>[] _fetchCommerceTermEntryRelParameterTypes4 =
		new Class[] {String.class, long.class, long.class};
	private static final Class<?>[] _getCommerceTermEntryRelParameterTypes5 =
		new Class[] {long.class};
	private static final Class<?>[] _getCommerceTermEntryRelsParameterTypes6 =
		new Class[] {long.class};
	private static final Class<?>[] _getCommerceTermEntryRelsParameterTypes7 =
		new Class[] {
			long.class, int.class, int.class,
			com.liferay.portal.kernel.util.OrderByComparator.class
		};
	private static final Class<?>[]
		_getCommerceTermEntryRelsCountParameterTypes8 = new Class[] {
			long.class
		};

}