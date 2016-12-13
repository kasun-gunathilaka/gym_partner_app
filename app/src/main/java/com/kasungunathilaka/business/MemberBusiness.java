package com.kasungunathilaka.business;


//region Imported
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kasungunathilaka.data.GymPartner;
import com.kasungunathilaka.domain.Member;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//endregion

// </summary>
// Source File		: IBusiness.java
// Package 			: com.kasungunathilaka.business
// Description		: Member Business class
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

public class MemberBusiness implements IBusiness<Member> {

    //region Class Members
    private static final String MEMBER = "member";

    // member Table Columns names
    private static final String MEMBERID = "memberid";
    private static final String MEMBERCODE = "membercode";
    private static final String NAME = "name";
    private static final String NIC = "nic";
    private static final String CONTACTNO = "contactno";
    private static final String ADDRESS = "address";
    private static final String BLOODTYPE = "bloodtype";
    private static final String BIRTHDAY = "birthday";
    private static final String AGE = "age";
    private static final String IMAGE = "image";
    String[] columns = new String[]{MEMBERID
            , MEMBERCODE
            , NAME
            , NIC
            , CONTACTNO
            , ADDRESS
            , BLOODTYPE
            , BIRTHDAY
            , AGE
            , IMAGE};
    private SQLiteDatabase sqLiteDatabase;
    private GymPartner gymPartner;
    //endregion

    //region Constructor
    public MemberBusiness(Context context, int version) {
        gymPartner = new GymPartner(context, null, null, version);
        sqLiteDatabase = gymPartner.getWritableDatabase();
    }
    //endregion

    //region Overridden Methods
    @Override
    public long insert(Member member) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MEMBERCODE, member.getMemberCode());
            contentValues.put(NAME, member.getName());
            contentValues.put(NIC, member.getNic());
            contentValues.put(CONTACTNO, member.getContactNo());
            contentValues.put(ADDRESS, member.getAddress());
            contentValues.put(BLOODTYPE, member.getBloodType());
            contentValues.put(BIRTHDAY, member.getBirthday().toString());
            contentValues.put(AGE, member.getAge());
            contentValues.put(IMAGE, member.getImage());
            return sqLiteDatabase.insert(MEMBER, null, contentValues);
        } catch (Exception x) {
            x.printStackTrace();
            return -1;
        }

    }

    @Override
    public ArrayList<Member> getAll() throws ParseException {
        ArrayList<Member> result = new ArrayList<Member>();
        Cursor cursor = sqLiteDatabase.query(MEMBER, columns, null, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Member member = new Member(cursor.getInt(cursor.getColumnIndex(MEMBERID))
                    , cursor.getString(cursor.getColumnIndex(MEMBERCODE))
                    , cursor.getString(cursor.getColumnIndex(NAME))
                    , cursor.getString(cursor.getColumnIndex(NIC))
                    , cursor.getString(cursor.getColumnIndex(CONTACTNO))
                    , cursor.getString(cursor.getColumnIndex(ADDRESS))
                    , cursor.getString(cursor.getColumnIndex(BLOODTYPE))
                    , getDate(cursor.getString(cursor.getColumnIndex(BIRTHDAY)))
                    , cursor.getInt(cursor.getColumnIndex(AGE))
                    , cursor.getBlob(cursor.getColumnIndex(IMAGE)));
            result.add(member);

        }
        cursor.close();
        return result;
    }

    @Override
    public Member getById(int id) throws ParseException {
        Cursor cursor = sqLiteDatabase.query(MEMBER, columns, MEMBERID + " = " + id, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Member member = new Member(cursor.getInt(cursor.getColumnIndex(MEMBERID))
                    , cursor.getString(cursor.getColumnIndex(MEMBERCODE))
                    , cursor.getString(cursor.getColumnIndex(NAME))
                    , cursor.getString(cursor.getColumnIndex(NIC))
                    , cursor.getString(cursor.getColumnIndex(CONTACTNO))
                    , cursor.getString(cursor.getColumnIndex(ADDRESS))
                    , cursor.getString(cursor.getColumnIndex(BLOODTYPE))
                    , getDate(cursor.getString(cursor.getColumnIndex(BIRTHDAY)))
                    , cursor.getInt(cursor.getColumnIndex(AGE))
                    , cursor.getBlob(cursor.getColumnIndex(IMAGE)));
            cursor.close();
            return member;
        } else {
            return null;
        }

    }

    @Override
    public void delete(int id) {
        try {
            sqLiteDatabase.delete(MEMBER, MEMBERID + " = " + id, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Member member) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MEMBERCODE, member.getMemberCode());
            contentValues.put(NAME, member.getName());
            contentValues.put(NIC, member.getNic());
            contentValues.put(CONTACTNO, member.getContactNo());
            contentValues.put(ADDRESS, member.getAddress());
            contentValues.put(BLOODTYPE, member.getBloodType());
            contentValues.put(BIRTHDAY, member.getBirthday().toString());
            contentValues.put(AGE, member.getAge());
            contentValues.put(IMAGE, member.getImage());
            sqLiteDatabase.update(MEMBER
                    , contentValues
                    , MEMBERID + " = " + member.getMemberId()
                    , null);
            deleteMemberDetails(member);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() {
        gymPartner.close();
    }
    //endregion

    //region Public Methods
    public int getLatestId() {
        Cursor cursor = sqLiteDatabase.query(MEMBER, columns, null, null, null, null, MEMBERID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(MEMBERID));
        } else {
            return 0;
        }
    }

    public boolean exist(String nic) throws ParseException {
        Cursor cursor = sqLiteDatabase.query(MEMBER, columns, NIC + " = '" + nic + "'", null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Member member = new Member(cursor.getInt(cursor.getColumnIndex(MEMBERID))
                    , cursor.getString(cursor.getColumnIndex(MEMBERCODE))
                    , cursor.getString(cursor.getColumnIndex(NAME))
                    , cursor.getString(cursor.getColumnIndex(NIC))
                    , cursor.getString(cursor.getColumnIndex(CONTACTNO))
                    , cursor.getString(cursor.getColumnIndex(ADDRESS))
                    , cursor.getString(cursor.getColumnIndex(BLOODTYPE))
                    , getDate(cursor.getString(cursor.getColumnIndex(BIRTHDAY)))
                    , cursor.getInt(cursor.getColumnIndex(AGE))
                    , cursor.getBlob(cursor.getColumnIndex(IMAGE)));
            cursor.close();
            return false;
        } else {
            return true;
        }
    }
    //endregion

    //region Private Method
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

    private void deleteMemberDetails(Member member) {
        try {
            final String MEMBER_SYMPTOM = "member_symptom";
            String MEMBER_EMERGENCY_CONTACT = "member_emergency_contact";
            sqLiteDatabase.delete(MEMBER_SYMPTOM, MEMBERID + " = " + member.getMemberId(), null);
            sqLiteDatabase.delete(MEMBER_EMERGENCY_CONTACT, MEMBERID + " = " + member.getMemberId(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion
}
