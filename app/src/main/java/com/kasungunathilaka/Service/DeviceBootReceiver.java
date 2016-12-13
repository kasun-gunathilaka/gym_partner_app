package com.kasungunathilaka.Service;

//region Imported
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
//endregion

// </summary>
// Source File		: DeviceBootReceiver.java
// Package 			: com.kasungunathilaka.service
// Description		: Class Responsible for application starting background services
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 28 May 2016		Kasun Gunathilak			    Created
// 18 June 2016		Kasun Gunathilak			    Added Auto Backup
// </remarks>

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

public class DeviceBootReceiver extends BroadcastReceiver {

    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("com.kasungunathilaka.gympartner.SETTINGS_CHANGE")) {

            Calendar calendar = Calendar.getInstance();

            //region Getting SharedPreferences
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            Boolean autoBackup = sharedPreferences.getBoolean("auto_backup", true);
            Boolean notification = sharedPreferences.getBoolean("notification", true);

            String backup_time = sharedPreferences.getString("backup_time", "-1");
            String backup_frequency = sharedPreferences.getString("backup_frequency", "-1");
            String notification_frequency = sharedPreferences.getString("notification_frequency", "-1");
            String notification_time = sharedPreferences.getString("notification_time", "-1");
            //endregion

            //region Setting-Up Pending Intent
            Intent gymPartnerNotificationServiceIntent = new Intent(context, GymPartnerNotificationService.class);
            PendingIntent gymPartnerNotificationServicePendingIntent = PendingIntent.getService(context, 0, gymPartnerNotificationServiceIntent, 0);

            Intent gymPartnerBackupServiceIntent = new Intent(context, GymPartnerBackupService.class);
            PendingIntent gymPartnerBackupServicePendingIntent = PendingIntent.getService(context, 0, gymPartnerBackupServiceIntent, 0);
            //endregion

            //region Remove Previous Pending Intent
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(gymPartnerNotificationServicePendingIntent);
            alarmManager.cancel(gymPartnerBackupServicePendingIntent);
            //endregion

            long interval;

            //region Backup Set-up
            if (autoBackup) {
                switch (backup_frequency) {
                    case "-1":
                        interval = 86400000;
                        switch (backup_time) {
                            case "-1":
                                calendar.set(Calendar.HOUR, 12);
                                calendar.set(Calendar.MINUTE, 00);
                                calendar.set(Calendar.AM_PM, Calendar.PM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerBackupServicePendingIntent);
                                break;
                            case "0":
                                calendar.set(Calendar.HOUR, 10);
                                calendar.set(Calendar.MINUTE, 00);
                                calendar.set(Calendar.AM_PM, Calendar.PM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerBackupServicePendingIntent);
                                break;
                        }
                        break;
                    case "0":
                        interval = 1000L * 60 * 60 * 720;
                        switch (backup_time) {
                            case "-1":
                                calendar.set(Calendar.HOUR, 12);
                                calendar.set(Calendar.MINUTE, 00);
                                calendar.set(Calendar.AM_PM, Calendar.AM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerBackupServicePendingIntent);
                                break;
                            case "0":
                                calendar.set(Calendar.HOUR, 10);
                                calendar.set(Calendar.MINUTE, 00);
                                calendar.set(Calendar.AM_PM, Calendar.PM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerBackupServicePendingIntent);
                                break;
                        }
                        break;
                }
            }
            //endregion

            //region Notification Set-up
            if (notification) {
                switch (notification_frequency) {
                    case "-1":
                        interval = 1000 * 60 * 60 * 24;
                        switch (notification_time) {
                            case "-1":
                                calendar.set(Calendar.HOUR, 5);
                                calendar.set(Calendar.MINUTE, 30);
                                calendar.set(Calendar.AM_PM, Calendar.AM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerNotificationServicePendingIntent);
                                break;
                            case "0":
                                calendar.set(Calendar.HOUR, 6);
                                calendar.set(Calendar.MINUTE, 30);
                                calendar.set(Calendar.AM_PM, Calendar.AM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerNotificationServicePendingIntent);
                                break;
                            case "1":
                                calendar.set(Calendar.HOUR, 7);
                                calendar.set(Calendar.MINUTE, 30);
                                calendar.set(Calendar.AM_PM, Calendar.AM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerNotificationServicePendingIntent);
                                break;
                            case "2":
                                calendar.set(Calendar.HOUR, 8);
                                calendar.set(Calendar.MINUTE, 30);
                                calendar.set(Calendar.AM_PM, Calendar.AM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerNotificationServicePendingIntent);
                                break;
                            case "3":
                                calendar.set(Calendar.HOUR, 5);
                                calendar.set(Calendar.MINUTE, 30);
                                calendar.set(Calendar.AM_PM, Calendar.PM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerNotificationServicePendingIntent);
                                break;
                            case "4":
                                calendar.set(Calendar.HOUR, 6);
                                calendar.set(Calendar.MINUTE, 30);
                                calendar.set(Calendar.AM_PM, Calendar.PM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerNotificationServicePendingIntent);
                                break;
                            case "5":
                                calendar.set(Calendar.HOUR, 7);
                                calendar.set(Calendar.MINUTE, 30);
                                calendar.set(Calendar.AM_PM, Calendar.PM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerNotificationServicePendingIntent);
                                break;
                            case "6":
                                calendar.set(Calendar.HOUR, 8);
                                calendar.set(Calendar.MINUTE, 30);
                                calendar.set(Calendar.AM_PM, Calendar.PM);
                                if (calendar.getTime().before(Calendar.getInstance().getTime()))
                                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                                Log.d("Gym Partner", calendar.getTime().toString());
                                ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, gymPartnerNotificationServicePendingIntent);
                                break;
                        }
                        break;
                }
            }
            //endregion
        }
    }
}
