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

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.view.interfaces.AddServerView;
import me.lehrner.newsgroupsndy.view.interfaces.GetServerId;

public class GroupActivity extends AppCompatActivity implements GetServerId {

    private final String LOG_TAG = this.getClass().getSimpleName();
    public static final String SERVER_ID_KEY = "serverIdKey";
    public static final String SERVER_NAME = "serverNameKey";
    public static final String FRAGMENT_TAG = "groupFragmentTag";
    private int mServerId;
    private String mServerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        if (savedInstanceState == null) {
            mServerId = getIntent().getIntExtra(SERVER_ID_KEY, AddServerView.SERVER_ID_NOT_SET);
            mServerName = getIntent().getStringExtra(SERVER_NAME);
        }
        else {
            mServerId = savedInstanceState.getInt(SERVER_ID_KEY);
            mServerName = savedInstanceState.getString(SERVER_NAME);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mServerName);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException e) {
            Log.e(LOG_TAG, "getSupportActionBar() returned null");
        }

        GroupFragment groupFragment;

        if (savedInstanceState == null) {
            groupFragment = GroupFragment.newInstance(mServerId);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.group_fragment_container, groupFragment, FRAGMENT_TAG).
                    commit();
        }
        else {
            groupFragment = (GroupFragment) getSupportFragmentManager().
                    findFragmentByTag(FRAGMENT_TAG);
        }

        if (groupFragment != null) {
            FloatingActionButton fabAddGroup = (FloatingActionButton) findViewById(R.id.fab_group);

            if (fabAddGroup != null) {
                fabAddGroup.setOnClickListener(groupFragment);
            }
        }
    }

    @Override
    public int getServerId() {
        return mServerId;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SERVER_ID_KEY, mServerId);
        outState.putString(SERVER_NAME, mServerName);
    }
}
