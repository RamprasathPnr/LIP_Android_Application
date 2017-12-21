package com.omneagate.lip.Model;

import lombok.Data;

/**
 * Created by user1 on 11/6/16.
 */
@Data
public class DepartmentFeedbackDto {

    String id;
    String departmentId;
    String generalVoterId;
    Feedbackenum feedbackType;
    String description;
    String feedbackDate;


}
