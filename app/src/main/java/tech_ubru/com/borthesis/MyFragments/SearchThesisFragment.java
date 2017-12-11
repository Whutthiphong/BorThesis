package tech_ubru.com.borthesis.MyFragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.Map;

import tech_ubru.com.borthesis.AppConfig.URLService;
import tech_ubru.com.borthesis.DataAdapter.ThesisAdapter;
import tech_ubru.com.borthesis.MainActivity;
import tech_ubru.com.borthesis.ModelItem.GET_ALL_BOOK;
import tech_ubru.com.borthesis.MySingleton;
import tech_ubru.com.borthesis.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchThesisFragment extends Fragment {


    String service_name = "get_all_book_detail.php";
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
                adapter.getFilter().filter(newText.trim());
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_search){
            //Do whatever you want to do

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
