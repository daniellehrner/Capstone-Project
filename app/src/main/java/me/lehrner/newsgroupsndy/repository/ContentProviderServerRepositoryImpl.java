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

import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;
import me.lehrner.newsgroupsndy.view.AddServerView;

public class ContentProviderServerRepositoryImpl implements ServerRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Context mContext;

    public ContentProviderServerRepositoryImpl(Context context) {
        mContext = context;
    }

    @Override
    public Server getServer(int id) {
        Server server;
        if (id == AddServerView.SERVER_ID_NOT_SET) {
            server = new Server();
            server.setId(AddServerView.SERVER_ID_NOT_SET);
            server.setServerName("");
            server.setServerUrl("");
            server.setUserName("");
            server.setUserMail("");
        }
        else {
            server = getServerFromContentProvider(id);
        }

        return server;
    }

    private Server getServerFromContentProvider(int id) {
        Server s = new Server();
        s.setId(id);

        String[] projection = {
                ServerEntry.COLUMN_NAME_SERVER_NAME,
                ServerEntry.COLUMN_NAME_SERVER_URL,
                ServerEntry.COLUMN_NAME_SERVER_USER,
                ServerEntry.COLUMN_NAME_SERVER_MAIL,
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

        if (serverCursor != null) {
            serverCursor.moveToFirst();
            s.setServerName(serverCursor.getString(
                    serverCursor.getColumnIndex(ServerEntry.COLUMN_NAME_SERVER_NAME)));
            s.setServerUrl(serverCursor.getString(
                    serverCursor.getColumnIndex(ServerEntry.COLUMN_NAME_SERVER_URL)));
            s.setUserName(serverCursor.getString(
                    serverCursor.getColumnIndex(ServerEntry.COLUMN_NAME_SERVER_USER)));
            s.setUserMail(serverCursor.getString(
                    serverCursor.getColumnIndex(ServerEntry.COLUMN_NAME_SERVER_MAIL)));

            serverCursor.close();
        }

        return s;
    }

    @Override
    public boolean saveServer(Server s) {
        boolean saveSuccess;

        ContentValues values = new ContentValues();

        values.put(ServerEntry.COLUMN_NAME_SERVER_NAME, s.getServerName());
        values.put(ServerEntry.COLUMN_NAME_SERVER_URL, s.getServerUrl());
        values.put(ServerEntry.COLUMN_NAME_SERVER_USER, s.getUserName());
        values.put(ServerEntry.COLUMN_NAME_SERVER_MAIL, s.getUserMail());

        // // create new server entry, _ID will be auto incremented by the database
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
}
