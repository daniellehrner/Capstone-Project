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

import java.io.IOException;
import java.util.ArrayList;

public class GetGroupsFromServerTask extends AsyncTask<String, Void, String[]> {
    public GetGroupsFromServerTask() {}

    @Override
    protected String[] doInBackground(String... urls) {
        NNTPClient nntpClient = new NNTPClient();
        ArrayList<String> groups = null;

        for (String url : urls) {
            try {
                nntpClient.connect(url);
            }
            catch (IOException e) {
                throw new RuntimeException("Can't connect to server: " + url);
            }

            try {
                groups = (ArrayList<String>) nntpClient.iterateNewsgroupListing();
            }
            catch (IOException e) {
                throw new RuntimeException("Can't get the groups from the server");
            }
        }

        return (String[]) groups.toArray();
    }

    @Override
    protected void onPostExecute(String[] groups) {

    }
}
