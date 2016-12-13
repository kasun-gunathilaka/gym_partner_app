package com.kasungunathilaka.business;

//region Imported

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kasungunathilaka.data.GymPartner;
import com.kasungunathilaka.domain.MemberSymptom;

import java.text.ParseException;
import java.util.ArrayList;
//endregion

// </summary>
// Source File		: MemberSymptomBusiness.java
// Package 			: com.kasungunathilaka.business
// Description		: Member Symptom Business class
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

public class MemberSymptomBusiness implements IBusiness<MemberSymptom> {

    //region Class Members
    private static final String MEMBER_SYMPTOM = "member_symptom";

    // member_symptom Table Columns names
    private static final String MEMBERSYMPTOMID = "membersymptomid";
    private static final String MEMBERID = "memberid";
    private static final String DESCRPTION = "descrption";
    private static final String CONDITION = "condition";

    private SQLiteDatabase sqLiteDatabase;
    private GymPartner gymPartner;
    String[] columns = new String[]{MEMBERSYMPTOMID
            , MEMBERID
            , DESCRPTION
            , CONDITION};
    //endregion

    //region Constructor
    public MemberSymptomBusiness(Context context, int version) {
        gymPartner = new GymPartner(context, null, null, version);
        sqLiteDatabase = gymPartner.getWritableDatabase();
    }
    //endregion

    //region Overridden Methods
    @Override
    public long insert(MemberSymptom memberSymptom) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEMBERID, memberSymptom.getMemberId());
        contentValues.put(DESCRPTION, memberSymptom.getDescription());
        contentValues.put(CONDITION, memberSymptom.getCondition());
        return sqLiteDatabase.insert(MEMBER_SYMPTOM, null, contentValues);
    }

    @Override
    public ArrayList<MemberSymptom> getAll() throws ParseException {
        ArrayList<MemberSymptom> result = new ArrayList<MemberSymptom>();
        Cursor cursor = sqLiteDatabase.query(MEMBER_SYMPTOM, columns, null, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            MemberSymptom memberSymptom = new MemberSymptom(cursor.getInt(cursor.getColumnIndex(MEMBERSYMPTOMID))
                    , cursor.getInt(cursor.getColumnIndex(MEMBERID))
                    , cursor.getString(cursor.getColumnIndex(DESCRPTION))
                    , cursor.getString(cursor.getColumnIndex(CONDITION)));
            result.add(memberSymptom);
        }
        cursor.close();
        return result;
    }

    @Override
    public MemberSymptom getById(int id) throws ParseException {
        Cursor cursor = sqLiteDatabase.query(MEMBER_SYMPTOM, columns, MEMBERSYMPTOMID + " = " + id, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            MemberSymptom memberSymptom = new MemberSymptom(cursor.getInt(cursor.getColumnIndex(MEMBERSYMPTOMID))
                    , cursor.getInt(cursor.getColumnIndex(MEMBERID))
                    , cursor.getString(cursor.getColumnIndex(DESCRPTION))
                    , cursor.getString(cursor.getColumnIndex(CONDITION)));
            cursor.close();
            return memberSymptom;
        } else {
            return null;
        }
    }

    @Override
    public void delete(int id) {
        try {
            sqLiteDatabase.delete(MEMBER_SYMPTOM, MEMBERSYMPTOMID + " = " + id, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(MemberSymptom memberSymptom) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MEMBERID, memberSymptom.getMemberId());
            contentValues.put(DESCRPTION, memberSymptom.getDescription());
            contentValues.put(CONDITION, memberSymptom.getCondition());
            sqLiteDatabase.update(MEMBER_SYMPTOM
                    , contentValues
                    , MEMBERSYMPTOMID + " = " + memberSymptom.getMemberSymptomsId()
                    , null);

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
    public ArrayList<MemberSymptom> getByMemberId(int id) {
        try {
            ArrayList<MemberSymptom> result = new ArrayList<MemberSymptom>();
            Cursor cursor = sqLiteDatabase.query(MEMBER_SYMPTOM, columns, MEMBERID + " = " + id, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                MemberSymptom memberSymptom = new MemberSymptom(cursor.getInt(cursor.getColumnIndex(MEMBERSYMPTOMID))
                        , cursor.getInt(cursor.getColumnIndex(MEMBERID))
                        , cursor.getString(cursor.getColumnIndex(DESCRPTION))
                        , cursor.getString(cursor.getColumnIndex(CONDITION)));
                result.add(memberSymptom);
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
