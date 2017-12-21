package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.net.ParseException;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Activity.CartActivity;
import com.omneagate.lip.Model.ProductDetails;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by user on 2/7/16.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> implements View.OnClickListener{

    private Context context;
    private List<ProductDetails> cart_list_pdt;
    private String language;
    private LayoutInflater mLayoutInflater;

    public CartAdapter(Context context, List<ProductDetails> cart_list_pdt, String language) {
        this.context = context;
        this.cart_list_pdt = cart_list_pdt;
        this.language = language;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =mLayoutInflater.inflate(R.layout.item_list_view_cart, parent, false);
        return new CartHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartHolder holder, final int position) {
        try {
            if(cart_list_pdt.get(position).getType().equalsIgnoreCase("SERVICE")){
                holder.plus_lay.setVisibility(View.GONE);
                holder.minus_lay.setVisibility(View.GONE);
            }
            String initial_qnty = cart_list_pdt.get(position).getQuantity();
            String intial_amt = cart_list_pdt.get(position).getCost();
            float total_amt=Float.valueOf(initial_qnty)*Float.valueOf(intial_amt);
            holder.text_view_product_amount.setText("₹ "+total_amt);
            holder.text_view_supplied.setText(cart_list_pdt.get(position).getSupplierName());
            holder.text_view_product_name.setText(cart_list_pdt.get(position).getName());
            String initial_qty = cart_list_pdt.get(position).getQuantity();
            cart_list_pdt.get(position).setQty(Integer.parseInt(initial_qty));
            holder.qty.setText(""+cart_list_pdt.get(position).getQuantity());
            int sendqty=cart_list_pdt.get(position).getQty();
            String finl_cost=holder.text_view_product_amount.getText().toString();

            ((CartActivity)context).send_Qty(sendqty);
            ((CartActivity)context).finalCost(finl_cost);
            ((CartActivity)context).findViewById(R.id.text_view_subtotal);

            Picasso.with(context)
                    .load(cart_list_pdt.get(position).getImageUrl())
                    .placeholder(R.drawable.no_available_image_150x150)
                    .into(holder.product_image);

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pdt_qty=CartActivity.cart_list_pdt.get(position).getQty()+1;
                    cart_list_pdt.get(position).setQty(pdt_qty);
                    CartActivity.cart_list_pdt.get(position).setQty(pdt_qty);
                    holder.qty.setText("" + pdt_qty);
                    int sedqty=cart_list_pdt.get(position).getQty();
                    int amtt_intent=cart_list_pdt.get(position).getQty();
                    ((CartActivity)context).send_Qty(sedqty);
                    String cost=cart_list_pdt.get(position).getCost();
                    String cost2=holder.text_view_product_amount.getText().toString();
                    float total=Float.valueOf(cost)+Float.valueOf(cost2);
                    holder.text_view_product_amount.setText(""+total);
                    ((CartActivity)context).finalCost(holder.text_view_product_amount.getText().toString());
                    ((CartActivity)context).total(cost);
                }
            });

            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int pdt_qty=CartActivity.cart_list_pdt.get(position).getQty();
                   if(pdt_qty <=1 )
                   {
                       holder.qty.setText("1");
                   }else{
                       int pdt_qty_=cart_list_pdt.get(position).getQty()-1;
                       CartActivity.cart_list_pdt.get(position).setQty(pdt_qty_);
                       cart_list_pdt.get(position).setQty(pdt_qty_);
                       holder.qty.setText("" + pdt_qty_);
                       String cost=cart_list_pdt.get(position).getCost();
                       String cost2=holder.text_view_product_amount.getText().toString();
                       float total=Float.valueOf(cost2)-Float.valueOf(cost);
                       holder.text_view_product_amount.setText(""+total);
                       ((CartActivity)context).subtract_total(cost);
                       ((CartActivity)context).finalCost(cost);
                   }
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cart_list_pdt.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_remove_item :
            break;
        }
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView text_view_supplied;
        TextView text_view_product_name;
        TextView text_view_product_amount;
        TextView qty;
        LinearLayout plus_lay, minus_lay;
        ImageView plus;
        ImageView minus;
        public CartHolder(View v) {
            super(v);
            product_image = (ImageView) v.findViewById(R.id.image_product);
            text_view_supplied = (TextView) v.findViewById(R.id.text_view_supplied);
            text_view_product_name = (TextView) v.findViewById(R.id.text_view_product_name);
            text_view_product_amount = (TextView) v.findViewById(R.id.text_view_product_amount);
            qty = (TextView) v.findViewById(R.id.qty);
            plus_lay = (LinearLayout) v.findViewById(R.id.plus_lay);
            minus_lay = (LinearLayout) v.findViewById(R.id.minus_lay);
            plus = (ImageView) v.findViewById(R.id.plus);
            minus = (ImageView) v.findViewById(R.id.minus);
        }
    }
}
