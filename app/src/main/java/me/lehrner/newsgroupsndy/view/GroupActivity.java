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

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.lehrner.newsgroupsndy.NDYApplication;
import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.presenter.GroupPresenter;
import me.lehrner.newsgroupsndy.repository.ServerRepository;

public class GroupActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, ListViewClickListener,
                   GetGroupId, GroupView {

    private static final int GROUP_LOADER = 1;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String ADD_GROUP_DIALOG_TAG = "ADD_GROUP_DIALOG_TAG";
    public static final String SERVER_ID_KEY = "serverIdKey";
    private Server mServer;

    @BindView(R.id.group_list) RecyclerView mGroupListView;
    @Inject GroupPresenter mGroupPresenter;
    @Inject ServerRepository mServerRepository;

    private AddGroupClickHandler mGroupClickHandler;
    private GroupAdapter mGroupAdapter;

    private boolean mTwoPane = false;
    private int mItemId = AddGroupView.GROUP_ID_NOT_SET;
    private int mServerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        ((NDYApplication) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Groups");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportLoaderManager().initLoader(GROUP_LOADER, null, this);

        if (savedInstanceState == null) {
            mServerId = getIntent().getIntExtra(SERVER_ID_KEY, AddServerView.SERVER_ID_NOT_SET);
        }
        else {
            mServerId = savedInstanceState.getInt(SERVER_ID_KEY);
        }

        mServer = mServerRepository.getServer(mServerId);

        mGroupListView.setHasFixedSize(true);

        mGroupAdapter = new GroupAdapter();
        mGroupListView.setAdapter(mGroupAdapter);

        mGroupPresenter.setGroupView(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        if (mTwoPane) {
            FragmentManager fm = getSupportFragmentManager();
            AddGroupDialogFragment.newInstance().show(fm, ADD_GROUP_DIALOG_TAG);
            mItemId = AddGroupView.GROUP_ID_NOT_SET;
        }
        else {
            startActivity(new Intent(this, AddGroupActivity.class));
        }
    }

    @SuppressWarnings("unused")
    public void onAddGroupSave(View view) {
        if (mGroupClickHandler != null) {
            mGroupClickHandler.onGroupSave();
        }
    }

    @SuppressWarnings("unused")
    public void onAddGroupCancel(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddGroupDialogFragment addGroupDialogFragment =
                (AddGroupDialogFragment) fm.findFragmentByTag(ADD_GROUP_DIALOG_TAG);
        addGroupDialogFragment.getDialog().cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGroupPresenter != null) {
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
    public void onAttachFragment(Fragment fragment) {
        try {
            mGroupClickHandler = (AddGroupClickHandler) fragment;
        }
        catch (ClassCastException e) {
            Log.e(LOG_TAG, "Fragment doesn't implement AddGroupClickHandler: " + e.toString());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case GROUP_LOADER:
                Uri groupUri = Uri.parse(mGroupPresenter.getLoaderUriString());

                return new CursorLoader(
                        this,                                   // Parent activity context
                        groupUri,                               // Table to query
                        mGroupPresenter.getLoaderProjection(),  // Projection to return
                        null,                                   // No selection clause
                        null,                                   // No selection arguments
                        mGroupPresenter.getLoaderOrder()        // Sort order
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
    public void onListViewClick(int itemId) {
        mItemId = itemId;

        if (mTwoPane) {

        }
        else {
            Intent editGroupIntent = new Intent(this, AddGroupActivity.class);
            editGroupIntent.putExtra(AddGroupActivity.GROUP_ID_KEY, mItemId);
            startActivity(editGroupIntent);
        }
    }

    @Override
    public void onListViewEditClick(int itemId) {
        mItemId = itemId;

        if (mTwoPane) {
            FragmentManager fm = getSupportFragmentManager();
            AddGroupDialogFragment.newInstance().show(fm, ADD_GROUP_DIALOG_TAG);
        }
        else {
            Intent editGroupIntent = new Intent(this, AddGroupActivity.class);
            editGroupIntent.putExtra(AddGroupActivity.GROUP_ID_KEY, mItemId);
            startActivity(editGroupIntent);
        }
    }

    @Override
    public int getGroupId() {
        return mItemId;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SERVER_ID_KEY, mServerId);
    }

    @Override
    public void reloadList() {
        this.onLoaderReset(new Loader<Cursor>(this));
    }
}
