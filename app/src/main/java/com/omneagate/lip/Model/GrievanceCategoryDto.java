package com.omneagate.lip.Model;

import lombok.Data;

/**
 * Created by user on 30/5/16.
 */
@Data
public class GrievanceCategoryDto {
    String id;
    String name;
    String description;
    String regionalName;
    String deleteStatus;
    String type;
    String constituencyId;
    String constituencyName;
    String createdBy;
    String createdDate;
    String updatedBy;
    String updatedDate;
    String createdUser;
    String updatedUser;
    String edit;
    String delete;
    String associate;
    String subCategory;
    String feetBackStatus;
    String feedBackComments;
}
