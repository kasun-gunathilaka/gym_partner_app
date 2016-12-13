package com.kasungunathilaka.adapter;

//region Imported
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kasungunathilaka.domain.MemberEmergencyContact;
import com.kasungunathilaka.gympartner.R;

import java.util.ArrayList;
//endregion

// </summary>
// Source File		: EmergencyContactDataAdapter.java
// Package 			: com.kasungunathilaka.adapter
// Description		: Emergency Contact Adapter
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 24 April 2016    Kasun Gunathilak			    Created
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

public class EmergencyContactDataAdapter extends RecyclerView.Adapter<EmergencyContactDataAdapter.ViewHolder> {
    public ArrayList<MemberEmergencyContact> EmergencyList;
    public OnItemLongClickListener listener;

    public interface OnItemLongClickListener {
        void OnItemLongClickListener(MemberEmergencyContact memberEmergencyContact);
    }

    public EmergencyContactDataAdapter(ArrayList<MemberEmergencyContact> memberEmergencyContactList, OnItemLongClickListener listener) {
        this.EmergencyList = memberEmergencyContactList;
        this.listener = listener;
    }

    @Override
    public EmergencyContactDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_row_emergency_contact, parent, false);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        MemberEmergencyContact memberEmergencyContact = EmergencyList.get(position);
        viewHolder.bind(memberEmergencyContact, listener);

    }

    @Override
    public int getItemCount() {
        return EmergencyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvContactName;
        TextView tvContactNo;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvContactName = (TextView) itemLayoutView.findViewById(R.id.tvContactName);
            tvContactNo = (TextView) itemLayoutView.findViewById(R.id.tvContactNo);

        }

        public void bind(final MemberEmergencyContact memberEmergencyContact, final OnItemLongClickListener listener) {
            tvContactName.setText(memberEmergencyContact.getName());
            tvContactNo.setText(memberEmergencyContact.getContactNo());
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.OnItemLongClickListener(memberEmergencyContact);
                    return true;
                }
            });
        }
    }
}
