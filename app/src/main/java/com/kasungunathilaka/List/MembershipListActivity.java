package com.kasungunathilaka.List;

//region Imported

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kasungunathilaka.adapter.MembershipAdapter;
import com.kasungunathilaka.business.MemberBusiness;
import com.kasungunathilaka.business.MemberSubscriptionBusiness;
import com.kasungunathilaka.business.SubscriptionBusiness;
import com.kasungunathilaka.dialog.AboutDialog;
import com.kasungunathilaka.dialog.MembershipDialog;
import com.kasungunathilaka.domain.Member;
import com.kasungunathilaka.domain.MemberSubscription;
import com.kasungunathilaka.domain.Subscription;
import com.kasungunathilaka.gympartner.BaseActivity;
import com.kasungunathilaka.gympartner.MemberDetailActivity;
import com.kasungunathilaka.gympartner.MembershipActivity;
import com.kasungunathilaka.gympartner.R;
import com.kasungunathilaka.gympartner.SettingsActivity;
import com.kasungunathilaka.showcase.MaterialShowcaseSequence;
import com.kasungunathilaka.showcase.MaterialShowcaseView;
import com.kasungunathilaka.showcase.ShowcaseConfig;
import com.kasungunathilaka.util.SharedPreferencesName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//endregion

// </summary>
// Source File		: MembershipListActivity.java
// Package 			: com.kasungunathilaka.List
// Description		: Class responsible for displaying Memberships
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 22 May 2016		Kasun Gunathilak			    Created
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

public class MembershipListActivity extends BaseActivity implements MembershipAdapter.OnItemClickListener, View.OnClickListener {

    //region Class Members
    private RecyclerView mRecyclerView;
    private MembershipAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fabAddMembership;
    private ArrayList<MemberSubscription> memberSubscriptionList = null;
    private MenuItem searchItem;
    private SearchView searchView;
    private SearchRecentSuggestions searchRecentSuggestions;
    private SearchView.SearchAutoComplete searchAutoComplete;
    private String filterString = "";
    private SharedPreferences sharedPreferences;
    //endregion

    //region Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_membership);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MembershipListActivity.this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchRecentSuggestions = new SearchRecentSuggestions(MembershipListActivity.this, MembershipListActivity.class.getName(), SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(1000);

