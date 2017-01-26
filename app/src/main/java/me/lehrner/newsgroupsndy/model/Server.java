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

import java.util.Calendar;
import java.util.Date;

public final class Server {
    private final int mId;
    private final String mServerName;
    private final String mServerUrl;
    private final User mUser;
    private final Calendar mLastVisit;

    public Server(int id, String serverName, String serverUrl,
                  User user, Calendar lastVisit) {
        mId = id;
        mServerName = serverName;
        mServerUrl = serverUrl;
        mUser = user;
        mLastVisit = lastVisit;
    }

    public int getId() {
        return mId;
    }

    public String getServerName() {
        return mServerName;
    }

    public String getServerUrl() {
        return mServerUrl;
    }

    public User getUser() {
        return mUser;
    }

    public String getUserName() {
        return mUser.getUserName();
    }

    public String getUserMail() {
        return mUser.getUserMail();
    }

    public Calendar getLastVisit() {
        return mLastVisit;
    }

}
