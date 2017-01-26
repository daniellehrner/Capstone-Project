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

package me.lehrner.newsgroupsndy.model;

import java.util.Date;
import java.util.GregorianCalendar;

public class ServerFactory {
    private int mId;
    private String mServerName;
    private String mServerUrl;
    private String mUserName;
    private String mUserMail;
    private Date mLastVisit = null;

    private ServerFactory() {}

    public static ServerFactory create() {
        return new ServerFactory();
    }

    public ServerFactory withId(final int id) {
        this.mId = id;
        return this;
    }

    public ServerFactory withServerName(final String serverName) {
        this.mServerName = serverName;
        return this;
    }

    public ServerFactory withServerUrl(final String serverUrl) {
        this.mServerUrl = serverUrl;
        return this;
    }

    public ServerFactory withUserName(final String userName) {
        this.mUserName = userName;
        return this;
    }

    public ServerFactory withUserMail(final String userMail) {
        this.mUserMail = userMail;
        return this;
    }

    public ServerFactory withLastVisit(Date lastVisit) {
        this.mLastVisit = lastVisit;
        return this;
    }

    public Server build() {
        GregorianCalendar lastVisit = null;

        if (mLastVisit != null) {
            lastVisit = new GregorianCalendar();
            lastVisit.setTime(mLastVisit);
        }

        return new Server(
                this.mId,
                this.mServerName,
                this.mServerUrl,
                new User(this.mUserName, this.mUserMail),
                lastVisit
        );
    }
}
