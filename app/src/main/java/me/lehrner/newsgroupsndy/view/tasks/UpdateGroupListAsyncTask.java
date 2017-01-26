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

package me.lehrner.newsgroupsndy.view.tasks;

import android.os.AsyncTask;

import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewGroupsOrNewsQuery;
import org.apache.commons.net.nntp.NewsgroupInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.lehrner.newsgroupsndy.model.Server;
import me.lehrner.newsgroupsndy.presenter.GroupPresenter;
import me.lehrner.newsgroupsndy.repository.GroupRepository;

public class UpdateGroupListAsyncTask extends AsyncTask<Server, Void, Boolean> {
    private GroupRepository mGroupRepository;
    private GroupPresenter mGroupPresenter;

    public UpdateGroupListAsyncTask(GroupPresenter groupPresenter,
                                    GroupRepository groupRepository) {
        mGroupPresenter = groupPresenter;
        mGroupRepository = groupRepository;
    }

    @Override
    protected Boolean doInBackground(Server... servers) {
        String hostName = servers[0].getServerUrl();
        Calendar lastVisit = servers[0].getLastVisit();

        NewsgroupInfo[] newsgroupInfos = {};
        ArrayList<String> groupNames = new ArrayList<>();

        try {
            NNTPClient nntpClient = new NNTPClient();
            nntpClient.connect(hostName, 119);

            if (lastVisit != null) {
                NewGroupsOrNewsQuery query = new NewGroupsOrNewsQuery(lastVisit, false);
                newsgroupInfos = nntpClient.listNewNewsgroups(query);
            } else {
                newsgroupInfos = nntpClient.listNewsgroups();
            }

            nntpClient.disconnect();
        } catch (IOException e) {
            mGroupPresenter.updateNotSuccessful();
        }

        if (newsgroupInfos == null) {
            return false;
        }

        for (NewsgroupInfo newsgroupInfo : newsgroupInfos) {
            groupNames.add(newsgroupInfo.getNewsgroup());
        }

        return mGroupRepository.saveGroups(
                servers[0].getId(), groupNames);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            mGroupPresenter.updateSuccessful();
        }
        else {
            mGroupPresenter.updateNotSuccessful();
        }
    }

    @Override
    protected void onCancelled(Boolean result) {
        mGroupPresenter = null;
        mGroupRepository = null;
    }
}