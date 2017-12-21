package com.omneagate.lip.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Activity.GroupSurveyQuestionActivity;
import com.omneagate.lip.Activity.SurveyActivity;
import com.omneagate.lip.Model.GroupSurveyListDto;
import com.omneagate.lip.Model.SurveyOptionDto;

import java.util.List;

/**
 * Created by USER1 on 17-06-2016.
 */
public class GroupSurveyListAdaptor extends RecyclerView.Adapter<GroupSurveyListAdaptor.AnswerHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<GroupSurveyListDto> surveyOptionList;
    private String selectedLanguage;

    public GroupSurveyListAdaptor(Context context, List<GroupSurveyListDto> surveyOptionList,
                                  String selectedLanguage) {
        this.mContext = context;
        this.surveyOptionList = surveyOptionList;
        this.selectedLanguage = selectedLanguage;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public AnswerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.group_list_adaptor, parent, false);
        return new AnswerHolder(v);
    }

    @Override
    public void onBindViewHolder(final AnswerHolder holder, final int position) {

        if (selectedLanguage.equalsIgnoreCase("ta"))
            holder.group_name_tv.setText(surveyOptionList.get(position).getRegionalGroupName());
        else holder.group_name_tv.setText(surveyOptionList.get(position).getGroupName());

        holder.start_survey_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupSurveyQuestionActivity.group_id = surveyOptionList.get(position).getId();
                Intent i = new Intent(mContext, SurveyActivity.class);
                mContext.startActivity(i);
                ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return surveyOptionList.size();
    }

    public class AnswerHolder extends RecyclerView.ViewHolder {

        private TextView group_name_tv;
        private Button start_survey_btn;
//        private CardView mCVRow;


        public AnswerHolder(View v) {
            super(v);
            group_name_tv = (TextView) v.findViewById(R.id.group_name_tv);
            start_survey_btn = (Button) v.findViewById(R.id.start_survey_btn);
//            mCVRow = (CardView) v.findViewById(R.id.answer_row_card_view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}

