package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Activity.ShoppingActivity;
import com.omneagate.lip.Model.ProductDetails;
import com.omneagate.lip.Service.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by USER1 on 12-07-2016.
 */
public class ShoppingAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private String selectedLanguage;

    private List<ProductDetails> studentList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private Context context;
    private OnLoadMoreListener onLoadMoreListener;



    public ShoppingAdapter(Context context, List<ProductDetails> newsDtoList,
                           String selectedLanguage, RecyclerView recyclerView) {
        this.context = context;
        this.studentList = newsDtoList;
        this.selectedLanguage = selectedLanguage;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return studentList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_shopping_item, parent, false);

            vh = new StudentViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.load_more_progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StudentViewHolder) {

            try {

                TextView headind_product = ((StudentViewHolder) holder).product_header_tv;
                TextView discount = ((StudentViewHolder) holder).discount_tv;
                TextView product_price_tv = ((StudentViewHolder) holder).product_price_tv;
                ImageView product_img = ((StudentViewHolder) holder).cart_image;
                Button cart_btn = ((StudentViewHolder) holder).cart_btn;

                discount.setText(studentList.get(position).getDiscount()+"% Discount");
                product_price_tv.setText("₹"+studentList.get(position).getCost());


                if (selectedLanguage.equalsIgnoreCase("ta")) {
                    headind_product.setText(studentList.get(position).getName());
                } else {
                    headind_product.setText(studentList.get(position).getName());
                }

                Picasso.with(context)
                        .load(studentList.get(position).getImageUrl())
                        .placeholder(R.drawable.no_available_image_150x150)
                        .into(product_img);

                cart_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ShoppingActivity) context).addToCart(studentList.get(position).getId(), studentList.get(position).getType());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView product_header_tv;
        TextView discount_tv;
        ImageView cart_image;
        TextView product_price_tv;
        Button cart_btn;

        public StudentViewHolder(View itemView) {
            super(itemView);
            this.product_header_tv = (TextView) itemView.findViewById(R.id.product_header_tv);
            this.discount_tv = (TextView) itemView.findViewById(R.id.discount_tv);
            this.cart_image = (ImageView) itemView.findViewById(R.id.iv_cart_image);
            this.product_price_tv = (TextView) itemView.findViewById(R.id.product_price_tv);
            this.cart_btn = (Button) itemView.findViewById(R.id.cart_btn);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }
}