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

public final class Group {
    private final int mId;
    private final String mGroupName;
    private final int mServerId;

    public Group(int id, @NonNull String groupName, int serverId) {
        mId = id;
        mGroupName = groupName;
        mServerId = serverId;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mGroupName;
    }

    public int getServerId() {
        return mServerId;
    }

    @Override
    public String toString() {
        return "Group{" +
                "Id=" + mId +
                ", GroupName='" + mGroupName + '\'' +
                ", ServerId=" + mServerId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (mId != group.mId) return false;
        if (mServerId != group.mServerId) return false;
        return mGroupName.equals(group.mGroupName);

    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + mGroupName.hashCode();
        result = 31 * result + mServerId;
        return result;
    }
}
