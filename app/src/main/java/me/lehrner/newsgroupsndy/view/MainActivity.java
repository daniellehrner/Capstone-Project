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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;
import me.lehrner.newsgroupsndy.presenter.ServerPresenter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SERVER_LOADER = 0;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String ADD_SERVER_DIALOG_TAG = "ADD_SERVER_DIALOG_TAG";

    @BindView(R.id.server_list) RecyclerView mServerListView;
    @Inject ServerPresenter mServerPresenter;

    private AddServerClickHandler mServerClickHandler;
    private ServerAdapter mServerAdapter;

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((NDYApplication) getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportLoaderManager().initLoader(SERVER_LOADER, null, this);

        if (findViewById(R.id.groups_fragment_container) != null) {
            mTwoPane = true;
        }

        mServerListView.setHasFixedSize(true);

        mServerListView.setLayoutManager(new LinearLayoutManager(this));

        mServerAdapter = new ServerAdapter();
        mServerListView.setAdapter(mServerAdapter);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddServerDialogFragment addServerDialogFragment = AddServerDialogFragment.newInstance();

        if (mTwoPane) {
            addServerDialogFragment.show(fm, ADD_SERVER_DIALOG_TAG);
        }
        else {
//            FragmentTransaction transaction = fm.beginTransaction();
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            transaction.add(R.id.add_server_placeholder,
//                            addServerDialogFragment,
//                            PLAYER_FRAGMENT_TAG)
//                    .addToBackStack(null)
//                    .commit();
            startActivity(new Intent(this, AddServerActivity.class));
        }
    }

    @SuppressWarnings("unused")
    public void onAddServerSave(View view) {
        if (mServerClickHandler != null) {
            mServerClickHandler.onServerSave();
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
        Log.d(LOG_TAG, "Attach fragment");
        try {
            mServerClickHandler = (AddServerClickHandler) fragment;
        }
        catch (ClassCastException e) {
            Log.e(LOG_TAG, "Fragment doesn't implement AddServerClickHandler: " + e.toString());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case SERVER_LOADER:
                Uri serverUri = Uri.parse(mServerPresenter.getLoaderUriString());

                return new CursorLoader(
                        this,                                   // Parent activity context
                        serverUri,                     // Table to query
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mServerAdapter.swapCursor(null);
    }
}
