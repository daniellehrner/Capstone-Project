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

public class Server {
    private int mId;
    private String mServerName;
    private String mServerUrl;
    private String mUserName;
    private String mUserMail;

    public Server() {
        // empty constructor
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getServerName() {
        return mServerName;
    }

    public void setServerName(String mServerName) {
        this.mServerName = mServerName;
    }

    public String getServerUrl() {
        return mServerUrl;
    }

    public void setServerUrl(String mServerUrl) {
        this.mServerUrl = mServerUrl;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserMail() {
        return mUserMail;
    }

    public void setUserMail(String mUserMail) {
        this.mUserMail = mUserMail;
    }
}
