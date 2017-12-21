package com.omneagate.lip.Model;

import lombok.Data;

/**
 * Created by user on 8/7/16.
 */
@Data
public class OrderedItemDetailsDto {
    String id;
    String itemId;
    String itemCode;
    String itemName;
    String itemType;
    String quantity;
    String rate;
    String totalAmount;
    String supplierId;
    String status;
    String updatedDate;
    String itemImage;
}
