package com.kasungunathilaka.domain;

import java.util.Date;

// </summary>
// Source File		: Member.java
// Package 			: com.kasungunathilaka.domain
// Description		: Member Domain Class
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

public class Member {

    //region Class Members
    private int memberId;
    private String memberCode;
    private String name;
    private String nic;
    private String contactNo;
    private String address;
    private String bloodType;
    private Date birthday;
    private int age;
    private byte[] image;
    //endregion

    //region Constructors
    public Member() {
    }

    public Member(int memberId
            , String memberCode
            , String name
            , String nic
            , String contactNo
            , String address
            , String bloodType
            , Date birthday
            , int age
            , byte[] image) {
        this.memberId = memberId;
        this.memberCode = memberCode;
        this.name = name;
        this.nic = nic;
        this.contactNo = contactNo;
        this.address = address;
        this.bloodType = bloodType;
        this.birthday = birthday;
        this.age = age;
        this.image = image;
    }
    //endregion

    //region Getters
    public int getMemberId() {
        return memberId;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public String getName() {
        return name;
    }

    public String getNic() {
        return nic;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getAddress() {
        return address;
    }

    public String getBloodType() {
        return bloodType;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getAge() {
        return age;
    }

    public byte[] getImage() {
        return image;
    }
    //endregion

    //region Setters
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAge(byte[] image) {
        this.image = image;
    }
    //endregion

    @Override
    public String toString() {
        return getMemberCode();
    }
}
