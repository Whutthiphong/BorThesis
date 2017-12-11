package tech_ubru.com.borthesis.MyFragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tech_ubru.com.borthesis.AppConfig.ConfigData;
import tech_ubru.com.borthesis.LoginActivity;
import tech_ubru.com.borthesis.MainActivity;
import tech_ubru.com.borthesis.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingFragment extends Fragment {

    Button btn_logout;
    TextView tv_name_std,tv_id_std,tv_email_std;
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
        tv_name_std = rootView.findViewById(R.id.tv_name_std);
        tv_id_std = rootView.findViewById(R.id.tv_id_std);
        tv_email_std = rootView.findViewById(R.id.tv_email_std);

        sp = getContext().getSharedPreferences(ConfigData.USER_TAG_SHARE, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        MainActivity activity = (MainActivity) getActivity();
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#265cff")));

        tv_name_std.setText("ชื่อ : "+sp.getString("name","")+"  "+sp.getString("lastname",""));
        tv_id_std.setText("รหัสนักศึกษา : "+sp.getString("id_student",""));
        tv_email_std.setText("อีเมลล์ : "+sp.getString("email",""));
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.info)
                        .setMessage("ยืนยันการออกจากระบบ")
                        .setTitle("แจ้งเตือน!!")
                        .setCancelable(false)
                        .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                editor.clear();
                                editor.commit();
                                getActivity().finish();
                                startActivity(new Intent(getContext(), LoginActivity.class));
                            }
                        }).setNegativeButton(R.string.dialog_text_btn_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                        .show();
            }
        });
        return rootView;
    }

}
