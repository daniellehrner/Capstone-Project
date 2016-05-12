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

import android.util.Log;

import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.view.AddServerView;

public class DatabaseServerRepositoryImpl implements ServerRepository {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Server mServer;

    @Override
    public Server getServer(int id) {
        if (id == AddServerView.SERVER_ID_NOT_SET) {
            mServer = new Server();
            mServer.setId(0);
            mServer.setServerName("");
            mServer.setServerUrl("");
            mServer.setUserName("");
            mServer.setUserMail("");
        }

        return mServer;
    }

    @Override
    public void saveServer(Server s) {
        Log.d(LOG_TAG, "Saving server with name " + s.getServerName());
        Log.d(LOG_TAG, "Saving server with url " + s.getServerUrl());
        Log.d(LOG_TAG, "Saving server with user name " + s.getUserName());
        Log.d(LOG_TAG, "Saving server with user mail " + s.getUserMail());
    }
}
