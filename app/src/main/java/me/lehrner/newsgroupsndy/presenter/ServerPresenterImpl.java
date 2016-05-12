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

package me.lehrner.newsgroupsndy.presenter;

import android.util.Log;

import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.repository.ServerRepository;
import me.lehrner.newsgroupsndy.view.AddServerView;

public class ServerPresenterImpl implements ServerPresenter {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private final ServerRepository mServerRepository;
    private AddServerView mAddServerView;
    private Server mServer;

    public ServerPresenterImpl(ServerRepository serverRepository) {
        mServerRepository = serverRepository;
    }

    @Override
    public void saveServer() {
        if (mServer == null) {
            return;
        }

        mServer.setServerName(mAddServerView.getServerName());
        mServer.setServerUrl(mAddServerView.getServerUrl());
        mServer.setUserName(mAddServerView.getUserName());
        mServer.setUserMail(mAddServerView.getUserMail());

        mServerRepository.saveServer(mServer);
    }

    @Override
    public void setView(AddServerView v) {
        mAddServerView = v;
        loadServerDetails();
    }

    @Override
    public void loadServerDetails() {
        int serverId = mAddServerView.getServerId();
        mServer = mServerRepository.getServer(serverId);
    }
}
