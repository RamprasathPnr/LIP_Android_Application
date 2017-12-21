package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Model.PolicyDto;

import java.util.List;

/**
 * Created by user1 on 11/6/16.
 */
public class PoliciesListAdapter extends BaseAdapter {
    Context ct;
    List<PolicyDto> menuList;
    private LayoutInflater mInflater;


    public PoliciesListAdapter(Context context, List<PolicyDto> orders) {
        ct = context;
        menuList = orders;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return menuList.size();
    }

    public PolicyDto getItem(int position) {

        return menuList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.policiesitem, null);
            holder = new ViewHolder();
            holder.header_tv = (TextView) view.findViewById(R.id.header);
            holder.sub_header_tv = (TextView) view.findViewById(R.id.subheader);
            holder.singleText_tv = (TextView) view.findViewById(R.id.textView6);
            holder.listimages = (ImageView) view.findViewById(R.id.iconid);
            holder.header_tv.setText(menuList.get(position).getType());
            Log.e("description",menuList.get(position).getDescription().toString());
            holder.sub_header_tv.setText(""+menuList.get(position).getDescription());
            try
            {
                holder.header_tv.setText(menuList.get(position).getType());
                Log.e("description",menuList.get(position).getDescription().toString());
                holder.sub_header_tv.setText(""+menuList.get(position).getDescription());
                if(menuList.get(position).getType().equals("VISION"))
                {
                    holder.listimages.setBackgroundResource(R.drawable.vision);

                }else if(menuList.get(position).getType().equals("VALUES"))
                {
                    holder.listimages.setBackgroundResource(R.drawable.value);

                }else if(menuList.get(position).getType().equals("MONITORING"))
                {
                    holder.listimages.setBackgroundResource(R.drawable.monitoring);

                }
                else if(menuList.get(position).getType().equals("STANDARDS"))
                {
                    holder.listimages.setBackgroundResource(R.drawable.standard);

                }else if(menuList.get(position).getType().equals("MISSION"))
                {
                    holder.listimages.setBackgroundResource(R.drawable.mission);

                }
            }catch(Exception e)
            {
                Log.e("PoliciesAdpter_Error",e.toString(),e);
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return view;


    }
    class ViewHolder {
        TextView header_tv, sub_header_tv, singleText_tv;
        ImageView listimages;

    }

}



