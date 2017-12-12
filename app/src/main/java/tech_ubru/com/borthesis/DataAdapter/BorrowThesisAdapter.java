package tech_ubru.com.borthesis.DataAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import tech_ubru.com.borthesis.AppConfig.URLService;
import tech_ubru.com.borthesis.ModelItem.BorrowItem;
import tech_ubru.com.borthesis.MySingleton;
import tech_ubru.com.borthesis.R;
import tech_ubru.com.borthesis.RegisterActivity;

/**
 * Created by wutthiphong.thu on 8/12/2560.
 */

public class BorrowThesisAdapter extends RecyclerView.Adapter<BorrowThesisAdapter.ViewHolder> {

    Context context;
    ArrayList<BorrowItem> borrowItems_list;
    private String txt_status_return;
    private String txt_status_borrow;
    private String service_name ="post_cancle_borrow.php";

    public BorrowThesisAdapter(Context context, ArrayList<BorrowItem> borrowItems_list) {
        this.context = context;
        this.borrowItems_list = borrowItems_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.borrow_item,null,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BorrowItem item = borrowItems_list.get(position);
        holder.tv_thesis_name_borow.setText(item.th_name);
        holder.tv_date_borrow.setText(item.date_borrow);
        String status_bor = item.status_borrow;
        String status_ret = item.status_return;
        if(status_bor!=null&&status_bor.equalsIgnoreCase("1")) {

            holder.tv_status_borrow.setTextColor(Color.parseColor("#75be51"));
            txt_status_borrow = "อนุมัติ";
        }else{
            holder.tv_status_borrow.setTextColor(Color.parseColor("#ffb41e"));
            txt_status_borrow = "รอการอนุมัติ";
        }
        if(item.status_return!=null&&item.status_return.equalsIgnoreCase("1")) {
            holder.tv_status_return.setTextColor(Color.parseColor("#75be51"));
            txt_status_return = "คืนแล้ว";
        }else{
            holder.tv_status_return.setTextColor(Color.parseColor("#ff5c2a"));
            txt_status_return = "ยังไม่คืน";
        }
        holder.tv_status_borrow.setText(txt_status_borrow);
        holder.tv_status_return.setText(txt_status_return);
        holder.cons_borrow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.info)
                        .setMessage("ยืนยันการยกเลิกการยืม")
                        .setTitle("แจ้งเตือน!!")
                        .setCancelable(false)
                        .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                final ProgressDialog pDialog = new ProgressDialog(context );
                                pDialog.setCancelable(false);
                                pDialog.setMessage("กำลังบันทึกข้อมูล...");
                                pDialog.show();
                                StringRequest request = new StringRequest(Request.Method.POST, URLService.getUrl()+service_name, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        pDialog.dismiss();
                                        try {
                                            pDialog.dismiss();
                                            Log.e("register",response.toString());
                                            JSONArray jsonArray = new JSONArray(response.toString());

                                            JSONObject jsnobject2 = new JSONObject(jsonArray.get(0).toString());
                                            if(jsnobject2.getString("result")!=null&&jsnobject2.getString("result").length()>0){
                                                if(jsnobject2.getString("result").equalsIgnoreCase("success")){
                                                    new AlertDialog.Builder(context)
                                                            .setIcon(R.drawable.info)
                                                            .setMessage("ยกเลิกการยืมเรียบร้อยแล้ว")
                                                            .setTitle("แจ้งเตือน!!")
                                                            .setCancelable(false)
                                                            .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.dismiss();
                                                                    borrowItems_list.remove(position);
                                                                    notifyDataSetChanged();
                                                                }
                                                            })
                                                            .show();


                                                }else{
                                                    new AlertDialog.Builder(context)
                                                            .setIcon(R.drawable.info)
                                                            .setMessage("เกิดข้อผิดพลาดในการในการยกเลิก \n "+jsnobject2.getString("result"))
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
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("pimerykey",item.pimerykey);
                                        return params;
                                    }
                                };

                                MySingleton.getInstance(context).addToRequestQueue(request);

                            }
                        }).setNegativeButton(R.string.dialog_text_btn_cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                        }).show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(borrowItems_list!=null)
         return borrowItems_list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_thesis_name_borow ,tv_date_borrow,tv_status_return,tv_status_borrow;
        ConstraintLayout cons_borrow;
        public ViewHolder(View itemView) {
            super(itemView);
            cons_borrow = itemView.findViewById(R.id.cons_borrow);
            tv_thesis_name_borow = itemView.findViewById(R.id.tv_thesis_name_borow);
            tv_date_borrow = itemView.findViewById(R.id.tv_date_borrow);
            tv_status_return = itemView.findViewById(R.id.tv_status_return);
            tv_status_borrow = itemView.findViewById(R.id.tv_status_borrow);
        }
    }
}
