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
import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;
import me.lehrner.newsgroupsndy.repository.ServerRepository;
import me.lehrner.newsgroupsndy.view.AddServerView;

public class ServerPresenterImpl implements ServerPresenter {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final String[] mServerLoaderProjection = new String[]{
            ServerEntry._ID,
            ServerEntry.COLUMN_NAME_SERVER_NAME};

    // sort case insensitive
    private static final String mLoaderSortOrder =
            ServerEntry.COLUMN_NAME_SERVER_NAME + " COLLATE NOCASE";


    private final ServerRepository mServerRepository;
    private AddServerView mAddServerView;

    public ServerPresenterImpl(ServerRepository serverRepository) {
        mServerRepository = serverRepository;
    }

    @Override
    public void saveServer() {
        if (mAddServerView == null) {
            return;
        }

        Server server = new Server();

        server.setId(mAddServerView.getServerId());
        server.setServerName(mAddServerView.getServerName());
        server.setServerUrl(mAddServerView.getServerUrl());
        server.setUserName(mAddServerView.getUserName());
        server.setUserMail(mAddServerView.getUserMail());

        if (mServerRepository.saveServer(server)) {
            Log.d(LOG_TAG, "Save success");
        }
        else {
            Log.d(LOG_TAG, "Save error");
        }
    }

    @Override
    public void setView(AddServerView v) {
        mAddServerView = v;
        loadServerDetails();
    }

    @Override
    public void loadServerDetails() {
        if (mAddServerView == null) {
            return;
        }

        int serverId = mAddServerView.getServerId();
        Server server = mServerRepository.getServer(serverId);

        mAddServerView.setServerName(server.getServerName());
        mAddServerView.setServerUrl(server.getServerUrl());
        mAddServerView.setUserName(server.getUserName());
        mAddServerView.setUserMail(server.getUserMail());
    }

    @Override
    public String[] getLoaderProjection() {
        return mServerLoaderProjection;
    }

    @Override
    public String getLoaderOrder() {
        return mLoaderSortOrder;
    }

    @Override
    public String getLoaderUriString() {
        return ServerEntry.SERVER_URI_STRING;
    }

    @Override
    public void deleteServer(int id) {
        mServerRepository.deleteServer(id);
    }
}
