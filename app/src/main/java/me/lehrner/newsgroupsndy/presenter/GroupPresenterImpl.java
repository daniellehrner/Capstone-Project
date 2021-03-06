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

import me.lehrner.newsgroupsndy.model.Group;
import me.lehrner.newsgroupsndy.model.GroupContract.GroupEntry;
import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.presenter.tasks.SubscribeGroupAsyncTask;
import me.lehrner.newsgroupsndy.repository.GroupRepository;
import me.lehrner.newsgroupsndy.repository.ServerRepository;
import me.lehrner.newsgroupsndy.presenter.tasks.UpdateGroupListAsyncTask;

public class GroupPresenterImpl implements GroupPresenter {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private final GroupRepository mGroupRepository;
    private final ServerRepository mServerRepository;
    private UpdateGroupListAsyncTask mUpdateListTask = null;

    // sort case insensitive
    private static final String mLoaderOrder = GroupEntry.COLUMN_NAME_GROUP_NAME +
            " COLLATE NOCASE";
    private static final String[] mLoaderProjection = {
            GroupEntry._ID,
            GroupEntry.COLUMN_NAME_GROUP_NAME,
            GroupEntry.COLUMN_NAME_SUBSCRIBED
    };

    private static final String mGroupSelection = GroupEntry.COLUMN_NAME_SERVER_ID +
            " = ";

    public GroupPresenterImpl(GroupRepository groupRepository,
                              ServerRepository serverRepository) {
        mGroupRepository = groupRepository;
        mServerRepository = serverRepository;
    }

    @Override
    public void toggleSubscribe(int id) {
        Group group = mGroupRepository.getGroup(id);

        SubscribeGroupAsyncTask subscribeTask = new SubscribeGroupAsyncTask(mGroupRepository);
        subscribeTask.execute(group);
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
        return GroupEntry.GROUP_URI_STRING;
    }

    @Override
    public void updateGroupList(int serverId) {
        if (mUpdateListTask != null) {
            mUpdateListTask.cancel(true);
        }

        Server server = mServerRepository.getServer(serverId);
        mUpdateListTask = new UpdateGroupListAsyncTask(this, mGroupRepository);
        mUpdateListTask.execute(server);
    }

    @Override
    public String getSubscribedGroupsSelection(int serverId) {
        return mGroupSelection + serverId + " AND "
                + GroupEntry.COLUMN_NAME_SUBSCRIBED + " = 1";
    }

    @Override
    public void updateNotSuccessful() {
        Log.d(LOG_TAG, "updateSuccessful");

        mUpdateListTask = null;
    }

    @Override
    public void updateSuccessful(int serverId) {
        Log.d(LOG_TAG, "updateSuccessful");

        mUpdateListTask = null;
        mServerRepository.setLastVisitedNow(serverId);
    }

    @Override
    public void onPause() {
        if (mUpdateListTask != null) {
            mUpdateListTask.cancel(true);
        }

        mUpdateListTask = null;
    }

    @Override
    public String getLoaderSelection(int serverId) {
        return mGroupSelection + serverId;
    }

    @Override
    public String getSearchSelection() {
        return mGroupSelection + "? AND " + GroupEntry.COLUMN_NAME_GROUP_NAME +
                " LIKE ?";
    }

    @Override
    public String[] getSearchSelectionArgs(int serverId, String search) {
        return  new String[]{String.valueOf(serverId), "%" + search + "%"};
    }
}
