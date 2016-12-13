package com.kasungunathilaka.Service;

//region Imported

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat;

import com.kasungunathilaka.business.MemberSubscriptionBusiness;
import com.kasungunathilaka.domain.MemberSubscription;
import com.kasungunathilaka.gympartner.MainActivity;
import com.kasungunathilaka.gympartner.R;

import java.util.ArrayList;
import java.util.Calendar;
//endregion

// </summary>
// Source File		: GymPartnerNotificationService.java
// Package 			: com.kasungunathilaka.service
// Description		: Class Responsible for Application Notification
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 28 May 2016		Kasun Gunathilak			    Created
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

public class GymPartnerNotificationService extends Service {

    int notificationId = 0001;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Calendar cal = Calendar.getInstance();
            String calenderDate = (cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR));
            MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(GymPartnerNotificationService.this, 1);
            ArrayList<MemberSubscription> memberSubscriptionList = memberSubscriptionBusiness.getExpiringMember(calenderDate);
            memberSubscriptionBusiness.close();
            if (memberSubscriptionList.size() > 0) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Membership Expiration")
                                .setContentText("(" + memberSubscriptionList.size() + ") memberships Expiration");
                Intent resultIntent = new Intent(this, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotificationManager.notify(notificationId, mBuilder.build());
            }
            stopSelf();
        } catch (Exception ex) {
            ex.printStackTrace();
            stopSelf();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
