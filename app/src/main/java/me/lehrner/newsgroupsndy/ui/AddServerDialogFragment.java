package me.lehrner.newsgroupsndy.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.lehrner.newsgroupsndy.R;

public class AddServerDialogFragment extends AppCompatDialogFragment {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private String mServerName = null;
    private String mServerUrl = null;
    private String mUserName = null;
    private String mUserMail = null;

    public AddServerDialogFragment() {
        // Required empty public constructor
    }

    public static AddServerDialogFragment newInstance() {
        return new AddServerDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
