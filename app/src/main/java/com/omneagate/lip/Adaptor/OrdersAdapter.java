package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Activity.OrdersHistoryActivity;
import com.omneagate.lip.Model.OrderHistoryDto;

import java.text.ParseException;
import java.util.List;

/**
 * Created by user on 8/7/16.
 */
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderHolder> {

    private Context context;
    private List<OrderHistoryDto> orders_history_dto;
    private LayoutInflater mLayoutInflater;

    public OrdersAdapter(Context context, List<OrderHistoryDto> orders_history_dto) {
        this.context = context;
        this.orders_history_dto = orders_history_dto;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                mLayoutInflater.inflate(R.layout.item_my_orders, parent, false);

        return new OrderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, final int position) {

        holder.order_id.setText(orders_history_dto.get(position).getOrderNo());
        holder.order_placed.setText(orders_history_dto.get(position).getMobileOrderDate());
        holder.order_items.setText(orders_history_dto.get(position).getQuantity());

        String status=orders_history_dto.get(position).getStatus();
        if(status.equalsIgnoreCase("ORDERED"))
        {
            holder.order_status.setBackground(context.getResources().getDrawable(R.drawable.ic_ordered));
        }else if(status.equalsIgnoreCase("SHIPPED"))
        {
            holder.order_status.setBackground(context.getResources().getDrawable(R.drawable.ic_shipped));
        }else if(status.equalsIgnoreCase("DELIVERED"))
        {
            holder.order_status.setBackground(context.getResources().getDrawable(R.drawable.ic_delivered));
        }else if(status.equalsIgnoreCase("CANCELED"))
        {
            holder.order_status.setBackground(context.getResources().getDrawable(R.drawable.ic_declined));
        }

        holder.order_amount.setText("₹"+orders_history_dto.get(position).getTotalAmount());

        holder.ordered_detail_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderId = orders_history_dto.get(position).getId();
                ((OrdersHistoryActivity)context).get_ordered_details(orderId);

            }
        });

    }

    @Override
    public int getItemCount() {
        return orders_history_dto.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        TextView order_id;
        TextView order_placed;
        TextView order_items;
        TextView order_amount;
        ImageView order_status;
        LinearLayout ordered_detail_lay;

        public OrderHolder(View v) {
            super(v);

            order_id = (TextView) v.findViewById(R.id.text_view_order_id);
            order_placed = (TextView) v.findViewById(R.id.text_view_order_placed);
            order_items = (TextView) v.findViewById(R.id.text_view_order_items);
            order_amount = (TextView) v.findViewById(R.id.text_view_order_amount);
            order_status = (ImageView) v.findViewById(R.id.text_view_order_status);
            ordered_detail_lay = (LinearLayout) v.findViewById(R.id.ordered_detail_lay);


        }
    }
}
