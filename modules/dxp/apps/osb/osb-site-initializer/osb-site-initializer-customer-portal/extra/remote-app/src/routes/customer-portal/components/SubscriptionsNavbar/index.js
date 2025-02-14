/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import {Button, DropDown} from '@clayui/core';
import ClayIcon from '@clayui/icon';
import classNames from 'classnames';
import React, {useEffect, useRef, useState} from 'react';
import BaseButton from '../../../../common/components/BaseButton';
import {useCustomerPortal} from '../../context';

const SubscriptionDropDownMenu = ({
	selectedSubscriptionGroup,
	setSelectedSubscriptionGroup,
	subscriptionGroups,
}) => {
	const [active, setActive] = useState(false);

	return (
		<div className="align-items-center d-flex mt-4 pb-3 subscription-navbar-dropdown">
			<h6>Type:</h6>

			<DropDown
				active={active}
				closeOnClickOutside
				menuElementAttrs={{
					className: 'subscription-group-filter',
				}}
				onActiveChange={setActive}
				trigger={
					<Button
						className="font-weight-semi-bold ml-2 pb-2 shadow-none text-brand-primary"
						displayType="unstyled"
					>
						{selectedSubscriptionGroup}

						<ClayIcon symbol="caret-bottom" />
					</Button>
				}
			>
				{subscriptionGroups.map((subscriptionGroup) => (
					<DropDown.Item
						key={subscriptionGroup.name}
						onClick={(event) => {
							setSelectedSubscriptionGroup(event.target.value);
							setActive(false);
						}}
						value={subscriptionGroup.name}
					>
						{subscriptionGroup.name}
					</DropDown.Item>
				))}
			</DropDown>
		</div>
	);
};

const SubscriptionsNavbar = ({
	selectedSubscriptionGroup,
	setSelectedSubscriptionGroup,
	subscriptionGroups,
}) => {
	const [selectedButton, setSelectedButton] = useState(
		subscriptionGroups[0]?.name
	);
	const [showDropDown, setShowDropDown] = useState(false);
	const [{isQuickLinksExpanded}] = useCustomerPortal();

	const subscriptionNavbarRef = useRef();

	useEffect(() => {
		setSelectedSubscriptionGroup(subscriptionGroups[0]?.name);
		setSelectedButton(subscriptionGroups[0]?.name);

		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [subscriptionGroups]);

	useEffect(() => {
		const updateShowDropDown = () => {
			setShowDropDown(
				subscriptionNavbarRef.current.offsetWidth <
					(isQuickLinksExpanded ? 500 : 570)
			);
		};

		updateShowDropDown();
		window.addEventListener('resize', updateShowDropDown);
	}, [isQuickLinksExpanded]);

	return (
		<div className="d-flex rounded-pill w-100" ref={subscriptionNavbarRef}>
			<nav className="mb-2 mt-4 pt-2">
				{subscriptionGroups.length === 1 &&
					subscriptionGroups.map((subscriptionGroup) => (
						<h5
							className="text-brand-primary"
							key={subscriptionGroup.name}
						>
							{subscriptionGroup.name}
						</h5>
					))}

				{subscriptionGroups.length > 1 &&
					subscriptionGroups.length < 5 && (
						<>
							{showDropDown && (
								<SubscriptionDropDownMenu
									selectedSubscriptionGroup={
										selectedSubscriptionGroup
									}
									setSelectedSubscriptionGroup={
										setSelectedSubscriptionGroup
									}
									subscriptionGroups={subscriptionGroups}
								/>
							)}

							{!showDropDown && (
								<div
									className="bg-neutral-1 border border-light btn-group rounded-pill"
									id="subscription-navbar"
									role="group"
								>
									{subscriptionGroups.map(
										(subscriptionGroup) => (
											<BaseButton
												className={classNames(
													'btn px-4 py-1 rounded-pill',
													{
														'bg-transparent text-neutral-4':
															selectedButton !==
															subscriptionGroup.name,
														'bg-white border border-primary label-primary text-brand-primary':
															selectedButton ===
															subscriptionGroup.name,
													}
												)}
												key={subscriptionGroup.name}
												onClick={(event) => {
													setSelectedSubscriptionGroup(
														event.target.value
													);
													setSelectedButton(
														event.target.value
													);
												}}
												value={subscriptionGroup.name}
											>
												{subscriptionGroup.name}
											</BaseButton>
										)
									)}
								</div>
							)}
						</>
					)}

				{subscriptionGroups.length > 4 && (
					<SubscriptionDropDownMenu
						selectedSubscriptionGroup={selectedSubscriptionGroup}
						setSelectedSubscriptionGroup={
							setSelectedSubscriptionGroup
						}
						subscriptionGroups={subscriptionGroups}
					/>
				)}
			</nav>
		</div>
	);
};

export default SubscriptionsNavbar;
