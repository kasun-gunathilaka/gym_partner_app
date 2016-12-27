package com.kasungunathilaka.gympartner;

//region Imported
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import com.kasungunathilaka.adapter.EmergencyContactDataAdapter;
import com.kasungunathilaka.adapter.SymptomDataAdapter;
import com.kasungunathilaka.business.MemberBusiness;
import com.kasungunathilaka.business.MemberEmergencyContactBusiness;
import com.kasungunathilaka.business.MemberSubscriptionBusiness;
import com.kasungunathilaka.business.MemberSymptomBusiness;
import com.kasungunathilaka.business.SubscriptionBusiness;
import com.kasungunathilaka.dialog.AboutDialog;
import com.kasungunathilaka.domain.Member;
import com.kasungunathilaka.domain.MemberEmergencyContact;
import com.kasungunathilaka.domain.MemberSubscription;
import com.kasungunathilaka.domain.MemberSymptom;
import com.kasungunathilaka.domain.Subscription;
import com.kasungunathilaka.showcase.MaterialShowcaseSequence;
import com.kasungunathilaka.showcase.MaterialShowcaseView;
import com.kasungunathilaka.showcase.ShowcaseConfig;
import com.kasungunathilaka.util.SharedPreferencesName;
import com.kasungunathilaka.view.CustomSpinner;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
//endregion

// </summary>
// Source File		: MembershipActivity.java
// Package 			: com.kasungunathilaka.gympartner
// Description		: Class Contain Functionality of Membership Activity
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 7 May 2016		Kasun Gunathilak			    Created
// 21 May 2016		Kasun Gunathilak			    Modified UI
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

public class MembershipActivity extends BaseActivity implements View.OnClickListener {

    //region Class Members
    private static final int cameraData = 0;
    private ImageView ivMemberPicture;
    private EditText etName, etNic, etContactNo, etAddress, etBirthday, etAge, etBloodType;
    private Button bEmergencyContact, bSymptom;
    private FloatingActionButton fabCamera;
    private RecyclerView rvSymptom, rvEmergency;
    private View view;
    private ArrayList<MemberSymptom> memberSymptomList = new ArrayList<MemberSymptom>();
    private ArrayList<MemberEmergencyContact> memberEmergencyContactList = new ArrayList<MemberEmergencyContact>();
    private Bitmap currentImage;
    private String memberCode;
    private EmergencyContactDataAdapter emergencyContactDataAdapter;
    private SymptomDataAdapter symptomDataAdapter;
    //endregion

    //region Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_member);
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            CollapsingToolbarLayout collapsingToolbar =
                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            MemberBusiness memberBusiness = new MemberBusiness(MembershipActivity.this, 1);
            int id = memberBusiness.getLatestId();
            memberBusiness.close();
            collapsingToolbar.setTitle("AA-" + String.format("%04d", ++id));
            memberCode = ("AA-" + String.format("%04d", id));
            initialize();

