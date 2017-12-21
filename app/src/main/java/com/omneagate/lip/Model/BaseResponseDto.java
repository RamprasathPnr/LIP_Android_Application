package com.omneagate.lip.Model;

import lombok.Data;

@Data
public class BaseResponseDto {

    String status;
    String code;
    String description;
    String loginStatus;
    String candidateList;
    String itemCount;
    String totalCost;
    String orderDate;
    String totalQuantity;
    String orderNo;
    String paymentStatus;

}