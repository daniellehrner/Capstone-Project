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

import android.provider.BaseColumns;

public final class ServerContract {
    public ServerContract() {
    }

    public static abstract class ServerEntry implements BaseColumns {
        public static final String TABLE_NAME = "server";
        public static final String COLUMN_NAME_SERVER_NAME = "server_name";
        public static final String COLUMN_NAME_SERVER_URL = "server_url";
        public static final String COLUMN_NAME_SERVER_USER = "user_name";
        public static final String COLUMN_NAME_SERVER_MAIL = "user_mail";
        public static final String COLUMN_NAME_LAST_VISIT = "last_visit";

        public static final String SERVER_URI_STRING = "content://"
                + "me.lehrner.newsgroupsndy.provider/" + TABLE_NAME;
    }
}
