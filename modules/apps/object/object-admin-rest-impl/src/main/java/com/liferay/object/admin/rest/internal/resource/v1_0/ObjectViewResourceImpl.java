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

package com.liferay.object.admin.rest.internal.resource.v1_0;

import com.liferay.object.admin.rest.dto.v1_0.ObjectView;
import com.liferay.object.admin.rest.dto.v1_0.ObjectViewColumn;
import com.liferay.object.admin.rest.resource.v1_0.ObjectViewResource;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectViewService;
import com.liferay.object.service.persistence.ObjectViewColumnPersistence;
import com.liferay.object.util.LocalizedMapUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.SearchUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/object-view.properties",
	scope = ServiceScope.PROTOTYPE, service = ObjectViewResource.class
)
public class ObjectViewResourceImpl extends BaseObjectViewResourceImpl {

	@Override
	public void deleteObjectView(Long objectViewId) throws Exception {
		_objectViewService.deleteObjectView(objectViewId);
	}

	@Override
	public Page<ObjectView> getObjectDefinitionObjectViewsPage(
			Long objectDefinitionId, String search, Pagination pagination)
		throws Exception {

		return SearchUtil.search(
			HashMapBuilder.put(
				"create",
				addAction(
					ActionKeys.UPDATE, "postObjectDefinitionObjectView",
					ObjectDefinition.class.getName(), objectDefinitionId)
			).put(
				"get",
				addAction(
					ActionKeys.VIEW, "getObjectDefinitionObjectViewsPage",
					ObjectDefinition.class.getName(), objectDefinitionId)
			).build(),
			booleanQuery -> {
			},
			null, com.liferay.object.model.ObjectView.class.getName(), search,
			pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			searchContext -> {
				searchContext.setAttribute(Field.NAME, search);
				searchContext.setAttribute(
					"objectDefinitionId", objectDefinitionId);
				searchContext.setCompanyId(contextCompany.getCompanyId());
			},
			null,
			document -> _toObjectView(
				_objectViewService.getObjectView(
					GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK)))));
	}

	@Override
	public ObjectView getObjectView(Long objectViewId) throws Exception {
		return _toObjectView(_objectViewService.getObjectView(objectViewId));
	}

	@Override
	public ObjectView postObjectDefinitionObjectView(
			Long objectDefinitionId, ObjectView objectView)
		throws Exception {

		return _toObjectView(
			_objectViewService.addObjectView(
				objectDefinitionId,
				GetterUtil.getBoolean(objectView.getDefaultObjectView()),
				com.liferay.portal.vulcan.util.LocalizedMapUtil.getLocalizedMap(
					objectView.getName()),
				transformToList(
					objectView.getObjectViewColumns(),
					this::_toObjectViewColumn)));
	}

	@Override
	public ObjectView putObjectView(Long objectViewId, ObjectView objectView)
		throws Exception {

		return _toObjectView(
			_objectViewService.updateObjectView(
				objectViewId, objectView.getDefaultObjectView(),
				com.liferay.portal.vulcan.util.LocalizedMapUtil.getLocalizedMap(
					objectView.getName()),
				transformToList(
					objectView.getObjectViewColumns(),
					this::_toObjectViewColumn)));
	}

	private ObjectView _toObjectView(
		com.liferay.object.model.ObjectView serviceBuilderObjectView) {

		if (serviceBuilderObjectView == null) {
			return null;
		}

		return new ObjectView() {
			{
				actions = HashMapBuilder.put(
					"delete",
					addAction(
						ActionKeys.DELETE, "deleteObjectView",
						ObjectDefinition.class.getName(),
						serviceBuilderObjectView.getObjectDefinitionId())
				).put(
					"get",
					addAction(
						ActionKeys.VIEW, "getObjectView",
						ObjectDefinition.class.getName(),
						serviceBuilderObjectView.getObjectDefinitionId())
				).put(
					"update",
					addAction(
						ActionKeys.UPDATE, "putObjectView",
						ObjectDefinition.class.getName(),
						serviceBuilderObjectView.getObjectDefinitionId())
				).build();
				dateCreated = serviceBuilderObjectView.getCreateDate();
				dateModified = serviceBuilderObjectView.getModifiedDate();
				defaultObjectView =
					serviceBuilderObjectView.getDefaultObjectView();
				id = serviceBuilderObjectView.getObjectViewId();
				name = LocalizedMapUtil.getLanguageIdMap(
					serviceBuilderObjectView.getNameMap());
				objectDefinitionId =
					serviceBuilderObjectView.getObjectDefinitionId();
				objectViewColumns = TransformUtil.transformToArray(
					serviceBuilderObjectView.getObjectViewColumns(),
					objectViewColumn -> _toObjectViewColumn(objectViewColumn),
					ObjectViewColumn.class);
			}
		};
	}

	private com.liferay.object.model.ObjectViewColumn _toObjectViewColumn(
		ObjectViewColumn objectViewColumn) {

		com.liferay.object.model.ObjectViewColumn
			serviceBuilderObjectViewColumn =
				_objectViewColumnPersistence.create(0L);

		serviceBuilderObjectViewColumn.setObjectFieldName(
			objectViewColumn.getObjectFieldName());
		serviceBuilderObjectViewColumn.setPriority(
			objectViewColumn.getPriority());

		return serviceBuilderObjectViewColumn;
	}

	private ObjectViewColumn _toObjectViewColumn(
		com.liferay.object.model.ObjectViewColumn
			serviceBuilderObjectViewColumn) {

		if (serviceBuilderObjectViewColumn == null) {
			return null;
		}

		return new ObjectViewColumn() {
			{
				id = serviceBuilderObjectViewColumn.getObjectViewColumnId();
				objectFieldName =
					serviceBuilderObjectViewColumn.getObjectFieldName();
				priority = serviceBuilderObjectViewColumn.getPriority();
			}
		};
	}

	@Reference
	private ObjectViewColumnPersistence _objectViewColumnPersistence;

	@Reference
	private ObjectViewService _objectViewService;

}