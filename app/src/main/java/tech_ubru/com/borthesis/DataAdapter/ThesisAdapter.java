package tech_ubru.com.borthesis.DataAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import tech_ubru.com.borthesis.CustomSearchThesis;
import tech_ubru.com.borthesis.Details.ThesisDetail;
import tech_ubru.com.borthesis.ModelItem.GET_ALL_BOOK;
import tech_ubru.com.borthesis.R;

/**
 * Created by wutthiphong.thu on 8/12/2560.
 */

public class ThesisAdapter extends RecyclerView.Adapter<ThesisAdapter.ViewHolder>  implements Filterable {
    Context context;
    public  ArrayList<GET_ALL_BOOK> get_all_books_list;
CustomSearchThesis customSearchThesis;
    public ThesisAdapter(Context context, ArrayList<GET_ALL_BOOK> get_all_books_list) {
        this.context = context;
        this.get_all_books_list = get_all_books_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thesis_item,null,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            final GET_ALL_BOOK item = get_all_books_list.get(position);
            holder.tv_thesis_name.setText(item.th_name);
            holder.tv_major.setText(item.major);
            String txt_status  ;
            if(item.status==null||item.status.equalsIgnoreCase("0")) {
                holder.tv_status.setTextColor(Color.parseColor("#75be51"));
                txt_status = "พร้อมใช้งาน";
            }else {

                holder.tv_status.setTextColor(Color.parseColor("#ff5c2a"));
                txt_status = "ไม่พร้อมใช้งาน";
            }
            holder.tv_status.setText(txt_status);
        holder.cons_thesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ThesisDetail.class);
                intent.putExtra("thesis", item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(get_all_books_list!=null)
            return  get_all_books_list.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        if(customSearchThesis == null){
            customSearchThesis = new CustomSearchThesis(get_all_books_list,this);
        }
        return customSearchThesis;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_thesis_name,tv_major,tv_status;
        ConstraintLayout cons_thesis;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_thesis_name = itemView.findViewById(R.id.tv_thesis_name);
            tv_major = itemView.findViewById(R.id.tv_detail_major);
            tv_status = itemView.findViewById(R.id.tv_status);
            cons_thesis = itemView.findViewById(R.id.cons_thesis);
        }
    }
}
