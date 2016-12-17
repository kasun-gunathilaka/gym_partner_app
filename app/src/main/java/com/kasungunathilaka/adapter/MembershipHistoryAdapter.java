package com.kasungunathilaka.adapter;

//region Imported
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasungunathilaka.domain.MemberSubscription;
import com.kasungunathilaka.gympartner.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//endregion

// </summary>
// Source File		: MembershipHistoryAdapter.java
// Package 			: com.kasungunathilaka.adapter
// Description		: Membership History Adapter
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 4 June 2016      Kasun Gunathilak			    Created
// 17 June 2016     Kasun Gunathilak			    Filter Added
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

public class MembershipHistoryAdapter extends RecyclerView.Adapter<MembershipHistoryAdapter.ViewHolder> {
    public ArrayList<MemberSubscription> memberSubscriptionList;
    public OnItemLongClickListener listener;

    public interface OnItemLongClickListener {
        void OnItemLongClickListener(MemberSubscription memberSubscription);
    }

    public MembershipHistoryAdapter(ArrayList<MemberSubscription> memberSubscriptionList, OnItemLongClickListener listener) {
        this.memberSubscriptionList = memberSubscriptionList;
        this.listener = listener;
    }

    @Override
    public MembershipHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_row_membership_history, parent, false);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        MemberSubscription memberSubscription = memberSubscriptionList.get(position);
        viewHolder.bind(memberSubscription, listener);

    }

    @Override
    public int getItemCount() {
        return memberSubscriptionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFrom;
        TextView tvTo;
        LinearLayout llHistory;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvFrom = (TextView) itemLayoutView.findViewById(R.id.tvFrom);
            tvTo = (TextView) itemLayoutView.findViewById(R.id.tvTo);
            llHistory = (LinearLayout) itemLayoutView.findViewById(R.id.llHistory);

        }

        public void bind(final MemberSubscription memberSubscription, final OnItemLongClickListener listener) {
            if (memberSubscription.getIsActive() < 1) {
                tvFrom.setText(getDate(memberSubscription.getStartDate()));
                tvTo.setText(getDate(memberSubscription.getEndDate()));
            } else if (Calendar.getInstance().getTime().after(memberSubscription.getEndDate())) {
                tvFrom.setText(getDate(memberSubscription.getStartDate()));
                tvTo.setText(getDate(memberSubscription.getEndDate()));
                llHistory.setBackgroundColor(Color.parseColor("#FF4500"));
            } else {
                tvFrom.setText(getDate(memberSubscription.getStartDate()));
                tvTo.setText(getDate(memberSubscription.getEndDate()));
                llHistory.setBackgroundColor(Color.parseColor("#9ACD32"));
            }

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.OnItemLongClickListener(memberSubscription);
                    return true;
                }
            });
        }

        private String getDate(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return (c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE));
        }
    }
}