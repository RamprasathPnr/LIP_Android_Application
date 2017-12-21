package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.net.ParseException;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.user.lip.R;

import com.omneagate.lip.Activity.PayConformActivity;
import com.omneagate.lip.Model.ProductDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by user on 1/7/16.
 */
public class CartPayAdapter extends RecyclerView.Adapter<CartPayAdapter.CartPayAdapterHolder> {

    private Context context;
    private List<ProductDetails> cart_list_pdt;
    private String language;
    private LayoutInflater mLayoutInflater;
    private String initial_qnty;
    private String intial_amt;
    private String initial_qty;


    public CartPayAdapter(Context context, List<ProductDetails> cart_list_pdt, String language) {

        this.context = context;
        this.cart_list_pdt = cart_list_pdt;
        this.language = language;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public CartPayAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView =
                mLayoutInflater.inflate(R.layout.item_pament_confirm_adapter, parent, false);

        return new CartPayAdapterHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final CartPayAdapterHolder holder, final int position) {


        try {


            initial_qnty = cart_list_pdt.get(position).getQuantity();
            intial_amt = cart_list_pdt.get(position).getCost();

            holder.qty.setText(initial_qnty);

            float total_amt = Float.valueOf(initial_qnty) * Float.valueOf(intial_amt);

            holder.text_view_product_amount.setText("₹ " + total_amt);
            holder.text_view_supplied.setText(cart_list_pdt.get(position).getSupplierName());
            holder.text_view_product_name.setText(cart_list_pdt.get(position).getName());

            ((PayConformActivity) context).findViewById(R.id.text_view_subtotal);

            Picasso.with(context)
                    .load(cart_list_pdt.get(position).getImageUrl())
                    .placeholder(R.drawable.no_available_image_150x150)
                    .into(holder.product_image);


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return cart_list_pdt.size();
    }

    public class CartPayAdapterHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView text_view_supplied;
        TextView text_view_product_name;
        TextView text_view_product_amount;
        TextView qty;

        ImageView plus;
        ImageView minus;

        public CartPayAdapterHolder(View v) {
            super(v);

            product_image = (ImageView) v.findViewById(R.id.image_product);
            text_view_supplied = (TextView) v.findViewById(R.id.text_view_supplied);
            text_view_product_name = (TextView) v.findViewById(R.id.text_view_product_name);
            text_view_product_amount = (TextView) v.findViewById(R.id.text_view_product_amount);
            qty = (TextView) v.findViewById(R.id.qty);


            plus = (ImageView) v.findViewById(R.id.plus);
            minus = (ImageView) v.findViewById(R.id.minus);

        }
    }


}
