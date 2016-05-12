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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import javax.inject.Inject;

import me.lehrner.newsgroupsndy.NDYApplication;
import me.lehrner.newsgroupsndy.R;
import me.lehrner.newsgroupsndy.presenter.ServerPresenter;

public class AddServerDialogFragment extends AppCompatDialogFragment
                                     implements AddServerView,
                                                AddServerClickHandler {
    private static final String SERVER_ID = "server_id";

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

        return inflater.inflate(R.layout.fragment_add_server_dialog, container, false);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams")
        View titleView = inflater.inflate(R.layout.dialog_add_server, null);

        builder.setView(R.layout.fragment_add_server_dialog)
                .setCustomTitle(titleView);

        return builder.create();
    }

    @Override
    public int getServerId() {
        return getArguments() == null ?
                AddServerView.SERVER_ID_NOT_SET :
                getArguments().getInt(SERVER_ID, 0);
    }

    @Override
    public String getServerName() {
        return getStringFromEditText(R.id.text_server_name);
    }

    @Override
    public String getServerUrl() {
        return getStringFromEditText(R.id.text_server_url);
    }

    @Override
    public String getUserName() {
        return getStringFromEditText(R.id.text_user_name);
    }

    @Override
    public String getUserMail() {
        return getStringFromEditText(R.id.text_user_mail);
    }

    @Override
    public void onResume() {
        super.onResume();
        mServerPresenter.setView(this);
    }

    @Override
    public void onServerSave() {
        mServerPresenter.saveServer();
    }

    private String getStringFromEditText(int viewId) {
        EditText editText;

        Dialog dialog = getDialog();

        if (dialog != null) {
            editText = (EditText) dialog.findViewById(viewId);
        }
        else {
            editText = (EditText) getActivity().findViewById(viewId);
        }

        return editText.getText().toString();
    }
}
