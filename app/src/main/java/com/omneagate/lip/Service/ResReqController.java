package com.omneagate.lip.Service;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.omneagate.lip.Utility.Constants;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sathya Rathinavelu on 5/8/2016.
 */
public class ResReqController extends Controller implements ServiceResponseListener {

    private static final String TAG = ResReqController.class.getSimpleName();
    private HandlerThread workerThread;
    private Handler workHandler;
    private Context mContext;
    private List<Object> listModel;
    private String post = "POST";
    private String get = "GET";
    //    private DeviceDto listModel;
    private Object detailsModel;

    public static final int LOGIN_NUMBER = 2;
    public static final int LOGIN_NUMBER_SUCCESS = 103;
    public static final int LOGIN_NUMBER_FAILED = 104;

    public static final int OTP_VERIFY = 3;
    public static final int OTP_VERIFY_SUCCESS = 105;
    public static final int OTP_VERIFY_FAILED = 106;

    public static final int GET_DISTRICT = 4;
    public static final int GET_DISTRICT_SUCCESS = 107;
    public static final int GET_DISTRICT_FAILED = 108;

    public static final int GET_CONSTITUENCY = 5;
    public static final int GET_CONSTITUENCY_SUCCESS = 109;
    public static final int GET_CONSTITUENCY_FAILED = 110;

    public static final int REGISTRATION = 6;
    public static final int REGISTRATION_SUCCESS = 111;
    public static final int REGISTRATION_FAILED = 112;

    public static final int GET_CATEGORY = 7;
    public static final int GET_CATEGORY_SUCCESS = 113;
    public static final int GET_CATEGORY_FAILED = 114;

    public static final int GET_SUB_CATEGORY = 8;
    public static final int GET_SUB_CATEGORY_SUCCESS = 115;
    public static final int GET_SUB_CATEGORY_FAILED = 116;

    public static final int SUB_GRIEVANCE = 9;
    public static final int SUB_GRIEVANCE_SUCCESS = 117;
    public static final int SUB_GRIEVANCE_FAILED = 118;

    public static final int GET_AVAILABLE_DAYS = 10;
    public static final int GET_AVAILABLE_DAYS_SUCCESS = 119;
    public static final int GET_AVAILABLE_DAYS_FAILED = 120;

    public static final int GET_TIMING = 11;
    public static final int GET_TIMING_SUCCESS = 121;
    public static final int GET_TIMING_FAILED = 122;

    public static final int APPOINTMENT = 12;
    public static final int APPOINTMENT_SUCCESS = 123;
    public static final int APPOINTMENT_FAILED = 124;

    public static final int GRIEVANCE_HISTORY = 13;
    public static final int GRIEVANCE_HISTORY_SUCCESS = 125;
    public static final int GRIEVANCE_HISTORY_FAILED = 126;

    public static final int APPOINTMENT_HISTORY = 14;
    public static final int APPOINTMENT_HISTORY_SUCCESS = 127;
    public static final int APPOINTMENT_HISTORY_FAILED = 128;

    public static final int ALL_NEWS = 15;
    public static final int ALL_NEWS_SUCCESS = 129;
    public static final int ALL_NEWS_FAILED = 130;

    public static final int COMPLAINT_FEEDBACK = 16;
    public static final int COMPLAINT_FEEDBACK_SUCCESS = 131;
    public static final int COMPLAINT_FEEDBACK_FAILED = 132;

    public static final int SURVEY_QUESTION = 17;
    public static final int SURVEY_QUESTION_SUCCESS = 133;
    public static final int SURVEY_QUESTION_FAILED = 134;

    public static final int GET_DEPARTMENT_LIST = 18;
    public static final int GET_DEPARTMENT_SUCCESS = 135;
    public static final int GET_DEPARTMENT_FAILED = 136;

    public static final int GET_DEPARTMENT_DETAIL = 19;
    public static final int GET_DEPARTDETAIL__SUCCESS = 137;
    public static final int GET_DEPARTDETAIL_FAILED = 138;

    public static final int DEPARTMENT_FEEDBACK = 20;
    public static final int DEPARTMENT_FEEDBACK__SUCCESS = 139;
    public static final int DEPARTMENT_FEEDBACK_FAILED = 140;

    public static final int GET_POLICES_LIST = 21;
    public static final int GET_POLICES_SUCCESS = 141;
    public static final int GET_POLICES_FAILED = 142;

