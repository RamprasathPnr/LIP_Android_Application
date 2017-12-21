package com.omneagate.lip.Adaptor;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.user.lip.R;
import com.omneagate.lip.Activity.Filter_Activity;
import com.omneagate.lip.Activity.SellerList;
import com.omneagate.lip.Model.SupplierDto;
import com.omneagate.lip.Model.SurveyOptionDto;
import com.omneagate.lip.Service.SurveyAnswerListener;

import java.util.List;

/**
 * Created by USER1 on 04-07-2016.
 */
public class SellerAdaptor extends RecyclerView.Adapter<SellerAdaptor.AnswerHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SupplierDto> surveyOptionList;
    private boolean optionType;
    private SurveyAnswerListener listener;
    private int selectPosition = -1;
    private int alphabet = 64;
    private String selectedLanguage;
    private boolean isIncrement;

    public SellerAdaptor(Context context, List<SupplierDto> surveyOptionList, String selectedLanguage) {

        this.mContext = context;
        this.surveyOptionList = surveyOptionList;
        this.selectedLanguage = selectedLanguage;
        this.isIncrement = true;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public AnswerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.seller_adaptor, parent, false);
        return new AnswerHolder(v);
    }

    @Override
    public void onBindViewHolder(final AnswerHolder holder, final int position) {

        if (selectedLanguage.equalsIgnoreCase("ta"))
            holder.mTvAnswer.setText(surveyOptionList.get(position).getContactName());
        else holder.mTvAnswer.setText(surveyOptionList.get(position).getContactName());

        if (isIncrement) {
            alphabet = alphabet + 1;
            holder.mTvQuestionNo.setText("" + (char) alphabet);
        }

        /*if (optionType) {
            holder.mCheckBox.setVisibility(View.VISIBLE);
            holder.mRbutton.setVisibility(View.GONE);
        } else {
            holder.mRbutton.setChecked(false);
            if (position == selectPosition) {
                holder.mRbutton.setChecked(true);
            }
            holder.mRbutton.setVisibility(View.VISIBLE);
            holder.mCheckBox.setVisibility(View.GONE);
        }*/

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
                }
                /*else {
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
                }*/
            }
        });

       /* holder.mRbutton.setOnClickListener(new View.OnClickListener() {
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
        });*/

        holder.
                mCheckBox.
                setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                                                   listener.selectAnswer(position,
//                                                           surveyOptionList.get(position).getId(), isChecked);

                                                   if (isChecked) {
                                                       Filter_Activity.supplierId.add(surveyOptionList.get(position).getId());
                                                       Filter_Activity.suppliperName.add(surveyOptionList.get(position).getContactName());

                                                   } else {
                                                       for (int i = 0; i < Filter_Activity.supplierId.size(); i++) {
                                                           if (Filter_Activity.supplierId.get(i) == surveyOptionList.get(i).getId()) {
                                                               Filter_Activity.supplierId.remove(i);
                                                               Filter_Activity.suppliperName.remove(i);
                                                               Log.d("size2==>", "" + Filter_Activity.supplierId.size());
                                                           }
                                                       }
                                                   }
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
//        private RadioButton mRbutton;
        private LinearLayout mCVRow;

        public AnswerHolder(View v) {
            super(v);

            mTvQuestionNo = (TextView) v.findViewById(R.id.txt_question_no);
            mTvAnswer = (TextView) v.findViewById(R.id.txt_answer);
//            mCVRow = (CardView) v.findViewById(R.id.answer_row_card_view);

            mCVRow = (LinearLayout) v.findViewById(R.id.answer_row);
            mCheckBox = (CheckBox) v.findViewById(R.id.cb_answer);
//            mRbutton = (RadioButton) v.findViewById(R.id.rb_answer);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
