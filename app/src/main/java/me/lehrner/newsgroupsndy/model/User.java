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

public final class User {
    private final String mUserName;
    private final String mUserMail;

    public User(@NonNull String userName, @NonNull String userMail) {
        mUserName = userName;
        mUserMail = userMail;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getUserMail() {
        return mUserMail;
    }

    @Override
    public String toString() {
        return "User{" +
                "Name='" + mUserName + '\'' +
                ", Mail='" + mUserMail + '\'' +
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

        User user = (User) o;

        return mUserName.equals(user.mUserName) && mUserMail.equals(user.mUserMail);
    }

    @Override
    public int hashCode() {
        int result = mUserName.hashCode();
        result = 31 * result + mUserMail.hashCode();
        return result;
    }
}
