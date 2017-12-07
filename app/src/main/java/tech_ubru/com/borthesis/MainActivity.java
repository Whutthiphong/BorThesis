package tech_ubru.com.borthesis;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.Map;

import tech_ubru.com.borthesis.ModelItem.GET_ALL_BOOK;

public class MainActivity extends AppCompatActivity {
    TextView tv_response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String service_name = "get_all_book_detail.php";
        final ProgressDialog dialog ;
        tv_response = (TextView) findViewById(R.id.tv_response);
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,URLService.getUrl()+service_name, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { 
                dialog.dismiss();
                tv_response.setText(response.toString());
                Log.e("GET_ALL_BOOK_res" ,response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("GET_ALL_BOOK" ,error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


    }
    private  class test_volle extends AsyncTask<Void,Void,String>{
        ProgressDialog dialog ;
        private String response_new;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            ArrayList<GET_ALL_BOOK> list_book = new JsonConverter<GET_ALL_BOOK>().toArrayList(response,GET_ALL_BOOK.class);

            dialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... voids) {

            return response_new;
        }
    }


}
