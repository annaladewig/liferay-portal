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

package com.liferay.commerce.order.content.web.internal.importer.type.util;

import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.exception.CommerceOrderValidatorException;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.order.importer.item.CommerceOrderImporterItem;
import com.liferay.commerce.order.importer.item.CommerceOrderImporterItemImpl;
import com.liferay.commerce.price.CommerceOrderPriceCalculation;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alessio Antonio Rendina
 */
public class CommerceOrderImporterTypeUtil {

	public static List<CommerceOrderImporterItem> getCommerceOrderImporterItems(
			CommerceContextFactory commerceContextFactory,
			CommerceOrder commerceOrder,
			CommerceOrderImporterItemImpl[] commerceOrderImporterItemImpls,
			CommerceOrderItemService commerceOrderItemService,
			CommerceOrderPriceCalculation commerceOrderPriceCalculation,
			CommerceOrderService commerceOrderService,
			UserLocalService userLocalService)
		throws Exception {

		CommerceOrder tempCommerceOrder = commerceOrderService.addCommerceOrder(
			commerceOrder.getGroupId(), commerceOrder.getCommerceAccountId(),
			commerceOrder.getCommerceCurrencyId(),
			commerceOrder.getCommerceOrderTypeId());

		CommerceContext commerceContext = commerceContextFactory.create(
			tempCommerceOrder.getCompanyId(), tempCommerceOrder.getGroupId(),
			PrincipalThreadLocal.getUserId(),
			tempCommerceOrder.getCommerceOrderId(),
			tempCommerceOrder.getCommerceAccountId());

		ServiceContext serviceContext = _getServiceContext(userLocalService);

		for (CommerceOrderImporterItemImpl commerceOrderImporterItemImpl :
				commerceOrderImporterItemImpls) {

			try {

				// Temporary commerce order item

				CommerceOrderItem commerceOrderItem =
					commerceOrderItemService.addCommerceOrderItem(
						tempCommerceOrder.getCommerceOrderId(),
						commerceOrderImporterItemImpl.getCPInstanceId(),
						commerceOrderImporterItemImpl.getJSON(),
						commerceOrderImporterItemImpl.getQuantity(), 0,
						commerceContext, serviceContext);

				commerceOrderImporterItemImpl.setCommerceOrderItemPrice(
					commerceOrderPriceCalculation.getCommerceOrderItemPrice(
						tempCommerceOrder.getCommerceCurrency(),
						commerceOrderItem));
			}
			catch (Exception exception) {
				if (exception instanceof CommerceOrderValidatorException) {
					CommerceOrderValidatorException
						commerceOrderValidatorException =
							(CommerceOrderValidatorException)exception;

					commerceOrderImporterItemImpl.setErrorMessages(
						TransformUtil.transformToArray(
							commerceOrderValidatorException.
								getCommerceOrderValidatorResults(),
							commerceOrderValidatorResult ->
								commerceOrderValidatorResult.
									getLocalizedMessage(),
							String.class));
				}
				else {
					String[] errorMessages =
						commerceOrderImporterItemImpl.getErrorMessages();

					if ((errorMessages == null) ||
						ArrayUtil.isNotEmpty(errorMessages)) {

						commerceOrderImporterItemImpl.setErrorMessages(
							TransformUtil.transform(
								errorMessages,
								errorMessage -> LanguageUtil.get(
									serviceContext.getLocale(), errorMessage),
								String.class));
					}
				}
			}
		}

		// Delete temporary commerce order

		commerceOrderService.deleteCommerceOrder(
			tempCommerceOrder.getCommerceOrderId());

		return ListUtil.fromArray(commerceOrderImporterItemImpls);
	}

	public static List<CommerceOrderImporterItem> getCommerceOrderImporterItems(
			CommerceContextFactory commerceContextFactory,
			CommerceOrder commerceOrder,
			List<CommerceOrderItem> commerceOrderItems,
			CommerceOrderItemService commerceOrderItemService,
			CommerceOrderPriceCalculation commerceOrderPriceCalculation,
			CommerceOrderService commerceOrderService,
			UserLocalService userLocalService)
		throws Exception {

		List<CommerceOrderImporterItem> commerceOrderImporterItems =
			new ArrayList<>(commerceOrderItems.size());

		// Temporary commerce order

		CommerceOrder tempCommerceOrder = commerceOrderService.addCommerceOrder(
			commerceOrder.getGroupId(), commerceOrder.getCommerceAccountId(),
			commerceOrder.getCommerceCurrencyId(),
			commerceOrder.getCommerceOrderTypeId());

		CommerceContext commerceContext = commerceContextFactory.create(
			tempCommerceOrder.getCompanyId(), tempCommerceOrder.getGroupId(),
			PrincipalThreadLocal.getUserId(),
			tempCommerceOrder.getCommerceOrderId(),
			tempCommerceOrder.getCommerceAccountId());

		ServiceContext serviceContext = _getServiceContext(userLocalService);

		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
			CommerceOrderImporterItemImpl commerceOrderImporterItemImpl =
				new CommerceOrderImporterItemImpl();

			commerceOrderImporterItemImpl.setCPDefinitionId(
				commerceOrderItem.getCPDefinitionId());
			commerceOrderImporterItemImpl.setCPInstanceId(
				commerceOrderItem.getCPInstanceId());
			commerceOrderImporterItemImpl.setJSON(commerceOrderItem.getJson());
			commerceOrderImporterItemImpl.setNameMap(
				commerceOrderItem.getNameMap());
			commerceOrderImporterItemImpl.
				setParentCommerceOrderItemCPDefinitionId(
					commerceOrderItem.
						getParentCommerceOrderItemCPDefinitionId());
			commerceOrderImporterItemImpl.setQuantity(
				commerceOrderItem.getQuantity());

			try {

				// Temporary commerce order item

				CommerceOrderItem tempCommerceOrderItem =
					commerceOrderItemService.addCommerceOrderItem(
						tempCommerceOrder.getCommerceOrderId(),
						commerceOrderItem.getCPInstanceId(),
						commerceOrderItem.getJson(),
						commerceOrderItem.getQuantity(), 0, commerceContext,
						serviceContext);

				commerceOrderImporterItemImpl.setCommerceOrderItemPrice(
					commerceOrderPriceCalculation.getCommerceOrderItemPrice(
						tempCommerceOrder.getCommerceCurrency(),
						tempCommerceOrderItem));
			}
			catch (CommerceOrderValidatorException
						commerceOrderValidatorException) {

				commerceOrderImporterItemImpl.setErrorMessages(
					TransformUtil.transformToArray(
						commerceOrderValidatorException.
							getCommerceOrderValidatorResults(),
						commerceOrderValidatorResult ->
							commerceOrderValidatorResult.getLocalizedMessage(),
						String.class));
			}
		}

		// Delete temporary commerce order

		commerceOrderService.deleteCommerceOrder(
			tempCommerceOrder.getCommerceOrderId());

		return commerceOrderImporterItems;
	}

	private static ServiceContext _getServiceContext(
			UserLocalService userLocalService)
		throws Exception {

		ServiceContext serviceContext = new ServiceContext();

		User user = userLocalService.getUser(PrincipalThreadLocal.getUserId());

		serviceContext.setCompanyId(user.getCompanyId());
		serviceContext.setLanguageId(user.getLanguageId());
		serviceContext.setScopeGroupId(user.getGroupId());
		serviceContext.setUserId(user.getUserId());

		return serviceContext;
	}

}