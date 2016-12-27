package com.kasungunathilaka.adapter;

//region Imported

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasungunathilaka.domain.MemberSymptom;
import com.kasungunathilaka.gympartner.R;

import java.util.ArrayList;
//endregion

// </summary>
// Source File		: SymptomDataAdapter.java
// Package 			: com.kasungunathilaka.adapter
// Description		: Symptom Adapter
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 22 May 2016      Kasun Gunathilak			    Created
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

public class SymptomDataAdapter extends RecyclerView.Adapter<SymptomDataAdapter.ViewHolder> {
    public ArrayList<MemberSymptom> SymptomList;
    public OnItemLongClickListener listener;

    public interface OnItemLongClickListener {
        void OnItemLongClickListener(MemberSymptom memberSymptom);
    }

    public SymptomDataAdapter(ArrayList<MemberSymptom> memberSymptomList, OnItemLongClickListener listener) {
        this.SymptomList = memberSymptomList;
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

        MemberSymptom memberSymptom = SymptomList.get(position);
        viewHolder.bind(memberSymptom, listener);

    }

    @Override
    public int getItemCount() {
        return SymptomList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView ivSymptom;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tv = (TextView) itemLayoutView.findViewById(R.id.tv);
            ivSymptom = (ImageView) itemLayoutView.findViewById(R.id.ivSymptom);

        }

        public void bind(final MemberSymptom memberSymptom, final OnItemLongClickListener listener) {
            tv.setText(memberSymptom.getDescription());
            switch (memberSymptom.getCondition()) {
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
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.OnItemLongClickListener(memberSymptom);
                    return false;
                }
            });
        }
    }

    public void dataChange(ArrayList<MemberSymptom> memberSymptomList) {
        this.SymptomList.clear();
        this.SymptomList.addAll(memberSymptomList);
        notifyDataSetChanged();
    }

}
