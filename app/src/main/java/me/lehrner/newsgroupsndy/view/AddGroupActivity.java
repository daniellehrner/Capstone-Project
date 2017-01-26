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
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import butterknife.ButterKnife;
import me.lehrner.newsgroupsndy.R;

public class AddGroupActivity extends AppCompatActivity implements GetGroupId {
    private final String LOG_TAG = this.getClass().getSimpleName();
    public static final String GROUP_ID_KEY = "groupIdKey";

    private AddGroupClickHandler mGroupClickHandler;
    private int mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setContentView(R.layout.activity_save_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }

        if (savedInstanceState == null) {
            mGroupId = getIntent().getIntExtra(GROUP_ID_KEY, AddGroupView.GROUP_ID_NOT_SET);
        }
        else {
            mGroupId = savedInstanceState.getInt(GROUP_ID_KEY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_group:
                if (mGroupClickHandler != null) {
                    mGroupClickHandler.onGroupSave();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GROUP_ID_KEY, mGroupId);
    }

    @Override
    public int getGroupId() {
        return mGroupId;
    }
}
