package com.kasungunathilaka.domain;

// </summary>
// Source File		: Subscription.java
// Package 			: com.kasungunathilaka.domain
// Description		: Subscription Domain Class
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 16 April 2016    Kasun Gunathilak                Created
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

public class Subscription {

    //region Class Members
    private int subscriptionid;
    private String name;
    private int duration;
    //endregion

    //region Constructor
    public Subscription(int subscriptionid
            , String name
            , int duration) {
        this.subscriptionid = subscriptionid;
        this.name = name;
        this.duration = duration;
    }
    //endregion

    //region Getters and Setters
    public int getSubscription() {
        return subscriptionid;
    }

    public void setSubscription(int subscription) {
        this.subscriptionid = subscription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    //endregion

    @Override
    public String toString() {
        return getName();
    }
}
