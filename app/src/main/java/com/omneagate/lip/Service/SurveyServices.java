package com.omneagate.lip.Service;

import com.omneagate.lip.Model.SurveyQuestionDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shanthakumar on 10-06-2016.
 */
public class SurveyServices {

    /**
     * currentQuesIndex starts from zero and ends question size -1
     */
    private static int currentQuesIndex = 0;
    private static List<SurveyQuestionDto> questions = new ArrayList<>();
    private static SurveyServices mSurveyService;
    public static String surveyId;
    private static final String TAG = SurveyServices.class.getCanonicalName();


    public SurveyServices() {

    }

    public static SurveyServices getInstance() {
        if (mSurveyService == null) {
            mSurveyService = new SurveyServices();
        }

        return mSurveyService;
    }

    public void loadFirstQuestion(QuestionChangeListener listener) {
        try {
            if (questions != null) {
                if (questions.isEmpty()) {
                    return;
                }
                currentQuesIndex = 0;
                listener.onQuestionChanged(questions.get(0));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void loadPreviousQuestion(QuestionChangeListener listener) {
        try {
            if (questions != null) {
                int prevQuestionNum = currentQuesIndex - 1;
                if (prevQuestionNum < 0) {
                    return;
                }
                currentQuesIndex = prevQuestionNum;
                listener.onQuestionChanged(questions.get(prevQuestionNum));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void loadNextQuestion(QuestionChangeListener listener) {
        try {
            if (questions != null) {
                int totalQues = questions.size();
                int nextQues = currentQuesIndex + 1;
                if (nextQues > totalQues) {
                    return;
                }
                currentQuesIndex = nextQues;
                listener.onQuestionChanged(questions.get(nextQues));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidQuestionNumber(int quesNo) {
        if (questions != null) {
            if (quesNo <= 0 || quesNo > questions.size()) {
                return false;
            }
            return true;
        }
        return false;
    }

    public int getTotalQuestionCount() {
        if (questions != null) {
            return questions.size();
        }
        return 0;
    }

    public int getCurrentQuestionNo() {
        return currentQuesIndex + 1;
    }

    public static List<SurveyQuestionDto> getQuestions() {
        if (questions != null) {
            return questions;
        }
        return null;
    }

    public String getCurrentQuestionId() {
        try {
            SurveyQuestionDto examQuestion = questions.get(currentQuesIndex);
            return examQuestion.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setAllQuestions(QuestionChangeListener listener,
                                List<SurveyQuestionDto> newsDtoList) {
        this.questions = newsDtoList;
        loadFirstQuestion(listener);
    }
}
