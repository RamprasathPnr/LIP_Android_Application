package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Model.GrievanceCategoryDto;

import java.util.List;

/**
 * Created by USER1 on 30-06-2016.
 */
public class SingleStringAdaptor extends BaseAdapter {
    Context ct;
    String language;

    List<String> menuList;
    private LayoutInflater mInflater;


    public SingleStringAdaptor(Context context, List<String > orders, String selectedLanguage) {
        ct = context;
        menuList = orders;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        language = selectedLanguage;

    }

    public int getCount() {
        return menuList.size();
    }

    public String getItem(int position) {

        return menuList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.spinner_adaptor, null);
            holder = new ViewHolder();
            holder.number = (TextView) view.findViewById(R.id.spinner_tv);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (language.equalsIgnoreCase("en")) {
            holder.number.setText(menuList.get(position));
        } else if (language.equalsIgnoreCase("ta")) {

            holder.number.setText(menuList.get(position));

        }


        return view;
    }

    class ViewHolder {
        TextView number;

    }

}
