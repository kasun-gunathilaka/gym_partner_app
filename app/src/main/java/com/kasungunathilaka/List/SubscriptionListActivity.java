package com.kasungunathilaka.List;

//region Imported

import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasungunathilaka.adapter.SubscriptionAdapter;
import com.kasungunathilaka.business.MemberSubscriptionBusiness;
import com.kasungunathilaka.business.SubscriptionBusiness;
import com.kasungunathilaka.dialog.AboutDialog;
import com.kasungunathilaka.domain.MemberSubscription;
import com.kasungunathilaka.domain.Subscription;
import com.kasungunathilaka.gympartner.BaseActivity;
import com.kasungunathilaka.gympartner.R;
import com.kasungunathilaka.gympartner.SettingsActivity;
import com.kasungunathilaka.showcase.MaterialShowcaseSequence;
import com.kasungunathilaka.showcase.MaterialShowcaseView;
import com.kasungunathilaka.showcase.ShowcaseConfig;
import com.kasungunathilaka.util.SharedPreferencesName;

import java.util.ArrayList;
//endregion

// </summary>
// Source File		: SubscriptionListActivity.java
// Package 			: com.kasungunathilaka.List
// Description		: Class responsible for displaying Subscriptions
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 21 May 2016		Kasun Gunathilak			    Created
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

public class SubscriptionListActivity extends BaseActivity implements SubscriptionAdapter.OnItemClickListener, View.OnClickListener, SubscriptionAdapter.OnItemLongClickListener {

    //region Class Members
    private RecyclerView mRecyclerView;
    private SubscriptionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fabAddSubscription;
    private MenuItem searchItem;
    private SearchView searchView;
    private SearchRecentSuggestions searchRecentSuggestions;
    private SearchView.SearchAutoComplete searchAutoComplete;
    private String filterString = "";
    //endregion

    //region Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subscription);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchRecentSuggestions = new SearchRecentSuggestions(SubscriptionListActivity.this, MembershipListActivity.class.getName(), SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES);

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

