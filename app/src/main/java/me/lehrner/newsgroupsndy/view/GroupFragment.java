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

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
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
import me.lehrner.newsgroupsndy.view.interfaces.ListViewClickListener;

public class GroupFragment extends Fragment
        implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor>,
        ListViewClickListener {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final int GROUP_LOADER = 1;
    private static final int NO_SERVER_ID = -1;
    private static final String SERVER_ID = "groupFragmentServerId";
    public static final String SERVER_ID_KEY = "serverIdKey";
    public static final String GROUP_FILTER_KEY = "groupFilterKey";

    private int mServerId;
    private GroupAdapter mGroupAdapter;
    private int mItemId = AddGroupView.GROUP_ID_NOT_SET;
    private String mFilter = "";
    private SearchView mSearchView;
    private boolean mDontUpdateFilter = false;

    @BindView(R.id.group_list)
    RecyclerView mGroupListView;

    @Inject GroupPresenter mGroupPresenter;

    public GroupFragment() {
        // Required empty public constructor
    }

    public static GroupFragment newInstance(int serverId) {
        GroupFragment groupFragment = new GroupFragment();

        Bundle args = new Bundle();
        args.putInt(SERVER_ID, serverId);
        groupFragment.setArguments(args);

        return groupFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((NDYApplication) getActivity().getApplication()).getComponent().inject(this);

        if (savedInstanceState == null) {
            mServerId = getArguments() != null ? getArguments().getInt(SERVER_ID) : NO_SERVER_ID;
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
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        ButterKnife.bind(this, view);

        mGroupListView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mGroupAdapter = new GroupAdapter(this, mGroupPresenter);
        mGroupListView.setAdapter(mGroupAdapter);

        if (mFilter.isEmpty()) {
            getActivity().getSupportLoaderManager().initLoader(GROUP_LOADER, null, this);
        }

        if (mGroupPresenter != null && mServerId != NO_SERVER_ID) {
            mGroupPresenter.updateGroupList(mServerId);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGroupPresenter != null) {
            mGroupPresenter.onPause();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (!mDontUpdateFilter) {
            if (s.length() > 1) {
                mFilter = s;
                mGroupAdapter.filter(mServerId, s);
            } else {
                if (!mFilter.isEmpty()) {
                    mFilter = "";
                    mGroupAdapter.resetFilter(mServerId);
                }
            }
        }

        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case GROUP_LOADER:
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
        if (mGroupAdapter != null) {
            mGroupAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mGroupAdapter != null) {
            mGroupAdapter.swapCursor(null);
        }
    }

    @Override
    public void onListViewClick(int itemId, String name) {
        mItemId = itemId;
    }

    @Override
    public void onListViewEditClick(int itemId) {
        mItemId = itemId;
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

        inflater.inflate(R.menu.group_menu, menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.search_group);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(this);

            if (!mFilter.isEmpty()) {
                mDontUpdateFilter = true;
                MenuItemCompat.expandActionView(searchMenuItem);
            }

            mSearchView.clearFocus();
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        final MenuItem searchMenuItem = menu.findItem(R.id.search_group);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        if (!mFilter.isEmpty()) {
            mDontUpdateFilter = false;
            mSearchView.setQuery(mFilter, true);
        }

    }
}
