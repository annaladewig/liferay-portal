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

package com.liferay.portal.workflow.kaleo.internal.upgrade.v3_2_0.util;

import java.sql.Types;

import java.util.HashMap;
import java.util.Map;

/**
 * @author	  Brian Wing Shun Chan
 * @generated
 */
public class KaleoInstanceTable {

	public static final String TABLE_NAME = "KaleoInstance";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"kaleoInstanceId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"kaleoDefinitionId", Types.BIGINT},
		{"kaleoDefinitionVersionId", Types.BIGINT},
		{"kaleoDefinitionName", Types.VARCHAR},
		{"kaleoDefinitionVersion", Types.INTEGER},
		{"rootKaleoInstanceTokenId", Types.BIGINT}, {"active_", Types.BOOLEAN},
		{"className", Types.VARCHAR}, {"classPK", Types.BIGINT},
		{"completed", Types.BOOLEAN}, {"completionDate", Types.TIMESTAMP},
		{"workflowContext", Types.CLOB}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
new HashMap<String, Integer>();

static {
TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);

TABLE_COLUMNS_MAP.put("kaleoInstanceId", Types.BIGINT);

TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);

TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);

TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);

TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);

TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);

TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);

TABLE_COLUMNS_MAP.put("kaleoDefinitionId", Types.BIGINT);

TABLE_COLUMNS_MAP.put("kaleoDefinitionVersionId", Types.BIGINT);

TABLE_COLUMNS_MAP.put("kaleoDefinitionName", Types.VARCHAR);

TABLE_COLUMNS_MAP.put("kaleoDefinitionVersion", Types.INTEGER);

TABLE_COLUMNS_MAP.put("rootKaleoInstanceTokenId", Types.BIGINT);

TABLE_COLUMNS_MAP.put("active_", Types.BOOLEAN);

TABLE_COLUMNS_MAP.put("className", Types.VARCHAR);

TABLE_COLUMNS_MAP.put("classPK", Types.BIGINT);

TABLE_COLUMNS_MAP.put("completed", Types.BOOLEAN);

TABLE_COLUMNS_MAP.put("completionDate", Types.TIMESTAMP);

TABLE_COLUMNS_MAP.put("workflowContext", Types.CLOB);

}
	public static final String TABLE_SQL_CREATE =
"create table KaleoInstance (mvccVersion LONG default 0 not null,kaleoInstanceId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(200) null,createDate DATE null,modifiedDate DATE null,kaleoDefinitionId LONG,kaleoDefinitionVersionId LONG,kaleoDefinitionName VARCHAR(200) null,kaleoDefinitionVersion INTEGER,rootKaleoInstanceTokenId LONG,active_ BOOLEAN,className VARCHAR(200) null,classPK LONG,completed BOOLEAN,completionDate DATE null,workflowContext TEXT null)";

	public static final String TABLE_SQL_DROP = "drop table KaleoInstance";

	public static final String[] TABLE_SQL_ADD_INDEXES = {
		"create index IX_58D85ECB on KaleoInstance (className[$COLUMN_LENGTH:200$], classPK)",
		"create index IX_BF5839F8 on KaleoInstance (companyId, kaleoDefinitionName[$COLUMN_LENGTH:200$], kaleoDefinitionVersion, completionDate)",
		"create index IX_C6D7A867 on KaleoInstance (companyId, userId)",
		"create index IX_ACF16238 on KaleoInstance (kaleoDefinitionId, completed)",
		"create index IX_3DA1A5AC on KaleoInstance (kaleoDefinitionVersionId, completed)",
		"create index IX_6D12641D on KaleoInstance (kaleoInstanceId, companyId, userId)"
	};

}