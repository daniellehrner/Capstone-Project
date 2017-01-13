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

class ListViewPresenterImpl implements ListViewPresenter {
    String[] mLoaderProjection;
    String mLoaderSortOrder;
    String mLoaderUriString;

    @Override
    public String[] getLoaderProjection() {
        if (mLoaderProjection == null) {
            throw new NullPointerException("mLoaderProjection has not been initialized");
        }

        return mLoaderProjection;
    }

    @Override
    public String getLoaderOrder() {
        if (mLoaderSortOrder == null) {
            throw new NullPointerException("mLoaderSortOrder has not been initialized");
        }

        return mLoaderSortOrder;
    }

    @Override
    public String getLoaderUriString() {
        if (mLoaderUriString == null) {
            throw new NullPointerException("mLoaderUriString has not been initialized");
        }

        return mLoaderUriString;
    }
}
