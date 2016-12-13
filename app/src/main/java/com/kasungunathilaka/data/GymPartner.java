package com.kasungunathilaka.data;

//region Imported
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//endregion

// </summary>
// Source File		: GymPartner.java
// Package 			: com.kasungunathilaka.data
// Description		: Application DataBase File
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


public class GymPartner extends SQLiteOpenHelper {


    //region Data-base Naming
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "GymPartnerdb";

    // Contacts table name
    private static final String MEMBER = "member";
    private static final String MEMBER_SYMPTOM = "member_symptom";
    private static final String SUBSCRIPTION = "subscription";
    private static final String MEMBER_EMERGENCY_CONTACT = "member_emergency_contact";
    private static final String MEMBER_SUBSCRIPTION = "member_subscription";

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

    // member_symptom Table Columns names
    private static final String MEMBERSYMPTOMID = "membersymptomid";
    //private static final String MEMBERID = "memberid";
    private static final String DESCRPTION = "descrption";
    private static final String CONDITION = "condition";

    // subscription Table Columns names
    private static final String SUBSCRIPTIONID = "subscriptionid";
    //private static final String NAME = "name";
    private static final String DURATION = "duration";

    // member_emergency_contact Table Columns names
    private static final String MEMBEREMERGENCYCONTACTID = "memberemergencycontactid";
    //private static final String MEMBERID = "memberid";
    //private static final String NAME = "name";
    //private static final String CONTACTNO = "contactno";

    // member_subscription Table Columns names
    private static final String MEMBERSUBSCRIPTIONID = "membersubscriptionid";
    //private static final String MEMBERID = "memberid";
    private static final String STARTDATE = "startdate";
    private static final String ENDDATE = "enddate";
    private static final String ISACTIVE = "isactive";
    //endregion

    public GymPartner(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + MEMBER + "("
                + MEMBERID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MEMBERCODE + " TEXT NOT NULL,"
                + NAME + " TEXT NOT NULL,"
                + NIC + " TEXT NOT NULL,"
                + CONTACTNO + " TEXT,"
                + ADDRESS + " TEXT,"
                + BLOODTYPE + " TEXT,"
                + BIRTHDAY + " TEXT,"
                + AGE + " INTEGER,"
                + IMAGE + " BLOB"
                + ");");

        db.execSQL("CREATE TABLE " + MEMBER_SYMPTOM + "("
                + MEMBERSYMPTOMID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MEMBERID + " INTEGER NOT NULL,"
                + DESCRPTION + " TEXT NOT NULL,"
                + CONDITION + " TEXT NOT NULL "
                + ");");

        db.execSQL("CREATE TABLE " + SUBSCRIPTION + "("
                + SUBSCRIPTIONID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT NOT NULL,"
                + DURATION + " INTEGER NOT NULL"
                + ");");

        db.execSQL("CREATE TABLE " + MEMBER_EMERGENCY_CONTACT + "("
                + MEMBEREMERGENCYCONTACTID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MEMBERID + " INTEGER NOT NULL,"
                + NAME + " TEXT NOT NULL,"
                + CONTACTNO + " TEXT NOT NULL"
                + ");");

        db.execSQL("CREATE TABLE " + MEMBER_SUBSCRIPTION + "("
                + MEMBERSUBSCRIPTIONID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MEMBERID + " INTEGER NOT NULL,"
                + SUBSCRIPTIONID + " INTEGER NOT NULL,"
                + STARTDATE + " TEXT NOT NULL,"
                + ENDDATE + " TEXT NOT NULL,"
                + ISACTIVE + " INTEGER NOT NULL"
                + ");");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEMBER);
        db.execSQL("DROP TABLE IF EXISTS " + MEMBER_SYMPTOM);
        db.execSQL("DROP TABLE IF EXISTS " + SUBSCRIPTION);
        db.execSQL("DROP TABLE IF EXISTS " + MEMBER_EMERGENCY_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + MEMBER_SUBSCRIPTION);
        onCreate(db);

    }
}
