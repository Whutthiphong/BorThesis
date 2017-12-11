package tech_ubru.com.borthesis;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
                        Log.e("Res",response.toString());
                        if(response.toString().equalsIgnoreCase("null")){
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setMessage("Username หรือ Password ไม่ถูกต้อง")
                                    .setTitle("แจ้งเตือน!!")
                                    .setIcon(R.drawable.ic_info_black_24dp)
                                    .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                            return;
                        }
                        JSONObject jsnobject = new JSONObject(response);
                        JSONArray jsonArray_stat = jsnobject.getJSONArray("result");

                        JSONObject jsnobject2 = jsonArray_stat.getJSONObject(0);
                        if(jsnobject2.getString("status")!=null&&jsnobject2.getString("status").length()>0){
                            editor.putString("LOGIN_STAT",jsnobject2.getString("status"));


                        }
                        if(jsnobject.getJSONArray("detail")!=null&&jsnobject.getJSONArray("detail").length()>0) {

                            ArrayList<Std_Item> lis_std_detail =new  JsonConverter<Std_Item>().toArrayList(jsnobject.getJSONArray("detail").toString(),Std_Item.class);
                            Std_Item item = lis_std_detail.get(0);
                            if(item.status_register.equalsIgnoreCase("1")){

                                editor.putString("pimerykey",item.pimerykey);
                                editor.putString("prefix",item.prefix);
                                editor.putString("email",item.email);
                                editor.putString("id_student",item.id_student);
                                editor.putString("name",item.name);
                                editor.putString("lastname",item.lastname);
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                finish();
                                startActivity(intent);
                            }else {
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setMessage("Username นี้ยังไม่ได้สมัครสมาชิก")
                                        .setTitle("แจ้งเตือน!!")
                                        .setIcon(R.drawable.ic_info_black_24dp)
                                        .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                }).show();
                            }


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
                    param.put("username",params[0] );
                    param.put("password",params[1] );
                    return param;
                }
            };

            MySingleton.getInstance(LoginActivity.this).addToRequestQueue(request);
            return null;
        }
    }
}
