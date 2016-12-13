package com.kasungunathilaka.adapter;

//region Imported
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kasungunathilaka.domain.Subscription;
import com.kasungunathilaka.gympartner.R;

import java.util.ArrayList;
import java.util.List;
//endregion

// </summary>
// Source File		: SubscriptionAdapter.java
// Package 			: com.kasungunathilaka.adapter
// Description		: Subscription Adapter
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

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> implements Filterable {
    public ArrayList<Subscription> subscriptionList;
    public ArrayList<Subscription> originalData;
    public OnItemClickListener listener;
    public OnItemLongClickListener longListener;
    private SubscriptionFilter subscriptionFilter = new SubscriptionFilter();

    @Override
    public Filter getFilter() {
        return subscriptionFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(Subscription subscription);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClickListener(Subscription subscription);
    }

    public SubscriptionAdapter(ArrayList<Subscription> subscriptionList, OnItemClickListener listener, OnItemLongClickListener longListener) {
        this.subscriptionList = subscriptionList;
        this.originalData = subscriptionList;
        this.listener = listener;
        this.longListener = longListener;
    }

    @Override
    public SubscriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_row_subscription, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Subscription subscription = subscriptionList.get(position);
        viewHolder.bind(subscription, listener, longListener);

    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDuration;
        TextView tvSubscription;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvSubscription = (TextView) itemLayoutView.findViewById(R.id.tvSubscription);
            tvDuration = (TextView) itemLayoutView.findViewById(R.id.tvDuration);
        }

        public void bind(final Subscription subscription, final OnItemClickListener listener, final OnItemLongClickListener longListener) {
            tvSubscription.setText(subscription.getName());
            tvDuration.setText(String.valueOf(subscription.getDuration()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(subscription);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.OnItemLongClickListener(subscription);
                    return true;
                }
            });
        }
    }

    private class SubscriptionFilter extends Filter {
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
                final List<Subscription> list = originalData;
                int count = list.size();
                final ArrayList<Subscription> subscriptions = new ArrayList<Subscription>(count);
                Subscription subscription;
                for (int i = 0; i < count; i++) {
                    subscription = list.get(i);
                    if (subscription.getName().toLowerCase().contains(filterString)
                            || String.valueOf(subscription.getDuration()).toLowerCase().contains(filterString)) {
                        subscriptions.add(subscription);
                    }
                }
                results.values = subscriptions;
                results.count = subscriptions.size();
                return results;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            subscriptionList = (ArrayList<Subscription>) results.values;
            notifyDataSetChanged();
        }

    }
}
