package com.kasungunathilaka.fragment;

//region Imported
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kasungunathilaka.business.MemberBusiness;
import com.kasungunathilaka.domain.Member;
import com.kasungunathilaka.gympartner.R;

import java.util.Calendar;
import java.util.Date;
//endregion

// </summary>
// Source File		: MemberFragment.java
// Package 			: com.kasungunathilaka.fragment
// Description		: Member Details Fragment
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

public class MemberFragment extends Fragment {

    private int memberId;
    private Member currentMember;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        collectMember();
        View inflatedView = inflater.inflate(R.layout.member_info_card_layout, container, false);

        TextView tvName = (TextView) inflatedView.findViewById(R.id.tvName);
        TextView tvNic = (TextView) inflatedView.findViewById(R.id.tvNic);
        TextView tvContactNo = (TextView) inflatedView.findViewById(R.id.tvContactNo);
        TextView tvAddress = (TextView) inflatedView.findViewById(R.id.tvAddress);
        TextView tvBloodType = (TextView) inflatedView.findViewById(R.id.tvBloodType);
        TextView tvBirthDay = (TextView) inflatedView.findViewById(R.id.tvBirthday);
        TextView tvAge = (TextView) inflatedView.findViewById(R.id.tvAge);

        tvName.setText(currentMember.getName());
        tvNic.setText(currentMember.getNic());
        tvContactNo.setText(currentMember.getContactNo());
        tvAddress.setText(currentMember.getAddress());
        tvBloodType.setText(currentMember.getBloodType());
        tvBirthDay.setText(getFormatedDate(currentMember.getBirthday()));
        tvAge.setText(String.valueOf(currentMember.getAge()));

        return inflatedView;
    }

    private String getFormatedDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String formatedDate = cal.get(Calendar.DATE)
                + "-"
                + (cal.get(Calendar.MONTH) + 1)
                + "-"
                + cal.get(Calendar.YEAR);
        return formatedDate;
    }

    private void collectMember() {
        try {
            memberId = getArguments().getInt("member_id");
            MemberBusiness memberBusiness = new MemberBusiness(getActivity(), 1);
            currentMember = memberBusiness.getById(memberId);
            memberBusiness.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
