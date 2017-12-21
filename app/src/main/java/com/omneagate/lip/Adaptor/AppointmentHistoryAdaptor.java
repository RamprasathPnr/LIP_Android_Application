package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Model.AppointmentListDto;

import java.util.List;

/**
 * Created by user1 on 6/6/16.
 */
public class AppointmentHistoryAdaptor extends BaseAdapter {
    Context ct;

    List<AppointmentListDto> menuList;
    private LayoutInflater mInflater;


    public AppointmentHistoryAdaptor(Context context, List<AppointmentListDto> orders) {
        ct = context;
        menuList = orders;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return menuList.size();
    }

    public AppointmentListDto getItem(int position) {

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
            holder.status_img = (ImageView)view.findViewById(R.id.status_img);
            holder.textView = (TextView) view.findViewById(R.id.textView);
            holder.textView7 = (TextView)view.findViewById(R.id.textView7);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(menuList.get(position).getStatus().equalsIgnoreCase("REQUEST")){
            holder.status_img.setBackground( ct.getResources().getDrawable(R.drawable.ic_pending));
        }else if(menuList.get(position).getStatus().equalsIgnoreCase("VERIFIED")){
            holder.status_img.setBackground( ct.getResources().getDrawable(R.drawable.ic_inprogress));
        }else if(menuList.get(position).getStatus().equalsIgnoreCase("APPROVED")){
            holder.status_img.setBackground( ct.getResources().getDrawable(R.drawable.ic_resolve));
        }else if(menuList.get(position).getStatus().equalsIgnoreCase("CANCEL")){
            holder.status_img.setBackground( ct.getResources().getDrawable(R.drawable.ic_reject));
        }
        holder.textView7.setText("Schedule On : ");
        holder.textView.setText("Appointment ID :");
        holder.grievance_id_tv.setText(menuList.get(position).getAppointmentNumber());
        holder.subject_tv.setText(menuList.get(position).getReason());
        holder.status_tv.setText(menuList.get(position).getStatus());
        holder.posted_date_tv.setText(menuList.get(position).getAppointmentDateShort());
        return view;

//        REQUEST, CLOSED, NEW,
    }
    class ViewHolder {
        TextView grievance_id_tv, subject_tv, status_tv,posted_date_tv, textView, textView7;
        ImageView status_img;
    }

}

