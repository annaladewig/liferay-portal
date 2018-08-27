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

package com.liferay.staging.security.internal.permission;

import aQute.bnd.annotation.ProviderType;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.exportimport.kernel.staging.StagingUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.UserBag;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Tomas Polesovsky
 */
@ProviderType
public class StagingPermissionChecker implements PermissionChecker {

	public StagingPermissionChecker(PermissionChecker permissionChecker) {
		_permissionChecker = permissionChecker;
	}

	@Override
	public PermissionChecker clone() {
		return new StagingPermissionChecker(_permissionChecker.clone());
	}

	@Override
	public long getCompanyId() {
		return _permissionChecker.getCompanyId();
	}

	@Override
	public long[] getGuestUserRoleIds() {
		return _permissionChecker.getGuestUserRoleIds();
	}

	@Override
	public List<Long> getOwnerResourceBlockIds(
		long companyId, long groupId, String name, String actionId) {

		return Collections.emptyList();
	}

	@Override
	public long getOwnerRoleId() {
		return _permissionChecker.getOwnerRoleId();
	}

	@Override
	public Map<Object, Object> getPermissionChecksMap() {
		return _permissionChecker.getPermissionChecksMap();
	}

	@Override
	public List<Long> getResourceBlockIds(
		long companyId, long groupId, long userId, String name,
		String actionId) {

		return _permissionChecker.getResourceBlockIds(
			companyId, groupId, userId, name, actionId);
	}

	@Override
	public long[] getRoleIds(long userId, long groupId) {
		long liveGroupId = StagingUtil.getLiveGroupId(groupId);

		return _permissionChecker.getRoleIds(userId, liveGroupId);
	}

	@Override
	public User getUser() {
		return _permissionChecker.getUser();
	}

	@Override
	public UserBag getUserBag() throws Exception {
		return _permissionChecker.getUserBag();
	}

	@Override
	public long getUserId() {
		return _permissionChecker.getUserId();
	}

	@Override
	public boolean hasOwnerPermission(
		long companyId, String name, long primKey, long ownerId,
		String actionId) {

		return _permissionChecker.hasOwnerPermission(
			companyId, name, primKey, ownerId, actionId);
	}

	@Override
	public boolean hasOwnerPermission(
		long companyId, String name, String primKey, long ownerId,
		String actionId) {

		return _permissionChecker.hasOwnerPermission(
			companyId, name, primKey, ownerId, actionId);
	}

	@Override
	public boolean hasPermission(
		Group group, String name, long primKey, String actionId) {

		Group liveGroup = StagingUtil.getLiveGroup(group);

		if (liveGroup != group) {
			if (primKey == group.getGroupId()) {
				primKey = liveGroup.getGroupId();
			}
		}

		if (_isStagingFolder(name, actionId)) {
			return true;
		}

		return _permissionChecker.hasPermission(
			liveGroup, name, primKey, actionId);
	}

	@Override
	public boolean hasPermission(
		Group group, String name, String primKey, String actionId) {

		Group liveGroup = StagingUtil.getLiveGroup(group);

		if (liveGroup != group) {
			if (primKey.equals(String.valueOf(group.getGroupId()))) {
				primKey = String.valueOf(liveGroup.getGroupId());
			}
		}

		if (_isStagingFolder(name, actionId)) {
			return true;
		}

		return _permissionChecker.hasPermission(
			liveGroup, name, primKey, actionId);
	}

	@Override
	public boolean hasPermission(
		long groupId, String name, long primKey, String actionId) {

		return hasPermission(
			GroupLocalServiceUtil.fetchGroup(groupId), name, primKey, actionId);
	}

	@Override
	public boolean hasPermission(
		long groupId, String name, String primKey, String actionId) {

		return hasPermission(
			GroupLocalServiceUtil.fetchGroup(groupId), name, primKey, actionId);
	}

	@Override
	public void init(User user) {
		_permissionChecker.init(user);
	}

	@Override
	public boolean isCheckGuest() {
		return _permissionChecker.isCheckGuest();
	}

	@Override
	public boolean isCompanyAdmin() {
		return _permissionChecker.isCompanyAdmin();
	}

	@Override
	public boolean isCompanyAdmin(long companyId) {
		return _permissionChecker.isCompanyAdmin(companyId);
	}

	@Override
	public boolean isContentReviewer(long companyId, long groupId) {
		long liveGroupId = StagingUtil.getLiveGroupId(groupId);

		return _permissionChecker.isContentReviewer(companyId, liveGroupId);
	}

	@Override
	public boolean isGroupAdmin(long groupId) {
		long liveGroupId = StagingUtil.getLiveGroupId(groupId);

		return _permissionChecker.isGroupAdmin(liveGroupId);
	}

	@Override
	public boolean isGroupMember(long groupId) {
		long liveGroupId = StagingUtil.getLiveGroupId(groupId);

		return _permissionChecker.isGroupMember(liveGroupId);
	}

	@Override
	public boolean isGroupOwner(long groupId) {
		long liveGroupId = StagingUtil.getLiveGroupId(groupId);

		return _permissionChecker.isGroupOwner(liveGroupId);
	}

	@Override
	public boolean isOmniadmin() {
		return _permissionChecker.isOmniadmin();
	}

	@Override
	public boolean isOrganizationAdmin(long organizationId) {
		return _permissionChecker.isOrganizationAdmin(organizationId);
	}

	@Override
	public boolean isOrganizationOwner(long organizationId) {
		return _permissionChecker.isOrganizationOwner(organizationId);
	}

	@Override
	public boolean isSignedIn() {
		return _permissionChecker.isSignedIn();
	}

	private boolean _isStagingFolder(String name, String actionId) {
		if (ExportImportThreadLocal.isStagingInProcessOnRemoteLive() &&
			actionId.equals("VIEW") &&
			(name.equals(Folder.class.getName()) ||
			 name.equals(DLFolder.class.getName()) ||
			 Objects.equals("com.liferay.document.library", name))) {

			return true;
		}

		return false;
	}

	private final PermissionChecker _permissionChecker;

}