package com.kasungunathilaka.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kasungunathilaka.data.GymPartner;
import com.kasungunathilaka.domain.MemberEmergencyContact;

import java.util.ArrayList;

// </summary>
// Source File		: MemberEmergencyContactBusiness.java
// Package 			: com.kasungunathilaka.business
// Description		: Emergency Contact Business class
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 23 April 2016    Kasun Gunathilak			    Created
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

public class MemberEmergencyContactBusiness implements IBusiness<MemberEmergencyContact> {

    //region Class Members
    private static final String MEMBER_EMERGENCY_CONTACT = "member_emergency_contact";

    // member_emergency_contact Table Columns names
    private static final String MEMBEREMERGENCYCONTACTID = "memberemergencycontactid";
    private static final String MEMBERID = "memberid";
    private static final String NAME = "name";
    private static final String CONTACTNO = "contactno";

    private SQLiteDatabase sqLiteDatabase;
    private GymPartner gymPartner;
    String[] columns = new String[]{MEMBEREMERGENCYCONTACTID
            , MEMBERID
            , NAME
            , CONTACTNO};
    //endregion

    //region Constructor
    public MemberEmergencyContactBusiness(Context context, int version) {
        gymPartner = new GymPartner(context, null, null, version);
        sqLiteDatabase = gymPartner.getWritableDatabase();
    }
    //endregion

    //region Overridden Methods
    @Override
    public void close() {
        gymPartner.close();
    }

    @Override
    public long insert(MemberEmergencyContact memberEmergencyContact) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MEMBERID, memberEmergencyContact.getMemberId());
            contentValues.put(NAME, memberEmergencyContact.getName());
            contentValues.put(CONTACTNO, memberEmergencyContact.getContactNo());
            return sqLiteDatabase.insert(MEMBER_EMERGENCY_CONTACT, null, contentValues);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }

    }

    @Override
    public ArrayList<MemberEmergencyContact> getAll() {
        ArrayList<MemberEmergencyContact> result = new ArrayList<MemberEmergencyContact>();
        Cursor cursor = sqLiteDatabase.query(MEMBER_EMERGENCY_CONTACT, columns, null, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            MemberEmergencyContact memberEmergencyContact = new MemberEmergencyContact(cursor.getInt(cursor.getColumnIndex(MEMBEREMERGENCYCONTACTID))
                    , cursor.getInt(cursor.getColumnIndex(MEMBERID))
                    , cursor.getString(cursor.getColumnIndex(NAME))
                    , cursor.getString(cursor.getColumnIndex(CONTACTNO)));
            result.add(memberEmergencyContact);
        }
        cursor.close();
        return result;
    }

    @Override
    public MemberEmergencyContact getById(int id) {
        Cursor cursor = sqLiteDatabase.query(MEMBER_EMERGENCY_CONTACT, columns, MEMBEREMERGENCYCONTACTID + " = " + id, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            MemberEmergencyContact memberEmergencyContact = new MemberEmergencyContact(cursor.getInt(cursor.getColumnIndex(MEMBEREMERGENCYCONTACTID))
                    , cursor.getInt(cursor.getColumnIndex(MEMBERID))
                    , cursor.getString(cursor.getColumnIndex(NAME))
                    , cursor.getString(cursor.getColumnIndex(CONTACTNO)));
            cursor.close();
            return memberEmergencyContact;
        } else {
            return null;
        }
    }

    @Override
    public void delete(int id) {
        try {
            sqLiteDatabase.delete(MEMBER_EMERGENCY_CONTACT, MEMBEREMERGENCYCONTACTID + " = " + id, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(MemberEmergencyContact memberEmergencyContact) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MEMBERID, memberEmergencyContact.getMemberId());
            contentValues.put(NAME, memberEmergencyContact.getName());
            contentValues.put(CONTACTNO, memberEmergencyContact.getContactNo());
            sqLiteDatabase.update(MEMBER_EMERGENCY_CONTACT
                    , contentValues
                    , MEMBEREMERGENCYCONTACTID + " = " + memberEmergencyContact.getMemberEmergencyContactId()
                    , null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    //endregion

    //region Public Methods
    public ArrayList<MemberEmergencyContact> getByMemberId(int id) {
        try {
            ArrayList<MemberEmergencyContact> result = new ArrayList<MemberEmergencyContact>();
            Cursor cursor = sqLiteDatabase.query(MEMBER_EMERGENCY_CONTACT, columns, MEMBERID + " = " + id, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                MemberEmergencyContact memberEmergencyContact = new MemberEmergencyContact(cursor.getInt(cursor.getColumnIndex(MEMBEREMERGENCYCONTACTID))
                        , cursor.getInt(cursor.getColumnIndex(MEMBERID))
                        , cursor.getString(cursor.getColumnIndex(NAME))
                        , cursor.getString(cursor.getColumnIndex(CONTACTNO)));
                result.add(memberEmergencyContact);
            }
            cursor.close();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    //endregion
}
