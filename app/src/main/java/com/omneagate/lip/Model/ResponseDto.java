package com.omneagate.lip.Model;

import java.util.List;

import lombok.Data;

/**
 * Created by user1 on 25/5/16.
 */
@Data
public class ResponseDto extends BaseResponseDto {
    List<GroupSurveyListDto> surveyUserGroupDtoList;
    VoterDetailsDto generalVoterDto;
    OtpSmsResponseDto otpSMSresponseDto;
    List<DistrictsDto> districtList;
    List<ConstitutencyDto> constituencyList;
    List<GrievanceCategoryDto> grievanceCategoryList;
    List<GrievanceSubCataegoryDto> grievanceSubCategoryList;
    List<AvailableDaysDto> listAvailableDay;
    AvailableTimingDto availableDay;
    List<GrievanceHistoryDto> listComplaintDto;
    List<AppointmentListDto> appointmentRequestDtoList;
    List<NewsDto> newsDtoList;
    List<SurveyQuestionDto> surveyQuestionList;
    List<PolicyDto> policyDtoList;
    CitizenEventDto citizenEventDto;
    List<EventDto> eventsDtoList;
    List<DesignationDto> designationDtoList;
    List<UserDetailsDto> userDtoList;
    List<ProductDetails> searchItemDtoList;
    List<CategoryListDto> serviceListAndroductList;
    List<String> sortList;
    List<SupplierDto> supplierList;
    List<PriceRangeDto> priceRangeList;
    List<DiscountRAngeDto> discountRangeList;

    List<OrderHistoryDto> customerOrderList;

    OrderedDetailsDto customerOrderDetailsDto;
}
