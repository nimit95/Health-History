package com.hackdtu.healthhistory.utils;

/*
 * Created by Nimit Agg on 12-12-2016.
 */
public interface Constants {

    String TAG_SHARED_PREF = "sharedPreferences";
    String DATA_URL="http://sahil99.pythonanywhere.com/history/history_records";
    String AUTH_URL="http://sahil99.pythonanywhere.com/api-auth/login/";
    String UPLOAD_URL="http://sahil99.pythonanywhere.com/history/history/";
    String DISEASE_LIST_URL="http://sahil99.pythonanywhere.com/history/user/100010001001/";
    String IMAGE_URL = "http://sahil99.pythonanywhere.com";

    String USER_ID = "user-id";
    String USERS_FB = "users";
    String USER_IMG_FB = "userImages";
    String XRAY_TYPE = "1";
    String MRI_TYPE = "2";
    String DOCTOR_PRESCRIPTION_TYPE = "3";
    String ULTRASOUND_TYPE = "4";
    String TEST_REPORT_TYPE = "5";
    String OTHERS_REPORT_TYPE = "6";
    String SUGAR_LVL_FASTING_FB = "sugarLevelFasting";
    String SUGAR_PP_FASTING_FB ="sugarLevelPP";
    String BLOOD_PRESSURE_FB = "bloodPressures";
}