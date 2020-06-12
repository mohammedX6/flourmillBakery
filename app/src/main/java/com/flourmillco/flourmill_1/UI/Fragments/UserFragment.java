package com.flourmillco.flourmill_1.UI.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.UI.LoginActivity;
import com.flourmillco.flourmill_1.UI.OrderHistoryActivity;
import com.flourmillco.flourmill_1.UI.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

public class UserFragment extends Fragment {

    private TextView EditAccount;
    private TextView OrderHistory;
    private TextView Help;
    private TextView Share;
    private TextView Settings;
    private TextView myInfo;

    private TextView Logout;
    private SharedPreferences pref3;
    private SharedPreferences.Editor editor;

    public UserFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EditAccount = getActivity().findViewById(R.id.edit);
        OrderHistory = getActivity().findViewById(R.id.orderhistory);
        Help = getActivity().findViewById(R.id.help);
        Share = getActivity().findViewById(R.id.share);
        myInfo = getActivity().findViewById(R.id.useinfo);

        Settings = getActivity().findViewById(R.id.settings);
        pref3 = getActivity().getSharedPreferences("secretcode", MODE_PRIVATE);
        editor = pref3.edit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        EditAccount = view.findViewById(R.id.edit);
        OrderHistory = view.findViewById(R.id.orderhistory);
        Help = view.findViewById(R.id.help);
        Share = view.findViewById(R.id.share);
        Settings = view.findViewById(R.id.settings);
        Logout = view.findViewById(R.id.logout2);
        myInfo = view.findViewById(R.id.useinfo);
        try {
            EditAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            OrderHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
                    startActivity(intent);

                }
            });
            Help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
            Share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String ShareSubject = "للاستفسار 0770133245";
                    String ShareBody = " مطحنةالشمال";
                    intent.putExtra(Intent.EXTRA_SUBJECT, ShareBody);
                    intent.putExtra(Intent.EXTRA_TEXT, ShareSubject);
                    startActivity(Intent.createChooser(intent, "Share using"));
                }
            });
            Settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), SettingsActivity.class);
                    startActivity(intent);

                }
            });
            Logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();

                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });


            String emailWithoutDomain = FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));
            myInfo.setText("Welcome \n " + emailWithoutDomain);


        } catch (Exception e) {

        }
        return view;
    }

}
