package tech_ubru.com.borthesis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tech_ubru.com.borthesis.AppConfig.ConfigData;
import tech_ubru.com.borthesis.AppConfig.URLService;
import tech_ubru.com.borthesis.ModelItem.Std_Item;

public class LoginActivity extends AppCompatActivity {

    CheckBox cb_showpass;
    Button btn_register,btn_login;
    EditText et_password,et_username;
    String service_name = "get_login_and_get_std_detail.php";
    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences(ConfigData.USER_TAG_SHARE, Context.MODE_PRIVATE);
        editor = sp.edit();

        cb_showpass = findViewById(R.id.cb_showpass);
        et_password = findViewById(R.id.et_password);
        et_username = findViewById(R.id.et_username);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

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
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginTask().execute(et_username.getText().toString(),et_password.getText().toString());



            }
        });
    }
    class LoginTask extends AsyncTask<String,Void,String>{
        private ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=ProgressDialog.show(LoginActivity.this,"","Please Wait",false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
        }

        @Override
        protected String doInBackground(final String... params) {

            final StringRequest request = new StringRequest(Request.Method.POST, URLService.getUrl()+service_name, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsnobject = new JSONObject(response);
                        JSONArray jsonArray_stat = jsnobject.getJSONArray("result");

                        JSONObject jsnobject2 = jsonArray_stat.getJSONObject(0);
                        if(jsnobject2.getString("status")!=null&&jsnobject2.getString("status").length()>0){
                            editor.putString("LOGIN_STAT",jsnobject2.getString("status"));
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        if(jsnobject.getJSONArray("detail")!=null&&jsnobject.getJSONArray("detail").length()>0) {

                            ArrayList<Std_Item> lis_std_detail =new  JsonConverter<Std_Item>().toArrayList(jsnobject.getJSONArray("detail").toString(),Std_Item.class);
                            Std_Item item = lis_std_detail.get(0);

                            editor.putString("pk_mem",item.pk_mem);
                            editor.putString("prefix_mem",item.prefix_mem);
                            editor.putString("email_mem",item.email_mem);
                            editor.putString("name_mem",item.name_mem);
                            editor.putString("lastname_mem",item.lastname_mem);
                            editor.commit();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }


            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("username_mem",params[0] );
                    param.put("password_mem",params[1] );
                    return param;
                }
            };

            MySingleton.getInstance(LoginActivity.this).addToRequestQueue(request);
            return null;
        }
    }
}
