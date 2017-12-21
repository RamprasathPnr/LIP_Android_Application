package com.omneagate.lip.Service;

import com.omneagate.lip.Model.SurveyQuestionDto;

/**
 * Created by Shanthakumar on 10-06-2016.
 */
public interface QuestionChangeListener {

    void onQuestionChanged(SurveyQuestionDto question);
}
