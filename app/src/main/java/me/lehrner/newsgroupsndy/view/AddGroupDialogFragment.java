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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.lehrner.newsgroupsndy.NDYApplication;
import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.presenter.GroupPresenter;
import me.lehrner.newsgroupsndy.view.interfaces.AddGroupView;

public class AddGroupDialogFragment extends AppCompatDialogFragment
        implements AddGroupView, android.support.v7.widget.SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final int ADD_GROUP_LOADER = 2;
    private static final int NO_SERVER_ID = -1;
    public static final String SERVER_ID_KEY = "serverIdKey";
    public static final String GROUP_FILTER_KEY = "groupFilterKey";

    @Inject
    GroupPresenter mGroupPresenter;

    private String mFilter = "";
    private AddGroupAdapter mAddGroupAdapter;
    private int mServerId;

    @BindView(R.id.add_group_subscribed)
    RecyclerView mGroupListView;

    public AddGroupDialogFragment() {
        // Required empty public constructor
    }

    public static AddGroupDialogFragment newInstance(int serverId) {
        AddGroupDialogFragment fragment = new AddGroupDialogFragment();

        Bundle args = new Bundle();
        args.putInt(SERVER_ID_KEY, serverId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((NDYApplication) getActivity().getApplication()).getComponent().inject(this);

        if (savedInstanceState == null) {
            mServerId = getArguments() != null ? getArguments().getInt(SERVER_ID_KEY) : NO_SERVER_ID;
        }
        else {
            mServerId = savedInstanceState.getInt(SERVER_ID_KEY);
            mFilter = savedInstanceState.getString(GROUP_FILTER_KEY);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((NDYApplication) getActivity().getApplication()).getComponent().inject(this);

        View view = inflater.inflate(R.layout.fragment_add_group_dialog, container, false);

        ButterKnife.bind(this, view);

        mGroupListView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mAddGroupAdapter = new AddGroupAdapter(this, mGroupPresenter);
        mGroupListView.setAdapter(mAddGroupAdapter);

        if (mFilter.isEmpty()) {
            getActivity().getSupportLoaderManager().initLoader(ADD_GROUP_LOADER, null, this);
        }
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
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() > 1) {
            mFilter = s;
            mAddGroupAdapter.filter(mServerId, s);
            mGroupListView.scrollToPosition(0);
        } else {
            if (!mFilter.isEmpty()) {
                mFilter = "";
                mAddGroupAdapter.resetFilter(mServerId);
                mGroupListView.scrollToPosition(0);
            }
        }

        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        switch (loaderID) {
            case ADD_GROUP_LOADER:
                Uri groupUri = Uri.parse(mGroupPresenter.getLoaderUriString());

                return new CursorLoader(
                        getContext(),
                        groupUri,
                        mGroupPresenter.getLoaderProjection(),
                        mGroupPresenter.getLoaderSelection(mServerId),
                        null,
                        mGroupPresenter.getLoaderOrder()
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAddGroupAdapter != null) {
            mAddGroupAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAddGroupAdapter != null) {
            mAddGroupAdapter.swapCursor(null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SERVER_ID_KEY, mServerId);
        outState.putString(GROUP_FILTER_KEY, mFilter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.group_search_menu, menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.search_group);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        if (searchView != null) {
            if (!mFilter.isEmpty()) {
                MenuItemCompat.expandActionView(searchMenuItem);
            }

            searchView.clearFocus();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.search_group);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setOnQueryTextListener(this);

        if (!mFilter.isEmpty()) {
            searchView.setQuery(mFilter, true);
        }
    }
}
