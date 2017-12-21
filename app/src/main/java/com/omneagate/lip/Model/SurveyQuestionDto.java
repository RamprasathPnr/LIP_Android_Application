package com.omneagate.lip.Model;

import java.util.List;

import lombok.Data;

/**
 * Created by USER1 on 10-06-2016.
 */
@Data
public class SurveyQuestionDto {
    String id;
    String question;
    String regionalQuestion;
    String constituencyId;
    String limitedQuestion;
    String limitedRegionalQuestion;
    String type;
    String optionType;
//    String surveyUserGroup;

    List<SurveyOptionDto> surveyOptionList;
}
