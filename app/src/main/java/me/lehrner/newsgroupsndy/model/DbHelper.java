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

package me.lehrner.newsgroupsndy.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;
import me.lehrner.newsgroupsndy.model.GroupContract.GroupEntry;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "Server.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_SERVERS =
            "CREATE TABLE " + ServerEntry.TABLE_NAME + " (" +
                    ServerEntry._ID + " INTEGER PRIMARY KEY," +
                    ServerEntry.COLUMN_NAME_SERVER_NAME + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.COLUMN_NAME_SERVER_URL + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.COLUMN_NAME_SERVER_USER + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.COLUMN_NAME_SERVER_MAIL + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.COLUMN_NAME_LAST_VISIT + INT_TYPE +
            " )";

    private static final String SQL_CREATE_GROUPS =
            "CREATE TABLE " + GroupEntry.TABLE_NAME + " (" +
                    GroupEntry._ID + " INTEGER PRIMARY KEY," +
                    GroupEntry.COLUMN_NAME_GROUP_NAME + TEXT_TYPE + COMMA_SEP +
                    GroupEntry.COLUMN_NAME_SERVER_ID + INT_TYPE +
            " )";

    private static final String SQL_DELETE_SERVER =
            "DROP TABLE IF EXISTS " + ServerEntry.TABLE_NAME;

    private static final String SQL_DELETE_GROUP =
            "DROP TABLE IF EXISTS " + GroupEntry.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SERVERS);
        db.execSQL(SQL_CREATE_GROUPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_SERVER);
        db.execSQL(SQL_DELETE_GROUP);
        onCreate(db);
    }
}
