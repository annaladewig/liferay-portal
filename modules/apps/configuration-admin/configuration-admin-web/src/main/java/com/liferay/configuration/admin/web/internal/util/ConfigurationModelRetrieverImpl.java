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

package com.liferay.configuration.admin.web.internal.util;

import com.liferay.configuration.admin.web.internal.model.ConfigurationModel;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.configuration.metatype.definitions.ExtendedMetaTypeInformation;
import com.liferay.portal.configuration.metatype.definitions.ExtendedMetaTypeService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jorge Ferrer
 * @author Michael C. Han
 */
@Component(immediate = true, service = ConfigurationModelRetriever.class)
public class ConfigurationModelRetrieverImpl
	implements ConfigurationModelRetriever {

	@Override
	public Map<String, Set<ConfigurationModel>> categorizeConfigurationModels(
		Map<String, ConfigurationModel> configurationModels) {

		Map<String, Set<ConfigurationModel>> categorizedConfigurationModels =
			new HashMap<>();

		for (ConfigurationModel configurationModel :
				configurationModels.values()) {

			String configurationCategory = configurationModel.getCategory();

			Set<ConfigurationModel> curConfigurationModels =
				categorizedConfigurationModels.get(configurationCategory);

			if (curConfigurationModels == null) {
				curConfigurationModels = new TreeSet<>(
					getConfigurationModelComparator());

				categorizedConfigurationModels.put(
					configurationCategory, curConfigurationModels);
			}

			curConfigurationModels.add(configurationModel);
		}

		return categorizedConfigurationModels;
	}

	@Override
	public Configuration getConfiguration(
		String pid, ExtendedObjectClassDefinition.Scope scope,
		Serializable scopePK) {

		try {
			String pidFilter = getPidFilterString(pid, false, scope, scopePK);

			Configuration[] configurations =
				_configurationAdmin.listConfigurations(pidFilter);

			if (configurations != null) {
				return configurations[0];
			}
		}
		catch (InvalidSyntaxException | IOException e) {
			ReflectionUtil.throwException(e);
		}

		return null;
	}

	@Override
	public Map<String, ConfigurationModel> getConfigurationModels(
		Bundle bundle, ExtendedObjectClassDefinition.Scope scope,
		Serializable scopePK) {

		Map<String, ConfigurationModel> configurationModels = new HashMap<>();

		collectConfigurationModels(
			bundle, configurationModels, true, null, scope, scopePK);
		collectConfigurationModels(
			bundle, configurationModels, false, null, scope, scopePK);

		return configurationModels;
	}

	@Override
	public Map<String, ConfigurationModel> getConfigurationModels(
		ExtendedObjectClassDefinition.Scope scope, Serializable scopePK) {

		return getConfigurationModels((String)null, scope, scopePK);
	}

	@Override
	public Map<String, ConfigurationModel> getConfigurationModels(
		String locale, ExtendedObjectClassDefinition.Scope scope,
		Serializable scopePK) {

		Map<String, ConfigurationModel> configurationModels = new HashMap<>();

		Bundle[] bundles = _bundleContext.getBundles();

		for (Bundle bundle : bundles) {
			if (bundle.getState() != Bundle.ACTIVE) {
				continue;
			}

			collectConfigurationModels(
				bundle, configurationModels, true, locale, scope, scopePK);
			collectConfigurationModels(
				bundle, configurationModels, false, locale, scope, scopePK);
		}

		return configurationModels;
	}

	@Override
	public Set<ConfigurationModel> getConfigurationModels(
		String configurationCategory, String languageId,
		ExtendedObjectClassDefinition.Scope scope, Serializable scopePK) {

		Map<String, ConfigurationModel> configurationModelsMap =
			getConfigurationModels(languageId, scope, scopePK);

		Map<String, Set<ConfigurationModel>> categorizedConfigurationModels =
			categorizeConfigurationModels(configurationModelsMap);

		Set<ConfigurationModel> configurationModels =
			categorizedConfigurationModels.get(configurationCategory);

		if (configurationModels == null) {
			configurationModels = Collections.emptySet();
		}

		return configurationModels;
	}

	@Override
	public List<ConfigurationModel> getFactoryInstances(
			ConfigurationModel factoryConfigurationModel,
			ExtendedObjectClassDefinition.Scope scope, Serializable scopePK)
		throws IOException {

		Configuration[] configurations = getFactoryConfigurations(
			factoryConfigurationModel.getFactoryPid());

		if (configurations == null) {
			return Collections.emptyList();
		}

		List<ConfigurationModel> factoryInstances = new ArrayList<>();

		for (Configuration configuration : configurations) {
			ConfigurationModel curConfigurationModel = new ConfigurationModel(
				factoryConfigurationModel, configuration,
				factoryConfigurationModel.getBundleSymbolicName(),
				configuration.getBundleLocation(), false);

			factoryInstances.add(curConfigurationModel);
		}

		return factoryInstances;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	protected void collectConfigurationModels(
		Bundle bundle, Map<String, ConfigurationModel> configurationModels,
		boolean factory, String locale,
		ExtendedObjectClassDefinition.Scope scope, Serializable scopePK) {

		ExtendedMetaTypeInformation extendedMetaTypeInformation =
			_extendedMetaTypeService.getMetaTypeInformation(bundle);

		if (extendedMetaTypeInformation == null) {
			return;
		}

		List<String> pids = new ArrayList<>();

		if (factory) {
			Collections.addAll(
				pids, extendedMetaTypeInformation.getFactoryPids());
		}
		else {
			Collections.addAll(pids, extendedMetaTypeInformation.getPids());
		}

		for (String pid : pids) {
			ConfigurationModel configurationModel = getConfigurationModel(
				bundle, pid, factory, locale, scope, scopePK);

			if (configurationModel == null) {
				continue;
			}

			configurationModels.put(pid, configurationModel);
		}
	}

	protected Configuration getCompanyDefaultConfiguration(String factoryPid) {
		Configuration configuration = null;

		try {
			Configuration[] factoryConfigurations = getFactoryConfigurations(
				factoryPid, ConfigurationModel.PROPERTY_KEY_COMPANY_ID,
				ConfigurationModel.PROPERTY_VALUE_COMPANY_ID_DEFAULT);

			if (ArrayUtil.isNotEmpty(factoryConfigurations)) {
				configuration = factoryConfigurations[0];
			}
		}
		catch (IOException ioe) {
			ReflectionUtil.throwException(ioe);
		}

		return configuration;
	}

	protected ConfigurationModel getConfigurationModel(
		Bundle bundle, String pid, boolean factory, String locale,
		ExtendedObjectClassDefinition.Scope scope, Serializable scopePK) {

		ExtendedMetaTypeInformation metaTypeInformation =
			_extendedMetaTypeService.getMetaTypeInformation(bundle);

		if (metaTypeInformation == null) {
			return null;
		}

		ConfigurationModel configurationModel = new ConfigurationModel(
			metaTypeInformation.getObjectClassDefinition(pid, locale),
			getConfiguration(pid, scope, scopePK), bundle.getSymbolicName(),
			StringPool.QUESTION, factory);

		if (configurationModel.isCompanyFactory()) {
			Configuration configuration = getCompanyDefaultConfiguration(pid);

			configurationModel = new ConfigurationModel(
				configurationModel.getExtendedObjectClassDefinition(),
				configuration, bundle.getSymbolicName(), StringPool.QUESTION,
				configurationModel.isFactory());
		}

		return configurationModel;
	}

	protected Comparator<ConfigurationModel> getConfigurationModelComparator() {
		return new ConfigurationModelComparator();
	}

	protected Configuration[] getFactoryConfigurations(String factoryPid)
		throws IOException {

		return getFactoryConfigurations(factoryPid, null, null);
	}

	protected Configuration[] getFactoryConfigurations(
			String factoryPid, String property, String value)
		throws IOException {

		Configuration[] configurations = null;

		StringBundler sb = new StringBundler(13);

		if (Validator.isNotNull(property) && Validator.isNotNull(value)) {
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(StringPool.AMPERSAND);
		}

		sb.append(StringPool.OPEN_PARENTHESIS);
		sb.append(ConfigurationAdmin.SERVICE_FACTORYPID);
		sb.append(StringPool.EQUAL);
		sb.append(factoryPid);
		sb.append(StringPool.CLOSE_PARENTHESIS);

		if (Validator.isNotNull(property) && Validator.isNotNull(value)) {
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(property);
			sb.append(StringPool.EQUAL);
			sb.append(value);
			sb.append(StringPool.CLOSE_PARENTHESIS);
			sb.append(StringPool.CLOSE_PARENTHESIS);
		}

		try {
			configurations = _configurationAdmin.listConfigurations(
				sb.toString());
		}
		catch (InvalidSyntaxException ise) {
			ReflectionUtil.throwException(ise);
		}

		return configurations;
	}

	protected String getPidFilterString(String pid, boolean factory) {
		StringBundler sb = new StringBundler(5);

		sb.append(StringPool.OPEN_PARENTHESIS);

		if (factory) {
			sb.append(ConfigurationAdmin.SERVICE_FACTORYPID);
		}
		else {
			sb.append(Constants.SERVICE_PID);
		}

		sb.append(StringPool.EQUAL);
		sb.append(pid);
		sb.append(StringPool.CLOSE_PARENTHESIS);

		return sb.toString();
	}

	private BundleContext _bundleContext;

	@Reference
	private ConfigurationAdmin _configurationAdmin;

	@Reference
	private ExtendedMetaTypeService _extendedMetaTypeService;

	private static class ConfigurationModelComparator
		implements Comparator<ConfigurationModel> {

		@Override
		public int compare(
			ConfigurationModel configurationModel1,
			ConfigurationModel configurationModel2) {

			String name1 = configurationModel1.getName();
			String name2 = configurationModel2.getName();

			return name1.compareTo(name2);
		}

	}

}