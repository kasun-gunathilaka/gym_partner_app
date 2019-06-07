package com.kasungunathilaka.gympartner;


//region Imported

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kasungunathilaka.util.AppCompatPreferenceActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
//endregion

// </summary>
// Source File		: SettingsActivity.java
// Package 			: com.kasungunathilaka.gympartner
// Description		: Class Contain Functionality of Setting of the Application
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 18 June 2016		Kasun Gunathilak			    Created from Template and Modified
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

public class SettingsActivity extends AppCompatPreferenceActivity {

    //region Private Methods
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    //endregion

    //region Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (getCallingActivity() != null) {
                Intent intent = new Intent("com.kasungunathilaka.gympartner.SETTINGS_CHANGE");
                sendBroadcast(intent);
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final View v = getListView();
        v.setPadding(16, 0, 16, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || DataBackupPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || MessagePreferenceFragment.class.getName().equals(fragmentName);
    }
    //endregion

    //region Settings Fragment
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference("notification_frequency"));
            bindPreferenceSummaryToValue(findPreference("notification_time"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataBackupPreferenceFragment extends PreferenceFragment {

        String backupFilePath = Environment.getExternalStorageDirectory().getPath() + "/Gym Partner/database_backup";
        File backupFilePathFile = new File(backupFilePath);
        ProgressDialog progressDialog;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_backup);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference("backup_frequency"));
            bindPreferenceSummaryToValue(findPreference("backup_time"));

            Preference restore = (Preference) findPreference("backup");
            restore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    createListPreferenceDialog();
                    return false;

                }
            });

            Preference backup = (Preference) findPreference("backupNow");
            backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    createBackup();
                    return false;

                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void createBackup() {
            try {
                showProgressDialog(true);
                final String dbFilePath = getString(R.string.db_file_path);
                File dbFile = new File(dbFilePath);
                FileInputStream fileInputStream = new FileInputStream(dbFile);

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
                showProgressDialog(false);
                Toast.makeText(getActivity(), "Backup Created!", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                ex.printStackTrace();
                showProgressDialog(false);
                Toast.makeText(getActivity(), "Backup Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        private void createListPreferenceDialog() {

            Dialog dialog;
            final String[] str = backupFilePathFile.list();
            AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.DialogAppTheme);
            b.setItems(str, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int position) {
                    try {
                        showProgressDialog(true);
                        final String dbFilePath = getString(R.string.db_file_path);
                        File dbFile = new File(dbFilePath);
                        FileOutputStream fileOutputStream = new FileOutputStream(dbFile);
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Gym Partner/database_backup";
                        String backupFileName = path + "/" + str[position];
                        File backupFileFile = new File(backupFileName);
                        FileInputStream fileInputStream = new FileInputStream(backupFileFile);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fileInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, length);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        fileInputStream.close();
                        showProgressDialog(false);
                        Toast.makeText(getActivity(), "Restore Successful!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        showProgressDialog(false);
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Restore Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            dialog = b.create();
            dialog.show();

        }

        private void showProgressDialog(boolean show) {
            if (show) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Restoring Database...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            } else {
                progressDialog.dismiss();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class MessagePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_message);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference("message"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
    //endregion
}
