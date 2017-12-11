package tech_ubru.com.borthesis;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tech_ubru.com.borthesis.AppConfig.URLService;

public class RegisterActivity extends AppCompatActivity {
    EditText et_std_id,et_conpassword,et_password,et_username;
    boolean mem_status = false;// true = สมัครแล้ว false = ยังไม่สมัคร
    CheckBox cb_showpass;
    private String service_name="get_is_member.php";
    private String service_name_register="post_register_member.php";
    private Button btn_register;

    @Override
    public boolean onSupportNavigateUp() {
        new AlertDialog.Builder(RegisterActivity.this)
                .setCancelable(false)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setIcon(R.drawable.info)
                .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton(R.string.dialog_text_btn_cancle, new DialogInterface.OnClickListener() {
            @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
        }).show();

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        et_std_id = findViewById(R.id.et_std_id);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_conpassword = findViewById(R.id.et_conpassword);
        btn_register = findViewById(R.id.btn_register);
        cb_showpass = findViewById(R.id.cb_showpass);
        cb_showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        cb_showpass.setButtonTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimary));
                    }
                    et_password.setTransformationMethod(null);
                    et_password.setSelection(et_password.getText().length());
                    et_conpassword.setTransformationMethod(null);
                    et_conpassword.setSelection(et_password.getText().length());
                }else{
                    et_password.setTransformationMethod(new PasswordTransformationMethod());
                    et_password.setSelection(et_password.getText().length());
                    et_conpassword.setTransformationMethod(new PasswordTransformationMethod());
                    et_conpassword.setSelection(et_password.getText().length());
                }
            }
        });
        et_std_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(!b){

                    final ProgressDialog pDialog = new ProgressDialog(RegisterActivity.this);
                    pDialog.setCancelable(false);
                    pDialog.setMessage("กำลังตรวจสอบข้อมูลนักศึกษา...");
                    pDialog.show();
                    StringRequest request = new StringRequest(Request.Method.POST, URLService.getUrl()+service_name, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                pDialog.dismiss();
                                Log.e("response",response.toString());
                                JSONObject jsnobject = new JSONObject(response);
                                JSONArray jsonArray_stat = jsnobject.getJSONArray("result");

                                JSONObject jsnobject2 = jsonArray_stat.getJSONObject(0);
                                if(jsnobject2.getString("status")!=null&&jsnobject2.getString("status").length()>0){
                                    if(jsnobject2.getString("status").equalsIgnoreCase("registered")){
                                        new AlertDialog.Builder(RegisterActivity.this)
                                                .setIcon(R.drawable.info)
                                                .setMessage("รหัส นศ. นี้ได้สมัครไปแล้ว")
                                                .setTitle("แจ้งเตือน!!")
                                                .setCancelable(false)
                                                .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        et_std_id.requestFocus();
                                                    }
                                                })
                                                .show();

                                        mem_status = false;

                                    }else if(jsnobject2.getString("status").equalsIgnoreCase("false")){
                                        new AlertDialog.Builder(RegisterActivity.this)
                                                .setIcon(R.drawable.info)
                                                .setMessage("รหัส นศ. นี้ไม่ได้อยู่ในระบบ")
                                                .setTitle("แจ้งเตือน!!")
                                                .setCancelable(false)
                                                .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        et_std_id.requestFocus();
                                                    }
                                                })
                                                .show();
                                        mem_status = false;
                                    }else if(jsnobject2.getString("status").equalsIgnoreCase("true")){
                                        mem_status = true;
                                    }
                                    Log.e("can register = ",mem_status+"");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<String, String>();
                            //input params to webservice
                            param.put("id_student",et_std_id.getText().toString() );
                            return param;
                        }
                    };

                    MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mem_status ==false){
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setIcon(R.drawable.info)
                            .setMessage("กรุณาตราจสอบรหัสนักศึกษา")
                            .setTitle("แจ้งเตือน!!")
                            .setCancelable(false)
                            .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    et_std_id.requestFocus();
                                    et_std_id.setError("ตราจสอบรหัสนักศึกษา");
                                }
                            })
                            .show();
                    return;
                }
                String pass = et_password.getText().toString().trim();
                String conpass = et_conpassword.getText().toString().trim();
                if(!pass.equals(conpass)){
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setIcon(R.drawable.info)
                            .setMessage("รหัสผ่านไม่ตรงกัน")
                            .setTitle("แจ้งเตือน!!")
                            .setCancelable(false)
                            .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    et_password.requestFocus();
                                }
                            })
                            .show();
                    return;
                        }
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setIcon(R.drawable.info)
                                    .setMessage("ยืนยันการบันทึกข้อมูล")
                                    .setTitle("แจ้งเตือน!!")
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            final ProgressDialog pDialog = new ProgressDialog(RegisterActivity.this);
                                            pDialog.setCancelable(false);
                                            pDialog.setMessage("กำลังบันทึกข้อมูล...");
                                            pDialog.show();
                                            StringRequest request = new StringRequest(Request.Method.POST, URLService.getUrl()+service_name_register, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        pDialog.dismiss();
                                                        Log.e("register",response.toString());
                                                        JSONArray jsonArray = new JSONArray(response.toString());

                                                        JSONObject jsnobject2 = new JSONObject(jsonArray.get(0).toString());
                                                        if(jsnobject2.getString("result")!=null&&jsnobject2.getString("result").length()>0){
                                                            if(jsnobject2.getString("result").equalsIgnoreCase("success")){
                                                                new AlertDialog.Builder(RegisterActivity.this)
                                                                        .setIcon(R.drawable.info)
                                                                        .setMessage("บันทึกข้อมูลเรียบร้อยแล้ว")
                                                                        .setTitle("แจ้งเตือน!!")
                                                                        .setCancelable(false)
                                                                        .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                                                dialogInterface.dismiss();
                                                                                finish();
                                                                            }
                                                                        })
                                                                        .show();

                                                                mem_status = false;

                                                            }else{
                                                                new AlertDialog.Builder(RegisterActivity.this)
                                                                        .setIcon(R.drawable.info)
                                                                        .setMessage("เกิดข้อผิดพลาดในการบันทึกข้อมูล")
                                                                        .setTitle("แจ้งเตือน!!")
                                                                        .setCancelable(false)
                                                                        .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                                dialogInterface.dismiss();
                                                                            }
                                                                        })
                                                                        .show();
                                                            }

                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    pDialog.dismiss();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> param = new HashMap<String, String>();
                                                    //input params to webservice
                                                    param.put("id_student",et_std_id.getText().toString() );
                                                    param.put("username",et_username.getText().toString() );
                                                    param.put("password",et_password.getText().toString() );
                                                    return param;
                                                }
                                            };

                                            MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);
                                        }
                                    })
                                    .setNegativeButton(R.string.dialog_text_btn_cancle, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();

                                        }
                                    })
                                    .show();

            }
        });
    }


}
