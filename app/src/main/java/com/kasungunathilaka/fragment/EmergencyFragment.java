package com.kasungunathilaka.fragment;

//region Imported
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kasungunathilaka.business.MemberBusiness;
import com.kasungunathilaka.business.MemberEmergencyContactBusiness;
import com.kasungunathilaka.domain.MemberEmergencyContact;
import com.kasungunathilaka.gympartner.R;

import java.util.ArrayList;
//endregion

// </summary>
// Source File		: EmergencyFragment.java
// Package 			: com.kasungunathilaka.fragment
// Description		: Emergency Contact Details Fragment
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 21 May 2016		Kasun Gunathilak			    Created
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

public class EmergencyFragment extends Fragment {

    int memberId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.member_emergency_info_card_layout, container, false);
        RecyclerView rvEmergency = (RecyclerView) inflatedView.findViewById(R.id.rvEmergency);
        collectMemberId();
        setupRecyclerView(rvEmergency);
        return inflatedView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        MemberEmergencyContactBusiness memberEmergencyContactBusiness = new MemberEmergencyContactBusiness(recyclerView.getContext(), 1);
        ArrayList<MemberEmergencyContact> memberEmergencyContact = memberEmergencyContactBusiness.getByMemberId(memberId);
        memberEmergencyContactBusiness.close();

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new EmergencyContactDataAdapter(memberEmergencyContact, new EmergencyContactDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MemberEmergencyContact memberEmergencyContact) {

            }
        }));
    }

    private void collectMemberId() {
        try {
            memberId = getArguments().getInt("member_id");
            MemberBusiness memberBusiness = new MemberBusiness(getActivity(), 1);
            memberBusiness.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static class EmergencyContactDataAdapter extends RecyclerView.Adapter<EmergencyContactDataAdapter.ViewHolder> {
        public ArrayList<MemberEmergencyContact> memberEmergencyContactList;
        public OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(MemberEmergencyContact memberEmergencyContact);
        }

        public EmergencyContactDataAdapter(ArrayList<MemberEmergencyContact> memberEmergencyContactList, OnItemClickListener listener) {
            this.memberEmergencyContactList = memberEmergencyContactList;
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

            MemberEmergencyContact memberEmergencyContact = memberEmergencyContactList.get(position);
            viewHolder.bind(memberEmergencyContact, listener);

        }

        @Override
        public int getItemCount() {
            return memberEmergencyContactList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvContactName;
            TextView tvContactNo;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                tvContactName = (TextView) itemLayoutView.findViewById(R.id.tvContactName);
                tvContactNo = (TextView) itemLayoutView.findViewById(R.id.tvContactNo);

            }

            public void bind(final MemberEmergencyContact memberEmergencyContact, final OnItemClickListener listener) {
                tvContactName.setText(memberEmergencyContact.getName());
                tvContactNo.setText(memberEmergencyContact.getContactNo());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(memberEmergencyContact);
                    }
                });
            }
        }

    }

}
