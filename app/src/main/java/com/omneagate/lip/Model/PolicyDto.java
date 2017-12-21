package com.omneagate.lip.Model;

import lombok.Data;

@Data
public class PolicyDto {

    String id;

    String type;

    String description;

    String regionalDescription;

    Long constituencyId;

    Long createdBy;

    String createdDate;

    Long updatedBy;

    String updatedDate;

    String createdUser;

    String updatedUser;

    String feetBackStatus;

    String feedBackComments;


}
