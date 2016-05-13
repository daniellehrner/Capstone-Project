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

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.lehrner.newsgroupsndy.model.ServerDbHelper;
import me.lehrner.newsgroupsndy.presenter.ServerPresenter;
import me.lehrner.newsgroupsndy.presenter.ServerPresenterImpl;
import me.lehrner.newsgroupsndy.repository.DatabaseServerRepositoryImpl;
import me.lehrner.newsgroupsndy.repository.ServerRepository;

@Module
public class AppModule {
    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public ServerRepository provideServerRepository(Context context) {
        return new DatabaseServerRepositoryImpl(context);
    }

    @Provides
    public ServerPresenter provideUserPresenter(ServerRepository serverRepository) {
        return new ServerPresenterImpl(serverRepository);
    }
}
