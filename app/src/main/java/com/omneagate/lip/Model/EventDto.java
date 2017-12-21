package com.omneagate.lip.Model;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ramprasath on 10/6/16.
 */
@Data
public class EventDto implements Serializable {

    public String id;

    public String title;

    public String regionalTitle;

    public String description;

    public String regionalDescription;

    public Long constituencyId;

    public String regionalShortDescription;

    public String shortDescription;

    public String constituencyName;

    public String imageUrl;

    public String image;

    public String type;

    public Boolean isVolunteerRequired;

    public Long volunteerCount;

    public String address;

    public String day;

    public String time;

    public String startDate;

    public String endDate;

    public String fromTime;

    public String toTime;

  //  public Status status;

    public Long createdBy;

    public String createdDate;

    public String updatedBy;

    public String updatedDate;

    public String createdUser;

    public String updatedUser;

    public boolean edit;

    public boolean delete;

    public  boolean associate;
}
