package com.kasungunathilaka.business;

//region Imported

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kasungunathilaka.data.GymPartner;
import com.kasungunathilaka.domain.MemberSubscription;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//endregion

// </summary>
// Source File		: MemberSubscriptionBusiness.java
// Package 			: com.kasungunathilaka.business
// Description		: Membership Business class
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 24 April 2016    Kasun Gunathilak			    Created
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

public class MemberSubscriptionBusiness implements IBusiness<MemberSubscription> {

    //region Class Members
    private static final String MEMBER_SUBSCRIPTION = "member_subscription";

    private static final String MEMBER = "member";

    // member_subscription Table Columns names
    private static final String MEMBERSUBSCRIPTIONID = "membersubscriptionid";
    private static final String MEMBERID = "memberid";
    private static final String SUBSCRIPTIONID = "subscriptionid";
    private static final String STARTDATE = "startdate";
    private static final String ENDDATE = "enddate";
    private static final String ISACTIVE = "isactive";
    private static final String ACTIVE_USERS = " isactive == 1";

    // member Table Columns names
    //private static final String MEMBERID = "memberid";
    private static final String MEMBERCODE = "membercode";
    private static final String NAME = "name";
    private static final String NIC = "nic";
    private static final String CONTACTNO = "contactno";
    private static final String ADDRESS = "address";
    private static final String BLOODTYPE = "bloodtype";
    private static final String BIRTHDAY = "birthday";
    private static final String AGE = "age";
    private static final String IMAGE = "image";


    private SQLiteDatabase sqLiteDatabase;
    private GymPartner gymPartner;
    String[] columns = new String[]{MEMBERSUBSCRIPTIONID
            , MEMBERID
            , SUBSCRIPTIONID
            , STARTDATE
            , ENDDATE
            , ISACTIVE};

    String[] memberColumns = new String[]{MEMBERID
            , MEMBERCODE
            , NAME
            , NIC
            , CONTACTNO
            , ADDRESS
            , BLOODTYPE
            , BIRTHDAY
            , AGE
            , IMAGE};
    //endregion

    //region Constructors
    public MemberSubscriptionBusiness(Context context, int version) {
        gymPartner = new GymPartner(context, null, null, version);
        sqLiteDatabase = gymPartner.getWritableDatabase();
    }
    //endregion

