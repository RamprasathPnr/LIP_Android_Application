package com.omneagate.lip.Adaptor;

import com.omneagate.lip.Model.DiscountRAngeDto;
import com.omneagate.lip.Model.PriceRangeDto;

import java.util.List;

import lombok.Data;

/**
 * Created by USER1 on 05-07-2016.
 */
@Data
public class FilterReqDto {
    String categoryId;
    String sortingType;
    List<String> supplierIdList;
    List<PriceRangeDto> priceRangeDtoList;
    List<DiscountRAngeDto> discountRangeDtoList;
    String index;
}
