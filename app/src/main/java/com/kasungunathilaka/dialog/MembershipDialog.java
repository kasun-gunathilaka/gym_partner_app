package com.kasungunathilaka.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.kasungunathilaka.List.MemberListActivity;
import com.kasungunathilaka.List.MembershipListActivity;
import com.kasungunathilaka.business.MemberBusiness;
import com.kasungunathilaka.business.MemberSubscriptionBusiness;
import com.kasungunathilaka.business.SubscriptionBusiness;
import com.kasungunathilaka.domain.Member;
import com.kasungunathilaka.domain.MemberSubscription;
import com.kasungunathilaka.domain.Subscription;
import com.kasungunathilaka.gympartner.MainActivity;
import com.kasungunathilaka.gympartner.MemberDetailActivity;
import com.kasungunathilaka.gympartner.R;
import com.kasungunathilaka.view.CustomSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


// </summary>
// Source File		: MembershipDialog.java
// Package 			: com.kasungunathilaka.dialog
// Description		: Renew or edit Membership Dialog
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 21 April 2016    Kasun Gunathilak			    Created
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

public class MembershipDialog {

    public MembershipDialog(final Context context, LayoutInflater layoutInflater) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = layoutInflater;
            View dialogView = inflater.inflate(R.layout.dialog_view_membership, null);
            alertDialogBuilder.setView(dialogView);
            final CustomSpinner sMember = (CustomSpinner) dialogView.findViewById(R.id.sMember);
            final CustomSpinner sSubscription = (CustomSpinner) dialogView.findViewById(R.id.sSubscription);
            final EditText etSubscriptionStartDate = (EditText) dialogView.findViewById(R.id.etSubscriptionStartDate);
            final EditText etSubscriptionEndDate = (EditText) dialogView.findViewById(R.id.etSubscriptionEndDate);
            ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
            ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
            alertDialogBuilder.setCancelable(false);
            etSubscriptionStartDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etSubscriptionStartDate.setError(null);
                        etSubscriptionStartDate.setInputType(InputType.TYPE_NULL);
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(context, R.style.Base_DialogCalendarAppTheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etSubscriptionStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }
                                , c.get(Calendar.YEAR)
                                , c.get(Calendar.MONTH)
                                , c.get(Calendar.DATE)).show();
                    }
                    return false;
                }
            });

            etSubscriptionEndDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etSubscriptionEndDate.setError(null);
                        etSubscriptionEndDate.setInputType(InputType.TYPE_NULL);
                        Calendar c = Calendar.getInstance();
                        Subscription subscription = (Subscription) sSubscription.getSelectedItem();
                        if (subscription != null) {
                            c.add(Calendar.MONTH, subscription.getDuration());
                        }
                        new DatePickerDialog(context, R.style.Base_DialogCalendarAppTheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etSubscriptionEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }
                                , c.get(Calendar.YEAR)
                                , c.get(Calendar.MONTH)
                                , c.get(Calendar.DATE)).show();
                    }
                    return false;
                }
            });
            SubscriptionBusiness subscriptionBusiness = new SubscriptionBusiness(context, 0);
            ArrayList<Subscription> subscriptionList = subscriptionBusiness.getAll();
            ArrayAdapter<Subscription> spinnerArrayAdapter
                    = new ArrayAdapter<Subscription>(context
                    , android.R.layout.simple_spinner_item
                    , subscriptionList);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sSubscription.setAdapter(spinnerArrayAdapter);
            sSubscription.setTitle("Select Subscription");

            MemberBusiness memberBusiness = new MemberBusiness(context, 0);
            ArrayList<Member> memberList = memberBusiness.getAll();
            ArrayAdapter<Member> memberSpinnerArrayAdapter
                    = new ArrayAdapter<Member>(context
                    , android.R.layout.simple_spinner_item
                    , memberList);
            memberSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sMember.setAdapter(memberSpinnerArrayAdapter);
            sMember.setTitle("Select Member");
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            ivSave.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (sMember.getSelectedItemPosition() < 0) {
                            sMember.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        } else if (sSubscription.getSelectedItemPosition() < 0) {
                            sSubscription.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        } else if (etSubscriptionStartDate.getText().toString().contentEquals("")) {
                            etSubscriptionStartDate.setError("Start Date can't be empty");
                        } else if (etSubscriptionEndDate.getText().toString().contentEquals("")) {
                            etSubscriptionEndDate.setError("Expire Date can't be empty");
                        } else {
                            try {
                                Subscription subscription = (Subscription) sSubscription.getSelectedItem();
                                Member member = (Member) sMember.getSelectedItem();
                                MemberSubscription memberSubscription = new MemberSubscription(0
                                        , member.getMemberId()
                                        , subscription.getSubscription()
                                        , new SimpleDateFormat("yyyy-MM-dd").parse(etSubscriptionStartDate.getText().toString())
                                        , new SimpleDateFormat("yyyy-MM-dd").parse(etSubscriptionEndDate.getText().toString())
                                        , 1);
                                MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(context, 1);
                                memberSubscriptionBusiness.insert(memberSubscription);
                                memberSubscriptionBusiness.close();
                                if (context.getClass() == MainActivity.class) {
                                    ((MainActivity) context).resume();
                                } else if (context.getClass() == MembershipListActivity.class) {
                                    ((MembershipListActivity) context).resume();
                                } else if (context.getClass() == MemberListActivity.class) {
                                    ((MemberListActivity) context).resume();
                                } else if (context.getClass() == MemberDetailActivity.class) {
                                    ((MemberDetailActivity) context).finish();
                                    context.startActivity(new Intent(context, MainActivity.class));
                                }
                                alertDialog.cancel();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                    }
                    return false;
                }
            });
            ivClose.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        alertDialog.cancel();
                    }
                    return false;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public MembershipDialog(final Context context, LayoutInflater layoutInflater, Member member) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = layoutInflater;
            View dialogView = inflater.inflate(R.layout.dialog_view_membership, null);
            alertDialogBuilder.setView(dialogView);
            final CustomSpinner sMember = (CustomSpinner) dialogView.findViewById(R.id.sMember);
            final CustomSpinner sSubscription = (CustomSpinner) dialogView.findViewById(R.id.sSubscription);
            final EditText etSubscriptionStartDate = (EditText) dialogView.findViewById(R.id.etSubscriptionStartDate);
            final EditText etSubscriptionEndDate = (EditText) dialogView.findViewById(R.id.etSubscriptionEndDate);
            ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
            ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
            etSubscriptionStartDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etSubscriptionStartDate.setError(null);
                        etSubscriptionStartDate.setInputType(InputType.TYPE_NULL);
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(context, R.style.Base_DialogCalendarAppTheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etSubscriptionStartDate.setText(String.format(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                                etSubscriptionStartDate.setError(null);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();
                    }
                    return false;
                }
            });

            etSubscriptionEndDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etSubscriptionEndDate.setError(null);
                        etSubscriptionEndDate.setInputType(InputType.TYPE_NULL);
                        Calendar c = Calendar.getInstance();
                        Subscription subscription = (Subscription) sSubscription.getSelectedItem();
                        if (subscription != null) {
                            c.add(Calendar.MONTH, subscription.getDuration());
                        }
                        new DatePickerDialog(context, R.style.Base_DialogCalendarAppTheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etSubscriptionEndDate.setText(String.format(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                                etSubscriptionEndDate.setError(null);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();
                    }
                    return false;
                }
            });
            alertDialogBuilder.setCancelable(false);
            SubscriptionBusiness subscriptionBusiness = new SubscriptionBusiness(context, 0);
            ArrayList<Subscription> subscriptionList = subscriptionBusiness.getAll();
            ArrayAdapter<Subscription> spinnerArrayAdapter
                    = new ArrayAdapter<Subscription>(context
                    , android.R.layout.simple_spinner_item
                    , subscriptionList);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sSubscription.setAdapter(spinnerArrayAdapter);
            sSubscription.setTitle("Select Subscription");

            ArrayList<Member> memberList = new ArrayList<Member>();
            memberList.add(member);
            ArrayAdapter<Member> memberSpinnerArrayAdapter
                    = new ArrayAdapter<Member>(context
                    , android.R.layout.simple_spinner_item
                    , memberList);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sMember.setAdapter(memberSpinnerArrayAdapter);
            sMember.setEnabled(false);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            ivSave.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (sMember.getSelectedItemPosition() < 0) {
                            sMember.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        } else if (sSubscription.getSelectedItemPosition() < 0) {
                            sSubscription.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        } else if (etSubscriptionStartDate.getText().toString().contentEquals("")) {
                            etSubscriptionStartDate.setError("Start Date can't be empty");
                        } else if (etSubscriptionEndDate.getText().toString().contentEquals("")) {
                            etSubscriptionEndDate.setError("Expire Date can't be empty");
                        } else {
                            try {
                                Subscription subscription = (Subscription) sSubscription.getSelectedItem();
                                Member member = (Member) sMember.getSelectedItem();
                                MemberSubscription memberSubscription = new MemberSubscription(0
                                        , member.getMemberId()
                                        , subscription.getSubscription()
                                        , new SimpleDateFormat("yyyy-MM-dd").parse(etSubscriptionStartDate.getText().toString())
                                        , new SimpleDateFormat("yyyy-MM-dd").parse(etSubscriptionEndDate.getText().toString())
                                        , 1);
                                MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(context, 1);
                                memberSubscriptionBusiness.insert(memberSubscription);
                                memberSubscriptionBusiness.close();
                                if (context.getClass() == MainActivity.class) {
                                    ((MainActivity) context).resume();
                                } else if (context.getClass() == MembershipListActivity.class) {
                                    ((MembershipListActivity) context).resume();
                                } else if (context.getClass() == MemberListActivity.class) {
                                    ((MemberListActivity) context).resume();
                                } else if (context.getClass() == MemberDetailActivity.class) {
                                    ((MemberDetailActivity) context).finish();
                                    context.startActivity(new Intent(context, MainActivity.class));
                                }
                                alertDialog.cancel();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                    }
                    return false;
                }
            });
            ivClose.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        alertDialog.cancel();
                    }
                    return false;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MembershipDialog(final Context context, LayoutInflater layoutInflater, Member member, Subscription subscription, final Date startDate, final Date endDate) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = layoutInflater;
            View dialogView = inflater.inflate(R.layout.dialog_view_membership, null);
            alertDialogBuilder.setView(dialogView);
            final CustomSpinner sMember = (CustomSpinner) dialogView.findViewById(R.id.sMember);
            final CustomSpinner sSubscription = (CustomSpinner) dialogView.findViewById(R.id.sSubscription);
            final EditText etSubscriptionStartDate = (EditText) dialogView.findViewById(R.id.etSubscriptionStartDate);
            final EditText etSubscriptionEndDate = (EditText) dialogView.findViewById(R.id.etSubscriptionEndDate);
            ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
            ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
            etSubscriptionStartDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etSubscriptionStartDate.setError(null);
                        etSubscriptionStartDate.setInputType(InputType.TYPE_NULL);
                        Calendar c = Calendar.getInstance();
                        c.setTime(startDate);
                        new DatePickerDialog(context, R.style.Base_DialogCalendarAppTheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etSubscriptionStartDate.setText(String.format(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                                etSubscriptionStartDate.setError(null);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();
                    }
                    return false;
                }
            });

            etSubscriptionEndDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etSubscriptionEndDate.setError(null);
                        etSubscriptionEndDate.setInputType(InputType.TYPE_NULL);
                        Calendar c = Calendar.getInstance();
                        c.setTime(endDate);
                        new DatePickerDialog(context, R.style.Base_DialogCalendarAppTheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etSubscriptionEndDate.setText(String.format(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                                etSubscriptionEndDate.setError(null);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();
                    }
                    return false;
                }
            });
            alertDialogBuilder.setCancelable(false);
            SubscriptionBusiness subscriptionBusiness = new SubscriptionBusiness(context, 0);
            ArrayList<Subscription> subscriptionList = subscriptionBusiness.getAll();
            ArrayAdapter<Subscription> spinnerArrayAdapter
                    = new ArrayAdapter<Subscription>(context
                    , android.R.layout.simple_spinner_item
                    , subscriptionList);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sSubscription.setAdapter(spinnerArrayAdapter);
            sSubscription.setTitle("Select Subscription");
            int position = 0;
            for (Subscription selectedSubscription : subscriptionList) {
                if (selectedSubscription.getSubscription() == subscription.getSubscription()) {
                    sSubscription.setSelection(position);
                }
                position++;
            }

            ArrayList<Member> memberList = new ArrayList<Member>();
            memberList.add(member);
            ArrayAdapter<Member> memberSpinnerArrayAdapter
                    = new ArrayAdapter<Member>(context
                    , android.R.layout.simple_spinner_item
                    , memberList);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sMember.setAdapter(memberSpinnerArrayAdapter);
            sMember.setEnabled(false);
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            etSubscriptionStartDate.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE));
            c.setTime(endDate);
            etSubscriptionEndDate.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE));

            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            ivSave.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (sMember.getSelectedItemPosition() < 0) {
                            sMember.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        } else if (sSubscription.getSelectedItemPosition() < 0) {
                            sSubscription.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        } else if (etSubscriptionStartDate.getText().toString().contentEquals("")) {
                            etSubscriptionStartDate.setError("Start Date can't be empty");
                        } else if (etSubscriptionEndDate.getText().toString().contentEquals("")) {
                            etSubscriptionEndDate.setError("Expire Date can't be empty");
                        } else {
                            try {
                                Subscription subscription = (Subscription) sSubscription.getSelectedItem();
                                Member member = (Member) sMember.getSelectedItem();
                                MemberSubscription memberSubscription = new MemberSubscription(0
                                        , member.getMemberId()
                                        , subscription.getSubscription()
                                        , new SimpleDateFormat("yyyy-MM-dd").parse(etSubscriptionStartDate.getText().toString())
                                        , new SimpleDateFormat("yyyy-MM-dd").parse(etSubscriptionEndDate.getText().toString())
                                        , 1);
                                MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(context, 1);
                                memberSubscriptionBusiness.update(memberSubscription);
                                memberSubscriptionBusiness.close();
                                if (context.getClass() == MainActivity.class) {
                                    ((MainActivity) context).resume();
                                } else if (context.getClass() == MembershipListActivity.class) {
                                    ((MembershipListActivity) context).resume();
                                } else if (context.getClass() == MemberListActivity.class) {
                                    ((MemberListActivity) context).resume();
                                } else if (context.getClass() == MemberDetailActivity.class) {
                                    ((MemberDetailActivity) context).finish();
                                    context.startActivity(new Intent(context, MainActivity.class));
                                }
                                alertDialog.cancel();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                    }
                    return false;
                }
            });
            ivClose.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        alertDialog.cancel();
                    }
                    return false;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
