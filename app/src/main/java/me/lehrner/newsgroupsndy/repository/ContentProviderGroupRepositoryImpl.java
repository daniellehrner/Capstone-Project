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

package me.lehrner.newsgroupsndy.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import me.lehrner.newsgroupsndy.model.Group;
import me.lehrner.newsgroupsndy.model.GroupContract.GroupEntry;
import me.lehrner.newsgroupsndy.model.GroupFactory;
import me.lehrner.newsgroupsndy.view.interfaces.AddGroupView;

public class ContentProviderGroupRepositoryImpl implements GroupRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private final Context mContext;
    private final ServerRepository mServerRepository;

    public ContentProviderGroupRepositoryImpl(Context context, ServerRepository serverRepository) {
        mContext = context;
        mServerRepository = serverRepository;
    }

    @Override
    public Group getGroup(int id) {
        Group group;
        if (id == AddGroupView.GROUP_ID_NOT_SET) {
            group = null;
        }
        else {
            group = getGroupFromContentProvider(id);
        }

        return group;
    }

    @Override
    public boolean saveGroups(int serverId, ArrayList<String> groupNames) {
        boolean saveSuccess = true;

        for (String groupName : groupNames) {
            if (!saveGroup(groupName, serverId)) {
                saveSuccess = false;
                break;
            }
        }

        return saveSuccess;
    }

    private Group getGroupFromContentProvider(int id) {
        Group group = null;

        String[] projection = {
                GroupEntry.COLUMN_NAME_GROUP_NAME,
                GroupEntry.COLUMN_NAME_SERVER_ID,
        };
        String selection = GroupEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor serverCursor = mContext.getContentResolver().query(
                Uri.parse(GroupEntry.GROUP_URI_STRING),
                projection,
                selection,
                selectionArgs,
                null
        );

        if (serverCursor != null && serverCursor.getCount() > 0) {
            serverCursor.moveToFirst();
            String groupName = serverCursor.getString(
                    serverCursor.getColumnIndex(GroupEntry.COLUMN_NAME_GROUP_NAME));
            int serverId = serverCursor.getInt(
                    serverCursor.getColumnIndex(GroupEntry.COLUMN_NAME_SERVER_ID));

            serverCursor.close();

            group = GroupFactory.create().
                    withId(id).
                    withGroupName(groupName).
                    withServerId(serverId).
                    build();
        }

        return group;
    }

    private boolean saveGroup(String groupName, int serverId) {
        ContentValues values = new ContentValues();

        values.put(GroupEntry.COLUMN_NAME_GROUP_NAME, groupName);
        values.put(GroupEntry.COLUMN_NAME_SERVER_ID, serverId);

        Uri newRowUri = mContext.getContentResolver().insert(
                Uri.parse(GroupEntry.GROUP_URI_STRING),
                values);

        return newRowUri != null;
    }

    @Override
    public boolean deleteGroups(int serverId) {
        int numberOfDeletedRows = mContext.getContentResolver().delete(
                Uri.parse(GroupEntry.GROUP_URI_STRING),
                GroupEntry.COLUMN_NAME_SERVER_ID + " = ?",
                new String[]{String.valueOf(serverId)}
        );

        return numberOfDeletedRows != 0;
    }
}
