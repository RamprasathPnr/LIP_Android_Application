package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Model.OrderedItemDetailsDto;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by user on 8/7/16.
 */
public class OrderItemsDetailsAdapter extends RecyclerView.Adapter<OrderItemsDetailsAdapter.OrderItemsDetailsHolder> {

    private Context context;
    private List<OrderedItemDetailsDto> orderItemList;
    private LayoutInflater mLayoutInflater;

    public OrderItemsDetailsAdapter(Context context, List<OrderedItemDetailsDto> customerOrderDetailsDto) {
        this.context = context;
        this.orderItemList = customerOrderDetailsDto;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public OrderItemsDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                mLayoutInflater.inflate(R.layout.item_ordered_details, parent, false);

        return new OrderItemsDetailsAdapter.OrderItemsDetailsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderItemsDetailsHolder holder, int position) {

        holder.text_view_code.setText(orderItemList.get(position).getItemCode());
        holder.text_view_product_name.setText(orderItemList.get(position).getItemName());
        holder.qty.setText(orderItemList.get(position).getQuantity());
        holder.text_view_product_amount.setText("₹"+orderItemList.get(position).getTotalAmount());
        holder.text_view_product_status.setText(orderItemList.get(position).getStatus());
        Picasso.with(context)
                .load(orderItemList.get(position).getItemImage())
                .placeholder(R.drawable.no_available_image_150x150)
                .into(holder.image_product);

    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    class OrderItemsDetailsHolder extends RecyclerView.ViewHolder {

        ImageView image_product;
        TextView text_view_code;
        TextView text_view_product_name;
        TextView qty;
        TextView text_view_product_amount;
        TextView text_view_product_status;

        public OrderItemsDetailsHolder(View v) {
            super(v);

            image_product = (ImageView) v.findViewById(R.id.image_product);
            text_view_code = (TextView) v.findViewById(R.id.text_view_code);
            text_view_product_name = (TextView) v.findViewById(R.id.text_view_product_name);

            qty = (TextView) v.findViewById(R.id.qty);
            text_view_product_amount = (TextView) v.findViewById(R.id.text_view_product_amount);
            text_view_product_status = (TextView) v.findViewById(R.id.text_view_product_status);

        }
    }
}