        searchAutoComplete = (SearchView.SearchAutoComplete) searchView
                .findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    showSearch(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterString = query;
                mAdapter.getFilter().filter(filterString);
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterString = newText;
                mAdapter.getFilter().filter(filterString);
                return true;
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rvMembershipList);
        fabAddMembership = (FloatingActionButton) findViewById(R.id.fabAddMembership);
        fabAddMembership.setOnClickListener(this);
    }

    @Override
    public boolean onSearchRequested() {
        showSearch(true);
        return false;
    }

    @Override
    public void onItemClick(final MemberSubscription memberSubscription) {
        try {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MembershipListActivity.this);
            final LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_view_membership_detail, null);
            alertDialogBuilder.setView(dialogView);
            final TextView tvName = (TextView) dialogView.findViewById(R.id.tvName);
            final TextView tvMemberCode = (TextView) dialogView.findViewById(R.id.tvMemberCode);
            final TextView tvStartDate = (TextView) dialogView.findViewById(R.id.tvStartDate);
            final TextView tvEndDate = (TextView) dialogView.findViewById(R.id.tvEndDate);
            ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
            ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", memberSubscription.getMember().getMemberId());
                    bundle.putInt("TYPE", R.integer.View);
                    Intent intent = new Intent(MembershipListActivity.this, MemberDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            tvName.setText(memberSubscription.getMember().getName());
            tvMemberCode.setText(memberSubscription.getSubscription().getName());
            tvStartDate.setText(getDate(memberSubscription.getStartDate().toString()));
            tvEndDate.setText(getDate(memberSubscription.getEndDate().toString()));
            alertDialogBuilder.setCancelable(false);
            final Date todayDate = Calendar.getInstance().getTime();
            final Calendar c = Calendar.getInstance();
            c.setTime(memberSubscription.getEndDate());
            if (todayDate.after(c.getTime())) {
                ivSave.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh));
            }
            final AlertDialog alertDialog = alertDialogBuilder.create();
            ivSave.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (todayDate.after(c.getTime())) {
                            new MembershipDialog(MembershipListActivity.this, layoutInflater()
                                    , memberSubscription.getMember());
                        } else {
                            new MembershipDialog(MembershipListActivity.this
                                    , layoutInflater()
                                    , memberSubscription.getMember()
                                    , memberSubscription.getSubscription()
                                    , memberSubscription.getStartDate()
                                    , memberSubscription.getEndDate());

                        }
                        alertDialog.dismiss();
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

    @Override
    public void onClick(View v) {
        final LayoutInflater layoutInflater = this.getLayoutInflater();
        switch (v.getId()) {
            case R.id.fabAddMembership:
                try {
                    CharSequence option[] = new CharSequence[0];
                    SubscriptionBusiness subscriptionBusiness = new SubscriptionBusiness(MembershipListActivity.this, 1);
                    ArrayList<Subscription> subscriptionList = subscriptionBusiness.getAll();
                    subscriptionBusiness.close();

                    MemberBusiness memberBusiness = new MemberBusiness(MembershipListActivity.this, 1);
                    ArrayList<Member> memberList = memberBusiness.getAll();
                    memberBusiness.close();

                    if (subscriptionList.size() < 1) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MembershipListActivity.this, R.style.Base_CustomDialogAppTheme);
                        builder.setTitle("Warring!");
                        builder.setMessage(R.string.warringNoSubscription);
                        builder.setPositiveButton("Go to Subscription", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                startActivity(new Intent(MembershipListActivity.this, SubscriptionListActivity.class));
                            }
                        });
                    } else if (memberList.size() < 1) {
                        option = new CharSequence[]{"New Member"};
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MembershipListActivity.this, R.style.Base_CustomDialogAppTheme);
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    startActivity(new Intent(MembershipListActivity.this, MembershipActivity.class));
                                } else {
                                    new MembershipDialog(MembershipListActivity.this, layoutInflater);
                                }

                            }
                        });
                        builder.show();
                    } else {
                        option = new CharSequence[]{"New Member", "Existing Member"};
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MembershipListActivity.this, R.style.Base_CustomDialogAppTheme);
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    startActivity(new Intent(MembershipListActivity.this, MembershipActivity.class));
                                } else {
                                    new MembershipDialog(MembershipListActivity.this, layoutInflater);
                                }

                            }
                        });
                        builder.show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.membership_list_menu, menu);
        searchItem = menu.findItem(R.id.menu_search);
        MenuItemCompat.setActionView(searchItem, searchView);
        MenuItemCompat.setShowAsAction(searchItem,
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_message:
                sendMessages();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(MembershipListActivity.this, SettingsActivity.class));
                break;
            case R.id.menu_about:
                new AboutDialog(MembershipListActivity.this, this.layoutInflater());
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
        MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(MembershipListActivity.this, 0);
        try {
            memberSubscriptionList = memberSubscriptionBusiness.getAll();
            memberSubscriptionBusiness.close();
            for (MemberSubscription memberSubscription : memberSubscriptionList) {
                MemberBusiness memberBusiness = new MemberBusiness(MembershipListActivity.this, 0);
                SubscriptionBusiness subscriptionBusiness = new SubscriptionBusiness(MembershipListActivity.this, 0);
                memberSubscription.setMember(memberBusiness.getById(memberSubscription.getMemberId()));
                memberSubscription.setSubscription(subscriptionBusiness.getById(memberSubscription.getSubscriptionId()));
                memberBusiness.close();
                subscriptionBusiness.close();
            }

            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new MembershipAdapter(memberSubscriptionList, this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.getFilter().filter(filterString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mainShowCase();

    }
    //endregion

    //region Private Methods
    private void showSearch(boolean visible) {
        if (visible)
            MenuItemCompat.expandActionView(searchItem);
        else
            MenuItemCompat.collapseActionView(searchItem);
    }

    private String getDate(String dateString) throws ParseException {
        DateFormat readFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = null;
        try {
            date = readFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String formatedDate = cal.get(Calendar.DATE)
                + "-"
                + (cal.get(Calendar.MONTH) + 1)
                + "-"
                + cal.get(Calendar.YEAR);
        return formatedDate;
    }

    private void sendMessages() {
        final ArrayList<String> numbers = new ArrayList<String>();
        final SmsManager sms = SmsManager.getDefault();
        for (MemberSubscription memberSubscription : memberSubscriptionList) {
            Calendar presentCalender = Calendar.getInstance();
            Calendar memberCalender = Calendar.getInstance();
            memberCalender.setTime(memberSubscription.getEndDate());
            if (presentCalender.after(memberCalender)) {
                numbers.add(memberSubscription.getMember().getContactNo());
            }
        }

        if (numbers.size() == 1) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MembershipListActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_view_default, null);
            alertDialogBuilder.setView(dialogView);
            TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
            TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
            ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
            ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
            tvTitle.setText("Message Confirmation");
            tvMessage.setText("Only one expired member. Send a message ?");
            final AlertDialog alertDialog = alertDialogBuilder.create();
            ivSave.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        try {
                            sms.sendTextMessage(numbers.get(0), null, sharedPreferences.getString("message", getString(R.string.message_expired_membership_sms)), null, null);
                            alertDialog.dismiss();
                            Toast.makeText(MembershipListActivity.this, "Message sent to " + numbers.get(0), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(MembershipListActivity.this, "Message Sending failed to " + numbers.get(0), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
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


        } else if (numbers.size() > 1) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MembershipListActivity.this, R.style.Base_CustomDialogAppTheme);
            CharSequence[] option = new CharSequence[]{"To All Members", "Select One Member"};
            builder.setItems(option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        ProgressDialog loading = new ProgressDialog(MembershipListActivity.this);
                        loading.setCancelable(true);
                        loading.setMessage("Sending Messages..");
                        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        loading.show();
                        for (String number : numbers) {
                            try {
                                sms.sendTextMessage(number, null, sharedPreferences.getString("message", getString(R.string.message_expired_membership_sms)), null, null);
                                Toast.makeText(MembershipListActivity.this, "Message sent to " + number, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(MembershipListActivity.this, "Message Sending failed to " + number, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                        loading.dismiss();
                        dialog.cancel();
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MembershipListActivity.this, R.style.Base_CustomDialogAppTheme);
                        builder.setItems(numbers.toArray(new CharSequence[numbers.size()]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sms.sendTextMessage(numbers.get(which), null, sharedPreferences.getString("message", getString(R.string.message_expired_membership_sms)), null, null);
                                Toast.makeText(MembershipListActivity.this, "Message sent to " + numbers.get(which), Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }
            });
            builder.show();
        }
    }

    private LayoutInflater layoutInflater() {
        return this.getLayoutInflater();
    }

    private void mainShowCase() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SharedPreferencesName.MEMBERSHIP_LIST_ACTIVITY_LAUNCH);
        sequence.setConfig(config);
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(fabAddMembership)
                .setDismissText("Next")
                .setContentText("Add a new membership.")
                .withCircleShape()
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.toolbar), "menu")
                .setDismissText("Done")
                .setContentText("You can search membership. Send text messages to members with expired membership are also available in the hidden menu. Settings also can be access from here.")
                .withCircleShape()
                .build()
        );
        sequence.start();
    }
    //endregion

    //region Public Members
    public void resume() {
        this.onResume();
    }
    //endregion
}
