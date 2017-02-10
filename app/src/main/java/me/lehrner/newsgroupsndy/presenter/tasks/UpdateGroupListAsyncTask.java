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

package me.lehrner.newsgroupsndy.presenter.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewGroupsOrNewsQuery;
import org.apache.commons.net.nntp.NewsgroupInfo;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.presenter.GroupPresenter;
import me.lehrner.newsgroupsndy.repository.GroupRepository;

public class UpdateGroupListAsyncTask extends AsyncTask<Server, Void, Boolean> {
    private String LOG_TAG = this.getClass().getSimpleName();
    private GroupRepository mGroupRepository;
    private WeakReference<GroupPresenter> mGroupPresenterWeakReference;
    private int mServerId;

    public UpdateGroupListAsyncTask(GroupPresenter groupPresenter,
                                    GroupRepository groupRepository) {
        mGroupPresenterWeakReference = new WeakReference<>(groupPresenter);
        mGroupRepository = groupRepository;
    }

    @Override
    protected Boolean doInBackground(Server... servers) {
        mServerId = servers[0].getId();

        String hostName = servers[0].getServerUrl();
        int lastVisitInt = servers[0].getLastVisit();

        NewsgroupInfo[] newsgroupInfos;
        ArrayList<String> groupNames = new ArrayList<>();

        try {
            NNTPClient nntpClient = new NNTPClient();
            nntpClient.connect(hostName, 119);

            Log.d(LOG_TAG, "lastVisitInt: " + lastVisitInt);

            if (lastVisitInt != 0) {
                Calendar lastVisit = Calendar.getInstance();
                lastVisit.setTimeInMillis(lastVisitInt * 1000L);

                NewGroupsOrNewsQuery query = new NewGroupsOrNewsQuery(lastVisit, false);
                newsgroupInfos = nntpClient.listNewNewsgroups(query);

                Log.d(LOG_TAG, "listNewNewsgroups");

            } else {
                newsgroupInfos = nntpClient.listNewsgroups();

                Log.d(LOG_TAG, "listNewsgroups");

            }

            nntpClient.disconnect();
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException");

            GroupPresenter groupPresenter = mGroupPresenterWeakReference.get();

            if (groupPresenter != null) {
                groupPresenter.updateNotSuccessful();
            }
            return false;
        }

        if (newsgroupInfos == null) {
            Log.d(LOG_TAG, "newsgroupInfos is null");
            return false;
        }

        for (NewsgroupInfo newsgroupInfo : newsgroupInfos) {
            groupNames.add(newsgroupInfo.getNewsgroup());
        }

        return mGroupRepository.saveGroups(mServerId, groupNames);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        Log.d(LOG_TAG, "success: " + success);

        GroupPresenter groupPresenter = mGroupPresenterWeakReference.get();

        if (groupPresenter != null) {
            if (success) {
                groupPresenter.updateSuccessful(mServerId);
            } else {
                groupPresenter.updateNotSuccessful();
            }
        }
        else {
            Log.d(LOG_TAG, "groupPresenter is null");

        }
    }
}