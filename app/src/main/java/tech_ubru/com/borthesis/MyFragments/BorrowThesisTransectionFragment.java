package tech_ubru.com.borthesis.MyFragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tech_ubru.com.borthesis.AppConfig.ConfigData;
import tech_ubru.com.borthesis.DataAdapter.BorrowThesisAdapter;
import tech_ubru.com.borthesis.MainActivity;
import tech_ubru.com.borthesis.ModelItem.BorrowItem;
import tech_ubru.com.borthesis.MySingleton;
import tech_ubru.com.borthesis.R;
import tech_ubru.com.borthesis.AppConfig.URLService;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowThesisTransectionFragment extends Fragment {


    String service_name = "get_borrow_thesis.php";
    private ArrayList<BorrowItem> list;
    private BorrowThesisAdapter adapter;
    private RecyclerView rv_borrow;

    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    public BorrowThesisTransectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rootView =inflater.inflate(R.layout.fragment_borrow_thesis_transection, container, false);
                MainActivity activity = (MainActivity) getActivity();

        sp = getContext().getSharedPreferences(ConfigData.USER_TAG_SHARE, Context.MODE_PRIVATE);
        editor = sp.edit();
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8c19ff")));
        rv_borrow = rootView.findViewById(R.id.rv_borrow);
        UpdateData();
        return rootView;
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
                Log.e("response",response.toString());
                list = new JsonConverter<BorrowItem>().toArrayList(response.toString(),BorrowItem.class);
                adapter = new BorrowThesisAdapter(getContext(),list);
                rv_borrow.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_borrow.setAdapter(adapter);
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
                params.put("pkstd_borrow",sp.getString("pimerykey","") );
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

}
