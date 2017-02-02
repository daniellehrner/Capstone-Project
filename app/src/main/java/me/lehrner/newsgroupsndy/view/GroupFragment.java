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

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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

    View mView;
    private int mServerId;
    private GroupAdapter mGroupAdapter;
    private int mItemId = AddGroupView.GROUP_ID_NOT_SET;

    @BindView(R.id.search_group)
    SearchView mSearchView;

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

        mServerId = getArguments() != null ? getArguments().getInt(SERVER_ID) : NO_SERVER_ID;

        getActivity().getSupportLoaderManager().initLoader(GROUP_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_group, container, false);

        ButterKnife.bind(this, mView);

        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);

        mSearchView.setSearchableInfo(searchManager.
                getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setIconifiedByDefault(false);

        mGroupListView.setHasFixedSize(true);

        mGroupAdapter = new GroupAdapter(this);

        mGroupListView.setAdapter(mGroupAdapter);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

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
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case GROUP_LOADER:
                Uri groupUri = Uri.parse(mGroupPresenter.getLoaderUriString());

                return new CursorLoader(
                        getContext(),                                   // Parent activity context
                        groupUri,                                       // Table to query
                        mGroupPresenter.getLoaderProjection(),          // Projection to return
                        mGroupPresenter.getLoaderSelection(mServerId),  // No selection clause
                        null,                                           // No selection arguments
                        mGroupPresenter.getLoaderOrder()                // Sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mGroupAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGroupAdapter.swapCursor(null);
    }

    @Override
    public void onListViewClick(int itemId, String name) {
        mItemId = itemId;

//        if (mTwoPane) {
//
//        }
//        else {
//            Intent editGroupIntent = new Intent(this, AddGroupActivity.class);
//            editGroupIntent.putExtra(AddGroupActivity.GROUP_ID_KEY, mItemId);
//            startActivity(editGroupIntent);
//        }
    }

    @Override
    public void onListViewEditClick(int itemId) {
        mItemId = itemId;

//        if (mTwoPane) {
//            FragmentManager fm = getSupportFragmentManager();
//            AddGroupDialogFragment.newInstance().show(fm, ADD_GROUP_DIALOG_TAG);
//        }
//        else {
//            Intent editGroupIntent = new Intent(this, AddGroupActivity.class);
//            editGroupIntent.putExtra(AddGroupActivity.GROUP_ID_KEY, mItemId);
//            startActivity(editGroupIntent);
//        }
    }
}
