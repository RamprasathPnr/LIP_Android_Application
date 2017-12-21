package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Model.EventDto;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by user on 26/5/16.
 */
public class SchemesAdapter extends RecyclerView.Adapter<SchemesAdapter.MyViewHolder> {


    Context context;

    List<EventDto> eventlist;
    private String selectedLanguage;


    public SchemesAdapter(Context context, List<EventDto> eventlist, String selectedLanguage) {
        this.context = context;
        this.eventlist = eventlist;
        this.selectedLanguage = selectedLanguage;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView headind_news;
        TextView news_description;
        TextView tv_Month, tv_date;
        ImageView news_img;
        LinearLayout ly_date_time;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.headind_news = (TextView) itemView.findViewById(R.id.news_heading);
            this.news_description = (TextView) itemView.findViewById(R.id.news_description);
            this.news_img = (ImageView) itemView.findViewById(R.id.news_img);
            this.tv_Month = (TextView) itemView.findViewById(R.id.month);
            this.tv_date = (TextView) itemView.findViewById(R.id.date);
            this.ly_date_time = (LinearLayout) itemView.findViewById(R.id.dat_lay);
            ly_date_time.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public SchemesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_latest_news, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(SchemesAdapter.MyViewHolder holder, int position) {

        try {

            TextView headind_news = holder.headind_news;
            TextView news_description = holder.news_description;
            ImageView news_img = holder.news_img;
            TextView Month = holder.tv_Month;
            TextView date = holder.tv_date;

            if(selectedLanguage.equalsIgnoreCase("ta")) {
                headind_news.setText(eventlist.get(position).getRegionalTitle());
                news_description.setText(eventlist.get(position).getRegionalShortDescription());
            } else{
                headind_news.setText(eventlist.get(position).getTitle());
                news_description.setText(eventlist.get(position).getShortDescription());
            }

            String month_date = eventlist.get(position).getDay();
            String[] separeted = month_date.split(" ");
            Month.setText(separeted[0]);
            date.setText(separeted[1]);
            // news_img.setImageResource();
            Picasso.with(context)
                    .load(eventlist.get(position).getImageUrl())
                    .placeholder(R.drawable.no_available_image_150x150)
                    .into(news_img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return eventlist.size();
    }
}


