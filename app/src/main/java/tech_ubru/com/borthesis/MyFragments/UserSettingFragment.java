package tech_ubru.com.borthesis.MyFragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tech_ubru.com.borthesis.AppConfig.ConfigData;
import tech_ubru.com.borthesis.LoginActivity;
import tech_ubru.com.borthesis.MainActivity;
import tech_ubru.com.borthesis.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingFragment extends Fragment {

    Button btn_logout;
    private SharedPreferences sp;
    public UserSettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_setting, container, false);
        btn_logout = rootView.findViewById(R.id.btn_logout);
        sp = getContext().getSharedPreferences(ConfigData.USER_TAG_SHARE, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        MainActivity activity = (MainActivity) getActivity();
        activity.getSupportActionBar().show();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                getActivity().finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        return rootView;
    }

}