    public static final int GET_POLICES_FEEDBACK = 22;
    public static final int GET_POLICES_FEEDBACK_SUCCESS = 143;
    public static final int GET_POLICES_FEEDBACK_FAILED = 144;

    //CitizenEvent
    public static final int CITIZEN_EVENT = 23;
    public static final int CITIZEN_EVENT_SUCCESS = 147;
    public static final int CITIZEN_EVENT_FAILED = 148;

    public static final int ADD_CITIZEN = 24;
    public static final int ADD_CITIZEN_SUCCESS = 149;
    public static final int ADD_CITIZEN_FAILED = 150;


    public static final int SURVEY_ANSWER = 25;
    public static final int SURVEY_ANSWER_SUCCESS = 151;
    public static final int SURVEY_ANSWER_FAILED = 152;

    public static final int ALL_EVENTS_ = 26;
    public static final int ALL_EVENTS_SUCCESS = 153;
    public static final int ALL_EVENTS_FAILED = 154;

    public static final int GROUP_SURVEY_LIST_ = 27;
    public static final int GROUP_SURVEY_LIST_SUCCESS = 155;
    public static final int GROUP_SURVEY_LIST_FAILED = 156;

    public static final int NEWS_LOAD_MORE = 28;
    public static final int NEWS_LOAD_MORE_SUCCESS = 157;
    public static final int NEWS_LOAD_MORE_FAILED = 158;

    public static final int DEPARTMENT_DESIGNATION = 29;
    public static final int DEPARTMENT_DESIGNATION_SUCCESS = 159;
    public static final int DEPARTMENT_DESIGNATION_FAILED = 160;

    public static final int DESIGNATION_USER = 30;
    public static final int DESIGNATION_USER_SUCCESS = 161;
    public static final int DESIGNATION_USER_FAILED = 162;

    public static final int GETLIST_PRODUCT = 31;
    public static final int GETLIST_PRODUCT_SUCCESS = 163;
    public static final int GETLIST_PRODUCT_FAILED = 164;

    public static final int GET_CATEGORY_LIST = 32;
    public static final int GET_CATEGORY_LIST_SUCCESS = 165;
    public static final int GET_CATEGORY_LIST_FAILED = 166;

    public static final int ADD_CART = 33;
    public static final int ADD_CART_LIST_SUCCESS = 167;
    public static final int ADD_CART_LIST_FAILED = 168;


    public static final int GET_CART_PRODUTS = 34;
    public static final int GET_CART_PRODUTS_SUCCESS = 168;
    public static final int GET_CART_PRODUTS_FAILED = 169;

    public static final int GET_FILTER = 35;
    public static final int GET_FILTER_SUCCESS = 170;
    public static final int GET_FILTER_FAILED = 171;

    public static final int GET_CONFIRM = 36;
    public static final int GET_CONFIRM_SUCCESS = 172;
    public static final int GET_CONFIRM_FAILED = 173;

    public static final int GET_ORDER_HISTORY = 37;
    public static final int GET_ORDER_HISTORY_SUCCESS = 173;
    public static final int GET_ORDER_HISTORY_FAILED = 174;

    public static final int GET_ORDER_DETAILS = 38;
    public static final int GET_ORDER_DETAILS_SUCCESS = 175;
    public static final int GET_ORDER_DETAILS_FAILED = 176;

    public static final int GETLIST_PRODUCT_LOADMORE = 39;
    public static final int GETLIST_PRODUCT_LOADMORE_SUCCESS = 177;
    public static final int GETLIST_PRODUCT_LOADMORE_FAILED = 178;

    public ResReqController(Context mContext) {
        this.mContext = mContext;
        workerThread = new HandlerThread("worker Thread");
        workerThread.start();
        workHandler = new Handler(workerThread.getLooper());
    }

