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

package me.lehrner.newsgroupsndy.model;


import android.support.annotation.NonNull;

final class Group {
    private final int mId;
    private final String mGroupName;

    public Group(int id, @NonNull String groupName) {
        mId = id;
        mGroupName = groupName;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mGroupName;
    }

    @Override
    public String toString() {
        return "Group{" +
                "Id=" + mId +
                ", Name='" + mGroupName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Group group = (Group) o;

        return (mId == group.mId) && mGroupName.equals(group.mGroupName);
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + mGroupName.hashCode();
        return result;
    }
}
