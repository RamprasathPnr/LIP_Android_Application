package com.omneagate.lip.Model;

import java.util.List;

import lombok.Data;

/**
 * Created by user1 on 2/6/16.
 */
@Data
public class AvailableDaysDto {
    String id;
    String availableDate;
    String constituencyId;
    String isAvailable;
    String createdBy;
    String createdDate;
    String updatedBy;
    String updatedDate;
    String createdUser;
    String updatedUser;
    String longDate;
    List<AvailableTimingDto> availableDayTimeList;
    List<String> stringTimeList;
}
