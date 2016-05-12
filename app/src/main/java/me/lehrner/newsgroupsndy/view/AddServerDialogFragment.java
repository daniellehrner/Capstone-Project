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

package me.lehrner.newsgroupsndy.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import me.lehrner.newsgroupsndy.NDYApplication;
import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.presenter.ServerPresenter;

public class AddServerDialogFragment extends AppCompatDialogFragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    @Inject ServerPresenter mServerPresenter;

    public AddServerDialogFragment() {
        // Required empty public constructor
    }

    public static AddServerDialogFragment newInstance() {
        return new AddServerDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((NDYApplication) getActivity().getApplication()).getComponent().inject(this);

        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_add_server_dialog, container, false);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View titleView = inflater.inflate(R.layout.dialog_add_server, null);

        builder.setView(R.layout.fragment_add_server_dialog)
                .setCustomTitle(titleView);

        return builder.create();
    }
}
