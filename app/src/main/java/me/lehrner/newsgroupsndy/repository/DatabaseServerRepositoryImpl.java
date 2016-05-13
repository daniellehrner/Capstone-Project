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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;
import me.lehrner.newsgroupsndy.model.ServerDbHelper;
import me.lehrner.newsgroupsndy.view.AddServerView;

public class DatabaseServerRepositoryImpl implements ServerRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Server mServer;
    private Context mContext;

    public DatabaseServerRepositoryImpl(Context context) {
        mContext = context;
    }

    @Override
    public Server getServer(int id) {
        id = 1;

        if (id == AddServerView.SERVER_ID_NOT_SET) {
            mServer = new Server();
            mServer.setId(0);
            mServer.setServerName("");
            mServer.setServerUrl("");
            mServer.setUserName("");
            mServer.setUserMail("");
        }
        else {
            mServer = getServerFromDb(id);
        }

        return mServer;
    }

    private Server getServerFromDb(int id) {
        Log.d(LOG_TAG, "getServerFromDb");

        Server s = new Server();

        ServerDbHelper serverDbHelper = new ServerDbHelper(mContext);
        SQLiteDatabase db = serverDbHelper.getReadableDatabase();

        String[] projection = {
                ServerEntry.COLUMN_NAME_SERVER_NAME,
                ServerEntry.COLUMN_NAME_SERVER_URL,
                ServerEntry.COLUMN_NAME_SERVER_USER,
                ServerEntry.COLUMN_NAME_SERVER_MAIL,
        };
        String selection = ServerEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor getServerCursor = db.query(
                ServerEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        getServerCursor.moveToFirst();
        s.setServerName(getServerCursor.getString(0));
        s.setServerUrl(getServerCursor.getString(1));
        s.setUserName(getServerCursor.getString(2));
        s.setUserMail(getServerCursor.getString(3));

        getServerCursor.close();

        return s;
    }

    @Override
    public void saveServer(Server s) {
        Log.d(LOG_TAG, "Saving server with name " + s.getServerName());
        Log.d(LOG_TAG, "Saving server with url " + s.getServerUrl());
        Log.d(LOG_TAG, "Saving server with user name " + s.getUserName());
        Log.d(LOG_TAG, "Saving server with user mail " + s.getUserMail());
    }
}
