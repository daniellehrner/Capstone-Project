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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import butterknife.OnClick;
import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.view.interfaces.AddServerView;
import me.lehrner.newsgroupsndy.view.interfaces.GetServerId;

public class GroupActivity extends AppCompatActivity implements GetServerId {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String ADD_GROUP_DIALOG_TAG = "ADD_GROUP_DIALOG_TAG";
    public static final String SERVER_ID_KEY = "serverIdKey";
    public static final String SERVER_NAME = "serverNameKey";
    private int mServerId;
    private String mServerName;
    private GroupFragment mGroupFragment;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            mGroupFragment = GroupFragment.newInstance(mServerId);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.group_fragment_container, mGroupFragment).
                    commit();
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        startActivity(new Intent(this, AddGroupActivity.class));
    }

//    @SuppressWarnings("unused")
//    public void onAddGroupSave(View view) {
//        if (mGroupClickHandler != null) {
//            mGroupClickHandler.onGroupSave();
//        }
//    }
//
//    @SuppressWarnings("unused")
//    public void onAddGroupCancel(View view) {
//        FragmentManager fm = getSupportFragmentManager();
//        AddGroupDialogFragment addGroupDialogFragment =
//                (AddGroupDialogFragment) fm.findFragmentByTag(ADD_GROUP_DIALOG_TAG);
//        addGroupDialogFragment.getDialog().cancel();
//    }

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
