package com.kasungunathilaka.adapter;

//region Imported

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasungunathilaka.domain.Member;
import com.kasungunathilaka.gympartner.R;

import java.util.ArrayList;
import java.util.List;
//endregion

// </summary>
// Source File		: MemberDataAdapter.java
// Package 			: com.kasungunathilaka.adapter
// Description		: Member Adapter
// </summary>
//
// <remarks>
// Modification History:
// Date				Author/Reviewer					Description
// -----------------------------------------------------------------------------------------------
// 21 May 2016      Kasun Gunathilak			    Created
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

public class MemberDataAdapter extends RecyclerView.Adapter<MemberDataAdapter.ViewHolder> implements Filterable {
    public ArrayList<Member> memberList;
    public OnItemClickListener listener;
    public OnItemLongClickListener longListener;
    private List<Member> originalData = null;
    private MemberFilter memberFilter = new MemberFilter();

    public MemberDataAdapter(ArrayList<Member> memberList, OnItemClickListener listener, OnItemLongClickListener longListener) {
        this.memberList = memberList;
        this.originalData = memberList;
        this.listener = listener;
        this.longListener = longListener;
    }

    @Override
    public Filter getFilter() {
        return memberFilter;
    }

    @Override
    public MemberDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_row_member, parent, false);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Member member = memberList.get(position);
        viewHolder.bind(member, listener, longListener);

    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Member member);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClickListener(Member member);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCardListMemberPic;
        TextView tvCardListMemberCode;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ivCardListMemberPic = (ImageView) itemLayoutView.findViewById(R.id.ivCardListMemberPic);
            tvCardListMemberCode = (TextView) itemLayoutView.findViewById(R.id.tvCardListMemberCode);

        }

        public void bind(final Member member, final OnItemClickListener listener, final OnItemLongClickListener longListener) {
            if (member.getImage() != null) {
                ivCardListMemberPic.setImageBitmap(BitmapFactory.decodeByteArray(member.getImage()
                        , 0, member.getImage().length));
            } else {
                ivCardListMemberPic.setImageResource(R.drawable.avatar);
            }


            tvCardListMemberCode.setText(member.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(member);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.OnItemLongClickListener(member);
                    return true;
                }
            });
        }
    }

    private class MemberFilter extends Filter {
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
                final List<Member> list = originalData;
                int count = list.size();
                final ArrayList<Member> members = new ArrayList<Member>(count);
                Member member;
                for (int i = 0; i < count; i++) {
                    member = list.get(i);
                    if (member.getMemberCode().toLowerCase().contains(filterString)
                            || member.getName().toLowerCase().contains(filterString)
                            || member.getAddress().toLowerCase().contains(filterString)
                            || member.getBloodType().toLowerCase().contains(filterString)
                            || member.getContactNo().toLowerCase().contains(filterString)
                            || member.getNic().toLowerCase().contains(filterString)) {
                        members.add(member);
                    }
                }
                results.values = members;
                results.count = members.size();
                return results;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            memberList = (ArrayList<Member>) results.values;
            notifyDataSetChanged();
        }

    }
}