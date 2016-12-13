package com.kasungunathilaka.domain;

// </summary>
// Source File		: MemberSymptom.java
// Package 			: com.kasungunathilaka.domain
// Description		: Symptom Domain Class
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 17 April 2016    Kasun Gunathilak                Created
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

public class MemberSymptom {

    //region Class Member
    private int memberSymptomsId;
    private int memberId;
    private String description;
    private String condition;
    //endregion

    //region Constructor
    public MemberSymptom(int memberSymptomsId
            , int memberId
            , String description
            , String condition) {
        this.memberSymptomsId = memberSymptomsId;
        this.memberId = memberId;
        this.description = description;
        this.condition = condition;
    }
    //endregion

    //region Getters
    public int getMemberSymptomsId() {
        return memberSymptomsId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getDescription() {
        return description;
    }

    public String getCondition() {
        return condition;
    }
    //endregion

    //region Setters
    public void setMemberSymptomsId(int memberSymptomsId) {
        this.memberSymptomsId = memberSymptomsId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    //endregion
}
