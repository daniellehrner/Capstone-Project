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

package me.lehrner.newsgroupsndy.view.tasks;

import android.os.AsyncTask;

import me.lehrner.newsgroupsndy.presenter.ServerPresenter;

public class SaveServerAsyncTask extends AsyncTask<Void, Void, Void> {
    private ServerPresenter mServerPresenter;

    public SaveServerAsyncTask(ServerPresenter presenter) {
        mServerPresenter = presenter;
    }

    @Override
    protected Void doInBackground(Void... params) {
        mServerPresenter.saveServer();
        return null;
    }
}
