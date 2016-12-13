package com.kasungunathilaka.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kasungunathilaka.gympartner.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kasun on 20/06/2016.
 */
public class SearchableDataDialog extends DialogFragment implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static final String ITEMS = "items";
    private ArrayAdapter arrayAdapter;
    private ListView lvMember;
    private ImageView ivClose;
    private TextView tvTitle;
    private SearchableItem searchableItem;
    private OnSearchTextChanged onSearchTextChanged;
    private SearchView svMember;
    private String title;
    private String positiveButtonText;
    private DialogInterface.OnClickListener onClickListener;

    public SearchableDataDialog() {

    }

    public static SearchableDataDialog newInstance(List items) {
        SearchableDataDialog multiSelectExpandableFragment = new
                SearchableDataDialog();

        Bundle args = new Bundle();
        args.putSerializable(ITEMS, (Serializable) items);

        multiSelectExpandableFragment.setArguments(args);

        return multiSelectExpandableFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Getting the layout inflater to inflate the view in an alert dialog.
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        // Crash on orientation change #7
        // Change Start
        // Description: As the instance was re initializing to null on rotating the device,
        // getting the instance from the saved instance
        if (null != savedInstanceState) {
            searchableItem = (SearchableItem) savedInstanceState.getSerializable("item");
        }
        // Change End

        View rootView = inflater.inflate(R.layout.dialog_view_member_select, null);
        setData(rootView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(rootView);

//        String strPositiveButton = positiveButtonText == null ? "CLOSE" : positiveButtonText;
//        alertDialog.setPositiveButton(strPositiveButton, onClickListener);
//
//        String strTitle = title == null ? "Select Item" : title;
//        alertDialog.setTitle(strTitle);

        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", searchableItem);
        super.onSaveInstanceState(outState);
    }

    public void setTitle(String strTitle) {
        title = strTitle;
    }

    public void setPositiveButton(String strPositiveButtonText) {
        positiveButtonText = strPositiveButtonText;
    }

    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        positiveButtonText = strPositiveButtonText;
        onClickListener = onClickListener;
    }

    public void setOnSearchableItemClickListener(SearchableItem searchableItem) {
        this.searchableItem = searchableItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this.onSearchTextChanged = onSearchTextChanged;
    }

    private void setData(View rootView) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context
                .SEARCH_SERVICE);

        svMember = (SearchView) rootView.findViewById(R.id.svMember);
        svMember.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName
                ()));
        svMember.setIconifiedByDefault(false);
        svMember.setOnQueryTextListener(this);
        svMember.setOnCloseListener(this);
        svMember.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(svMember.getWindowToken(), 0);


        List items = (List) getArguments().getSerializable(ITEMS);

        lvMember = (ListView) rootView.findViewById(R.id.lvMember);
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                items);
        lvMember.setAdapter(arrayAdapter);
        lvMember.setTextFilterEnabled(true);
        lvMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchableItem.onSearchableItemClicked(arrayAdapter.getItem(position), position);
                getDialog().dismiss();
            }
        });

        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        ivClose = (ImageView) rootView.findViewById(R.id.ivClose);
        ivClose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    getDialog().dismiss();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        svMember.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
//        arrayAdapter.filterData(s);
        if (TextUtils.isEmpty(s)) {
//                lvMember.clearTextFilter();
            ((ArrayAdapter) lvMember.getAdapter()).getFilter().filter(null);
        } else {
            ((ArrayAdapter) lvMember.getAdapter()).getFilter().filter(s);
        }
        if (null != onSearchTextChanged) {
            onSearchTextChanged.onSearchTextChanged(s);
        }
        return true;
    }

    public interface SearchableItem<T> extends Serializable {
        void onSearchableItemClicked(T item, int position);
    }

    public interface OnSearchTextChanged {
        void onSearchTextChanged(String strText);
    }
}
