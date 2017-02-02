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

import android.content.Context;
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
import me.lehrner.newsgroupsndy.model.GroupContract.GroupEntry;
import me.lehrner.newsgroupsndy.presenter.GroupPresenter;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupAdapterViewHolder> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Cursor mCursor;
    private final GroupFragment mGroupFragment;

    public class GroupAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        final TextView mGroupName;
        final ImageButton mGroupMenu;
        private final ListViewClickListener mListViewClickListener;
        private final Context mContext;

        @Inject GroupPresenter mGroupPresenter;

        GroupAdapterViewHolder(View view, GroupFragment groupFragment) {
            super(view);

            mContext = view.getContext();
            ((NDYApplication) mContext.getApplicationContext()).getComponent().inject(this);

            mListViewClickListener = groupFragment;
            mGroupName = (TextView) view.findViewById(R.id.group_item_name);
            mGroupMenu = (ImageButton) view.findViewById(R.id.group_item_menu);

            view.setOnClickListener(this);
            mGroupMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            switch(v.getId()) {
                case R.id.group_item:
                    Log.d(LOG_TAG, "Text: " + mCursor.getString(
                            mCursor.getColumnIndex(GroupEntry.COLUMN_NAME_GROUP_NAME)));
                    mListViewClickListener.onListViewClick(
                            mCursor.getInt(
                                    mCursor.getColumnIndex(GroupEntry._ID)));
                    break;
                case R.id.group_item_menu:
                    Log.d(LOG_TAG, "Menu click");
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.inflate(R.menu.group_item_menu);
                    popup.setOnMenuItemClickListener(this);
                    popup.show();
                    break;
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = mCursor.getInt(mCursor.getColumnIndex(GroupEntry._ID));

            switch (item.getItemId()) {
                case R.id.group_menu_edit:
                    Log.d(LOG_TAG, "Edit: " + id);
                    mListViewClickListener.onListViewEditClick(id);
                    break;
                case R.id.group_menu_delete:
                    mGroupPresenter.unsubscribeFromGroup(id);
                    break;
                default:
                    throw new RuntimeException("Undefined group menu item: " + item.getTitle());
            }

            return true;
        }
    }

    public GroupAdapter(GroupFragment groupFragment) {
        mGroupFragment = groupFragment;
    }

    @Override
    public GroupAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.group_item, viewGroup, false);
            view.setFocusable(true);
            return new GroupAdapterViewHolder(view, mGroupFragment);
        }
        else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(GroupAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String groupName = mCursor.getString(
                mCursor.getColumnIndex(GroupEntry.COLUMN_NAME_GROUP_NAME));

        holder.mGroupName.setText(groupName);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
