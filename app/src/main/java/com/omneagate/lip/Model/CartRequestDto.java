package com.omneagate.lip.Model;

import java.util.List;

import lombok.Data;

/**
 * Created by user on 6/7/16.
 */
@Data
public class CartRequestDto {
    String generalVoterId;
    String name;
    String mobile;
    String email;
    String address;
    String districtId;
    List<ProductDetails> itemDtoList;
}
