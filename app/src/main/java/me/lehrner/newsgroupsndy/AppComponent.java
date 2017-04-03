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

package me.lehrner.newsgroupsndy;

import javax.inject.Singleton;

import dagger.Component;
import me.lehrner.newsgroupsndy.view.AddGroupAdapter.AddGroupAdapterViewHolder;
import me.lehrner.newsgroupsndy.view.AddGroupFragment;
import me.lehrner.newsgroupsndy.view.AddServerDialogFragment;
import me.lehrner.newsgroupsndy.view.GroupActivity;
import me.lehrner.newsgroupsndy.view.GroupAdapter.GroupAdapterViewHolder;
import me.lehrner.newsgroupsndy.view.GroupFragment;
import me.lehrner.newsgroupsndy.view.MainActivity;
import me.lehrner.newsgroupsndy.view.ServerAdapter.ServerAdapterViewHolder;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {
    void inject(AddServerDialogFragment target);
    void inject(MainActivity target);
    void inject(ServerAdapterViewHolder target);
    void inject(AddGroupFragment target);
    void inject(GroupActivity target);
    void inject(GroupAdapterViewHolder target);
    void inject(GroupFragment target);
    void inject(AddGroupAdapterViewHolder target);
}
