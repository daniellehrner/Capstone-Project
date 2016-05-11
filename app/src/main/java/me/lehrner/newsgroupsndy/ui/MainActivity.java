package me.lehrner.newsgroupsndy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.lehrner.newsgroupsndy.R;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String PLAYER_FRAGMENT_TAG = "PLAYER_FRAGMENT_TAG";

    @BindView(R.id.server_list) RecyclerView mServerListView;
//    @BindView(R.id.groupsFragmentContainer) FrameLayout mGroupFragmentLayout;

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.groups_fragment_container) != null) {
            Log.d(LOG_TAG, "Tablet mode");
            mTwoPane = true;
        }
        else {
            Log.d(LOG_TAG, "Phone mode");
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddServerDialogFragment addServerDialogFragment = AddServerDialogFragment.newInstance();

        if (mTwoPane) {
            addServerDialogFragment.show(fm, "addServerDialog");
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
}
