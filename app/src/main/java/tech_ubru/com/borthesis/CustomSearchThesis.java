package tech_ubru.com.borthesis;

import android.content.Context;
import android.widget.Filter;

import java.util.ArrayList;

import tech_ubru.com.borthesis.DataAdapter.ThesisAdapter;
import tech_ubru.com.borthesis.ModelItem.GET_ALL_BOOK;

/**
 * Created by Wutthiphong on 12/10/2017.
 */

public class CustomSearchThesis extends Filter {
    Context context;
    ArrayList<GET_ALL_BOOK> list_data;

    ThesisAdapter adapter;
    public CustomSearchThesis(  ArrayList<GET_ALL_BOOK> list, ThesisAdapter adapter) {

        this.list_data = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if(constraint!=null && constraint.length()>0){
            constraint = constraint.toString().toLowerCase();
            ArrayList<GET_ALL_BOOK> list = new ArrayList<>();
            for (int i=0;i<list_data.size();i++){
                if(list_data.get(i).th_name.toLowerCase().contains(constraint)){
                    list.add(list_data.get(i));
                }


            }
            filterResults.count=list.size();
            filterResults.values = list;

        }else {

            filterResults.count = list_data.size();
            filterResults.values = list_data;
        }

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.get_all_books_list = (ArrayList<GET_ALL_BOOK>) filterResults.values;
        adapter.notifyDataSetChanged();
    }
}
