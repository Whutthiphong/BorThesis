package tech_ubru.com.borthesis.MyFragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tech_ubru.com.borthesis.AppConfig.URLService;
import tech_ubru.com.borthesis.DataAdapter.ThesisAdapter;
import tech_ubru.com.borthesis.Details.ThesisDetail;
import tech_ubru.com.borthesis.MainActivity;
import tech_ubru.com.borthesis.ModelItem.GET_ALL_BOOK;
import tech_ubru.com.borthesis.MySingleton;
import tech_ubru.com.borthesis.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchThesisFragment extends Fragment {


    String service_name = "get_all_book_detail.php";
    String service_name_date = "get_book_by_date.php";
    RecyclerView rv_all_thesis;
    ThesisAdapter adapter;
    ArrayList<GET_ALL_BOOK> list;

    public SearchThesisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rootView = inflater.inflate(R.layout.fragment_search_thesis, container, false);
        MainActivity activity = (MainActivity) getActivity();

        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#04AEDA")));
        rv_all_thesis =rootView.findViewById(R.id.rv_all_thesis);
        activity.getSupportActionBar().show();
        setHasOptionsMenu(true);

        UpdateData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        UpdateData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter!=null) {
                    if(adapter.getFilter()!=null)
                    adapter.getFilter().filter(newText.trim());
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_filter){
            //Do whatever you want to do
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_search);
            dialog.show();
            Button btn_cancle_search = dialog.findViewById(R.id.btn_cancle_search);
//            CheckBox cb_enable_date_search = dialog.findViewById(R.id.cb_enable_date_search);
            Button btn_apply_search = dialog.findViewById(R.id.btn_apply_search);
            final EditText et_date = dialog.findViewById(R.id.et_date);
//            cb_enable_date_search.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if(b){
//                        et_date.setEnabled(true);
//                    }else{
//
//                        et_date.setEnabled(false);
//                    }
//                }
//            });
            btn_cancle_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btn_apply_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(et_date!=null&&et_date.getText().toString().trim().length()==0){
                        et_date.requestFocus();
                        et_date.setError("กรุณากรอกปี");
                        return;
                    }
                    if(Integer.parseInt(et_date.getText().toString().trim())<2530||Integer.parseInt(et_date.getText().toString().trim())>2560){
                        new AlertDialog.Builder(getContext())
                                .setIcon(R.drawable.info)
                                .setMessage("วันที่ไม่ถูกต้อง ต้องอยู่ระหว่าง \"2530-2560\" ")
                                .setTitle("แจ้งเตือน!!")
                                .setCancelable(false)
                                .setPositiveButton(R.string.dialog_text_btn_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();


                                    }
                                })
                                .show();
                    }else{
                        final ProgressDialog pDialog = new ProgressDialog(getContext() );
                        pDialog.setCancelable(false);
                        pDialog.setMessage("กำลังโหลดข้อมูล...");
                        pDialog.show();
                        StringRequest request = new StringRequest(Request.Method.POST, URLService.getUrl()+service_name_date, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("ResSearchYear", response.toString());
                                dialog.dismiss();
                                pDialog.dismiss();
                                try {
                                    list = new JsonConverter<GET_ALL_BOOK>().toArrayList(response.toString(), GET_ALL_BOOK.class);
                                    adapter = new ThesisAdapter(getContext(), list);
                                    rv_all_thesis.setLayoutManager(new LinearLayoutManager(getContext()));
                                    rv_all_thesis.setAdapter(adapter);
                                }catch (Exception ex){
                                    Log.e("ExcSearchYear",ex.toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismiss();
                                Log.e("ErrSearchYear",error.toString());
                            }


                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("year",et_date.getText().toString().trim() );
                                return params;
                            }
                        };

                        MySingleton.getInstance(getContext()).addToRequestQueue(request);
                    }
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void UpdateData(){
        final ProgressDialog pDialog = new ProgressDialog(getContext() );
        pDialog.setCancelable(false);
        pDialog.setMessage("กำลังโหลดข้อมูล...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, URLService.getUrl()+service_name, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                 list = new JsonConverter<GET_ALL_BOOK>().toArrayList(response.toString(),GET_ALL_BOOK.class);
                 adapter = new ThesisAdapter(getContext(),list);
                 rv_all_thesis.setLayoutManager(new LinearLayoutManager(getContext()));
                 rv_all_thesis.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }


        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
