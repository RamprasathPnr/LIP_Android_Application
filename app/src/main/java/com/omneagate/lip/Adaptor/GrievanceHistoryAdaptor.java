package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Model.GrievanceHistoryDto;

import java.util.List;

/**
 * Created by user1 on 3/6/16.
 */
public class GrievanceHistoryAdaptor extends BaseAdapter {
    Context ct;

    List<GrievanceHistoryDto> menuList;
    private LayoutInflater mInflater;


    public GrievanceHistoryAdaptor(Context context, List<GrievanceHistoryDto> orders) {
        ct = context;
        menuList = orders;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return menuList.size();
    }

    public GrievanceHistoryDto getItem(int position) {

        return menuList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.grievance_history, null);
            holder = new ViewHolder();
            holder.grievance_id_tv = (TextView) view.findViewById(R.id.grievance_id_tv);
            holder.subject_tv = (TextView) view.findViewById(R.id.subject_tv);
            holder.status_tv = (TextView) view.findViewById(R.id.status_tv);
            holder.posted_date_tv = (TextView) view.findViewById(R.id.posted_date_tv);
            holder.status_img = (ImageView) view.findViewById(R.id.status_img);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (menuList.get(position).getProgressStatus().equalsIgnoreCase("NEW") || menuList.get(position).getProgressStatus().equalsIgnoreCase("REOPEN")) {
            holder.status_img.setBackground(ct.getResources().getDrawable(R.drawable.ic_pending));
        } else if (menuList.get(position).getProgressStatus().equalsIgnoreCase("INPROGRESS")) {
            holder.status_img.setBackground(ct.getResources().getDrawable(R.drawable.ic_inprogress));
        } else if (menuList.get(position).getProgressStatus().equalsIgnoreCase("COMPLETED")) {
            holder.status_img.setBackground(ct.getResources().getDrawable(R.drawable.ic_resolve));
        } else if (menuList.get(position).getProgressStatus().equalsIgnoreCase("CLOSED")) {
            holder.status_img.setBackground(ct.getResources().getDrawable(R.drawable.ic_reject));
        }
        holder.grievance_id_tv.setText(menuList.get(position).getComplaintNumber());
        holder.subject_tv.setText(menuList.get(position).getDescription());
        //holder.status_tv.setText(menuList.get(position).getProgressStatus());
        holder.posted_date_tv.setText(menuList.get(position).getStringPostedDate());

        String status = menuList.get(position).getProgressStatus();

        switch (status) {

            case "NEW":
                status = "Pending";
                break;
            case "COMPLETED":
                status = "Inprogress";
                break;
            case "INPROGRESS":
                status = "Inprogress";
                break;
            case "CLOSED":
                status = "Resolved";
                break;
            default:
                status = "Inprogress";
                break;

        }

        holder.status_tv.setText(status);
        return view;

//        REQUEST, CLOSED, NEW,
    }

    class ViewHolder {
        TextView grievance_id_tv, subject_tv, status_tv, posted_date_tv;
        ImageView status_img;
    }

}
