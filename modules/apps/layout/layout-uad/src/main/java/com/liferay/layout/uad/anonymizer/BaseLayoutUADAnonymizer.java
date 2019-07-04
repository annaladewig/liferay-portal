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

package com.liferay.layout.uad.anonymizer;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.layout.uad.constants.LayoutUADConstants;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.user.associated.data.anonymizer.DynamicQueryUADAnonymizer;

import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the layout UAD anonymizer.
 *
 * <p>
 * This implementation exists only as a container for the default methods
 * generated by ServiceBuilder. All custom service methods should be put in
 * {@link LayoutUADAnonymizer}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public abstract class BaseLayoutUADAnonymizer
	extends DynamicQueryUADAnonymizer<Layout> {

	@Override
	public void autoAnonymize(Layout layout, long userId, User anonymousUser)
		throws PortalException {

		if (layout.getUserId() == userId) {
			layout.setUserId(anonymousUser.getUserId());
			layout.setUserName(anonymousUser.getFullName());

			autoAnonymizeAsset(layout, anonymousUser);
		}

		layoutLocalService.updateLayout(layout);
	}

	@Override
	public void delete(Layout layout) throws PortalException {
		layoutLocalService.deleteLayout(layout);
	}

	@Override
	public Class<Layout> getTypeClass() {
		return Layout.class;
	}

	protected void autoAnonymizeAsset(Layout layout, User anonymousUser) {
		AssetEntry assetEntry = fetchAssetEntry(layout);

		if (assetEntry != null) {
			assetEntry.setUserId(anonymousUser.getUserId());
			assetEntry.setUserName(anonymousUser.getFullName());

			assetEntryLocalService.updateAssetEntry(assetEntry);
		}
	}

	@Override
	protected ActionableDynamicQuery doGetActionableDynamicQuery() {
		return layoutLocalService.getActionableDynamicQuery();
	}

	@Override
	protected String[] doGetUserIdFieldNames() {
		return LayoutUADConstants.USER_ID_FIELD_NAMES_LAYOUT;
	}

	protected AssetEntry fetchAssetEntry(Layout layout) {
		return assetEntryLocalService.fetchEntry(
			Layout.class.getName(), layout.getPlid());
	}

	@Reference
	protected AssetEntryLocalService assetEntryLocalService;

	@Reference
	protected LayoutLocalService layoutLocalService;

}