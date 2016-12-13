package com.kasungunathilaka.domain;

import java.io.Serializable;

// </summary>
// Source File		: MemberEmergencyContact.java
// Package 			: com.kasungunathilaka.domain
// Description		: Emergency Contact Domain Class
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 16 April 2016    Kasun Gunathilak			    Created
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

public class MemberEmergencyContact implements Serializable {

    //region Class Members
    private int memberEmergencyContactId;
    private int memberId;
    private String name;
    private String contactNo;
    //endregion

    //region Constructors
    public MemberEmergencyContact(int memberEmergencyContactId
            , int memberId
            , String name
            , String contactNo) {
        this.memberEmergencyContactId = memberEmergencyContactId;
        this.memberId = memberId;
        this.name = name;
        this.contactNo = contactNo;
    }
    //endregion

    //region Getters
    public int getMemberEmergencyContactId() {
        return memberEmergencyContactId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getContactNo() {
        return contactNo;
    }
    //endregion

    //region Setters
    public void setMemberEmergencyContactId(int memberEmergencyContactId) {
        this.memberEmergencyContactId = memberEmergencyContactId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    //endregion

}
