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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import javax.inject.Inject;

import me.lehrner.newsgroupsndy.NDYApplication;
import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;
import me.lehrner.newsgroupsndy.presenter.ServerPresenter;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ServerAdapterViewHolder> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Cursor mCursor;

    public class ServerAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        public final TextView mServerName;
        public final ImageButton mServerMenu;
        private final Activity mActivity;
        @Inject ServerPresenter mServerPresenter;

        public ServerAdapterViewHolder(View view) {
            super(view);
            MainActivity mainActivity = (MainActivity) view.getContext();
            ((NDYApplication) mainActivity.getApplication()).getComponent().inject(this);

            mActivity = mainActivity;
            mServerName = (TextView) view.findViewById(R.id.server_item_name);
            mServerMenu  = (ImageButton) view.findViewById(R.id.server_item_menu);

            view.setOnClickListener(this);
            mServerMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            switch(v.getId()) {
                case R.id.server_item:
                    Log.d(LOG_TAG, "Text: " + mCursor.getString(
                            mCursor.getColumnIndex(ServerEntry.COLUMN_NAME_SERVER_NAME)));
                    break;
                case R.id.server_item_menu:
                    Log.d(LOG_TAG, "Menu click");
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.inflate(R.menu.server_item_menu);
                    popup.setOnMenuItemClickListener(this);
                    popup.show();
                    break;
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = mCursor.getInt(mCursor.getColumnIndex(ServerEntry._ID));

            switch (item.getItemId()) {
                case R.id.server_menu_edit:
                    Log.d(LOG_TAG, "Edit: " + id);

                    Intent editServerIntent = new Intent(mActivity, AddServerActivity.class);
                    editServerIntent.putExtra(AddServerActivity.SERVER_ID_KEY, id);
                    mActivity.startActivity(editServerIntent);

                    break;
                case R.id.server_menu_delete:
                    mServerPresenter.deleteServer(id);
                    break;
                default:
                    throw new RuntimeException("Undefined server menu item: " + item.getTitle());
            }

            return true;
        }
    }

    @Override
    public ServerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.server_item, viewGroup, false);
            view.setFocusable(true);
            return new ServerAdapterViewHolder(view);
        }
        else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(ServerAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String serverName = mCursor.getString(
                mCursor.getColumnIndex(ServerEntry.COLUMN_NAME_SERVER_NAME));

        holder.mServerName.setText(serverName);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
