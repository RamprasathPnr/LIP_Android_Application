package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Model.GrievanceCategoryDto;

import java.util.List;

/**
 * Created by user1 on 11/6/16.
 */
public class DepartmentListAdapter extends BaseAdapter {
    Context ct;

    List<GrievanceCategoryDto> menuList;
    private LayoutInflater mInflater;


    public DepartmentListAdapter(Context context, List<GrievanceCategoryDto> orders) {
        ct = context;
        menuList = orders;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return menuList.size();
    }

    public GrievanceCategoryDto getItem(int position) {

        return menuList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.department_item, null);
            holder = new ViewHolder();
            holder.header_tv = (TextView) view.findViewById(R.id.header);
            holder.sub_header_tv = (TextView) view.findViewById(R.id.subheader);
            holder.singleText_tv = (TextView) view.findViewById(R.id.textView6);
            try
            {
                holder.header_tv.setText(menuList.get(position).getName());
                holder.sub_header_tv.setText(menuList.get(position).getDescription());
                char firstLetter = menuList.get(position).getName().charAt(0);
                Log.e("firstLetter",""+firstLetter);
                holder.singleText_tv.setText(""+firstLetter);
                holder.singleText_tv.setAllCaps(true);

            }catch(Exception e)
            {
                Log.e("DepartmentAdpter_Error",e.toString(),e);
            }


            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return view;


    }
    class ViewHolder {
        TextView header_tv, sub_header_tv, singleText_tv;

    }

}


