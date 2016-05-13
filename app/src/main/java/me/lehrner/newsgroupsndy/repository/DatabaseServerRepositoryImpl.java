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
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;
import me.lehrner.newsgroupsndy.model.ServerDbHelper;
import me.lehrner.newsgroupsndy.view.AddServerView;

public class DatabaseServerRepositoryImpl implements ServerRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Context mContext;

    public DatabaseServerRepositoryImpl(Context context) {
        mContext = context;
    }

    @Override
    public Server getServer(int id) {
        Server server;
        id = 1;
        if (id == AddServerView.SERVER_ID_NOT_SET) {
            server = new Server();
            server.setId(0);
            server.setServerName("");
            server.setServerUrl("");
            server.setUserName("");
            server.setUserMail("");
        }
        else {
            server = getServerFromDb(id);
        }

        return server;
    }

    private Server getServerFromDb(int id) {
        Server s = new Server();
        s.setId(id);

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
    public boolean saveServer(Server s) {
        ServerDbHelper serverDbHelper = new ServerDbHelper(mContext);
        SQLiteDatabase db = serverDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ServerEntry._ID, s.getId());
        values.put(ServerEntry.COLUMN_NAME_SERVER_NAME, s.getServerName());
        values.put(ServerEntry.COLUMN_NAME_SERVER_URL, s.getServerUrl());
        values.put(ServerEntry.COLUMN_NAME_SERVER_USER, s.getUserName());
        values.put(ServerEntry.COLUMN_NAME_SERVER_MAIL, s.getUserMail());

        try {
            db.replaceOrThrow(ServerEntry.TABLE_NAME, null, values);
        }
        catch (SQLException e) {
            Log.e(LOG_TAG, "Inserting row not successful: " + e.toString());
            return false;
        }

        return true;
    }
}
