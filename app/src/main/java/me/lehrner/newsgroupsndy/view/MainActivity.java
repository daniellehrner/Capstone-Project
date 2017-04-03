/*
 * Copyright (C) 2016 Daniel Lehrner
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
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.lehrner.newsgroupsndy.NDYApplication;
import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.presenter.ServerPresenter;
import me.lehrner.newsgroupsndy.view.interfaces.AddServerClickHandler;
import me.lehrner.newsgroupsndy.view.interfaces.AddServerView;
import me.lehrner.newsgroupsndy.view.interfaces.GetServerId;
import me.lehrner.newsgroupsndy.view.interfaces.ListViewClickListener;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, ListViewClickListener, GetServerId {

    private static final int SERVER_LOADER = 0;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String ADD_SERVER_DIALOG_TAG = "ADD_SERVER_DIALOG_TAG";
    private static final String KEY_ITEM_ID = "ndyKeyItemID";
    private static final String KEY_GROUP_NAME = "ndyKeyGroupName";

    @BindView(R.id.server_list) RecyclerView mServerListView;

    @Nullable @BindView(R.id.groups_fragment_container) FrameLayout mGroupContainer;
    FloatingActionButton mFabMain;
    @Inject ServerPresenter mServerPresenter;

    private WeakReference<AddServerClickHandler> mServerClickHandlerWeakReference;
    private ServerAdapter mServerAdapter;

    private boolean mTwoPane = false;
    private int mItemId = AddServerView.SERVER_ID_NOT_SET;
    private Toolbar mToolbar;
    private String mGroupName;
    private ActionBar mActionBar;
    private boolean mClickItemAfterLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.groups_fragment_container) != null) {
            mTwoPane = true;
        }

        if (mTwoPane) {
            if (savedInstanceState != null) {
                mItemId = savedInstanceState.getInt(KEY_ITEM_ID);
                mGroupName = savedInstanceState.getString(KEY_GROUP_NAME);

                if (mItemId != AddServerView.SERVER_ID_NOT_SET) {
                    mClickItemAfterLoad = true;
                }
            }
        }

        ((NDYApplication) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();

        getSupportLoaderManager().initLoader(SERVER_LOADER, null, this);

        mServerListView.setHasFixedSize(true);

        mServerAdapter = new ServerAdapter();
        mServerListView.setAdapter(mServerAdapter);

        if (mTwoPane) {
            mFabMain = (FloatingActionButton) findViewById(R.id.fab_main);
            mFabMain.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab_main)
    public void onFabClick(View view) {
        if (mTwoPane) {
            FragmentManager fm = getSupportFragmentManager();
            AddGroupFragment addGroupFragment = AddGroupFragment.newInstance(mItemId);

            if (mGroupContainer != null) {
                mGroupContainer.removeAllViews();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.groups_fragment_container, addGroupFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();

            mToolbar.setTitle(
                    getString(R.string.subscribe_to, mGroupName)
            );

            if (mActionBar != null) {
                mActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        else {
            startActivity(new Intent(this, AddServerActivity.class));
        }
    }

    @SuppressWarnings("unused")
    public void onAddServerClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddServerDialogFragment.newInstance().show(fm, ADD_SERVER_DIALOG_TAG);
        mItemId = AddServerView.SERVER_ID_NOT_SET;
    }

    @SuppressWarnings("unused")
    public void onAddServerSave(View view) {
        AddServerClickHandler addServerClickHandler = mServerClickHandlerWeakReference.get();

        if (addServerClickHandler != null) {
            addServerClickHandler.onServerSave();
        }
    }

    @SuppressWarnings("unused")
    public void onAddServerCancel(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddServerDialogFragment addServerDialogFragment =
                (AddServerDialogFragment) fm.findFragmentByTag(ADD_SERVER_DIALOG_TAG);
        addServerDialogFragment.getDialog().cancel();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment.getClass().getSimpleName().equals("AddServerDialogFragment")) {
            try {
                mServerClickHandlerWeakReference = new WeakReference<>((AddServerClickHandler) fragment);
            } catch (ClassCastException e) {
                Log.e(LOG_TAG, "Fragment doesn't implement AddServerClickHandler: " + e.toString());
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case SERVER_LOADER:
                Uri serverUri = Uri.parse(mServerPresenter.getLoaderUriString());

                return new CursorLoader(
                        this,                                   // Parent activity context
                        serverUri,                              // Table to query
                        mServerPresenter.getLoaderProjection(), // Projection to return
                        null,                                   // No selection clause
                        null,                                   // No selection arguments
                        mServerPresenter.getLoaderOrder()       // Sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mServerAdapter.swapCursor(data);

        if (mClickItemAfterLoad && (mItemId != AddServerView.SERVER_ID_NOT_SET)) {
            onListViewClick(mItemId, mGroupName);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mServerAdapter.swapCursor(null);
    }

    @Override
    public void onListViewClick(int itemId, String name) {
        mGroupName = name;

        if (mTwoPane) {

            GroupFragment groupFragment = GroupFragment.newInstance(itemId);

            if (mGroupContainer != null) {
                mGroupContainer.removeAllViews();
            }

            if (!mClickItemAfterLoad) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.groups_fragment_container, groupFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
            }
            else {
                mClickItemAfterLoad = false;
            }

            mFabMain.setVisibility(View.VISIBLE);

            mToolbar.setTitle(name);
        }
        else {
            Intent groupIntent = new Intent(this, GroupActivity.class);
            groupIntent.putExtra(GroupActivity.SERVER_ID_KEY, itemId);
            groupIntent.putExtra(GroupActivity.SERVER_NAME, name);
            startActivity(groupIntent);
        }

        mItemId = itemId;
    }

    @Override
    public void onListViewEditClick(int itemId) {
        mItemId = itemId;

        if (mTwoPane) {
            FragmentManager fm = getSupportFragmentManager();
            AddServerDialogFragment.newInstance().show(fm, ADD_SERVER_DIALOG_TAG);
        }
        else {
            Intent editServerIntent = new Intent(this, AddServerActivity.class);
            editServerIntent.putExtra(AddServerActivity.SERVER_ID_KEY, mItemId);
            startActivity(editServerIntent);
        }
    }

    @Override
    public int getServerId() {
        return mItemId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStackImmediate();

                mToolbar.setTitle(mGroupName);

                if (mActionBar != null) {
                    mActionBar.setDisplayHomeAsUpEnabled(false);
                }
                return true;
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_ITEM_ID, mItemId);
        outState.putString(KEY_GROUP_NAME, mGroupName);
    }
}
