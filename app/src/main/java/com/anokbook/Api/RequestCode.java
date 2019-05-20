package com.anokbook.Api;

/**
 * Created by suarebits on 3/12/15.
 */
public class RequestCode {
    public static final int CODE_Register = 1;
    public static final int CODE_Login = 2;
    public static final int CODE_OtpCheck = 3;
    public static final int CODE_ResendOtp = 4;
    public static final int CODE_ForgetMobile = 5;
    public static final int CODE_Password_Reset = 6;
    public static final int CODE_Book_List_All = 7;
    public static final int CODE_Category_List = 8;
    public static final int CODE_Purpose_List = 9;
    public static final int CODE_Book_Viewby_Bookid = 10;
    public static final int CODE_Book_ListBy_User = 11;
    public static final int CODE_Like_Books=12;
    public static final int CODE_Privacypolicy=13;
    public static final int CODE_Book_Image_Delete=14;
    public static final int CODE_Fav_Book_List_User = 15;
    public static final int CODE_Faq_List_Api = 16;
    public static final int CODE_Preparing_Cat_List = 17;
    public static final int CODE_User_Preparing_Cat=18;
    public static final int CODE_Logout_Api=19;
    public static final int CODE_Book_Request_Api=20;
    public static final int CODE_Sent_Request_List=21;
    public static final int CODE_Recieved_Request_List=22;
    public static final int CODE_StatusReceivedBook=23;
    public static final int CODE_UserProfile=24;
    public static final int CODE_ChangeStatusReceivedBokk=25;
    public static final int CODE_StatusSentBook=26;
    public static final int CODE_ChangePassword=27;
    public static final int CODE_Add_Money_Wallet=28;
    public static final int CODE_Get_Wallet= 29;
    public static final int CODE_Get_Transaction=30;
    public static final int CODE_Update_Wallet =31;
    public static final int CODE_Rent_Calculation =32;
    public static final int CODE_PaidList =33;
    public static final int CODE_BuyRequest =34;
    public static final int CODE_HandOverStatus=35;
    public static final int CODE_ChangeLanguage=36;
    public static final int CODE_GetSubTypesObj=37;
    public static final int CODE_UpdateCustomerProfile=38;
    public static final int CODE_UpdateAdminProfile=39;
    public static final int CODE_UpdateLabourProfile=42;
    public static final int CODE_getProfileInfo=43;
    public static final int CODE_createJob=44;
    public static final int CODE_GetCustomerAllJobs=45;
    public static final int CODE_GetSingleJob=46;
    public static final int CODE_GetLabourAllJobs=47;
    public static final int CODE_CreateBid=48;
    public static final int CODE_EditBid=49;
    public static final int CODE_getAllBid=50;
    public static final int CODE_getSingleBid=51;
    public static final int CODE_acceptBid=52;
    public static final int CODE_rejectBid=53;
    public static final int CODE_getProfileById=54;


    public static final int CODE_GetAllGroups=100;
    public static final int CODE_GetAllrequest=101;
    public static final int CODE_MakeRequest = 102;
    public static final int CODE_GetAllSentrequest=103;
    public static final int CODE_AcceptRequest=104;
    public static final int CODE_RejectRequest=105;
    public static final int CODE_GetConfirmedRequests=106;
    public static final int CODE_RemoveFromGroupList = 107;
    public static final int CODE_AddMember = 108;
    public static final int CODE_GetGroupData=109;
    public static final int CODE_RemoveMember = 110;
    public static final int CODE_CancelRequest = 111;
    public static final int CODE_GetGroupList = 112;
    public static final int CODE_LeftGroupList = 113;
    public static final int CODE_GetMyLabourList = 114;
    public static final int CODE_addOwnMember = 115;
    public static final int CODE_removeOwnMember = 116;
    public static final int Code_archiveAcceptBids = 117;
    public static final int Code_archiveRejectBids = 118;




    //Constants
    public static String SP_CURRENT_LAT = "lat";
    public static String SP_CURRENT_LONG = "lng";
    public static String SP_NEW_LAT = "newLat";
    public static String SP_NEW_LONG = "newLng";
    public static String SP_DriverStatus = "driverStatus";
    public static final String UserID = "userID";
    public static final String userType = "user_type";
    public static final String KEY_ANIM_TYPE="anim_type";
    public static final String KEY_TITLE="anim_title";
    public static final String LangId = "langid";

    public static int AUTOPLACE_CODE=80;


    public enum TransitionType{
        ExplodeJava,ExplodeXML,SlideJava,SlideXML,FadeJava,FadeXML;
    }

}
