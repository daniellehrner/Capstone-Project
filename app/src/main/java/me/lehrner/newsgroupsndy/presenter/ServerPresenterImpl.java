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
import android.util.Patterns;

import java.util.regex.Matcher;

import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.model.ServerContract.ServerEntry;
import me.lehrner.newsgroupsndy.model.ServerFactory;
import me.lehrner.newsgroupsndy.repository.ServerRepository;
import me.lehrner.newsgroupsndy.view.interfaces.AddServerView;

public class ServerPresenterImpl implements ServerPresenter {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private final ServerRepository mServerRepository;
    private AddServerView mAddServerView;

    // sort case insensitive
    private static final String mLoaderOrder = ServerEntry.COLUMN_NAME_SERVER_NAME +
            " COLLATE NOCASE";
    private static final String[] mLoaderProjection = {
            ServerEntry._ID,
            ServerEntry.COLUMN_NAME_SERVER_NAME};

    public ServerPresenterImpl(ServerRepository serverRepository) {
        mServerRepository = serverRepository;
    }

    @Override
    public void saveServer() {
        if (mAddServerView == null) {
            return;
        }

        String serverUrl = mAddServerView.getServerUrl();

        if (!isValidUrl(serverUrl)) {
            Log.e(LOG_TAG, "URL is not valid");
            mAddServerView.showUrlIsInvalid();
            return;
        }

        Server server = ServerFactory.create().
                withId(mAddServerView.getServerId()).
                withServerName(mAddServerView.getServerName()).
                withServerUrl(mAddServerView.getServerUrl()).
                withUserName(mAddServerView.getUserName()).
                withUserMail(mAddServerView.getUserMail()).
                build();

        if (mServerRepository.saveServer(server)) {
            Log.d(LOG_TAG, "Save success");
            mAddServerView.closeAddServerView();
        }
        else {
            Log.e(LOG_TAG, "Save error");
        }
    }

    private boolean isValidUrl(String url) {
        Matcher urlMatcher = Patterns.WEB_URL.matcher(url);

        return urlMatcher.matches();
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

        if (serverId != AddServerView.SERVER_ID_NOT_SET) {
            mAddServerView.setServerName(server.getServerName());
            mAddServerView.setServerUrl(server.getServerUrl());
            mAddServerView.setUserName(server.getUserName());
            mAddServerView.setUserMail(server.getUserMail());
        }
    }

    @Override
    public void deleteServer(int id) {
        mServerRepository.deleteServer(id);
    }

    @Override
    public String[] getLoaderProjection() {
        return mLoaderProjection;
    }

    @Override
    public String getLoaderOrder() {
         return mLoaderOrder;
    }

    @Override
    public String getLoaderUriString() {
        return ServerEntry.SERVER_URI_STRING;
    }
}
