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
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
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

    private final String LOG_TAG = this.getClass().getSimpleName();

    @Inject ServerPresenter mServerPresenter;
    GetServerId mGetServerId;
    View mView;

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

        mView = inflater.inflate(R.layout.fragment_add_server_dialog, container, false);
        return mView;
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
        return mGetServerId.getServerId();
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
    public void setServerName(String s) {
        setStringInEditText(R.id.text_server_name, s);
    }

    @Override
    public void setServerUrl(String s) {
        setStringInEditText(R.id.text_server_url, s);
    }

    @Override
    public void setUserName(String s) {
        setStringInEditText(R.id.text_user_name, s);
    }

    @Override
    public void setUserMail(String s) {
        setStringInEditText(R.id.text_user_mail, s);
    }

    @Override
    public void showUrlIsInvalid() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                EditText serveUrlText = (EditText) mView.findViewById(R.id.text_server_url);
                serveUrlText.setError(getString(R.string.url_invalid));
            }
        });
    }

    @Override
    public void closeAddServerView() {
        Dialog dialog = getDialog();

        if (dialog != null) {
            dialog.cancel();
        }
        else {
            NavUtils.navigateUpFromSameTask(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mServerPresenter.setView(this);
        mServerPresenter.loadServerDetails();
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

    private void setStringInEditText(int viewId, final String s) {
        final EditText editText;

        Dialog dialog = getDialog();

        if (dialog != null) {
            editText = (EditText) dialog.findViewById(viewId);
        }
        else {
            editText = (EditText) getActivity().findViewById(viewId);
        }

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                editText.setText(s);
            }
        });
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);

        try {
            mGetServerId = (GetServerId) context;
        }
        catch (ClassCastException e) {
            Log.e(LOG_TAG, "Context doesn't implement GetServerId: " + e.toString());
        }
    }
}
