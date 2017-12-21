package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Model.SurveyOptionDto;
import com.omneagate.lip.Service.SurveyAnswerListener;

import java.util.List;

/**
 * Created by USER1 on 10-06-2016.
 */
public class SurveyAnswerAdapter extends RecyclerView.Adapter<SurveyAnswerAdapter.AnswerHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SurveyOptionDto> surveyOptionList;
    private boolean optionType;
    private SurveyAnswerListener listener;
    private int selectPosition = -1;
    private int alphabet = 64;
    private String selectedLanguage;
    private boolean isIncrement;

    public SurveyAnswerAdapter(Context context, List<SurveyOptionDto> surveyOptionList, boolean optionType,
                               SurveyAnswerListener listener, String selectedLanguage) {

        this.mContext = context;
        this.surveyOptionList = surveyOptionList;
        this.optionType = optionType;
        this.listener = listener;
        this.selectedLanguage = selectedLanguage;
        this.isIncrement = true;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public AnswerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.survey_answer_row, parent, false);
        return new AnswerHolder(v);
    }

    @Override
    public void onBindViewHolder(final AnswerHolder holder, final int position) {

        if (selectedLanguage.equalsIgnoreCase("ta"))
            holder.mTvAnswer.setText(surveyOptionList.get(position).getRegionalOptionValue());
        else holder.mTvAnswer.setText(surveyOptionList.get(position).getOptionValue());

        if (isIncrement) {
            alphabet = alphabet + 1;
            holder.mTvQuestionNo.setText("" + (char) alphabet);
        }

        if (optionType) {
            holder.mCheckBox.setVisibility(View.VISIBLE);
            holder.mRbutton.setVisibility(View.GONE);
        } else {
            holder.mRbutton.setChecked(false);
            if (position == selectPosition) {
                holder.mRbutton.setChecked(true);
            }
            holder.mRbutton.setVisibility(View.VISIBLE);
            holder.mCheckBox.setVisibility(View.GONE);
        }

        holder.mCVRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionType) {
                    if (holder.mCheckBox.isChecked()) {
                        holder.mCheckBox.setChecked(false);
                    } else {
                        holder.mCheckBox.setChecked(true);
                    }

                    listener.selectAnswer(position,
                            surveyOptionList.get(position).getId(), holder.mCheckBox.isChecked());
                } else {
                    if (holder.mRbutton.isChecked()) {
                        holder.mRbutton.setChecked(false);
                        isIncrement = false;
                        notifyDataSetChanged();
                    } else {
                        selectPosition = position;
                        holder.mRbutton.setChecked(true);
                        isIncrement = false;
                        notifyDataSetChanged();
                    }
                    listener.selectAnswer(position,
                            surveyOptionList.get(position).getId(), holder.mRbutton.isChecked());
                }
            }
        });

        holder.mRbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mRbutton.isChecked()) {
                    holder.mRbutton.setChecked(false);
                    isIncrement = false;
                    notifyDataSetChanged();
                } else {
                    selectPosition = position;
                    holder.mRbutton.setChecked(true);
                    isIncrement = false;
                    notifyDataSetChanged();
                }
            }
        });

        holder.
                mCheckBox.
                setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                   listener.selectAnswer(position,
                                                           surveyOptionList.get(position).getId(), isChecked);
                                               }
                                           }
                );
    }

    @Override
    public int getItemCount() {
        return surveyOptionList.size();
    }

    public class AnswerHolder extends RecyclerView.ViewHolder {

        private TextView mTvQuestionNo;
        private TextView mTvAnswer;
        private CheckBox mCheckBox;
        private RadioButton mRbutton;
        private CardView mCVRow;

        public AnswerHolder(View v) {
            super(v);

            mTvQuestionNo = (TextView) v.findViewById(R.id.txt_question_no);
            mTvAnswer = (TextView) v.findViewById(R.id.txt_answer);
            mCVRow = (CardView) v.findViewById(R.id.answer_row_card_view);
            mCheckBox = (CheckBox) v.findViewById(R.id.cb_answer);
            mRbutton = (RadioButton) v.findViewById(R.id.rb_answer);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
