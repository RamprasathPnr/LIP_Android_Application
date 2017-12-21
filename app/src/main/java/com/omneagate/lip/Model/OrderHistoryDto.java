package com.omneagate.lip.Model;

import lombok.Data;

/**
 * Created by user on 8/7/16.
 */
@Data
public class OrderHistoryDto {
    String id;
    String orderNo;
    String quantity;
    String status;
    String orderDate;
    String totalAmount;
    String mobileOrderDate;
}
