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
import android.widget.ImageView;
import android.widget.TextView;

import com.kasungunathilaka.adapter.MemberDataAdapter;
import com.kasungunathilaka.business.MemberBusiness;
import com.kasungunathilaka.business.MemberSubscriptionBusiness;
import com.kasungunathilaka.dialog.AboutDialog;
import com.kasungunathilaka.domain.Member;
import com.kasungunathilaka.domain.MemberSubscription;
import com.kasungunathilaka.gympartner.MemberActivity;
import com.kasungunathilaka.gympartner.MemberDetailActivity;
import com.kasungunathilaka.gympartner.R;
import com.kasungunathilaka.gympartner.SettingsActivity;
import com.kasungunathilaka.showcase.MaterialShowcaseSequence;
import com.kasungunathilaka.showcase.MaterialShowcaseView;
import com.kasungunathilaka.showcase.ShowcaseConfig;
import com.kasungunathilaka.util.SharedPreferencesName;

import java.util.ArrayList;
//endregion

// </summary>
// Source File		: MemberListActivity.java
// Package 			: com.kasungunathilaka.List
// Description		: Class responsible for displaying Members
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

public class MemberListActivity extends AppCompatActivity implements MemberDataAdapter.OnItemClickListener, MemberDataAdapter.OnItemLongClickListener {

    //region Class Members
    private RecyclerView rvMemberList;
    private MemberDataAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fabAddMembers;
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
        setContentView(R.layout.activity_member_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        rvMemberList = (RecyclerView) findViewById(R.id.rvMemberList);
        fabAddMembers = (FloatingActionButton) findViewById(R.id.fabAddMembers);
        fabAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MemberListActivity.this, MemberActivity.class));
            }
        });

        searchRecentSuggestions = new SearchRecentSuggestions(MemberListActivity.this, MemberListActivity.class.getName(), SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES);

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

    }

    @Override
    public void onItemClick(Member member) {
        Bundle bundle = new Bundle();
        bundle.putInt("ID", member.getMemberId());
        bundle.putInt("TYPE", R.integer.Edit);
        Intent intent = new Intent(MemberListActivity.this, MemberDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.member_list_menu, menu);
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
            case R.id.menu_settings:
                startActivity(new Intent(MemberListActivity.this, SettingsActivity.class));
                break;
            case R.id.menu_about:
                new AboutDialog(MemberListActivity.this, this.layoutInflater());
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
        MemberBusiness memberBusiness = new MemberBusiness(MemberListActivity.this, 1);
        ArrayList<Member> memberList = null;
        try {
            memberList = memberBusiness.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        rvMemberList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rvMemberList.setLayoutManager(mLayoutManager);
        mAdapter = new MemberDataAdapter(memberList, this, this);
        rvMemberList.setAdapter(mAdapter);
        mAdapter.getFilter().filter(filterString);
        mainShowCase();
    }

    @Override
    public void OnItemLongClickListener(final Member member) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemberListActivity.this);
        final LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_view_default, null);
        alertDialogBuilder.setView(dialogView);
        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
        ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
        tvTitle.setText("Confirm Delete");
        tvMessage.setText(getString(R.string.confirmationDeleteMember));
        final AlertDialog parentAlertDialog = alertDialogBuilder.create();
        ivSave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(MemberListActivity.this, 1);
                    ArrayList<MemberSubscription> memberSubscriptionList = memberSubscriptionBusiness.getByMemberId(member.getMemberId());
                    memberSubscriptionBusiness.close();
                    if (memberSubscriptionList.size() < 1) {
                        MemberBusiness memberBusiness = new MemberBusiness(MemberListActivity.this, 1);
                        memberBusiness.delete(member.getMemberId());
                        memberBusiness.close();
                        parentAlertDialog.cancel();
                        onResume();
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemberListActivity.this);
                        View dialogView = inflater.inflate(R.layout.dialog_view_default, null);
                        alertDialogBuilder.setView(dialogView);
                        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
                        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
                        ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
                        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
                        tvTitle.setText("Delete Error");
                        tvMessage.setText(getString(R.string.errorMemberDeleteFailed));
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

    private void mainShowCase() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SharedPreferencesName.MEMBER_LIST_ACTIVITY_LAUNCH);
        sequence.setConfig(config);
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(fabAddMembers)
                .setDismissText("Next")
                .setContentText("Add a new member.")
                .withCircleShape()
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.toolbar), "menu")
                .setDismissText("Done")
                .setContentText("You can search members. Settings also can be access from here.")
                .withCircleShape()
                .build()
        );
        sequence.start();
    }
    //endregion

    //region Public Methods
    public void resume() {
        this.onResume();
    }
    //endregion
}
