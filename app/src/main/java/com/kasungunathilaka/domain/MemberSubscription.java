package com.kasungunathilaka.domain;

import java.io.Serializable;
import java.util.Date;

// </summary>
// Source File		: MemberSubscription.java
// Package 			: com.kasungunathilaka.domain
// Description		: Membership Domain Class
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 17 April 2016    Kasun Gunathilak			    Created
// </remarks>
//
// <license>
// Copyright 2016 Kasun Gunathilaka
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// </license>

public class MemberSubscription implements Serializable {

    //region Class Members
    private int memberSubscriptionId;
    private int memberId;
    private int subscriptionId;
    private Date startDate;
    private Date endDate;
    private int isActive;
    private Member member;
    private Subscription subscription;
    //endregion

    //region Constructors
    public MemberSubscription(int memberSubscriptionId, int memberId, int subscriptionId, Date startDate, Date endDate, int isActive) {
        this.memberSubscriptionId = memberSubscriptionId;
        this.memberId = memberId;
        this.subscriptionId = subscriptionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }
    //endregion

    //region Getters
    public int getMemberSubscriptionId() {
        return memberSubscriptionId;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Member getMember() {
        return member;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public int getIsActive() {
        return isActive;
    }
    //endregion

    //region Setters
    public void setMemberSubscriptionId(int memberSubscriptionId) {
        this.memberSubscriptionId = memberSubscriptionId;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
    //endregion
}