            if (savedInstanceState != null) {
                Bitmap bitmap = savedInstanceState.getParcelable("image");
                if (bitmap != null)
                    ivMemberPicture.setImageBitmap(bitmap);

                if ((ArrayList<MemberSymptom>) savedInstanceState.getSerializable("memberSymptomList") != null) {
                    memberSymptomList = (ArrayList<MemberSymptom>) savedInstanceState.getSerializable("memberSymptomList");
                }

                if ((ArrayList<MemberEmergencyContact>) savedInstanceState.getSerializable("memberEmergencyContactList") != null) {
                    memberEmergencyContactList = (ArrayList<MemberEmergencyContact>) savedInstanceState.getSerializable("memberEmergencyContactList");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.fabCamera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, cameraData);
                break;
            case R.id.bEmergencyContact:
                addEmergencyContact();
                break;
            case R.id.bSymptom:
                addSymptom();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap bmp = (Bitmap) extras.get("data");
                ivMemberPicture.setImageBitmap(bmp);
            } else {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                currentImage = BitmapFactory.decodeFile(filePath);
                ivMemberPicture.setImageBitmap(BitmapFactory.decodeFile(filePath));
                ivMemberPicture.invalidate();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.membership_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_next:
                if (validate()) {
                    try {
                        long saveid = SaveData();
                        if (saveid > 0) {
                            subscriptionSelection((int) saveid);
                        } else if (saveid == 0) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MembershipActivity.this);
                            alertDialogBuilder.setTitle(R.string.titleError);
                            alertDialogBuilder
                                    .setMessage(R.string.errorMemberSavedFailed)
                                    .setCancelable(false)
                                    .setNegativeButton(R.string.btnOK, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.menu_settings:
                startActivity(new Intent(MembershipActivity.this, SettingsActivity.class));
                break;
            case R.id.menu_about:
                new AboutDialog(MembershipActivity.this, this.layoutInflater());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        symptomDataAdapter = new SymptomDataAdapter(this.memberSymptomList, new SymptomDataAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClickListener(final MemberSymptom memberSymptom) {
                new AlertDialog.Builder(MembershipActivity.this, R.style.Base_CustomDialogAppTheme)
                        .setMessage(R.string.confirmationDeleteSymptom)
                        .setCancelable(false)
                        .setPositiveButton(R.string.btnYes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                memberSymptomList.remove(memberSymptom);
                                dialog.cancel();
                                onResume();
                                setFocus(R.id.cvSymptom);
                            }
                        })
                        .setNegativeButton(R.string.btnNo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();

            }
        });
        rvSymptom.setHasFixedSize(true);
        rvSymptom.setLayoutManager(new LinearLayoutManager(this));
        rvSymptom.setAdapter(symptomDataAdapter);

        emergencyContactDataAdapter = new EmergencyContactDataAdapter(this.memberEmergencyContactList, new EmergencyContactDataAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClickListener(final MemberEmergencyContact memberEmergencyContact) {
                new AlertDialog.Builder(MembershipActivity.this, R.style.Base_CustomDialogAppTheme)
                        .setMessage(R.string.confirmationDeleteEmergencyContact)
                        .setCancelable(false)
                        .setPositiveButton(R.string.btnYes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                memberEmergencyContactList.remove(memberEmergencyContact);
                                dialog.cancel();
                                onResume();
                                setFocus(R.id.cvEmergency);
                            }
                        })
                        .setNegativeButton(R.string.btnNo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });
        rvEmergency.setHasFixedSize(true);
        rvEmergency.setLayoutManager(new LinearLayoutManager(this));
        rvEmergency.setAdapter(emergencyContactDataAdapter);
        mainShowCase();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (currentImage != null) {
            ivMemberPicture.setImageBitmap(currentImage);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        BitmapDrawable drawable = (BitmapDrawable) ivMemberPicture.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            outState.putParcelable("image", bitmap);
        }
        outState.putSerializable("memberSymptomList", memberSymptomList);
        outState.putSerializable("memberEmergencyContactList", memberEmergencyContactList);
        super.onSaveInstanceState(outState);
    }
    //endregion

    //region Private Methods
    private long SaveData() throws ParseException {
        try {
            Member member = new Member(0
                    , memberCode //etMemberCode.getText().toString()
                    , etName.getText().toString()
                    , etNic.getText().toString()
                    , etContactNo.getText().toString()
                    , etAddress.getText().toString()
                    , etBloodType.getText().toString()
                    , new SimpleDateFormat("yyyy-MM-dd").parse(etBirthday.getText().toString())
                    , Integer.parseInt(etAge.getText().toString())
                    , ivMemberPicture.getDrawable() != null ? getImageByte(((BitmapDrawable) ivMemberPicture.getDrawable()).getBitmap()) : null);
            MemberBusiness memberBusiness = new MemberBusiness(MembershipActivity.this, 1);
            boolean memberExist = memberBusiness.exist(etNic.getText().toString());
            memberBusiness.close();
            if (memberExist) {
                memberBusiness = new MemberBusiness(MembershipActivity.this, 1);
                long saveId = memberBusiness.insert(member);
                memberBusiness.close();
                for (MemberSymptom memberSymptom : memberSymptomList) {
                    memberSymptom.setMemberId(Integer.parseInt(String.valueOf(saveId)));
                    MemberSymptomBusiness memberSymptomBusiness = new MemberSymptomBusiness(MembershipActivity.this, 1);
                    memberSymptomBusiness.insert(memberSymptom);
                    memberSymptomBusiness.close();
                }

                for (MemberEmergencyContact memberEmergencyContact : memberEmergencyContactList) {
                    memberEmergencyContact.setMemberId(Integer.parseInt(String.valueOf(saveId)));
                    MemberEmergencyContactBusiness memberEmergencyContactBusiness = new MemberEmergencyContactBusiness(MembershipActivity.this, 1);
                    memberEmergencyContactBusiness.insert(memberEmergencyContact);
                    memberEmergencyContactBusiness.close();
                }
                return saveId;
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MembershipActivity.this);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder
                        .setMessage(R.string.errorMemberAlreadyExist)
                        .setCancelable(false)
                        .setNegativeButton(R.string.btnOK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return -1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }

    }

    private void subscriptionSelection(final int memberId) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MembershipActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_view_subscription_member, null);
            alertDialogBuilder.setView(dialogView);
            final CustomSpinner sSubscription = (CustomSpinner) dialogView.findViewById(R.id.sSubscription);
            final EditText etSubscriptionStartDate = (EditText) dialogView.findViewById(R.id.etSubscriptionStartDate);
            final EditText etSubscriptionEndDate = (EditText) dialogView.findViewById(R.id.etSubscriptionEndDate);
            ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
            ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            SubscriptionBusiness subscriptionBusiness = new SubscriptionBusiness(MembershipActivity.this, 0);
            ArrayList<Subscription> subscriptionList = subscriptionBusiness.getAll();
            ArrayAdapter<Subscription> spinnerArrayAdapter
                    = new ArrayAdapter<Subscription>(MembershipActivity.this
                    , android.R.layout.simple_spinner_item
                    , subscriptionList);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sSubscription.setAdapter(spinnerArrayAdapter);
            sSubscription.setTitle("Select Subscription");
            etSubscriptionStartDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etSubscriptionStartDate.setInputType(InputType.TYPE_NULL);
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(MembershipActivity.this, R.style.Base_DialogCalendarAppTheme, new DatePickerDialog.OnDateSetListener() {
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
                        etSubscriptionEndDate.setInputType(InputType.TYPE_NULL);
                        Calendar c = Calendar.getInstance();
                        if (sSubscription.getSelectedItemPosition() > -1) {
                            Subscription subscription = (Subscription) sSubscription.getSelectedItem();
                            c.add(Calendar.MONTH, subscription.getDuration());
                        }
                        new DatePickerDialog(MembershipActivity.this, R.style.Base_DialogCalendarAppTheme, new DatePickerDialog.OnDateSetListener() {
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
            ivSave.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (sSubscription.getSelectedItemPosition() < 0) {
                            sSubscription.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        } else if (etSubscriptionStartDate.getText().toString().contentEquals("")) {
                            etSubscriptionStartDate.setError("Start Date can't be empty");
                        } else if (etSubscriptionEndDate.getText().toString().contentEquals("")) {
                            etSubscriptionEndDate.setError("Expire Date can't be empty");
                        } else {
                            try {
                                Subscription subscription = (Subscription) sSubscription.getSelectedItem();
                                MemberSubscription memberSubscription = new MemberSubscription(0
                                        , memberId
                                        , subscription.getSubscription()
                                        , new SimpleDateFormat("yyyy-MM-dd").parse(etSubscriptionStartDate.getText().toString())
                                        , new SimpleDateFormat("yyyy-MM-dd").parse(etSubscriptionEndDate.getText().toString())
                                        , 1);
                                MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(MembershipActivity.this, 1);
                                memberSubscriptionBusiness.insert(memberSubscription);
                                memberSubscriptionBusiness.close();
                                alertDialog.cancel();
                                finish();
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
                        alertDialog.dismiss();
                        finish();
                    }
                    return false;
                }
            });
            alertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initialize() {

        ivMemberPicture = (ImageView) findViewById(R.id.ivMemberPicture);
        etName = (EditText) findViewById(R.id.etName);
        etNic = (EditText) findViewById(R.id.etNic);
        etContactNo = (EditText) findViewById(R.id.etContactNo);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etBirthday = (EditText) findViewById(R.id.etBirthday);
        etAge = (EditText) findViewById(R.id.etAge);
        etBloodType = (EditText) findViewById(R.id.etBloodType);
        fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        bEmergencyContact = (Button) findViewById(R.id.bEmergencyContact);
        bSymptom = (Button) findViewById(R.id.bSymptom);

        fabCamera.setOnClickListener(this);
        bEmergencyContact.setOnClickListener(this);
        bSymptom.setOnClickListener(this);

        rvSymptom = (RecyclerView) findViewById(R.id.rvSymptom);
        rvEmergency = (RecyclerView) findViewById(R.id.rvEmergency);

        etBirthday.setInputType(InputType.TYPE_NULL);
        etBirthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow((null == getCurrentFocus()) ? null
                                    : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    etBirthday.setInputType(InputType.TYPE_NULL);
                    new DatePickerDialog(MembershipActivity.this, R.style.Base_DialogCalendarAppTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            etBirthday.setText(String.format(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                            etAge.setText(String.format(String.valueOf((Calendar.getInstance().get(Calendar.YEAR) - year))));
                            etBirthday.setError(null);
                        }
                    }, 1991, 01, 01).show();
                }
                return false;
            }
        });

        etBloodType.setInputType(InputType.TYPE_NULL);
        etBloodType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow((null == getCurrentFocus()) ? null
                                    : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    etBloodType.setInputType(InputType.TYPE_NULL);
                    final CharSequence[] bloodType = {"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MembershipActivity.this, R.style.Base_CustomDialogAppTheme);
                    builder.setItems(R.array.blood_types, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etBloodType.setText(Arrays.asList(bloodType).get(which).toString());
                        }
                    });
                    builder.show();
                }
                return false;
            }
        });
    }

    private boolean validate() {
        if (etName.getText().toString().contentEquals("")) {
            etName.setError("Name can't be empty");
            return false;
        } else if (etNic.getText().toString().contentEquals("")) {
            etNic.setError("NIC can't be empty");
            return false;
        } else if (etContactNo.getText().toString().contentEquals("")) {
            etContactNo.setError("Contact Number can't be empty");
            return false;
        } else if (etBirthday.getText().toString().contentEquals("")) {
            etBirthday.setError("Birthday can't be empty");
            return false;
        } else {
            return true;
        }

    }

    private byte[] getImageByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    private void addEmergencyContact() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MembershipActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_view_emergency, null);
            alertDialogBuilder.setView(dialogView);
            final EditText etEmergName = (EditText) dialogView.findViewById(R.id.etEmergName);
            final EditText etEmergContactNo = (EditText) dialogView.findViewById(R.id.etEmergContactNo);
            ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
            ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            ivSave.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (etEmergName.getText().toString().contentEquals("")) {
                            etEmergName.setError("Name can't be empty");
                        } else if (etEmergContactNo.getText().toString().contentEquals("")) {
                            etEmergContactNo.setError("Contact No can't be empty");
                        } else {
                            MemberEmergencyContact memberEmergencyContact = new MemberEmergencyContact(0
                                    , 0
                                    , etEmergName.getText().toString()
                                    , etEmergContactNo.getText().toString());
                            memberEmergencyContactList.add(memberEmergencyContact);
                            alertDialog.dismiss();
                            onResume();
                            view = findViewById(R.id.cvEmergency);
                            view.requestFocus();
                        }
                    }
                    return false;
                }

            });
            ivClose.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        alertDialog.dismiss();
                    }
                    return false;
                }
            });
            alertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addSymptom() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MembershipActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_view_symptom, null);
            alertDialogBuilder.setView(dialogView);
            final EditText etSymptom = (EditText) dialogView.findViewById(R.id.etSymptom);
            final Spinner sSymptomState = (Spinner) dialogView.findViewById(R.id.sSymptomState);
            ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
            ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            ivSave.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (etSymptom.getText().toString().contentEquals("")) {
                            etSymptom.setError("Symptom can't be empty");
                        } else {
                            MemberSymptom memberSymptom = new MemberSymptom(0
                                    , 0
                                    , etSymptom.getText().toString()
                                    , sSymptomState.getSelectedItem().toString());
                            memberSymptomList.add(memberSymptom);
                            alertDialog.cancel();
                            onResume();
                            view = findViewById(R.id.cvSymptom);
                            view.requestFocus();
                        }
                    }
                    return false;
                }

            });
            ivClose.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        alertDialog.dismiss();
                    }
                    return false;
                }
            });
            alertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setFocus(int id) {
        View view = findViewById(id);
        view.requestFocus();
    }

    private void mainShowCase() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SharedPreferencesName.MEMBERSHIP_ACTIVITY_LAUNCH);
        sequence.setConfig(config);
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(ivMemberPicture)
                .setDismissText("Next")
                .setContentText("Add a member picture to identify easily.")
                .withRectangleShape(true)
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(fabCamera)
                .setDismissText("Next")
                .setContentText("Add member picture using camera.")
                .withCircleShape()
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.llDetailLayout))
                .setDismissText("Next")
                .setContentText("Add member personal details and scroll up to add additional details like Emergency Contact and any Medical Symptoms.")
                .withRectangleShape(true)
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.toolbar), "menu")
                .setDismissText("Done")
                .setContentText("Click to select the subscription. Settings also can be access from here.")
                .withCircleShape()
                .build()
        );
        sequence.start();
    }

    private LayoutInflater layoutInflater() {
        return getLayoutInflater();
    }
    //endregion
}
