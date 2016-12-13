package com.kasungunathilaka.business;

//region Imported
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kasungunathilaka.data.GymPartner;
import com.kasungunathilaka.domain.Subscription;

import java.text.ParseException;
import java.util.ArrayList;
//endregion

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

public class SubscriptionBusiness implements IBusiness<Subscription> {

    //region Class Members
    private static final String SUBSCRIPTION = "subscription";

    // subscription Table Columns names
    private static final String SUBSCRIPTIONID = "subscriptionid";
    private static final String NAME = "name";
    private static final String DURATION = "duration";

    private SQLiteDatabase sqLiteDatabase;
    private GymPartner gymPartner;
    String[] columns = new String[]{SUBSCRIPTIONID
            , NAME
            , DURATION};
    //endregion

    //region Constructor
    public SubscriptionBusiness(Context context, int version) {
        gymPartner = new GymPartner(context, null, null, version);
        sqLiteDatabase = gymPartner.getWritableDatabase();
    }
    //endregion

    //region Overridden Methods
    @Override
    public long insert(Subscription subscription) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, subscription.getName());
        contentValues.put(DURATION, subscription.getDuration());
        return sqLiteDatabase.insert(SUBSCRIPTION, null, contentValues);
    }

    @Override
    public void update(Subscription subscription) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NAME, subscription.getName());
            contentValues.put(DURATION, subscription.getDuration());
            sqLiteDatabase.update(SUBSCRIPTION
                    , contentValues
                    , SUBSCRIPTIONID + " = " + subscription.getSubscription()
                    , null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void delete(int id) {
        try {
            sqLiteDatabase.delete(SUBSCRIPTION, SUBSCRIPTIONID + " = " + id, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ArrayList<Subscription> getAll() throws ParseException {
        try {
            ArrayList<Subscription> result = new ArrayList<Subscription>();
            Cursor cursor = sqLiteDatabase.query(SUBSCRIPTION, columns, null, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Subscription subscription = new Subscription(cursor.getInt(cursor.getColumnIndex(SUBSCRIPTIONID))
                        , cursor.getString(cursor.getColumnIndex(NAME))
                        , cursor.getInt(cursor.getColumnIndex(DURATION)));
                result.add(subscription);
            }
            cursor.close();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public Subscription getById(int id) {
        try {
            Cursor cursor = sqLiteDatabase.query(SUBSCRIPTION, columns, SUBSCRIPTIONID + " = " + id, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                Subscription subscription = new Subscription(cursor.getInt(cursor.getColumnIndex(SUBSCRIPTIONID))
                        , cursor.getString(cursor.getColumnIndex(NAME))
                        , cursor.getInt(cursor.getColumnIndex(DURATION)));
                cursor.close();
                return subscription;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public void close() {
        gymPartner.close();
    }
    //endregion
}
