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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.view.interfaces.AddServerView;

public class AddGroupActivity extends AppCompatActivity {
    public static final String FRAGMENT_TAG = "addGroupFragmentTag";
    public static final String SERVER_ID_KEY = "serverIdKey";

    private int mServerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_save_group);

        if (savedInstanceState == null) {
            mServerId = getIntent().getIntExtra(SERVER_ID_KEY, AddServerView.SERVER_ID_NOT_SET);
        }
        else {
            mServerId = savedInstanceState.getInt(SERVER_ID_KEY);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        AddGroupFragment addGroupFragment;

        if (savedInstanceState == null) {
            addGroupFragment = AddGroupFragment.newInstance(mServerId);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.add_group_fragment_container, addGroupFragment, FRAGMENT_TAG).
                    commit();
        }
        else {
            addGroupFragment = (AddGroupFragment) getSupportFragmentManager().
                    findFragmentByTag(FRAGMENT_TAG);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SERVER_ID_KEY, mServerId);
    }
}
