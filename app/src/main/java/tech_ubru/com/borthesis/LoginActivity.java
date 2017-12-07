package tech_ubru.com.borthesis;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    CheckBox cb_showpass;
    EditText et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cb_showpass = findViewById(R.id.cb_showpass);
        et_password = findViewById(R.id.et_password);

        cb_showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        cb_showpass.setButtonTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimary));
                    }
                    et_password.setTransformationMethod(null);
                    et_password.setSelection(et_password.getText().length());
                }else{
                    et_password.setTransformationMethod(new PasswordTransformationMethod());
                    et_password.setSelection(et_password.getText().length());
                }
            }
        });
    }
}
