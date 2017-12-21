package com.omneagate.lip.Utility;

/**
 * Created by user1 on 23/5/16.
 */
public class Constants {

    //public static final String BASE_URL = "http://192.168.1.236:8080/";
    //public static final String BASE_URL = "http://192.168.1.24:8080/";/*Development Rebendhra*/
    //public static final String BASE_URL = "http://192.168.1.102:5504/";
    //public static final String BASE_URL = "http://192.168.1.103:7070/";
    public static final String BASE_URL = "http://52.77.146.57:7001/"; //Cloud Url


    public static final String MOBILE_NUMBER_LOGIN = BASE_URL + "voterMobile/";
    public static final String OTP_VERIFY = BASE_URL + "/voterMobile/checkOtp/";
    public static final String VOTER_REGISTRATION = BASE_URL + "voterMobile/add";
    public static final String DISTRICT = BASE_URL + "voterMobile/getAllDistrict";
    public static final String CONSTITUENCY = BASE_URL + "voterMobile/getAllConstituencyByDistrictId/";

    public static final String CATEGORY = BASE_URL + "/voterMobile/getGrievanceCategoryByGeneralVoterId/";
    public static final String SUB_CATEGORY = BASE_URL + "/voterMobile/getAllGrievanceSubCategoryByCategoryId/";
    public static final String REGISTATION_GRIEVANCE = BASE_URL + "/voterMobile/addComplaint/";

    public static final String APPOINTMENT_DATE_AVAILABILITY = BASE_URL + "voterMobile/getAllAvailableDay/";
    public static final String TIMING_DETAILS = BASE_URL + "/voterMobile/getAllAvailableTimingByDate/";
    public static final String APPOINTMENT_ADD = BASE_URL + "/voterMobile/addAppointment/";

    public static final String GRIEVANCE_HISTORY = BASE_URL + "/voterMobile/getAllComplaintsByUser/";
    public static final String APPOINTMENT_HISTORY_ = BASE_URL + "/voterMobile/getAppointmentByGeneralVoterId/";

    public static final String ALL_NEWS_LOADING = BASE_URL + "voterMobile/getAllNewsByLazyloading/";
    public static final String COMPLAINT_FEEDBACK = BASE_URL + "voterMobile/addComplaintFeedBack";
    public static final String SURVEY_QUESTION = BASE_URL + "/voterMobile/getQuestionsByGroupIdAndGeneralVoter/";

    public static final String ALL_NEWS_LOAD_MORE = BASE_URL + "/voterMobile/getAllNewsByLazyloading/";

    //department_api
    public static final String DEPARTMENT_LIST = BASE_URL + "voterMobile/getAllDepartments/";
    public static final String DEPARTMENT_DETAIL = BASE_URL + "voterMobile/getDepartmentFeedBack/";
    public static final String DEPARTMENT_COMMENDS = BASE_URL + "voterMobile/addDepartmentFeedBack";
    public static final String DEPARTMENT_DESIGNATION = BASE_URL + "voterMobile/getDesignationByGeneralVoterAndDepartmentId/";
    public static final String DESIGNATION_USER = BASE_URL + "voterMobile/getUserByGeneralVoterAndDepartmentAndDesignation/";

    //PRODUCT LIST
    public static final String PRODUCT_LIST = BASE_URL + "/voterMobile/itemSearchForMobileByLazyloading";
    public static final String PRODUCT_LIST_LOADMORE = BASE_URL + "voterMobile/itemSearchForMobileByLazyloading";
    public static final String CATEGORY_LIST = BASE_URL + "/voterMobile/getAllServiceAndProduct";
    public static final String ADD_CART = BASE_URL + "/voterMobile/addCardDetilas";

    public static final String PAYMENT_CONFIRM = BASE_URL + "/voterMobile/addOrderDetails";

    public static final String GET_CART_PRODUCT = BASE_URL + "/voterMobile/loadCardDetails/";
    public static final String GET_FILTER = BASE_URL + "/voterMobile/getFilters";

    public static final String GET_ORDERRD_HISTORY = BASE_URL + "/voterMobile/getAllCustomerOrdersByLazyloading/";
    public static final String GET_ORDERED_DETAILS = BASE_URL + "/voterMobile/getCustomerOrderItems/";


    //polices_api
    public static final String POLICES_LIST = BASE_URL + "voterMobile/getAllPolicies/";
    public static final String POLICES_FEEDBACK_LIST = BASE_URL + "voterMobile/addPolicyFeedBack";

    public static final String SURVEY_ANSWER_SUBMIT = BASE_URL + "voterMobile/addGeneralVoterSurveyWithSurveyDetails";
    public static final String ALL_EVENTS = BASE_URL + "voterMobile/getconstituencyevents";

    public static final String CITIZEN_EVENT = BASE_URL + "voterMobile/getCitizenEvent/";
    public static final String ADD_CITIZEN = BASE_URL + "voterMobile/addCitizenEvent";

    public static final String GROUP_SURVEY_LIST = BASE_URL + "/voterMobile/getGroup/1";


}
