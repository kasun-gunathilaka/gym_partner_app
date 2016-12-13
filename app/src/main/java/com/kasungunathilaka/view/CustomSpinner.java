package com.kasungunathilaka.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.kasungunathilaka.dialog.SearchableDataDialog;
import com.kasungunathilaka.gympartner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasun on 20/06/2016.
 */
public class CustomSpinner extends Spinner implements View.OnTouchListener,
        SearchableDataDialog.SearchableItem {

    public static final int NO_ITEM_SELECTED = 0;
    private Context context;
    private List list;
    private SearchableDataDialog searchableDataDialog;

    private boolean isDirty;
    private ArrayAdapter arrayAdapter;
    private String string;
    private boolean isFromInit;

    public CustomSpinner(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CustomSpinner_hintText) {
                string = a.getString(attr);
            }
        }
        a.recycle();
        init();
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        list = new ArrayList();
        searchableDataDialog = SearchableDataDialog.newInstance
                (list);
        searchableDataDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);

        arrayAdapter = (ArrayAdapter) getAdapter();
        if (!TextUtils.isEmpty(string)) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout
                    .simple_list_item_1, new String[]{string});
            isFromInit = true;
            setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            this.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));

            if (null != arrayAdapter) {

                // Refresh content #6
                // Change Start
                // Description: The items were only set initially, not reloading the data in the
                // spinner every time it is loaded with items in the adapter.
                list.clear();
                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                    list.add(arrayAdapter.getItem(i));
                }
                // Change end.

                searchableDataDialog.show(scanForActivity(context).getFragmentManager(), "TAG");
            }
        }
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {

        if (!isFromInit) {
            arrayAdapter = (ArrayAdapter) adapter;
            if (!TextUtils.isEmpty(string) && !isDirty) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout
                        .simple_list_item_1, new String[]{string});
                super.setAdapter(arrayAdapter);
            } else {
                super.setAdapter(adapter);
            }

        } else {
            isFromInit = false;
            super.setAdapter(adapter);
        }
    }

    @Override
    public void onSearchableItemClicked(Object item, int position) {
        setSelection(list.indexOf(item));

        if (!isDirty) {
            isDirty = true;
            setAdapter(arrayAdapter);
            setSelection(list.indexOf(item));
        }
    }

    public void setTitle(String strTitle) {
        searchableDataDialog.setTitle(strTitle);
    }

//    public void setPositiveButton(String strPositiveButtonText) {
//        searchableDataDialog.setPositiveButton(strPositiveButtonText);
//    }
//
//    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
//        searchableDataDialog.setPositiveButton(strPositiveButtonText, onClickListener);
//    }

    public void setOnSearchTextChangedListener(SearchableDataDialog.OnSearchTextChanged onSearchTextChanged) {
        searchableDataDialog.setOnSearchTextChangedListener(onSearchTextChanged);
    }

    private Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        if (!TextUtils.isEmpty(string) && !isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        if (!TextUtils.isEmpty(string) && !isDirty) {
            return null;
        } else {
            return super.getSelectedItem();
        }
    }
}