        fabAddSubscription = (FloatingActionButton) findViewById(R.id.fabAddSubscription);
    }

    @Override
    public boolean onSearchRequested() {
        showSearch(true);
        return false;
    }

    @Override
    public void onItemClick(final Subscription subscription) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubscriptionListActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_view_subscription_detail, null);
        alertDialogBuilder.setView(dialogView);
        final TextView tvMonthDuration = (TextView) dialogView.findViewById(R.id.tvMonthDuration);
        final TextView title = (TextView) dialogView.findViewById(R.id.title);
        ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
        tvMonthDuration.setText(String.valueOf(subscription.getDuration()));
        title.setText(subscription.getName());
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        ivSave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    saveSubscription(subscription);
                    alertDialog.cancel();
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddSubscription:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubscriptionListActivity.this);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_view_subscription, null);
                alertDialogBuilder.setView(dialogView);
                final EditText etSubscriptionName = (EditText) dialogView.findViewById(R.id.etSubscriptionName);
                final EditText etSubscriptionDuration = (EditText) dialogView.findViewById(R.id.etSubscriptionDuration);
                ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
                ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
                alertDialogBuilder.setCancelable(false);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                ivSave.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (etSubscriptionName.getText().toString().contentEquals("")) {
                                etSubscriptionName.setError("Name can't be empty");
                            } else if (etSubscriptionDuration.getText().toString().contentEquals("")) {
                                etSubscriptionDuration.setError("Contact No can't be empty");
                            } else {
                                Subscription subscription = new Subscription(0
                                        , etSubscriptionName.getText().toString()
                                        , Integer.parseInt(etSubscriptionDuration.getText().toString()));
                                SubscriptionBusiness subscriptionBusiness =
                                        new SubscriptionBusiness(SubscriptionListActivity.this, 0);
                                long saveId = subscriptionBusiness.insert(subscription);
                                subscriptionBusiness.close();
                                onResume();
                                alertDialog.cancel();
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
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subscrption_list_menu, menu);
        searchItem = menu.findItem(R.id.menu_search);
        MenuItemCompat.setActionView(searchItem, searchView);
        MenuItemCompat.setShowAsAction(searchItem,
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SubscriptionBusiness subscriptionBusiness = new SubscriptionBusiness(SubscriptionListActivity.this, 0);
        ArrayList<Subscription> subscriptionList = null;
        try {
            subscriptionList = subscriptionBusiness.getAll();
            mRecyclerView = (RecyclerView) findViewById(R.id.rvSubscriptionList);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new SubscriptionAdapter(subscriptionList, this, this);
            mRecyclerView.setAdapter(mAdapter);
            fabAddSubscription.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainShowCase();
    }

    @Override
    public void OnItemLongClickListener(final Subscription subscription) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubscriptionListActivity.this);
        final LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_view_default, null);
        alertDialogBuilder.setView(dialogView);
        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
        ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
        tvTitle.setText("Confirm Delete");
        tvMessage.setText(getString(R.string.confirmationDeleteSubscription));
        final AlertDialog parentAlertDialog = alertDialogBuilder.create();
        ivSave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(SubscriptionListActivity.this, 1);
                    ArrayList<MemberSubscription> memberSubscriptionList = memberSubscriptionBusiness.getBySubscriptionId(subscription.getSubscription());
                    memberSubscriptionBusiness.close();
                    if (memberSubscriptionList.size() < 1) {
                        SubscriptionBusiness subscriptionBusiness =
                                new SubscriptionBusiness(SubscriptionListActivity.this, 0);
                        subscriptionBusiness.delete(subscription.getSubscription());
                        subscriptionBusiness.close();
                        parentAlertDialog.dismiss();
                        onResume();
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubscriptionListActivity.this);
                        View dialogView = inflater.inflate(R.layout.dialog_view_default, null);
                        alertDialogBuilder.setView(dialogView);
                        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
                        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
                        ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
                        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
                        tvTitle.setText("Delete Error");
                        tvMessage.setText(getString(R.string.errorSubscriptionDeleteFailed));
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        ivSave.setVisibility(View.INVISIBLE);
                        ivSave.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(SubscriptionListActivity.this, SettingsActivity.class));
                break;
            case R.id.menu_about:
                new AboutDialog(SubscriptionListActivity.this, this.layoutInflater());
                break;
        }
        return super.onOptionsItemSelected(item);

    }
    //endregion

    //region Private Methods
    private void showSearch(boolean visible) {
        if (visible)
            MenuItemCompat.expandActionView(searchItem);
        else
            MenuItemCompat.collapseActionView(searchItem);
    }

    private LayoutInflater layoutInflater() {
        return getLayoutInflater();
    }

    private void saveSubscription(final Subscription subscription) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SubscriptionListActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_view_subscription, null);
        alertDialogBuilder.setView(dialogView);
        final EditText etSubscriptionName = (EditText) dialogView.findViewById(R.id.etSubscriptionName);
        final EditText etSubscriptionDuration = (EditText) dialogView.findViewById(R.id.etSubscriptionDuration);
        ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
        etSubscriptionName.setText(subscription.getName());
        etSubscriptionDuration.setText(String.valueOf(subscription.getDuration()));
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        ivSave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (etSubscriptionName.getText().toString().contentEquals("")) {
                        etSubscriptionName.setError("Name can't be empty");
                    } else if (etSubscriptionDuration.getText().toString().contentEquals("")) {
                        etSubscriptionDuration.setError("Contact No can't be empty");
                    } else {
                        subscription.setName(etSubscriptionName.getText().toString());
                        subscription.setDuration(Integer.parseInt(etSubscriptionDuration.getText().toString()));
                        SubscriptionBusiness subscriptionBusiness =
                                new SubscriptionBusiness(SubscriptionListActivity.this, 0);
                        subscriptionBusiness.update(subscription);
                        subscriptionBusiness.close();
                        alertDialog.cancel();
                        onResume();
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

    }

    private void mainShowCase() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SharedPreferencesName.SUBSCRIPTION_LIST_ACTIVITY_LAUNCH);
        sequence.setConfig(config);
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(fabAddSubscription)
                .setDismissText("Next")
                .setContentText("Add a new subscription.")
                .withCircleShape()
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.toolbar), "menu")
                .setDismissText("Done")
                .setContentText("You can search subscription. Settings also can be access from here.")
                .withCircleShape()
                .build()
        );
        sequence.start();
    }
    //endregion
}
