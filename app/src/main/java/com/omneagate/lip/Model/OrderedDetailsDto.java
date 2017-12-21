package com.omneagate.lip.Model;

import java.util.List;

import lombok.Data;

/**
 * Created by user on 8/7/16.
 */
@Data
public class OrderedDetailsDto {
    String id;
    String orderNo;
    String customerId;
    String totalAmount;
    String totalQuantity;
    String discountAmount;
    String shippingCharges;
    String otherCharges;
    String status;
    String customerName;
    String mobile;
    String email;
    String address;
    String districtId;
    String  paymentStatus;
    String districtName;
    String createdDate;
    String updatedDate;
    List<OrderedItemDetailsDto> orderItemList;








}
