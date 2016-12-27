package com.kasungunathilaka.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasungunathilaka.gympartner.BuildConfig;
import com.kasungunathilaka.gympartner.MainActivity;
import com.kasungunathilaka.gympartner.R;
import com.kasungunathilaka.showcase.MaterialShowcaseSequence;
import com.kasungunathilaka.showcase.MaterialShowcaseView;
import com.kasungunathilaka.showcase.ShowcaseConfig;
import com.kasungunathilaka.util.SharedPreferencesName;

// </summary>
// Source File		: AboutDialog.java
// Package 			: com.kasungunathilaka.dialog
// Description		: Dialog containing info about Developer and Application
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 28 June 2016     Kasun Gunathilak			    Created
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


public class AboutDialog {

    public AboutDialog(final Context context, final LayoutInflater layoutInflater) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View dialogView = layoutInflater.inflate(R.layout.dialog_view_about, null);
        alertDialogBuilder.setView(dialogView);
        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
        final Button bAboutDeveloper = (Button) dialogView.findViewById(R.id.bAboutDeveloper);
        ((TextView) dialogView.findViewById(R.id.tvName)).setText("Gym Partner v" + BuildConfig.VERSION_NAME);
        final AlertDialog parentAlertDialog = alertDialogBuilder.create();
        bAboutDeveloper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    View dialogView = layoutInflater.inflate(R.layout.dialog_view_about_developer, null);
                    alertDialogBuilder.setView(dialogView);
                    ImageView ivBack = (ImageView) dialogView.findViewById(R.id.ivBack);
                    ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
                    TextView tvEmail = (TextView) dialogView.findViewById(R.id.tvEmail);
                    tvEmail.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    tvEmail.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("plain/text");
                                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"developer.kasun.gunathilaka@gmail.com"});
                                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Gym Partner");
                                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                                context.startActivity(emailIntent);
                                parentAlertDialog.dismiss();
                                alertDialog.dismiss();
                            }
                            return false;
                        }
                    });
                    ivBack.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                alertDialog.dismiss();
                            }
                            return false;
                        }
                    });
                    ivClose.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                parentAlertDialog.dismiss();
                                alertDialog.dismiss();
                            }
                            return false;
                        }
                    });
                    alertDialog.show();
                }
                return false;
            }

        });
        ivClose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    parentAlertDialog.dismiss();
                }
                return false;
            }
        });
        parentAlertDialog.show();
    }
}
