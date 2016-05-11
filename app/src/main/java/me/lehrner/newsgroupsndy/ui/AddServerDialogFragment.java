package me.lehrner.newsgroupsndy.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.lehrner.newsgroupsndy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddServerDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddServerDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddServerDialogFragment extends AppCompatDialogFragment {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_server_dialog, container, false);
    }
}
