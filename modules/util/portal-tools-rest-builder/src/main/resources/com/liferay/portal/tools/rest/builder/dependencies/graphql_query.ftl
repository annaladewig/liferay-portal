package ${configYAML.apiPackagePath}.internal.graphql.query.${escapedVersion};

<#list allSchemas?keys as schemaName>
	import ${configYAML.apiPackagePath}.dto.${escapedVersion}.${schemaName};
	import ${configYAML.apiPackagePath}.resource.${escapedVersion}.${schemaName}Resource;
</#list>

import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.multipart.MultipartBody;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Generated;

import javax.ws.rs.core.Response;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author ${configYAML.author}
 * @generated
 */
@Generated("")
public class Query {

	<#assign
		javaMethodSignatures = freeMarkerTool.getGraphQLJavaMethodSignatures(configYAML, "query", openAPIYAML)

		schemaNames = freeMarkerTool.getGraphQLSchemaNames(javaMethodSignatures)
	/>

	<#list schemaNames as schemaName>
		public static void set${schemaName}ResourceComponentServiceObjects(ComponentServiceObjects<${schemaName}Resource> ${freeMarkerTool.getSchemaVarName(schemaName)}ResourceComponentServiceObjects) {
			_${freeMarkerTool.getSchemaVarName(schemaName)}ResourceComponentServiceObjects = ${freeMarkerTool.getSchemaVarName(schemaName)}ResourceComponentServiceObjects;
		}
	</#list>

	<#list schemaNames as schemaName>
		@GraphQLName("${schemaName}Page")
		public class ${schemaName}Page {

			public ${schemaName}Page(Page ${freeMarkerTool.getSchemaVarName(schemaName)}Page) {
				items = ${freeMarkerTool.getSchemaVarName(schemaName)}Page.getItems();
				page = ${freeMarkerTool.getSchemaVarName(schemaName)}Page.getPage();
				pageSize = ${freeMarkerTool.getSchemaVarName(schemaName)}Page.getPageSize();
				totalCount = ${freeMarkerTool.getSchemaVarName(schemaName)}Page.getTotalCount();
			}

			@GraphQLField
			protected Collection<${schemaName}> items;
			@GraphQLField
			protected long page;
			@GraphQLField
			protected long pageSize;
			@GraphQLField
			protected long totalCount;
		}
	</#list>

	<#list javaMethodSignatures as javaMethodSignature>
		${freeMarkerTool.getGraphQLMethodAnnotations(javaMethodSignature)}
		public

		<#if javaMethodSignature.returnType?contains("Collection<")>
			${javaMethodSignature.schemaName}Page
		<#else>
			${javaMethodSignature.returnType}
		</#if>

		${javaMethodSignature.methodName}(${freeMarkerTool.getGraphQLParameters(javaMethodSignature.javaMethodParameters, javaMethodSignature.operation, true)}) throws Exception {
			<#assign arguments = freeMarkerTool.getGraphQLArguments(javaMethodSignature.javaMethodParameters) />

			<#if javaMethodSignature.returnType?contains("Collection<")>
				return _applyComponentServiceObjects(
					_${freeMarkerTool.getSchemaVarName(javaMethodSignature.schemaName)}ResourceComponentServiceObjects,
					this::_populateResourceContext,
					${freeMarkerTool.getSchemaVarName(javaMethodSignature.schemaName)}Resource ->
						new ${javaMethodSignature.schemaName}Page(
							${freeMarkerTool.getSchemaVarName(javaMethodSignature.schemaName)}Resource.${javaMethodSignature.methodName}(${arguments?replace("pageSize,page", "Pagination.of(page, pageSize)")}))
					);
			<#else>
				return _applyComponentServiceObjects(_${freeMarkerTool.getSchemaVarName(javaMethodSignature.schemaName)}ResourceComponentServiceObjects, this::_populateResourceContext, ${freeMarkerTool.getSchemaVarName(javaMethodSignature.schemaName)}Resource -> ${freeMarkerTool.getSchemaVarName(javaMethodSignature.schemaName)}Resource.${javaMethodSignature.methodName}(${arguments?replace("pageSize,page", "Pagination.of(page, pageSize)")}));
			</#if>
		}
	</#list>

	private <T, R, E1 extends Throwable, E2 extends Throwable> R _applyComponentServiceObjects(ComponentServiceObjects<T> componentServiceObjects, UnsafeConsumer<T, E1> unsafeConsumer, UnsafeFunction<T, R, E2> unsafeFunction) throws E1, E2 {
		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	<#list schemaNames as schemaName>
		private void _populateResourceContext(${schemaName}Resource ${freeMarkerTool.getSchemaVarName(schemaName)}Resource) throws Exception {
			${freeMarkerTool.getSchemaVarName(schemaName)}Resource.setContextAcceptLanguage(_acceptLanguage);
			${freeMarkerTool.getSchemaVarName(schemaName)}Resource.setContextCompany(_company);
		}
	</#list>

	<#list schemaNames as schemaName>
		private static ComponentServiceObjects<${schemaName}Resource> _${freeMarkerTool.getSchemaVarName(schemaName)}ResourceComponentServiceObjects;
	</#list>

	private AcceptLanguage _acceptLanguage;
	private Company _company;

}