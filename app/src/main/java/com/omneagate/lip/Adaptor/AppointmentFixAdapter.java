package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Activity.AppointmentFixActivity;
import com.omneagate.lip.Model.ResponseDto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AppointmentFixAdapter extends BaseAdapter {
    int mSelectedMonth, mSelectedYear;
    Context mContext;
    LayoutInflater inflater;
    ViewHolder holder;
    AppointmentFixAdapter mCalendaradapter;
    public static String currentDay;
    public static ArrayList<String> mCalendarDates = new ArrayList<String>();
    public static ArrayList<String> mDateString = new ArrayList<String>();
    int pos_;
    ResponseDto daystime;

    public AppointmentFixAdapter(Context context, int month, int year, int pos, ResponseDto days) {
        // TODO Auto-generated constructor stub
        Calendar c = Calendar.getInstance();
        currentDay = "" + (c.get(Calendar.DAY_OF_MONTH)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR);
        Log.d("Date=>", "" + currentDay);
        this.mContext = context;
        this.mSelectedMonth = month;
        this.mSelectedYear = year;
        this.pos_ = pos;
        this.daystime = days;
        mCalendaradapter = new AppointmentFixAdapter(mContext);
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        refreshDays(mSelectedMonth, mSelectedYear);
    }

    public AppointmentFixAdapter(Context mContext2) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext2;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mCalendarDates.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mCalendarDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class ViewHolder {
        public TextView txtDate;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = convertView;
        if (rowView == null) {

            rowView = inflater
                    .inflate(R.layout.row_calendar, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtDate = (TextView) rowView
                    .findViewById(R.id.text_date);
            viewHolder.txtDate.setText(mCalendarDates.get(position));

            viewHolder.txtDate.setTag(mDateString.get(position));
//			MainActivity.mApplication.setDroidSansNormalTypeface(viewHolder.txtDate);


//            viewHolder.txtDate.setBackgroundResource(R.drawable.shape_circle_calendar_selected_date);
//            AppointmentFixActivity.PreviousTextView=viewHolder.txtDate;

            if(daystime.getListAvailableDay()!=null){
            for(int i=0;i<daystime.getListAvailableDay().size();i++) {
                if (daystime.getListAvailableDay().get(i).getLongDate() != null) {
                    long millisecond = Long.parseLong(daystime.getListAvailableDay().get(i).getLongDate());
                    String chekCurrentDate_ = DateFormat.format("d-M-yyyy", new Date(millisecond)).toString();
                    if (chekCurrentDate_.equalsIgnoreCase(mDateString.get(position))) {
                        AppointmentFixActivity.position_.add(String.valueOf(position));
                        viewHolder.txtDate.setTextColor(Color.parseColor("#FFFFFF"));
                        viewHolder.txtDate.setBackgroundResource(R.drawable.shape_circle_calendar_selected_date);
                    }
                }
            }

                if(pos_ == position){
                    viewHolder.txtDate.setTextColor(Color.parseColor("#FFFFFF"));
                    viewHolder.txtDate.setBackgroundResource(R.drawable.freeday_select_bg);
                }
            }

           /* if (currentDay.equals(mDateString.get(position))) {

                viewHolder.txtDate.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.txtDate.setBackgroundResource(R.drawable.shape_circle_calendar_selected_date);
                CalendarDialog.PreviousTextView=viewHolder.txtDate;
            }else {
                viewHolder.txtDate.setBackgroundColor(Color.TRANSPARENT);
            }*/

        }

        //	holder = (ViewHolder) rowView.getTag();

        return rowView;
    }

    public void refreshDays(int mMonth, int mYear) {
        int startDay = getStartDay(mYear, mMonth);
        Log.i("startDay", "" + startDay);
        int numberOfDaysInMonth = getNumberOfDaysInMonth(mYear, mMonth);
        mCalendarDates.clear();
        mDateString.clear();
        for (int i = 0; i < startDay; i++) {
            mCalendarDates.add("");
        }
        for (int i = 1; i <= numberOfDaysInMonth; i++) {
            mCalendarDates.add("" + i);
        }
        for (int i = 0; i < mCalendarDates.size(); i++) {
            if (mCalendarDates.get(i).equals("")) {
                mDateString.add("");
            } else {
                mDateString.add(mCalendarDates.get(i) + "-" + mMonth + "-" + mYear);
            }
        }
        mCalendaradapter.notifyDataSetChanged();
        mCalendaradapter.notifyDataSetInvalidated();
    }

    /**
     * Get the start day of the first day in a month
     */

    public int getStartDay(int year, int month) {
        // Get total number of days since 1/1/1800
        int startDay1800 = 3;
        int totalNumberOfDays = getTotalNumberOfDays(year, month);
        // Return the start day
        return (totalNumberOfDays + startDay1800) % 7;
    }

    /**
     * Get the total number of days since January 1, 1800
     */
    public int getTotalNumberOfDays(int year, int month) {
        int total = 0;
        // Get the total days from 1800 to year - 1
        for (int i = 1800; i < year; i++)
            if (isLeapYear(i))
                total = total + 366;

            else
                total = total + 365;

        // Add days from Jan to the month prior to the calendar month
        for (int i = 1; i < month; i++)
            total = total + getNumberOfDaysInMonth(year, i);

        return total;

    }

    /**
     * Get the number of days in a month
     */

    public int getNumberOfDaysInMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 ||
                month == 8 || month == 10 || month == 12)
            return 31;

        if (month == 4 || month == 6 || month == 9 || month == 11)
            return 30;

        if (month == 2) return isLeapYear(year) ? 29 : 28;
        return 0; // If month is incorrect
    }

    /**
     * Determine if it is a leap year
     */
    public boolean isLeapYear(int year) {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }
}

