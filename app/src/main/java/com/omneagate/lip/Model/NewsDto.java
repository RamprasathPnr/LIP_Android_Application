package com.omneagate.lip.Model;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by user1 on 7/6/16.
 */
@Data
public class NewsDto implements Serializable {
    String id;
    String constituencyId;
    String title;
    String regionalTitle;
    String description;
    String shareDescription;
    String regionalShareDescription;
    String regionalDescription;
    String shortDescription;
    String regionalShortDescription;
    String imageUrl;
    String imageExtension;
    String videoUrl;
    String videoExtension;
    String videoFileSize;
    String status;
    String createdBy;
    String createdDate;
    String updatedBy;
    String updatedDate;
    String createdDateString;
    String constituencyName;
    String createdUserName;
    String updatedUserName;

}

