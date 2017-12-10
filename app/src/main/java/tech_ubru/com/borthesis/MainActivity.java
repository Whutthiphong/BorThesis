package tech_ubru.com.borthesis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;

import tech_ubru.com.borthesis.AppConfig.ConfigData;
import tech_ubru.com.borthesis.ModelItem.GET_ALL_BOOK;
import tech_ubru.com.borthesis.MyFragments.BorrowThesisTransectionFragment;
import tech_ubru.com.borthesis.MyFragments.MainAppFragment;
import tech_ubru.com.borthesis.MyFragments.SearchThesisFragment;
import tech_ubru.com.borthesis.MyFragments.UserSettingFragment;

public class MainActivity extends AppCompatActivity  {
    TextView tv_response;

    AHBottomNavigation bottomNavigation;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String service_name = "get_all_book_detail.php";
        bottomNavigation =  findViewById(R.id.myNavigation_ID);
        createNavItem();

        sp = getSharedPreferences(ConfigData.USER_TAG_SHARE, Context.MODE_PRIVATE);
        if(!sp.getString("LOGIN_STAT","").equalsIgnoreCase("success")){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
//
//        MainAppFragment home = new MainAppFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.content_id,home).commit();
//        final ProgressDialog dialog ;
//        dialog = new ProgressDialog(MainActivity.this);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage("Loading...");
//        dialog.show();

//        StringRequest request = new StringRequest(Request.Method.POST,URLService.getUrl()+service_name, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                dialog.dismiss();
//                tv_response.setText(response.toString());
//                Log.e("GET_ALL_BOOK_res" ,response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.e("GET_ALL_BOOK" ,error.toString());
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
//            }
//        };
//
//        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(position ==0){
                    MainAppFragment home = new MainAppFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_id,home).commit();
                    return  true;

                }else if(position==1){
                    SearchThesisFragment search = new SearchThesisFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_id,search).commit();
                    return  true;

                }else if(position==2){
                    BorrowThesisTransectionFragment borrow = new BorrowThesisTransectionFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_id,borrow).commit();
                    return  true;

                }else if(position==3){
                    UserSettingFragment user_setting = new UserSettingFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_id,user_setting).commit();
                    return  true;

                }
                return wasSelected;
            }
        });
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

    private void createNavItem(){
        //Create Item
        AHBottomNavigationItem home = new AHBottomNavigationItem(getResources().getString(R.string.bottombar_main), R.drawable.ic_account_balance_black_24dp);
        AHBottomNavigationItem search = new AHBottomNavigationItem(getResources().getString(R.string.bottombar_search), R.drawable.search);
        AHBottomNavigationItem trans = new AHBottomNavigationItem(getResources().getString(R.string.bottombar_trans), R.drawable.ic_format_list_bulleted_black_24dp);
        AHBottomNavigationItem settingItem = new AHBottomNavigationItem(getResources().getString(R.string.bottombar_setting), R.drawable.setting);

        //Add Item

        bottomNavigation.addItem(home);
        bottomNavigation.addItem(search);
        bottomNavigation.addItem(trans);
        bottomNavigation.addItem(settingItem);

        //Set currect Item
        bottomNavigation.setAccentColor(Color.parseColor("#04AEDA"));
        bottomNavigation.setCurrentItem(0);

        MainAppFragment home_fagment = new MainAppFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_id,home_fagment).commit();

    }

}
