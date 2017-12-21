package com.omneagate.lip.Model;

import lombok.Data;

/**
 * Created by user1 on 3/6/16.
 */
@Data
public class GrievanceHistoryDto {
    String id;
    String complaintNumber;
    String lat;
    String lon;
    String progressStatus;
    String requestMode;
    String complaintType;
    String categoryId;
    String subCategoryId;
    String constituencyId;
    String description;
    String postedBy;
    String postedDate;
    String feedbackComments;
    String feedbackType;
    String feedbackDate;
    String categoryName;
    String subCategoryName;
    String generalVoterDto;
    String attachments;
    String stringPostedDate;
    String lastUpdatedComplaintDate;
}
