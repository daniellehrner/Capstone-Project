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

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.view.interfaces.AddServerClickHandler;
import me.lehrner.newsgroupsndy.view.interfaces.AddServerView;
import me.lehrner.newsgroupsndy.view.interfaces.GetServerId;

public class AddServerActivity extends AppCompatActivity implements GetServerId {
    private final String LOG_TAG = this.getClass().getSimpleName();
    public static final String SERVER_ID_KEY = "serverIdKey";

    private WeakReference<AddServerClickHandler> mServerClickHandlerWeakReference;
    private int mServerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_server);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }

        if (savedInstanceState == null) {
            mServerId = getIntent().getIntExtra(SERVER_ID_KEY, AddServerView.SERVER_ID_NOT_SET);
        }
        else {
            mServerId = savedInstanceState.getInt(SERVER_ID_KEY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_server_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_server:
                AddServerClickHandler addServerClickHandler = mServerClickHandlerWeakReference.get();
                if (addServerClickHandler != null) {
                    addServerClickHandler.onServerSave();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        try {
            mServerClickHandlerWeakReference = new WeakReference<>((AddServerClickHandler) fragment);
        }
        catch (ClassCastException e) {
            Log.e(LOG_TAG, "Fragment doesn't implement AddServerClickHandler: " + e.toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SERVER_ID_KEY, mServerId);
    }

    @Override
    public int getServerId() {
        return mServerId;
    }
}