    public ResReqController(Context mContext, List<Object> listModel, Object detailsModel) {
        this.listModel = listModel;
        this.detailsModel = detailsModel;
        this.mContext = mContext;
        workerThread = new HandlerThread("worker Thread");
        workerThread.start();
        workHandler = new Handler(workerThread.getLooper());
//        fileUpload = new FileUpload(mContext);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void getListData(final String url, final HashMap<String, Object> inputParams,
                            final boolean showProgress, final String tag, final String url_enum, final String method) {
        try {

            workHandler.post(new Runnable() {
                @Override
                public void run() {
//                synchronized (listModel) {
                    Utilities.getInstance().makeServiceCall(mContext, showProgress, url, inputParams, tag, ResReqController.this, url_enum, method);
//                }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getListData_LoadMore(final String url, final HashMap<String, Object> inputParams,
                                     final boolean showProgress, final String tag, final String url_enum, final String method) {
        try {

            workHandler.post(new Runnable() {
                @Override
                public void run() {
//                synchronized (listModel) {
                    Utilities.getInstance().serviceCall_loadMore(mContext, showProgress, url, inputParams, tag, ResReqController.this, url_enum, method);
//                }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submitData(final String url, final String inputParams, final boolean showProgress, final String tag, final String url_enum, final String method) {
        try {
            workHandler.post(new Runnable() {
                @Override
                public void run() {
//                synchronized (listModel) {
                    Utilities.getInstance().makeServiceCall_(mContext, showProgress, url, inputParams, tag, ResReqController.this, url_enum, method);
//                }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean handleMessage(int what, final HashMap<String, Object> inputParams, final Object data) {
        try {
            switch (what) {
                case LOGIN_NUMBER:
                    getListData(Constants.MOBILE_NUMBER_LOGIN + data.toString(), inputParams, false, "LOGIN NUMBER", Constants.MOBILE_NUMBER_LOGIN, get);
                    break;

                case OTP_VERIFY:
                    getListData(Constants.OTP_VERIFY, inputParams, false, "OTP VERIFY", Constants.OTP_VERIFY, post);
                    break;

                case GET_DISTRICT:
                    getListData(Constants.DISTRICT, inputParams, false, "GET DISTRUCTS", Constants.DISTRICT, get);
                    break;

                case GET_CONSTITUENCY:
                    getListData(Constants.CONSTITUENCY + data.toString(), inputParams, false, "GET CONSTITUENCY", Constants.CONSTITUENCY, get);
                    break;

                case REGISTRATION:
                    getListData(Constants.VOTER_REGISTRATION, inputParams, false, "REGISTRATION", Constants.VOTER_REGISTRATION, post);
                    break;

                case GET_CATEGORY:
                    getListData(Constants.CATEGORY + data.toString(), inputParams, false, "CATEGORY", Constants.CATEGORY, get);
                    break;

                case GET_SUB_CATEGORY:
                    getListData(Constants.SUB_CATEGORY + data.toString(), inputParams, false, "SUB CATEGORY", Constants.SUB_CATEGORY, get);
                    break;

                case GET_AVAILABLE_DAYS:
                    getListData(Constants.APPOINTMENT_DATE_AVAILABILITY + data.toString(), inputParams, false, "AVAILABLE DAYS", Constants.APPOINTMENT_DATE_AVAILABILITY, get);
                    break;

                case GET_TIMING:
                    getListData(Constants.TIMING_DETAILS, inputParams, false, "AVAILABLE TIMING", Constants.TIMING_DETAILS, post);
                    break;

                case APPOINTMENT:
                    getListData(Constants.APPOINTMENT_ADD, inputParams, false, "APPOINTMENT ADD", Constants.APPOINTMENT_ADD, post);
                    break;

                case GRIEVANCE_HISTORY:
                    getListData(Constants.GRIEVANCE_HISTORY + data.toString(), inputParams, false, "GRIEVCANCE HISTORY", Constants.GRIEVANCE_HISTORY, get);
                    break;

                case APPOINTMENT_HISTORY:
                    getListData(Constants.APPOINTMENT_HISTORY_ + data.toString(), inputParams, false, "APPOINTMENT HISTORY", Constants.APPOINTMENT_HISTORY_, get);
                    break;

                case ALL_NEWS:
                    String url = Constants.ALL_NEWS_LOADING + data.toString();
                    getListData(url, inputParams, false, "GET ALL NEWS", Constants.ALL_NEWS_LOADING, get);
                    break;

                case COMPLAINT_FEEDBACK:

                    getListData(Constants.COMPLAINT_FEEDBACK, inputParams, false, "COMPLAINT FEEDBACK",
                            Constants.COMPLAINT_FEEDBACK, post);
                    break;

                case SURVEY_QUESTION:
                    getListData(Constants.SURVEY_QUESTION + data.toString(), inputParams, false, "GET SURVEY QUESTIONS",
                            Constants.SURVEY_QUESTION, get);
                    break;

                case GET_DEPARTMENT_LIST:
                    getListData(Constants.DEPARTMENT_LIST + data.toString(), inputParams, false, "GET DEPARTMENT LIST", Constants.DEPARTMENT_LIST, get);
                    break;

                case GET_DEPARTMENT_DETAIL:
                    getListData(Constants.DEPARTMENT_DETAIL + data.toString(), inputParams, false, "GET DEPARTMENT DETAIL", Constants.DEPARTMENT_DETAIL, get);
                    break;

                case DEPARTMENT_FEEDBACK:
                    getListData(Constants.DEPARTMENT_COMMENDS, inputParams, false, "GET DEPARTMENT FEEDBACK", Constants.DEPARTMENT_COMMENDS, post);
                    break;

                case GET_POLICES_LIST:
                    getListData(Constants.POLICES_LIST + data.toString(), inputParams, false, "GET POLICEIS LIST", Constants.POLICES_LIST, get);
                    break;

                case GET_POLICES_FEEDBACK:
                    getListData(Constants.POLICES_FEEDBACK_LIST, inputParams, false, "GET POLICEIS LIST", Constants.POLICES_FEEDBACK_LIST, post);
                    break;

                case ALL_EVENTS_:
                    String url1 = Constants.ALL_EVENTS + "/" + data.toString();
                    getListData(url1, inputParams, false, "GET ALL EVENTS",
                            Constants.ALL_EVENTS, get);
                    break;

                case SURVEY_ANSWER:

                    getListData(Constants.SURVEY_ANSWER_SUBMIT, inputParams, false, "SUBMIT SURVEY ANSWER", Constants.SURVEY_ANSWER_SUBMIT, post);
                    break;

                case CITIZEN_EVENT:

                    getListData(Constants.CITIZEN_EVENT + data.toString(), inputParams, false, "GET CITIZEN EVENT", Constants.CITIZEN_EVENT, get);
                    break;

                case ADD_CITIZEN:
                    getListData(Constants.ADD_CITIZEN, inputParams, false, "ADD CITIZEN EVENT",
                            Constants.ADD_CITIZEN, post);
                    break;

                case GROUP_SURVEY_LIST_:
                    getListData(Constants.GROUP_SURVEY_LIST + data.toString(), inputParams, false, "GROUP SURVEY",
                            Constants.GROUP_SURVEY_LIST, get);
                    break;

                case NEWS_LOAD_MORE:
                    getListData_LoadMore(Constants.ALL_NEWS_LOAD_MORE + data.toString(), inputParams, false, "GET ALL NEWS LOAD MORE", Constants.ALL_NEWS_LOAD_MORE, get);
                    break;

                case DEPARTMENT_DESIGNATION:
                    getListData(Constants.DEPARTMENT_DESIGNATION + data.toString(), inputParams, false, "GROUP SURVEY",
                            Constants.DEPARTMENT_DESIGNATION, get);
                    break;

                case DESIGNATION_USER:
                    getListData(Constants.DESIGNATION_USER + data.toString(), inputParams, false, "GROUP SURVEY",
                            Constants.DESIGNATION_USER, get);
                    break;

                case GETLIST_PRODUCT:
                    getListData(Constants.PRODUCT_LIST, inputParams, false, "PRODUCT LIST",
                            Constants.PRODUCT_LIST, post);
                    break;

                case GET_CATEGORY_LIST:
                    getListData_LoadMore(Constants.CATEGORY_LIST, inputParams, false, "CATEGORY LIST",
                            Constants.CATEGORY_LIST, get);
                    break;

                case ADD_CART:
                    getListData_LoadMore(Constants.ADD_CART, inputParams, false, "CATEGORY LIST",
                            Constants.ADD_CART, post);
                    break;

                case GET_CART_PRODUTS:
                    String url2 = Constants.GET_CART_PRODUCT + data.toString();
                    getListData(url2, inputParams, false, "PRODUCT LIST",
                            Constants.GET_CART_PRODUCT, get);
                    break;

                case GET_FILTER:
                    String filter_url = Constants.GET_FILTER;
                    getListData(filter_url, inputParams, false, "PRODUCT LIST",
                            Constants.GET_FILTER, post);
                    break;

                case GET_ORDER_HISTORY:
                    String order_url = Constants.GET_ORDERRD_HISTORY + data.toString();
                    getListData(order_url, inputParams, false, "PRODUCT LIST",
                            Constants.GET_ORDERRD_HISTORY, get);
                    break;

                case GET_ORDER_DETAILS:
                    String order_details_url = Constants.GET_ORDERED_DETAILS + data.toString();
                    getListData(order_details_url, inputParams, false, "ORDERED DETAILS",
                            Constants.GET_ORDERED_DETAILS, get);
                    break;

                case GETLIST_PRODUCT_LOADMORE:
                    getListData_LoadMore(Constants.PRODUCT_LIST_LOADMORE, inputParams, false, "GET PRODUCT LOAD MORE", Constants.ALL_NEWS_LOAD_MORE, post);
                    break;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean handleMessage_(int what, final String inputParams, final Object data) {
        try {
            switch (what) {
                case SUB_GRIEVANCE:
                    submitData(Constants.REGISTATION_GRIEVANCE, inputParams, false, "SUBMIT COMPLAINTS", Constants.REGISTATION_GRIEVANCE, post);
                    break;
                case GETLIST_PRODUCT:
                    submitData(Constants.PRODUCT_LIST, inputParams, false, "PRODUCT LIST",
                            Constants.PRODUCT_LIST, post);
                    break;

                case GET_CONFIRM:
                    submitData(Constants.PAYMENT_CONFIRM, inputParams, false, "CONFIRM PAYMENT",
                            Constants.PAYMENT_CONFIRM, post);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onSuccessResponse(String url, HashMap<String, Object> requestParams, String response) {
        try {
            Utilities.getInstance().dismissProgressBar();
            Log.e(TAG + " Success Response", response);

            switch (url) {
                case Constants.MOBILE_NUMBER_LOGIN:
                    notifyOutboxHandlers(LOGIN_NUMBER_SUCCESS, 0, 0, response);
                    break;
                case Constants.OTP_VERIFY:
                    notifyOutboxHandlers(OTP_VERIFY_SUCCESS, 0, 0, response);
                    break;
                case Constants.DISTRICT:
                    notifyOutboxHandlers(GET_DISTRICT_SUCCESS, 0, 0, response);
                    break;
                case Constants.CONSTITUENCY:
                    notifyOutboxHandlers(GET_CONSTITUENCY_SUCCESS, 0, 0, response);
                    break;
                case Constants.VOTER_REGISTRATION:
                    notifyOutboxHandlers(REGISTRATION_SUCCESS, 0, 0, response);
                    break;
                case Constants.CATEGORY:
                    notifyOutboxHandlers(GET_CATEGORY_SUCCESS, 0, 0, response);
                    break;
                case Constants.SUB_CATEGORY:
                    notifyOutboxHandlers(GET_SUB_CATEGORY_SUCCESS, 0, 0, response);
                    break;
                case Constants.REGISTATION_GRIEVANCE:
                    notifyOutboxHandlers(SUB_GRIEVANCE_SUCCESS, 0, 0, response);
                    break;
                case Constants.APPOINTMENT_DATE_AVAILABILITY:
                    notifyOutboxHandlers(GET_AVAILABLE_DAYS_SUCCESS, 0, 0, response);
                    break;
                case Constants.TIMING_DETAILS:
                    notifyOutboxHandlers(GET_TIMING_SUCCESS, 0, 0, response);
                    break;
                case Constants.APPOINTMENT_ADD:
                    notifyOutboxHandlers(APPOINTMENT_SUCCESS, 0, 0, response);
                    break;
                case Constants.GRIEVANCE_HISTORY:
                    notifyOutboxHandlers(GRIEVANCE_HISTORY_SUCCESS, 0, 0, response);
                    break;
                case Constants.APPOINTMENT_HISTORY_:
                    notifyOutboxHandlers(APPOINTMENT_HISTORY_SUCCESS, 0, 0, response);
                    break;
                case Constants.ALL_NEWS_LOADING:
                    notifyOutboxHandlers(ALL_NEWS_SUCCESS, 0, 0, response);
                    break;
                case Constants.COMPLAINT_FEEDBACK:
                    notifyOutboxHandlers(COMPLAINT_FEEDBACK_SUCCESS, 0, 0, response);
                    break;
                case Constants.SURVEY_QUESTION:
                    notifyOutboxHandlers(SURVEY_QUESTION_SUCCESS, 0, 0, response);
                    break;
                case Constants.DEPARTMENT_LIST:
                    notifyOutboxHandlers(GET_DEPARTMENT_SUCCESS, 0, 0, response);
                    break;
                case Constants.DEPARTMENT_DETAIL:
                    notifyOutboxHandlers(GET_DEPARTDETAIL__SUCCESS, 0, 0, response);
                    break;
                case Constants.DEPARTMENT_COMMENDS:
                    notifyOutboxHandlers(DEPARTMENT_FEEDBACK__SUCCESS, 0, 0, response);
                    break;
                case Constants.POLICES_LIST:
                    notifyOutboxHandlers(GET_POLICES_SUCCESS, 0, 0, response);
                    break;
                case Constants.POLICES_FEEDBACK_LIST:
                    notifyOutboxHandlers(GET_POLICES_FEEDBACK_SUCCESS, 0, 0, response);
                    break;
                case Constants.SURVEY_ANSWER_SUBMIT:
                    notifyOutboxHandlers(SURVEY_ANSWER_SUCCESS, 0, 0, response);
                    break;
                case Constants.ALL_EVENTS:
                    notifyOutboxHandlers(ALL_EVENTS_SUCCESS, 0, 0, response);
                    break;

                case Constants.ADD_CITIZEN:
                    notifyOutboxHandlers(ADD_CITIZEN_SUCCESS, 0, 0, response);
                    break;

                case Constants.CITIZEN_EVENT:
                    notifyOutboxHandlers(CITIZEN_EVENT_SUCCESS, 0, 0, response);
                    break;

                case Constants.GROUP_SURVEY_LIST:
                    notifyOutboxHandlers(GROUP_SURVEY_LIST_SUCCESS, 0, 0, response);
                    break;

                case Constants.ALL_NEWS_LOAD_MORE:
                    notifyOutboxHandlers(NEWS_LOAD_MORE_SUCCESS, 0, 0, response);
                    break;

                case Constants.DEPARTMENT_DESIGNATION:
                    notifyOutboxHandlers(DEPARTMENT_DESIGNATION_SUCCESS, 0, 0, response);
                    break;

                case Constants.DESIGNATION_USER:
                    notifyOutboxHandlers(DESIGNATION_USER_SUCCESS, 0, 0, response);
                    break;

                case Constants.PRODUCT_LIST:
                    notifyOutboxHandlers(GETLIST_PRODUCT_SUCCESS, 0, 0, response);
                    break;

                case Constants.CATEGORY_LIST:
                    notifyOutboxHandlers(GET_CATEGORY_LIST_SUCCESS, 0, 0, response);
                    break;
                case Constants.ADD_CART:
                    notifyOutboxHandlers(ADD_CART_LIST_SUCCESS, 0, 0, response);
                    break;
                case Constants.GET_CART_PRODUCT:
                    notifyOutboxHandlers(GET_CART_PRODUTS_SUCCESS, 0, 0, response);
                    break;
                case Constants.GET_FILTER:
                    notifyOutboxHandlers(GET_FILTER_SUCCESS, 0, 0, response);
                    break;

                case Constants.PAYMENT_CONFIRM:
                    notifyOutboxHandlers(GET_CONFIRM_SUCCESS, 0, 0, response);
                    break;

                case Constants.GET_ORDERRD_HISTORY:
                    notifyOutboxHandlers(GET_ORDER_HISTORY_SUCCESS, 0, 0, response);
                    break;
                case Constants.GET_ORDERED_DETAILS:
                    notifyOutboxHandlers(GET_ORDER_DETAILS_SUCCESS, 0, 0, response);
                    break;
                case Constants.PRODUCT_LIST_LOADMORE:
                    notifyOutboxHandlers(GETLIST_PRODUCT_LOADMORE_SUCCESS, 0, 0, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String url, HashMap<String, Object> requestParams, String errorMessage, Boolean isVolleyError) {
        try {
            Utilities.getInstance().dismissProgressBar();
            Log.e(TAG + " Error Response", errorMessage);
            switch (url) {
                case Constants.MOBILE_NUMBER_LOGIN:
                    notifyOutboxHandlers(LOGIN_NUMBER_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.OTP_VERIFY:
                    notifyOutboxHandlers(OTP_VERIFY_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.DISTRICT:
                    notifyOutboxHandlers(GET_DISTRICT_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.CONSTITUENCY:
                    notifyOutboxHandlers(GET_CONSTITUENCY_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.VOTER_REGISTRATION:
                    notifyOutboxHandlers(REGISTRATION_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.CATEGORY:
                    notifyOutboxHandlers(GET_CATEGORY_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.SUB_CATEGORY:
                    notifyOutboxHandlers(GET_SUB_CATEGORY_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.REGISTATION_GRIEVANCE:
                    notifyOutboxHandlers(SUB_GRIEVANCE_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.APPOINTMENT_DATE_AVAILABILITY:
                    notifyOutboxHandlers(GET_AVAILABLE_DAYS_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.TIMING_DETAILS:
                    notifyOutboxHandlers(GET_TIMING_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.APPOINTMENT_ADD:
                    notifyOutboxHandlers(APPOINTMENT_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.GRIEVANCE_HISTORY:
                    notifyOutboxHandlers(GRIEVANCE_HISTORY_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.APPOINTMENT_HISTORY_:
                    notifyOutboxHandlers(APPOINTMENT_HISTORY_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.ALL_NEWS_LOADING:
                    notifyOutboxHandlers(ALL_NEWS_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.COMPLAINT_FEEDBACK:
                    notifyOutboxHandlers(COMPLAINT_FEEDBACK_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.SURVEY_QUESTION:
                    notifyOutboxHandlers(SURVEY_QUESTION_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.DEPARTMENT_LIST:
                    notifyOutboxHandlers(GET_DEPARTMENT_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.DEPARTMENT_DETAIL:
                    notifyOutboxHandlers(GET_DEPARTDETAIL_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.DEPARTMENT_COMMENDS:
                    notifyOutboxHandlers(DEPARTMENT_FEEDBACK_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.POLICES_LIST:
                    notifyOutboxHandlers(GET_POLICES_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.POLICES_FEEDBACK_LIST:
                    notifyOutboxHandlers(GET_POLICES_FEEDBACK_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.CITIZEN_EVENT:
                    notifyOutboxHandlers(CITIZEN_EVENT_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.ALL_EVENTS:
                    notifyOutboxHandlers(ALL_EVENTS_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.ADD_CITIZEN:
                    notifyOutboxHandlers(ADD_CITIZEN_FAILED, 0, 0, errorMessage);
                    break;

                case Constants.SURVEY_ANSWER_SUBMIT:
                    notifyOutboxHandlers(SURVEY_ANSWER_FAILED, 0, 0, errorMessage);
                    break;

                case Constants.GROUP_SURVEY_LIST:
                    notifyOutboxHandlers(GROUP_SURVEY_LIST_FAILED, 0, 0, errorMessage);
                    break;

                case Constants.ALL_NEWS_LOAD_MORE:
                    notifyOutboxHandlers(NEWS_LOAD_MORE_FAILED, 0, 0, errorMessage);
                    break;

                case Constants.DEPARTMENT_DESIGNATION:
                    notifyOutboxHandlers(DEPARTMENT_DESIGNATION_FAILED, 0, 0, errorMessage);
                    break;

                case Constants.DESIGNATION_USER:
                    notifyOutboxHandlers(DESIGNATION_USER_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.PRODUCT_LIST:
                    notifyOutboxHandlers(GETLIST_PRODUCT_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.CATEGORY_LIST:
                    notifyOutboxHandlers(GET_CATEGORY_LIST_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.ADD_CART:
                    notifyOutboxHandlers(ADD_CART_LIST_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.GET_CART_PRODUCT:
                    notifyOutboxHandlers(GET_CART_PRODUTS_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.GET_FILTER:
                    notifyOutboxHandlers(GET_FILTER_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.PAYMENT_CONFIRM:
                    notifyOutboxHandlers(GET_CONFIRM_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.GET_ORDERRD_HISTORY:
                    notifyOutboxHandlers(GET_ORDER_HISTORY_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.GET_ORDERED_DETAILS:
                    notifyOutboxHandlers(GET_ORDER_DETAILS_FAILED, 0, 0, errorMessage);
                    break;
                case Constants.PRODUCT_LIST_LOADMORE:
                    notifyOutboxHandlers(GETLIST_PRODUCT_LOADMORE_FAILED, 0, 0, errorMessage);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNetWorkFailure(String url) {
        Utilities.getInstance().dismissProgressBar();

    }
}