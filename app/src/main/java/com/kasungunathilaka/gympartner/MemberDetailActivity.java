package com.kasungunathilaka.gympartner;

//region Imported

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.kasungunathilaka.adapter.FragmentAdapter;
import com.kasungunathilaka.adapter.MembershipHistoryAdapter;
import com.kasungunathilaka.business.MemberBusiness;
import com.kasungunathilaka.business.MemberSubscriptionBusiness;
import com.kasungunathilaka.dialog.AboutDialog;
import com.kasungunathilaka.dialog.MembershipDialog;
import com.kasungunathilaka.domain.Member;
import com.kasungunathilaka.domain.MemberSubscription;
import com.kasungunathilaka.fragment.EmergencyFragment;
import com.kasungunathilaka.fragment.MemberFragment;
import com.kasungunathilaka.fragment.SymptomFragment;
import com.kasungunathilaka.showcase.MaterialShowcaseSequence;
import com.kasungunathilaka.showcase.MaterialShowcaseView;
import com.kasungunathilaka.showcase.ShowcaseConfig;
import com.kasungunathilaka.util.SharedPreferencesName;

import java.text.ParseException;
import java.util.ArrayList;
//endregion

// </summary>
// Source File		: MemberDetailActivity.java
// Package 			: com.kasungunathilaka.gympartner
// Description		: Class Contain Functionality of Member Detail Activity
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 7 May 2016		Kasun Gunathilak			    Created
// 18 June 2016		Kasun Gunathilak			    Modified UI
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

public class MemberDetailActivity extends AppCompatActivity {

    //region Class Members
    private Member currentMember = null;
    private int intentValue;
    private int viewMenuType;
    private BottomSheetDialog bottomSheetDialog;
    //endregion

    //region Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_member_detail);

            Bundle intentBundle = getIntent().getExtras();
            intentValue = intentBundle.getInt("ID");
            viewMenuType = intentBundle.getInt("TYPE");
            MemberBusiness memberBusiness = new MemberBusiness(MemberDetailActivity.this, 1);
            currentMember = memberBusiness.getById(intentValue);
            memberBusiness.close();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            final ActionBar ab = getSupportActionBar();
            ab.setHomeAsUpIndicator(R.drawable.ic_back);
            ab.setDisplayHomeAsUpEnabled(true);

            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(currentMember.getMemberCode());

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            ImageView ivMemberCardPic = (ImageView) findViewById(R.id.ivMemberPicture);

            if (currentMember.getImage() != null)
                ivMemberCardPic.setImageBitmap(BitmapFactory.decodeByteArray(currentMember.getImage()
                        , 0, currentMember.getImage().length));

            viewPager.setAdapter(getAdapter());
            tabLayout.setupWithViewPager(viewPager);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean returnValue = false;
        switch (viewMenuType) {
            case R.integer.Edit:
                getMenuInflater().inflate(R.menu.member_detail_edit_menu, menu);
                returnValue = true;
                break;
            case R.integer.Renew:
                getMenuInflater().inflate(R.menu.member_detail_renew_menu, menu);
                returnValue = true;
                break;
            case R.integer.View:
                getMenuInflater().inflate(R.menu.member_detail_blank_menu, menu);
                returnValue = true;
                break;
            default:
                returnValue = false;
                break;
        }
        return returnValue;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_edit:
                Bundle bundle = new Bundle();
                bundle.putInt("ID", currentMember.getMemberId());
                Intent intent = new Intent(MemberDetailActivity.this, MemberActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_renew:
                new MembershipDialog(MemberDetailActivity.this, layoutInflater(), currentMember);
                break;
            case R.id.menu_history:
                MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(MemberDetailActivity.this, 1);
                ArrayList<MemberSubscription> memberSubscriptionList = memberSubscriptionBusiness.getByMemberId(currentMember.getMemberId());
                MembershipHistoryAdapter membershipHistoryAdapter = new MembershipHistoryAdapter(memberSubscriptionList, new MembershipHistoryAdapter.OnItemLongClickListener() {
                    @Override
                    public void OnItemLongClickListener(MemberSubscription memberSubscription) {

                    }
                });
                View view = getLayoutInflater().inflate(R.layout.dialog_view_membership_history, null);
                RecyclerView rvHistoryList = (RecyclerView) view.findViewById(R.id.rvHistoryList);
                rvHistoryList.setHasFixedSize(true);
                rvHistoryList.setLayoutManager(new LinearLayoutManager(this));
                rvHistoryList.setAdapter(membershipHistoryAdapter);
                bottomSheetDialog = new BottomSheetDialog(this);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(MemberDetailActivity.this, SettingsActivity.class));
                break;
            case R.id.menu_about:
                new AboutDialog(MemberDetailActivity.this, this.layoutInflater());
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.cancel();
        } else {
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mainShowCase();
    }
    //endregion

    //region Private Members
    private FragmentAdapter getAdapter() {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        Fragment memberFragment = new MemberFragment();
        Fragment emergencyFragment = new EmergencyFragment();
        Fragment symptomFragment = new SymptomFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("member_id", currentMember.getMemberId());
        memberFragment.setArguments(bundle);
        emergencyFragment.setArguments(bundle);
        symptomFragment.setArguments(bundle);

        adapter.addFragment(memberFragment, "Member Info");
        adapter.addFragment(emergencyFragment, "Emg. Contact");
        adapter.addFragment(symptomFragment, "Symptoms");
        return adapter;
    }

    private void mainShowCase() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SharedPreferencesName.MEMBER_DETAILS_ACTIVITY_LAUNCH);
        sequence.setConfig(config);
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.ivMemberPicture))
                .setDismissText("Next")
                .setContentText("Displays a picture of the member.")
                .withRectangleShape(true)
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.tabs))
                .setDismissText("Next")
                .setContentText("swipe left to see additional details like Emergency Contact and Symptoms.")
                .withRectangleShape(true)
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.viewpager))
                .setDismissText("Next")
                .setContentText("Member personal details display here.")
                .withRectangleShape(true)
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.toolbar), "menu")
                .setDismissText("Done")
                .setContentText("Previous memberships, current membership status and settings can be access from here.")
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
