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
import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.repository.GroupRepository;
import me.lehrner.newsgroupsndy.repository.ServerRepository;
import me.lehrner.newsgroupsndy.view.GroupView;
import me.lehrner.newsgroupsndy.view.tasks.UpdateGroupListAsyncTask;

public class GroupPresenterImpl implements GroupPresenter {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private final GroupRepository mGroupRepository;
    private final ServerRepository mServerRepository;
    private GroupView mGroupView;
    private UpdateGroupListAsyncTask mUpdateListTask = null;

    // sort case insensitive
    private static final String mLoaderOrder = GroupEntry.COLUMN_NAME_GROUP_NAME +
            " COLLATE NOCASE";
    private static final String[] mLoaderProjection = {
            GroupEntry._ID,
            GroupEntry.COLUMN_NAME_GROUP_NAME
    };

    public GroupPresenterImpl(GroupRepository groupRepository,
                              ServerRepository serverRepository) {
        mGroupRepository = groupRepository;
        mServerRepository = serverRepository;
    }

    @Override
    public void subscribeToGroup(int id) {

    }

    @Override
    public void unsubscribeFromGroup(int id) {
//        mGroupRepository.deleteGroup(id);
    }

    @Override
    public void setGroupView(GroupView groupView) {
        mGroupView = groupView;
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
    public String getSubscribedGroupsSelection() {
        return null;
    }

    @Override
    public String getUnsubscribedGroupsSelection() {
        return null;
    }

    @Override
    public void updateNotSuccessful() {
        mUpdateListTask = null;
    }

    @Override
    public void updateSuccessful() {
        mUpdateListTask = null;

        if (mGroupView == null) {
            throw new RuntimeException(LOG_TAG + ": mGroupView not set");
        }

        mGroupView.reloadList();
    }

    @Override
    public void onPause() {
        if (mUpdateListTask != null) {
            mUpdateListTask.cancel(true);
        }

        mUpdateListTask = null;
    }
}
