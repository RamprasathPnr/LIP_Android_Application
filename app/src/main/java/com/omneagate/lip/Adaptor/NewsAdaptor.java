package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lip.R;
import com.omneagate.lip.Model.NewsDto;
import com.omneagate.lip.Service.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by USER1 on 21-06-2016.
 */
public class NewsAdaptor extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private String selectedLanguage;

    private List<NewsDto> studentList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private Context context;
    private OnLoadMoreListener onLoadMoreListener;



    public NewsAdaptor(Context context, List<NewsDto> newsDtoList,
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
                    R.layout.adapter_view_latest_news, parent, false);

            vh = new StudentViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.load_more_progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentViewHolder) {

            try {
                TextView headind_news = ((StudentViewHolder) holder).heading_news;
                TextView news_description = ((StudentViewHolder) holder).news_description;
                TextView date = ((StudentViewHolder) holder).date;
                ImageView news_img = ((StudentViewHolder) holder).news_img;

                if (selectedLanguage.equalsIgnoreCase("ta")) {
                    headind_news.setText(studentList.get(position).getRegionalTitle());
                    news_description.setText(studentList.get(position).getRegionalShortDescription());
                } else {
                    headind_news.setText(studentList.get(position).getTitle());
                    news_description.setText(studentList.get(position).getShortDescription());
                }

                //news_img.setImageResource(img[position]);
                date.setText(studentList.get(position).getCreatedDateString());
                Picasso.with(context)
                        .load(studentList.get(position).getImageUrl())
                        .placeholder(R.drawable.no_available_image_150x150)
                        .into(news_img);
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


    //
    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView heading_news;
        TextView news_description;
        ImageView news_img;
        TextView date;

        public StudentViewHolder(View itemView) {
            super(itemView);
            this.heading_news = (TextView) itemView.findViewById(R.id.news_heading);
            this.news_description = (TextView) itemView.findViewById(R.id.news_description);
            this.news_img = (ImageView) itemView.findViewById(R.id.news_img);
            this.date = (TextView) itemView.findViewById(R.id.news_date);
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