package com.kasungunathilaka.gympartner;

//region Imported

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.kasungunathilaka.List.MemberListActivity;
import com.kasungunathilaka.List.MembershipListActivity;
import com.kasungunathilaka.List.SubscriptionListActivity;
import com.kasungunathilaka.Service.GymPartnerNotificationService;
import com.kasungunathilaka.adapter.MemberDataAdapter;
import com.kasungunathilaka.business.MemberBusiness;
import com.kasungunathilaka.business.MemberSubscriptionBusiness;
import com.kasungunathilaka.business.SubscriptionBusiness;
import com.kasungunathilaka.dialog.AboutDialog;
import com.kasungunathilaka.dialog.MembershipDialog;
import com.kasungunathilaka.domain.Member;
import com.kasungunathilaka.domain.MemberSubscription;
import com.kasungunathilaka.domain.Subscription;
import com.kasungunathilaka.showcase.MaterialShowcaseSequence;
import com.kasungunathilaka.showcase.MaterialShowcaseView;
import com.kasungunathilaka.showcase.PrefsManager;
import com.kasungunathilaka.showcase.ShowcaseConfig;
import com.kasungunathilaka.util.SharedPreferencesName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
//endregion

// </summary>
// Source File		: MainActivity.java
// Package 			: com.kasungunathilaka.gympartner
// Description		: Class Contain Functionality of Main Activity
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 7 May 2016		Kasun Gunathilak			    Created
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

public class MainActivity extends AppCompatActivity implements MemberDataAdapter.OnItemClickListener, MemberDataAdapter.OnItemLongClickListener, View.OnClickListener {

    //region Class Members
    private FloatingActionsMenu fabMenu;
    private TextView tvMonth;
    private FloatingActionButton fabSubscription, fabMembers, fabAddMembers, fabMemberships;
    private ImageView ivNext, ivPrevious;
    private FrameLayout flFloatingMenu;
    private CompactCalendarView cvMainCalendar;
    private NotificationManager notificationManager;
    RecyclerView rvExpiredList;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String calenderDate;
    SimpleDateFormat headingFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    SimpleDateFormat stringFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    AlertDialog firstDialog;
    int notificationId = 0001;

    //endregion

    //region Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialize();

        //region Close Service
        if (isMyServiceRunning(GymPartnerNotificationService.class)) {
            stopService(new Intent(MainActivity.this, GymPartnerNotificationService.class));
        }
        //endregion

        //region Close Notification
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
        //endregion

        cvMainCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setCalendar(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setCalendar(firstDayOfNewMonth);
            }
        });
    }

    @Override
    public void onItemClick(Member member) {
        Bundle bundle = new Bundle();
        bundle.putInt("ID", member.getMemberId());
        bundle.putInt("TYPE", R.integer.Renew);
        Intent intent = new Intent(MainActivity.this, MemberDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_today:
                setCalendar(Calendar.getInstance().getTime());
                break;
            case R.id.menu_settings:
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), 1);
                break;
            case R.id.menu_about:
                new AboutDialog(MainActivity.this, this.layoutInflater());
                //startService(new Intent(MainActivity.this, GymPartnerBackupService.class));
                //MaterialShowcaseView.resetAll(MainActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fabMenu.isExpanded()) {
            fabMenu.collapse();
        } else if (firstDialog == null) {
            exitDialogDisplay();
        } else if (firstDialog != null || !firstDialog.isShowing()) {
            exitDialogDisplay();
        } else if (firstDialog != null && firstDialog.isShowing()) {
            exitDialogDisplay();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            addCalendarEvents();
            if (calenderDate != null) {
                setCalendar(stringFormat.parse(calenderDate));
            } else {
                Calendar calendar = Calendar.getInstance();
                setCalendar(calendar.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        welComeDialog();
    }

    @Override
    public void OnItemLongClickListener(Member member) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabSubscription:
                //region fabSubscription
                startActivity(new Intent(MainActivity.this, SubscriptionListActivity.class));
                fabMenu.collapse();
                //endregion
                break;
            case R.id.fabMembers:
                //region fabMembers
                startActivity(new Intent(MainActivity.this, MemberListActivity.class));
                fabMenu.collapse();
                //endregion
                break;
            case R.id.fabMemberships:
                //region fabMemberships
                startActivity(new Intent(MainActivity.this, MembershipListActivity.class));
                fabMenu.collapse();
                //endregion
                break;
            case R.id.fabAddMembers:
                //region fabAddMembers
                try {
                    CharSequence option[] = new CharSequence[0];
                    SubscriptionBusiness subscriptionBusiness = new SubscriptionBusiness(MainActivity.this, 1);
                    ArrayList<Subscription> subscriptionList = subscriptionBusiness.getAll();
                    subscriptionBusiness.close();

                    MemberBusiness memberBusiness = new MemberBusiness(MainActivity.this, 1);
                    ArrayList<Member> memberList = memberBusiness.getAll();
                    memberBusiness.close();

                    if (subscriptionList.size() < 1) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_view_no_subscription, null);
                        alertDialogBuilder.setView(dialogView);
                        ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
                        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        ivSave.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    startActivity(new Intent(MainActivity.this, SubscriptionListActivity.class));
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
                    } else if (memberList.size() < 1) {
                        option = new CharSequence[]{"New Member"};
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Base_CustomDialogAppTheme);
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    startActivity(new Intent(MainActivity.this, MembershipActivity.class));
                                } else {
                                    new MembershipDialog(MainActivity.this, layoutInflater());
                                }

                            }
                        });
                        builder.show();
                    } else {
                        option = new CharSequence[]{"New Member", "Existing Member"};
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Base_CustomDialogAppTheme);
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    startActivity(new Intent(MainActivity.this, MembershipActivity.class));
                                } else {
                                    new MembershipDialog(MainActivity.this, layoutInflater());
                                }

                            }
                        });
                        builder.show();
                    }
                    fabMenu.collapse();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //endregion
                break;
            case R.id.ivNext:
                cvMainCalendar.showNextMonth();
                setCalendar(cvMainCalendar.getFirstDayOfCurrentMonth());
                break;
            case R.id.ivPrevious:
                cvMainCalendar.showPreviousMonth();
                setCalendar(cvMainCalendar.getFirstDayOfCurrentMonth());
                break;

        }
    }
    //endregion

    //region Private Methods
    private void initialize() {
        cvMainCalendar = (CompactCalendarView) findViewById(R.id.cvMainCalendar);

        fabMenu = (FloatingActionsMenu) findViewById(R.id.fabMenu);
        fabSubscription = (FloatingActionButton) findViewById(R.id.fabSubscription);
        fabMembers = (FloatingActionButton) findViewById(R.id.fabMembers);
        fabAddMembers = (FloatingActionButton) findViewById(R.id.fabAddMembers);
        fabMemberships = (FloatingActionButton) findViewById(R.id.fabMemberships);

        ivNext = (ImageView) findViewById(R.id.ivNext);
        ivPrevious = (ImageView) findViewById(R.id.ivPrevious);

        tvMonth = (TextView) findViewById(R.id.tvMonth);

        flFloatingMenu = (FrameLayout) findViewById(R.id.flFloatingMenu);
        flFloatingMenu.getBackground().setAlpha(0);

        rvExpiredList = (RecyclerView) findViewById(R.id.rvExpiredList);
        rvExpiredList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        rvExpiredList.setLayoutManager(mLayoutManager);

        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                //region expand
                menuShowCase();
                flFloatingMenu.getBackground().setAlpha(200);
                flFloatingMenu.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
                //endregion
            }

            @Override
            public void onMenuCollapsed() {
                //region Collapsed
                flFloatingMenu.getBackground().setAlpha(0);
                flFloatingMenu.setOnTouchListener(null);
                //endregion
            }
        });

        fabSubscription.setOnClickListener(this);
        fabMembers.setOnClickListener(this);
        fabAddMembers.setOnClickListener(this);
        fabMemberships.setOnClickListener(this);

        ivNext.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private LayoutInflater layoutInflater() {
        return this.getLayoutInflater();
    }

    private ArrayList<Member> getMembers(ArrayList<MemberSubscription> subscriptionList) throws ParseException {
        ArrayList<Member> memberList = new ArrayList<Member>();
        for (MemberSubscription memberSubscription : subscriptionList) {
            MemberBusiness memberBusiness = new MemberBusiness(MainActivity.this, 0);
            memberList.add(memberBusiness.getById(memberSubscription.getMemberId()));
            memberBusiness.close();
        }
        return memberList;

    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private void setCalendar(Date date) {
        try {
            cvMainCalendar.setCurrentDate(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            tvMonth.setText(headingFormat.format(date));
            calenderDate = (calendar.get(Calendar.DATE) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));
            MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(MainActivity.this, 1);
            ArrayList<MemberSubscription> memberSubscriptionList = memberSubscriptionBusiness.getExpiringMember(calenderDate);
            memberSubscriptionBusiness.close();
            ArrayList<Member> memberList = getMembers(memberSubscriptionList);
            mAdapter = new MemberDataAdapter(memberList, new MemberDataAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Member member) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", member.getMemberId());
                    bundle.putInt("TYPE", R.integer.Renew);
                    Intent intent = new Intent(MainActivity.this, MemberDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }, new MemberDataAdapter.OnItemLongClickListener() {
                @Override
                public void OnItemLongClickListener(Member member) {

                }
            });
            rvExpiredList.setAdapter(mAdapter);
            rvExpiredList.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addCalendarEvents() {
        try {
            ArrayList<Event> events = new ArrayList<>();
            MemberSubscriptionBusiness memberSubscriptionBusiness = new MemberSubscriptionBusiness(MainActivity.this, 1);
            ArrayList<MemberSubscription> memberSubscriptionList = memberSubscriptionBusiness.getAll();
            memberSubscriptionBusiness.close();
            for (MemberSubscription memberSubscription : memberSubscriptionList) {
                events.add(new Event(getResources().getColor(R.color.colorPrimary), memberSubscription.getEndDate().getTime()));
            }
            cvMainCalendar.removeAllEvents();
            cvMainCalendar.addEvents(events);
            cvMainCalendar.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mainShowCase() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SharedPreferencesName.MAIN_ACTIVITY_LAUNCH);
        sequence.setConfig(config);
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(cvMainCalendar)
                .setDismissText("Next")
                .setContentText("Calender Shows all expiring members as events.")
                .withRectangleShape(true)
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(rvExpiredList)
                .setDismissText("Next")
                .setContentText("Here displays all the expiring members according to selected date from the calendar.")
                .withRectangleShape(true)
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.toolbar), "menu")
                .setDismissText("Done")
                .setContentText("This menu item will return you back to the today's date. Settings and About Application will be accessible from hidden menu.")
                .withCircleShape()
                .build()
        );
        sequence.start();
    }

    private void menuShowCase() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SharedPreferencesName.MAIN_ACTIVITY_MENU_LAUNCH);
        sequence.setConfig(config);
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(fabMenu)
                .setDismissText("Next")
                .setContentText("This is the main menu of the application. You can use this to navigate with in the application.")
                .withCircleShape()
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(fabSubscription)
                .setDismissText("Next")
                .setContentText("View all available subscriptions.")
                .withCircleShape()
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(fabMembers)
                .setDismissText("Next")
                .setContentText("View all registered members.")
                .withCircleShape()
                .build()
        );

        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(fabMemberships)
                .setDismissText("Next")
                .setContentText("View all Active membership.")
                .withCircleShape()
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(fabAddMembers)
                .setDismissText("Done")
                .setContentText("Add a new membership or renew a existing membership.")
                .withCircleShape()
                .build()
        );
        sequence.start();

    }

    private void welComeDialog(){
        PrefsManager mPrefsManager = new PrefsManager(MainActivity.this, SharedPreferencesName.MAIN_ACTIVITY_LAUNCH);
        int result = mPrefsManager.getSequenceStatus();
        if (result == 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_view_welcome, null);
            alertDialogBuilder.setView(dialogView);
            Button bLetsGo = (Button) dialogView.findViewById(R.id.bLetGo);
            firstDialog = alertDialogBuilder.create();
            firstDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        firstDialog.cancel();
                        onBackPressed();
                    }
                    return false;
                }
            });
            bLetsGo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        firstDialog.cancel();
                        Intent intent = new Intent("com.kasungunathilaka.gympartner.SETTINGS_CHANGE");
                        sendBroadcast(intent);
                        mainShowCase();
                    }
                    return false;
                }

            });
            firstDialog.show();
        }
    }

    private void exitDialogDisplay(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_view_exit, null);
        alertDialogBuilder.setView(dialogView);
        ImageView ivSave = (ImageView) dialogView.findViewById(R.id.ivSave);
        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        ivSave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    finish();
                    System.exit(0);
                }
                return false;
            }

        });
        ivClose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    alertDialog.dismiss();
                    welComeDialog();
                }
                return false;
            }
        });
        alertDialog.show();
    }
    //endregion

    //region Public Methods
    public void resume() {
        this.onResume();
    }
    //endregion


}
