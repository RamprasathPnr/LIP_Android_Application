package com.omneagate.lip.Model;

import lombok.Data;

/**
 * Created by user1 on 6/6/16.
 */
@Data
public class AppointmentListDto {
    String id;
    String appointmentNumber;
    String address;
    String constituencyId;
    String numberOfPeople;
    String requestBy;
    String requestDate;
    String appointmentDate;
    String requestMode;
    String status;
    String reason;
    String description;
    String requestedUser;
    String generalVoterDto;
    String appointmentDateString;
    String constituencyAdminName;
    String appointmentTime;
    String appointmentDateShort;
}
