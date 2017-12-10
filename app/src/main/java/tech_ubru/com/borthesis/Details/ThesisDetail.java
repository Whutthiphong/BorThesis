package tech_ubru.com.borthesis.Details;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import tech_ubru.com.borthesis.MainActivity;
import tech_ubru.com.borthesis.ModelItem.BorrowItem;
import tech_ubru.com.borthesis.ModelItem.GET_ALL_BOOK;
import tech_ubru.com.borthesis.MySingleton;
import tech_ubru.com.borthesis.R;

public class ThesisDetail extends AppCompatActivity {

    TextView tv_detail_th_name,tv_detail_en_name,tv_detail_major,tv_detail_year,tv_detail_chan,tv_derail_status,tv_detail_wang;
    Button btn_detail_borr_thesis;
    private String service_name ="post_borrow.php";
    private String service_name_search_qrcode ="get_book_detail_By_qrcode.php";
    private String service_name_borrow_detail ="get_borrow_detail.php";

    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    GET_ALL_BOOK item;

    String pkthesis_borrow,pkstd_borrow,num_borrow,name_th, name_eng,major,year,chan,wang,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesis_detail);
        sp = getSharedPreferences(ConfigData.USER_TAG_SHARE, Context.MODE_PRIVATE);
        editor = sp.edit();
        tv_detail_th_name = findViewById(R.id.tv_detail_th_name);
        tv_detail_en_name = findViewById(R.id.tv_detail_en_name);
        tv_detail_major = findViewById(R.id.tv_detail_major);
        tv_detail_year = findViewById(R.id.tv_detail_year);
        tv_detail_chan = findViewById(R.id.tv_detail_chan);
        tv_derail_status = findViewById(R.id.tv_derail_status);
        tv_detail_wang = findViewById(R.id.tv_detail_wang);
        btn_detail_borr_thesis = findViewById(R.id.btn_detail_borr_thesis);
        final Bundle bundle = getIntent().getExtras();
        if(bundle.get("thesis")!=null) {
             item = (GET_ALL_BOOK) bundle.get("thesis");
             name_th = item.th_name;
             name_eng = item.eng_name;
             major = item.major;
             year = item.year;
             chan = item.chan;
             wang = item.wang;
             status = item.status;
             pkthesis_borrow= item.pk_thesis;
            tv_detail_th_name.setText(name_th);
            tv_detail_en_name.setText(name_eng);
            tv_detail_major.setText(major);
            tv_detail_year.setText("ปี : " + year);
            tv_detail_chan.setText("ชั้นวาง : " + chan);
            tv_detail_wang.setText("ตู้วาง : " + wang);
            String txt_status;
            if (status == null || status.equalsIgnoreCase("0")) {
                tv_derail_status.setTextColor(Color.parseColor("#75be51"));
                txt_status = "พร้อมใช้งาน";
                btn_detail_borr_thesis.setEnabled(true);
            } else {

                tv_derail_status.setTextColor(Color.parseColor("#ff5c2a"));
                txt_status = "ไม่พร้อมใช้งาน";
                btn_detail_borr_thesis.setBackgroundColor(0xffa4a4a4);

                btn_detail_borr_thesis.setEnabled(false);
            }

            tv_derail_status.setText(txt_status);

        }else if(bundle.get("qrcode")!=null){
            final ProgressDialog pDialog = new ProgressDialog(ThesisDetail.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("กำลังตรวจสอบข้อมูลน...");
            pDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, URLService.getUrl()+service_name_search_qrcode, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        pDialog.dismiss();
                        Log.e("responseQR",response.toString());
                        if(response.toString().equalsIgnoreCase("null")){
                            Toast.makeText(ThesisDetail.this, "ไม่พบข้อมูล", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ThesisDetail.this, MainActivity.class));
                        }
                        JSONObject jsnobject = new JSONObject(response.toString());
                        ArrayList<GET_ALL_BOOK> item_list = new JsonConverter<GET_ALL_BOOK>().toArrayList(jsnobject.getString("book"),GET_ALL_BOOK.class);
                        item  = item_list.get(0);
                        name_th = item.th_name;
                        name_eng = item.eng_name;
                        major = item.major;
                        year = item.year;
                        chan = item.chan;
                        wang = item.wang;
                        status = item.status;
                        pkthesis_borrow= item.pk_thesis;

                        tv_detail_th_name.setText(name_th);
                        tv_detail_en_name.setText(name_eng);
                        tv_detail_major.setText(major);
                        tv_detail_year.setText("ปี : " + year);
                        tv_detail_chan.setText("ชั้นวาง : " + chan);
                        tv_detail_wang.setText("ตู้วาง : " + wang);
                        String txt_status;
                        if (status == null || status.equalsIgnoreCase("0")) {
                            tv_derail_status.setTextColor(Color.parseColor("#75be51"));
                            txt_status = "พร้อมใช้งาน";
                            btn_detail_borr_thesis.setEnabled(true);
                        } else {

                            tv_derail_status.setTextColor(Color.parseColor("#ff5c2a"));
                            txt_status = "ไม่พร้อมใช้งาน";
                            btn_detail_borr_thesis.setBackgroundColor(0xffa4a4a4);

                            btn_detail_borr_thesis.setEnabled(false);
                        }

                        tv_derail_status.setText(txt_status);

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
                    param.put("qrcode",bundle.getString("qrcode").toString() );
                    return param;
                }
            };

            MySingleton.getInstance(ThesisDetail.this).addToRequestQueue(request);
        }else{
            Toast.makeText(this, "ไม่พบข้อมูล", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }


        btn_detail_borr_thesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(ThesisDetail.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


                    dialog.setContentView(R.layout.custom_dialog_input_amount_date);
                    Button btn_borrow_ok,btn_borrow_cancle;
                    final EditText et_amount_borrow;
                    et_amount_borrow = dialog.findViewById(R.id.et_amount_borrow);
                    btn_borrow_ok = dialog.findViewById(R.id.btn_borrow_ok);
                    btn_borrow_cancle = dialog.findViewById(R.id.btn_borrow_cancle);
                        btn_borrow_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Log.e("DATA",item.thesis_id.toString()+"<<"+sp.getString("pk_mem","")+">>"+et_amount_borrow.getText().toString());
                                Toast.makeText(ThesisDetail.this, item.thesis_id.toString()+"<<"+sp.getString("pk_mem","")+">>"+et_amount_borrow.getText().toString(), Toast.LENGTH_SHORT).show();
                                final ProgressDialog pDialog = new ProgressDialog(ThesisDetail.this);
                                pDialog.setCancelable(false);
                                pDialog.setMessage("กำลังบันทึกข้อมูล...");
                                pDialog.show();

                                StringRequest request = new StringRequest(Request.Method.POST, URLService.getUrl()+service_name, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            pDialog.cancel();

                                            JSONArray jsnobject2 =  new JSONArray(response.toString());
                                            JSONObject jsonObject = jsnobject2.getJSONObject(0);
                                            if(jsonObject.getString("result")!=null&&jsonObject.getString("result").length()>0){
                                                if(jsonObject.getString("result").equalsIgnoreCase("success")){
                                                    new AlertDialog.Builder(ThesisDetail.this)
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


                                                }else{
                                                    new AlertDialog.Builder(ThesisDetail.this)
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
                                        } catch (Exception e) {
                                            Log.e("error",e.toString());
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
                                        param.put("pkthesis_borrow",pkthesis_borrow );
                                        param.put("pkstd_borrow",sp.getString("pk_mem","") );
                                        param.put("num_borrow",et_amount_borrow.getText().toString() );
                                        return param;
                                    }
                                };

                                MySingleton.getInstance(ThesisDetail.this).addToRequestQueue(request);
                            }
                        });
                btn_borrow_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.borrow_detail_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_borrow_detail){
            final ProgressDialog pDialog = new ProgressDialog(ThesisDetail.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("กำลังตรวจสอบข้อมูล...");
            pDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, URLService.getUrl()+service_name_borrow_detail, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        pDialog.dismiss();
                        Log.e("response",response.toString());
                        if(response.toString().equalsIgnoreCase("[]")) {
                            Log.e("IS NULL", response.toString());
                        }else{
                            ArrayList<BorrowItem> item_list = new JsonConverter<BorrowItem>().toArrayList(response.toString(),BorrowItem.class);
                            BorrowItem item = item_list.get(0);
                            Dialog dialog = new Dialog(ThesisDetail.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


                            dialog.setContentView(R.layout.custom_dialog_borrow_detail);
                            TextView tv_name_std_borrow,tv_borrow_date,tv_amount_borrow;
                            tv_name_std_borrow = dialog.findViewById(R.id.tv_name_std_borrow);
                            tv_borrow_date = dialog.findViewById(R.id.tv_borrow_date);
                            tv_amount_borrow = dialog.findViewById(R.id.tv_amount_borrow);

                            tv_name_std_borrow.setText("ผู้ยืม : "+item.name_mem+"  "+item.lastname_mem);
                            tv_borrow_date.setText("วันที่ยืม : "+item.date_borrow);
                            tv_amount_borrow.setText("จำนวนวัน : "+item.num_borrow);

                            dialog.show();
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Error",e.toString());
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
                    param.put("pkthesis_borrow",pkthesis_borrow);
                    return param;
                }
            };

            MySingleton.getInstance(ThesisDetail.this).addToRequestQueue(request);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
