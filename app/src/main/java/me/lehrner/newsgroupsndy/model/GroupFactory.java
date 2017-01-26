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

package me.lehrner.newsgroupsndy.model;

public class GroupFactory {
    private int mId;
    private String mGroupName;
    private int mServerId;

    private GroupFactory() {}

    public static GroupFactory create() {
        return new GroupFactory();
    }

    public GroupFactory withId(int id) {
        this.mId = id;
        return this;
    }

    public GroupFactory withGroupName(String groupName) {
        this.mGroupName = groupName;
        return this;
    }

    public GroupFactory withServerId(int serverId) {
        this.mServerId = serverId;
        return this;
    }

    public Group build() {
        return new Group(this.mId,
                this.mGroupName,
                this.mServerId);
    }
}
