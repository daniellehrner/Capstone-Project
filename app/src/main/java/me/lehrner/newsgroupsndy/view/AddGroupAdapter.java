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
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import me.lehrner.newsgroupsndy.NDYApplication;
import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.model.DbHelper;
import me.lehrner.newsgroupsndy.model.GroupContract;
import me.lehrner.newsgroupsndy.presenter.GroupPresenter;

import static me.lehrner.newsgroupsndy.repository.GroupRepository.SUBSCRIBED;

public class AddGroupAdapter extends RecyclerView.Adapter<AddGroupAdapter.AddGroupAdapterViewHolder> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Cursor mCursor;
    private final AddGroupDialogFragment mAddGroupFragment;
    private final GroupPresenter mGroupPresenter;

    public class AddGroupAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView mGroupName;
        final ImageView mFavourite;
        private final Context mContext;

        @Inject
        GroupPresenter mGroupPresenter;

        AddGroupAdapterViewHolder(View view) {
            super(view);

            mContext = view.getContext();
            ((NDYApplication) mContext.getApplicationContext()).getComponent().inject(this);

            mGroupName = (TextView) view.findViewById(R.id.add_group_item_name);
            mFavourite = (ImageView) view.findViewById(R.id.add_group_item_image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());

            mGroupPresenter.toggleSubscribe(mCursor.getInt(
                    mCursor.getColumnIndex(GroupContract.GroupEntry._ID))
            );

            String description = (String) mFavourite.getContentDescription();

            if (description.equals(mContext.getString(R.string.subscribed))) {
                mFavourite.setImageResource(R.drawable.ic_favorite_border);
                mFavourite.setContentDescription(mContext.getString(R.string.not_subscribed));
            }
            else {
                mFavourite.setImageResource(R.drawable.ic_favorite);
                mFavourite.setContentDescription(mContext.getString(R.string.subscribed));
            }
        }
    }

    public AddGroupAdapter(AddGroupDialogFragment addGroupFragment, GroupPresenter groupPresenter) {
        mAddGroupFragment = addGroupFragment;
        mGroupPresenter = groupPresenter;
    }

    @Override
    public AddGroupAdapter.AddGroupAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.add_group_item, viewGroup, false);
            view.setFocusable(true);
            return new AddGroupAdapter.AddGroupAdapterViewHolder(view);
        }
        else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(AddGroupAdapter.AddGroupAdapterViewHolder holder, int position) {
        if (!mCursor.isClosed()) {
            mCursor.moveToPosition(position);
            String groupName = mCursor.getString(
                    mCursor.getColumnIndex(GroupContract.GroupEntry.COLUMN_NAME_GROUP_NAME));

            int subscribed = mCursor.getInt(
                    mCursor.getColumnIndex(GroupContract.GroupEntry.COLUMN_NAME_SUBSCRIBED));

            holder.mGroupName.setText(groupName);

            String description;

            if (subscribed == SUBSCRIBED) {
                holder.mFavourite.setImageResource(R.drawable.ic_favorite);
                description = holder.mContext.getString(R.string.subscribed);
            }
            else {
                holder.mFavourite.setImageResource(R.drawable.ic_favorite_border);
                description = holder.mContext.getString(R.string.not_subscribed);
            }

            holder.mGroupName.setContentDescription(groupName + " " + description);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public void filter(int serverId, String search) {
        DbHelper dbHelper = new DbHelper(mAddGroupFragment.getContext());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                GroupContract.GroupEntry.TABLE_NAME,
                mGroupPresenter.getLoaderProjection(),
                mGroupPresenter.getSearchSelection(),
                mGroupPresenter.getSearchSelectionArgs(serverId, search),
                null,
                null,
                mGroupPresenter.getLoaderOrder());

        this.swapCursor(cursor);
    }

    public void resetFilter(int serverId) {
        DbHelper dbHelper = new DbHelper(mAddGroupFragment.getContext());

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                GroupContract.GroupEntry.TABLE_NAME,
                mGroupPresenter.getLoaderProjection(),
                mGroupPresenter.getLoaderSelection(serverId),
                null,
                null,
                null,
                mGroupPresenter.getLoaderOrder());

        this.swapCursor(cursor);
    }
}

