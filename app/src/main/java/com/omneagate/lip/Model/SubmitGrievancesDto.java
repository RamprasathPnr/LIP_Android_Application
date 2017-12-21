package com.omneagate.lip.Model;

import java.util.List;

import lombok.Data;

/**
 * Created by user1 on 29/2/16.
 */
@Data
public class SubmitGrievancesDto {
    long categoryId;
    long subCategoryId;
    String description;
    long postedBy;
    String postedDate;
    String lat;
    String lon;
    List<ImagesStringDto> attachments;
}
