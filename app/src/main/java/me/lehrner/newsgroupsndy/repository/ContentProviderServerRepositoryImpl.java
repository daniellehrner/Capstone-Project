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

package me.lehrner.newsgroupsndy.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;

import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;
import me.lehrner.newsgroupsndy.model.ServerFactory;
import me.lehrner.newsgroupsndy.view.interfaces.AddServerView;

public class ContentProviderServerRepositoryImpl implements ServerRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private final Context mContext;

    public ContentProviderServerRepositoryImpl(Context context) {
        mContext = context;
    }

    @Override
    public Server getServer(int id) {
        Server server;
        if (id == AddServerView.SERVER_ID_NOT_SET) {
            server = null;
        }
        else {
            server = getServerFromContentProvider(id);
        }

        return server;
    }

    private Server getServerFromContentProvider(int id) {
        Server server = null;

        String[] projection = {
                ServerEntry.COLUMN_NAME_SERVER_NAME,
                ServerEntry.COLUMN_NAME_SERVER_URL,
                ServerEntry.COLUMN_NAME_SERVER_USER,
                ServerEntry.COLUMN_NAME_SERVER_MAIL,
                ServerEntry.COLUMN_NAME_LAST_VISIT
        };
        String selection = ServerEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor serverCursor = mContext.getContentResolver().query(
                Uri.parse(ServerEntry.SERVER_URI_STRING),
                projection,
                selection,
                selectionArgs,
                null
        );

        if (serverCursor != null && serverCursor.getCount() > 0) {
            serverCursor.moveToFirst();

            String serverName = serverCursor.getString(
                    serverCursor.getColumnIndex(ServerEntry.COLUMN_NAME_SERVER_NAME));
            String url = serverCursor.getString(
                    serverCursor.getColumnIndex(ServerEntry.COLUMN_NAME_SERVER_URL));
            String userName = serverCursor.getString(
                    serverCursor.getColumnIndex(ServerEntry.COLUMN_NAME_SERVER_USER));
            String userMail = serverCursor.getString(
                    serverCursor.getColumnIndex(ServerEntry.COLUMN_NAME_SERVER_MAIL));
            int lastVisitInt = serverCursor.getInt(
                    serverCursor.getColumnIndex(ServerEntry.COLUMN_NAME_LAST_VISIT));
            
            serverCursor.close();

            server = ServerFactory.create().
                withId(id).
                withServerName(serverName).
                withServerUrl(url).
                withUserName(userName).
                withUserMail(userMail).
                withLastVisit(lastVisitInt).
                build();
        }

        return server;
    }

    @Override
    public boolean saveServer(Server s) {
        boolean saveSuccess;

        ContentValues values = new ContentValues();

        values.put(ServerEntry.COLUMN_NAME_SERVER_NAME, s.getServerName());
        values.put(ServerEntry.COLUMN_NAME_SERVER_URL, s.getServerUrl());
        values.put(ServerEntry.COLUMN_NAME_SERVER_USER, s.getUser().getUserName());
        values.put(ServerEntry.COLUMN_NAME_SERVER_MAIL, s.getUser().getUserMail());

        // create new server entry, _ID will be auto incremented by the database
        if (s.getId() == AddServerView.SERVER_ID_NOT_SET) {
            Uri newRowUri = mContext.getContentResolver().insert(
                    Uri.parse(ServerEntry.SERVER_URI_STRING),
                    values);

            saveSuccess = newRowUri != null;

        }
        // update existing server entry
        else {
            int numberOfUpdatedRows = mContext.getContentResolver().update(
                    Uri.parse(ServerEntry.SERVER_URI_STRING),
                    values,
                    ServerEntry._ID + " = ?",
                    new String[]{String.valueOf(s.getId())}
            );

            saveSuccess = numberOfUpdatedRows != 0;
        }

        return saveSuccess;
    }

    @Override
    public boolean deleteServer(int id) {
        int numberOfDeletedRows = mContext.getContentResolver().delete(
                Uri.parse(ServerEntry.SERVER_URI_STRING),
                ServerEntry._ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return numberOfDeletedRows != 0;
    }

    @Override
    public boolean setLastVisitedNow(int id) {
        ContentValues values = new ContentValues();
        Date date = new Date();
        int nowUnixTime = (int) (date.getTime() / 1000);

        values.put(ServerEntry.COLUMN_NAME_LAST_VISIT, nowUnixTime);

        int numberOfUpdatedRows = mContext.getContentResolver().update(
                Uri.parse(ServerEntry.SERVER_URI_STRING),
                values,
                ServerEntry._ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return numberOfUpdatedRows != 0;
    }
}
