package tech_ubru.com.borthesis.DataAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tech_ubru.com.borthesis.ModelItem.BorrowItem;
import tech_ubru.com.borthesis.R;

/**
 * Created by wutthiphong.thu on 8/12/2560.
 */

public class BorrowThesisAdapter extends RecyclerView.Adapter<BorrowThesisAdapter.ViewHolder> {

    Context context;
    ArrayList<BorrowItem> borrowItems_list;
    private String txt_status_return;
    private String txt_status_borrow;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        BorrowItem item = borrowItems_list.get(position);
        holder.tv_thesis_name_borow.setText(item.th_name);
        holder.tv_date_borrow.setText(item.date_borrow);
        String status_bor = item.status_borrow;
        String status_ret = item.status_return;
        if(status_bor!=null&&status_bor.equalsIgnoreCase("1")) {

            holder.tv_status_borrow.setTextColor(Color.parseColor("#75be51"));
            txt_status_borrow = "อนุมัต";
        }else{
            holder.tv_status_borrow.setTextColor(Color.parseColor("#ffb41e"));
            txt_status_borrow = "รอการอนุมัต";
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
    }

    @Override
    public int getItemCount() {
        if(borrowItems_list!=null)
         return borrowItems_list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_thesis_name_borow ,tv_date_borrow,tv_status_return,tv_status_borrow;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_thesis_name_borow = itemView.findViewById(R.id.tv_thesis_name_borow);
            tv_date_borrow = itemView.findViewById(R.id.tv_date_borrow);
            tv_status_return = itemView.findViewById(R.id.tv_status_return);
            tv_status_borrow = itemView.findViewById(R.id.tv_status_borrow);
        }
    }
}