    //region Overridden Methods
    @Override
    public long insert(MemberSubscription memberSubscription) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SUBSCRIPTIONID, memberSubscription.getSubscriptionId());
            contentValues.put(MEMBERID, memberSubscription.getMemberId());
            contentValues.put(STARTDATE, memberSubscription.getStartDate().toString());
            contentValues.put(ENDDATE, memberSubscription.getEndDate().toString());
            contentValues.put(ISACTIVE, 1);
            updateOldSubscriptions(memberSubscription.getMemberId());
            long savedId = sqLiteDatabase.insert(MEMBER_SUBSCRIPTION, null, contentValues);
            return savedId;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }

    }

    @Override
    public void update(MemberSubscription memberSubscription) throws Exception {
        sqLiteDatabase.delete(MEMBER_SUBSCRIPTION, MEMBERSUBSCRIPTIONID + " = " + memberSubscription.getMemberSubscriptionId(), null);
        this.insert(memberSubscription);
    }

    @Override
    public ArrayList<MemberSubscription> getAll() throws ParseException {
        try {
            ArrayList<MemberSubscription> result = new ArrayList<MemberSubscription>();
            Cursor cursor = sqLiteDatabase.query(MEMBER_SUBSCRIPTION, columns, ACTIVE_USERS, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                MemberSubscription memberEmergencyContact = new MemberSubscription(cursor.getInt(cursor.getColumnIndex(MEMBERSUBSCRIPTIONID))
                        , cursor.getInt(cursor.getColumnIndex(MEMBERID))
                        , cursor.getInt(cursor.getColumnIndex(SUBSCRIPTIONID))
                        , getDate(cursor.getString(cursor.getColumnIndex(STARTDATE)))
                        , getDate(cursor.getString(cursor.getColumnIndex(ENDDATE)))
                        , cursor.getInt(cursor.getColumnIndex(ISACTIVE)));
                result.add(memberEmergencyContact);
            }
            cursor.close();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public MemberSubscription getById(int id) throws ParseException {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void close() {
        gymPartner.close();
    }
    //endregion

    //region Public Methods
    public ArrayList<MemberSubscription> getExpiringMember(String expireDate) throws ParseException {
        ArrayList<MemberSubscription> expiringMemberSubscriptionList = new ArrayList<MemberSubscription>();
        ArrayList<MemberSubscription> activeMemberList = this.getAll();
        for (MemberSubscription memberSubscription : activeMemberList) {
            if (memberSubscription != null) {
                String subscriptionDate = getStringDate(memberSubscription.getEndDate().toString());
                if (subscriptionDate.contentEquals(expireDate)) {
                    expiringMemberSubscriptionList.add(memberSubscription);
                }
            }
        }
        return expiringMemberSubscriptionList;
    }

    public MemberSubscription getById(int id, int subscriptionId) throws ParseException {
        try {
            Cursor cursor = sqLiteDatabase.query(MEMBER_SUBSCRIPTION, columns
                    , MEMBERID + " = " + id
                            + " AND " + MEMBERSUBSCRIPTIONID + " = "
                            + subscriptionId + " AND" + ACTIVE_USERS, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                MemberSubscription memberSubscription = new MemberSubscription(cursor.getInt(cursor.getColumnIndex(MEMBERSUBSCRIPTIONID))
                        , cursor.getInt(cursor.getColumnIndex(MEMBERID))
                        , cursor.getInt(cursor.getColumnIndex(SUBSCRIPTIONID))
                        , getDate(cursor.getString(cursor.getColumnIndex(STARTDATE)))
                        , getDate(cursor.getString(cursor.getColumnIndex(ENDDATE)))
                        , cursor.getInt(cursor.getColumnIndex(ISACTIVE)));
                cursor.close();
                return memberSubscription;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public ArrayList<MemberSubscription> getByMemberId(int id) {
        try {
            ArrayList<MemberSubscription> result = new ArrayList<MemberSubscription>();
            Cursor cursor = sqLiteDatabase.query(MEMBER_SUBSCRIPTION, columns, MEMBERID + " = " + id, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                MemberSubscription memberEmergencyContact = new MemberSubscription(cursor.getInt(cursor.getColumnIndex(MEMBERSUBSCRIPTIONID))
                        , cursor.getInt(cursor.getColumnIndex(MEMBERID))
                        , cursor.getInt(cursor.getColumnIndex(SUBSCRIPTIONID))
                        , getDate(cursor.getString(cursor.getColumnIndex(STARTDATE)))
                        , getDate(cursor.getString(cursor.getColumnIndex(ENDDATE)))
                        , cursor.getInt(cursor.getColumnIndex(ISACTIVE)));
                result.add(memberEmergencyContact);
            }
            cursor.close();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<MemberSubscription> getBySubscriptionId(int id) {
        try {
            ArrayList<MemberSubscription> result = new ArrayList<MemberSubscription>();
            Cursor cursor = sqLiteDatabase.query(MEMBER_SUBSCRIPTION, columns, SUBSCRIPTIONID + " = " + id, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                MemberSubscription memberEmergencyContact = new MemberSubscription(cursor.getInt(cursor.getColumnIndex(MEMBERSUBSCRIPTIONID))
                        , cursor.getInt(cursor.getColumnIndex(MEMBERID))
                        , cursor.getInt(cursor.getColumnIndex(SUBSCRIPTIONID))
                        , getDate(cursor.getString(cursor.getColumnIndex(STARTDATE)))
                        , getDate(cursor.getString(cursor.getColumnIndex(ENDDATE)))
                        , cursor.getInt(cursor.getColumnIndex(ISACTIVE)));
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

    //region Private Methods
    private String getStringDate(String dateString) throws ParseException {
        DateFormat readFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = null;
        try {
            date = readFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String formatedDate = cal.get(Calendar.DATE)
                + "-"
                + (cal.get(Calendar.MONTH) + 1)
                + "-"
                + cal.get(Calendar.YEAR);
        return formatedDate;
    }

    private Date getDate(String dateString) throws ParseException {
        DateFormat readFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat writeFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = readFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Date formatedDate = writeFormat.parse(cal.get(Calendar.DATE)
                + "-"
                + (cal.get(Calendar.MONTH) + 1)
                + "-"
                + cal.get(Calendar.YEAR));
        return formatedDate;
    }

    private void updateOldSubscriptions(int memberId) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ISACTIVE, 0);
            sqLiteDatabase.execSQL("UPDATE "
                    + MEMBER_SUBSCRIPTION + " SET "
                    + ISACTIVE + " = 0 WHERE "
                    + MEMBERID + " = " + memberId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion
}
