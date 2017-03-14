package me.lehrner.newsgroupsndy.presenter.tasks;
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

import android.os.AsyncTask;

import me.lehrner.newsgroupsndy.model.Group;
import me.lehrner.newsgroupsndy.repository.GroupRepository;

public class SubscribeGroupAsyncTask extends AsyncTask<Group, Void, Boolean> {
    private GroupRepository mGroupRepository;

    public SubscribeGroupAsyncTask(GroupRepository groupRepository) {
        mGroupRepository = groupRepository;
    }

    @Override
    protected Boolean doInBackground(Group... groups) {
        boolean isSubscribed = false;
        Group group = groups[0];

        if (group.getSubscribed() == GroupRepository.SUBSCRIBED) {
            mGroupRepository.unsubscribe(group.getId());
        }
        else {
            mGroupRepository.subscribe(group.getId());
            isSubscribed = true;
        }

        return isSubscribed;
    }
}
