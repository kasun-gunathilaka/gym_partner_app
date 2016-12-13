package com.kasungunathilaka.Service;

//region Imported
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import com.kasungunathilaka.gympartner.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
//endregion

// </summary>
// Source File		: GymPartnerBackupService.java
// Package 			: com.kasungunathilaka.service
// Description		: Class Responsible for backing-up application database
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 18 June 2016		Kasun Gunathilak			    Created
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

public class GymPartnerBackupService extends Service {

    final long MAXFILEAGE = 2678400000L; // 1 month in milliseconds

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            //region Remove Month Old Backup
            String backupFilePath = Environment.getExternalStorageDirectory().getPath() + "/Gym Partner/database_backup";
            File backupFilePathFile = new File(backupFilePath);
            if (backupFilePathFile.isDirectory()) {
                String[] children = backupFilePathFile.list();
                for (String aChildren : children) {
                    File file = new File(backupFilePathFile, aChildren); //.delete();
                    long lastModified = file.lastModified();
                    if (lastModified + MAXFILEAGE < System.currentTimeMillis()) {
                        file.delete();
                    }
                }
            } else {
                new File(backupFilePath).mkdir();
            }
            //endregion

            //region Getting Backup File
            //Input File Details
            final String dbFilePath = getString(R.string.db_file_path);
            File dbFile = new File(dbFilePath);
            FileInputStream fileInputStream = new FileInputStream(dbFile);
            //endregion

            //region Backup Process
            //Output File Details
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Gym Partner/database_backup";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();


            String backupFileName = path + "/Gym Partner Backup " + date;
            File backupFileFile = new File(backupFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(backupFileFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            fileInputStream.close();
            stopSelf();
            //endregion
        } catch (Exception e) {
            e.printStackTrace();
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
