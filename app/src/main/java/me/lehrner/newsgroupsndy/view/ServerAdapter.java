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

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ServerAdapterViewHolder> {
    private Cursor mCursor;

    public class ServerAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final TextView mServerName;

        public ServerAdapterViewHolder(View view) {
            super(view);
            mServerName = (TextView) view.findViewById(R.id.server_item_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

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
        if ( null == mCursor ) {
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
