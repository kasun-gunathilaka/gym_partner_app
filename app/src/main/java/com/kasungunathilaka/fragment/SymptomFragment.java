package com.kasungunathilaka.fragment;

//region Imported
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasungunathilaka.business.MemberBusiness;
import com.kasungunathilaka.business.MemberSymptomBusiness;
import com.kasungunathilaka.domain.MemberSymptom;
import com.kasungunathilaka.gympartner.R;

import java.util.ArrayList;
//endregion

// </summary>
// Source File		: SymptomFragment.java
// Package 			: com.kasungunathilaka.fragment
// Description		: Symptom Details Fragment
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

public class SymptomFragment extends Fragment {

    int memberId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.member_symptom_info_card_layout, container, false);
        RecyclerView rvEmergency = (RecyclerView) inflatedView.findViewById(R.id.rvSymptom);
        collectMemberId();
        setupRecyclerView(rvEmergency);
        return inflatedView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        MemberSymptomBusiness memberSymptomBusiness = new MemberSymptomBusiness(recyclerView.getContext(), 1);
        ArrayList<MemberSymptom> memberSymptom = memberSymptomBusiness.getByMemberId(memberId);
        memberSymptomBusiness.close();

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SymptomDataAdapter(memberSymptom, new SymptomDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MemberSymptom memberSymptom) {

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

    public static class SymptomDataAdapter extends RecyclerView.Adapter<SymptomDataAdapter.ViewHolder> {
        public ArrayList<MemberSymptom> memberSymptomList;
        public OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(MemberSymptom memberSymptom);
        }

        public SymptomDataAdapter(ArrayList<MemberSymptom> memberSymptomList, OnItemClickListener listener) {
            this.memberSymptomList = memberSymptomList;
            this.listener = listener;
        }

        @Override
        public SymptomDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.adapter_row_symptom, parent, false);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            MemberSymptom memberSymptom = memberSymptomList.get(position);
            viewHolder.bind(memberSymptom, listener);

        }

        @Override
        public int getItemCount() {
            return memberSymptomList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            ImageView ivSymptom;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                tv = (TextView) itemLayoutView.findViewById(R.id.tv);
                ivSymptom = (ImageView) itemLayoutView.findViewById(R.id.ivSymptom);

            }

            public void bind(final MemberSymptom memberSymptom, final OnItemClickListener listener) {
                tv.setText(memberSymptom.getDescription());
                switch (memberSymptom.getCondition()){
                    case "Normal":
                        ivSymptom.setBackgroundColor(Color.parseColor("#9ACD32"));
                        break;
                    case "Priority":
                        ivSymptom.setBackgroundColor(Color.parseColor("#FFFF00"));
                        break;
                    case "Critical":
                        ivSymptom.setBackgroundColor(Color.parseColor("#FF4500"));
                        break;
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(memberSymptom);
                    }
                });
            }
        }

    }
}
