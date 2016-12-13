package com.kasungunathilaka.adapter;

//region Imported

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasungunathilaka.domain.MemberSubscription;
import com.kasungunathilaka.gympartner.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//endregion

// </summary>
// Source File		: MembershipAdapter.java
// Package 			: com.kasungunathilaka.adapter
// Description		: Membership Adapter
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 22 May 2016      Kasun Gunathilak			    Created
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

public class MembershipAdapter extends RecyclerView.Adapter<MembershipAdapter.ViewHolder> implements Filterable {
    public ArrayList<MemberSubscription> memberSubscriptionList;
    public OnItemClickListener listener;
    private boolean late = true;
    private List<MemberSubscription> originalData = null;
    private MembershipFilter membershipFilter = new MembershipFilter();

    @Override
    public Filter getFilter() {
        return membershipFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(MemberSubscription memberSubscription);
    }

    public MembershipAdapter(ArrayList<MemberSubscription> memberSubscriptionList, OnItemClickListener listener) {
        this.memberSubscriptionList = memberSubscriptionList;
        this.originalData = memberSubscriptionList;
        this.listener = listener;
    }

    @Override
    public MembershipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_row_membership, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tvExpireDate;
        TextView tvMemberCode;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            iv = (ImageView) itemLayoutView.findViewById(R.id.iv);
            tvExpireDate = (TextView) itemLayoutView.findViewById(R.id.tvExpireDate);
            tvMemberCode = (TextView) itemLayoutView.findViewById(R.id.tvMemberCode);
        }

        public void bind(final MemberSubscription memberSubscription, final OnItemClickListener listener) {
            try {
                tvExpireDate.setText(getDate(memberSubscription.getEndDate().toString()));
                tvMemberCode.setText(memberSubscription.getMember().getMemberCode());
                if (late)
                    iv.setBackgroundColor(Color.rgb(238, 58, 84));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(memberSubscription);
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private String getDate(String dateString) throws ParseException {
        DateFormat readFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = null;
        try {
            date = readFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar currentCal = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if (currentCal.after(cal)) {
            late = true;
        } else if (currentCal.before(cal)) {
            late = false;
        }


        String formatedDate = cal.get(Calendar.DATE)
                + "-"
                + (cal.get(Calendar.MONTH) + 1)
                + "-"
                + cal.get(Calendar.YEAR);
        return formatedDate;
    }

    private class MembershipFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            if (constraint.toString().contentEquals("")) {
                FilterResults results = new FilterResults();
                results.values = originalData;
                results.count = originalData.size();
                return results;
            } else {
                String filterString = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                final List<MemberSubscription> list = originalData;
                int count = list.size();
                final ArrayList<MemberSubscription> memberSubscriptions = new ArrayList<MemberSubscription>(count);
                MemberSubscription memberSubscription;
                for (int i = 0; i < count; i++) {
                    memberSubscription = list.get(i);
                    if (memberSubscription.getMember().getMemberCode().toLowerCase().contains(filterString)
                            || memberSubscription.getMember().getName().toLowerCase().contains(filterString)
                            || memberSubscription.getMember().getAddress().toLowerCase().contains(filterString)
                            || memberSubscription.getMember().getBloodType().toLowerCase().contains(filterString)
                            || memberSubscription.getMember().getContactNo().toLowerCase().contains(filterString)
                            || memberSubscription.getMember().getNic().toLowerCase().contains(filterString)) {
                        memberSubscriptions.add(memberSubscription);
                    }
                }
                results.values = memberSubscriptions;
                results.count = memberSubscriptions.size();
                return results;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            memberSubscriptionList = (ArrayList<MemberSubscription>) results.values;
            notifyDataSetChanged();
        }

    }
}

