package tech_ubru.com.borthesis.MyFragments;


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

import tech_ubru.com.borthesis.DataAdapter.BorrowThesisAdapter;
import tech_ubru.com.borthesis.MainActivity;
import tech_ubru.com.borthesis.ModelItem.BorrowItem;
import tech_ubru.com.borthesis.MySingleton;
import tech_ubru.com.borthesis.R;
import tech_ubru.com.borthesis.URLService;

/**
 * A simple {@link Fragment} subclass.
 */
public class BorrowThesisTransectionFragment extends Fragment {


    String service_name = "get_borrow_thesis.php";
    private ArrayList<BorrowItem> list;
    private BorrowThesisAdapter adapter;
    private RecyclerView rv_borrow;

    public BorrowThesisTransectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rootView =inflater.inflate(R.layout.fragment_borrow_thesis_transection, container, false);
                MainActivity activity = (MainActivity) getActivity();
        activity.getSupportActionBar().show();
        rv_borrow = rootView.findViewById(R.id.rv_borrow);
        UpdateData();
        return rootView;
    }

    private void UpdateData(){

        StringRequest request = new StringRequest(Request.Method.POST, URLService.getUrl()+service_name, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response.toString());
                list = new JsonConverter<BorrowItem>().toArrayList(response.toString(),BorrowItem.class);
                adapter = new BorrowThesisAdapter(getContext(),list);
                rv_borrow.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_borrow.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }


        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pkstd_borrow","123" );
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

}
