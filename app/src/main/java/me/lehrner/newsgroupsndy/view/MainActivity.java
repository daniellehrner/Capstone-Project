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
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.lehrner.newsgroupsndy.R;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String ADD_SERVER_DIALOG_TAG = "ADD_SERVER_DIALOG_TAG";

    @BindView(R.id.server_list) RecyclerView mServerListView;

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.groups_fragment_container) != null) {
            mTwoPane = true;
        }
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
        Log.d(LOG_TAG, "save");
    }

    @SuppressWarnings("unused")
    public void onAddServerCancel(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddServerDialogFragment addServerDialogFragment =
                (AddServerDialogFragment) fm.findFragmentByTag(ADD_SERVER_DIALOG_TAG);
        addServerDialogFragment.getDialog().cancel();
    }
}
