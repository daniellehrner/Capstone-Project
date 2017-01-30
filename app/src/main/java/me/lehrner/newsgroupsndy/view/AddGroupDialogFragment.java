/*
 * Copyright (C) 2017 Daniel Lehrner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.lehrner.newsgroupsndy.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.lehrner.newsgroupsndy.NDYApplication;
import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.presenter.GroupPresenter;

public class AddGroupDialogFragment extends AppCompatDialogFragment
                                    implements AddGroupView,
                                        AddGroupClickHandler,
                                        SearchView.OnQueryTextListener{

    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final int ADD_GROUP_LOADER = 0;

    @Inject
    GroupPresenter mGroupPresenter;
    GetGroupId mGetGroupId;
    GetServerId mGetServerId;
    View mView;

    @BindView(R.id.search_add_group)
    SearchView mSearchView;

    @BindView(R.id.add_group_list)
    RecyclerView mGroupListView;


    public AddGroupDialogFragment() {
        // Required empty public constructor
    }

    public static AddGroupDialogFragment newInstance() {
        return new AddGroupDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((NDYApplication) getActivity().getApplication()).getComponent().inject(this);

        mView = inflater.inflate(R.layout.fragment_add_group_dialog, container, false);

        ButterKnife.bind(this, mView);

        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);

        mSearchView.setSearchableInfo(searchManager.
                getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setIconifiedByDefault(false);

//        getActivity().getSupportLoaderManager().initLoader(ADD_GROUP_LOADER, null, this);

        mGroupListView.setHasFixedSize(true);

//        mGroupAdapter = new GroupAdapter();
//        mGroupListView.setAdapter(mGroupAdapter);

        return mView;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams")
        View titleView = inflater.inflate(R.layout.dialog_add_group, null);

        builder.setView(R.layout.fragment_add_group_dialog)
                .setCustomTitle(titleView);

        return builder.create();
    }

    @Override
    public int getGroupId() {
        return mGetGroupId.getGroupId();
    }

    @Override
    public String getGroupName() {
//        return getStringFromAutoCompleteText(R.id.text_group_name);
        return null;
    }

    @Override
    public int getServerId() {
        return mGetServerId.getServerId();
    }

    @Override
    public void closeAddGroupView() {
        Dialog dialog = getDialog();

        if (dialog != null) {
            dialog.cancel();
        }
        else {
            NavUtils.navigateUpFromSameTask(getActivity());
        }
    }

    @Override
    public void onGroupSave() {
    }

    private String getStringFromAutoCompleteText(int viewId) {
        AutoCompleteTextView groupText;

        Dialog dialog = getDialog();

        if (dialog != null) {
            groupText = (AutoCompleteTextView) dialog.findViewById(viewId);
        }
        else {
            groupText = (AutoCompleteTextView) getActivity().findViewById(viewId);
        }

        return groupText.getText().toString();
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);

        try {
            mGetGroupId = (GetGroupId) context;
        }
        catch (ClassCastException e) {
            Log.e(LOG_TAG, "Context doesn't implement GetGroupId: " + e.toString());
        }

        try {
            mGetServerId = (GetServerId) context;
        }
        catch (ClassCastException e) {
            Log.e(LOG_TAG, "Context doesn't implement GetServerId: " + e.toString());
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
