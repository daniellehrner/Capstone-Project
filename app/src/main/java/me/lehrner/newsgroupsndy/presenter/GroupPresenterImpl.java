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

import me.lehrner.newsgroupsndy.model.GroupContract.GroupEntry;
import me.lehrner.newsgroupsndy.repository.ServerRepository;

public class GroupPresenterImpl extends ListViewPresenterImpl implements GroupPresenter {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private final ServerRepository mServerRepository;

    public GroupPresenterImpl(ServerRepository serverRepository) {
        mServerRepository = serverRepository;
        mLoaderUriString = GroupEntry.SERVER_URI_STRING;
        mLoaderProjection = new String[]{
                GroupEntry._ID,
                GroupEntry.COLUMN_NAME_GROUP_NAME};
        // sort case insensitive
        mLoaderSortOrder = GroupEntry.COLUMN_NAME_GROUP_NAME + " COLLATE NOCASE";
    }

    @Override
    public void subscribeToGroup() {

    }

    @Override
    public void loadGroupDetails() {

    }

    @Override
    public void unsubscribeFromGroup(int id) {

    }

    @Override
    public void setView() {

    }
}
