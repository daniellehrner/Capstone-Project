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

import android.content.ContentResolver;
import android.provider.BaseColumns;

public class GroupContract {
    public GroupContract() {}

    public static abstract class GroupEntry implements BaseColumns {
        static final String CONTENT_AUTHORITY = "me.lehrner.newsgroupsndy";
        static final String TABLE_NAME = "newsgroup";
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String COLUMN_NAME_GROUP_NAME = "name";
        static final String COLUMN_NAME_SERVER_ID = "serverId";

        public static final String SERVER_URI_STRING = "content://"
                + CONTENT_AUTHORITY +
                "/" + TABLE_NAME;
    }
}
