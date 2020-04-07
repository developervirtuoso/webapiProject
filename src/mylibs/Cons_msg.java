package mylibs;

public class Cons_msg {
	
	
	//=====api user_login========//
	public static String user_login_truestatus = "Otp has been sent to your mobile number!";
	public static String user_login_falsestatus = "Mobile no. & Password does't match";
	
	//=====api add_user========//
	public static String add_user_mobilenoexists = "This mobile number is already registered with us!";
	public static String add_user_truestatus = "Registration completed, Otp has been sent to your mobile number!";
	public static String add_user_falsestatus = "Please try again after some time!";
	
	
	//=====api otp_verification========//
	public static String otp_verification_true_status = "Otp has been verified successfully!";
	public static String otp_verification_false_status = "Wrong otp, please try with currect otp!";
	
	
	//=====api resend_OTP========//
	public static String resend_OTP_text = "You need an OTP for verifying your Feedback app. DO NOT SHARE IT WITH ANYONE. The OTP is ";	
	public static String resend_OTP_true_status = "We have re-sent otp to your mobile number";	
	public static String resend_OTP_false_status = "User does't exist!";	
	
	
	
	//=====api profile_View========//
	public static String profile_View_true_status = "Success";	
	public static String profile_View_false_status = "Failure";	
	
	
	//=====api profile_Update========//
	public static String profile_Update_true_status = "Profile updated successfully!";	
	public static String profile_Update_false_status = "User not found!";	
	
	//=====api change_Password========//
	public static String change_Password_true_status = "Password changed successfully!";
	public static String change_Password_false_status = "Failure, Please try again later!";
	public static String change_Password_not_matched = "Old password do not match!";
	
	
	//=====api create_Complaint========//
	public static String create_Complaint_true_status = "Success!";
	public static String create_Complaint_false_status = "Failed! Please try again.";
	
	
	
	//=====api up_Complaint_file========//
	public static String up_Complaint_file_true_status = "Success!";
	public static String up_Complaint_file_false_status = "Failed! Please try again.";
	
	
	//===common file path base path===//
	public static String user_complaint_file_path = "UploadedImages/User_complaint/";
	
	
	//===common file path user image base path===//

public static String user_profile_file_path = "UploadedImages/User/profile_images/";
public static String admin_profile_file_path = "UploadedImages/Admin/profile_images/";
	
	
	
	
	
	//******************************************AllUserServices_AdminUser**********************************************//
	//=====api admin_user_login========//
	public static String admin_user_true_status = "Dear Admin, Otp has been sent to your mobile number!";
	public static String admin_user_false_status = "MobileNo. is not registered. Please contact admin.";
	
	//=====api admin_user_otp_verification========//
	public static String admin_user_otp_verification_true_status = "Otp has been verified successfully!";
	public static String admin_user_otp_verification_false_status = "Wrong otp, please try with currect otp!";
	
	
	//=====api admin_user_resend_OTP========//
	public static String admin_user_resend_OTP_text = "You need an OTP for verifying your Feedback app. DO NOT SHARE IT WITH ANYONE. The OTP is ";	
	public static String admin_user_resend_OTP_true_status = "We have re-sent otp to your mobile number";	
	public static String admin_user_resend_OTP_false_status = "User does't exist!";	
	
	
	//=====api admin_user_profile_View========//
	public static String admin_user_profile_View_true_status = "Success";	
	public static String admin_user_profile_View_false_status = "Failure";	
	
	//=====api admin_user_profile_Update========//
	public static String admin_user_profile_Update_true_status = "Profile updated successfully!";	
	public static String admin_user_profile_Update_false_status = "User not found!";	
	
	
	//=====api admin_user_UpdateComplaintStatus========//
	public static String admin_user_UpdateComplaintStatus_true_status = "Success!";	
	public static String admin_user_UpdateComplaintStatus_false_status = "Failure";	
	
	
	//******************************************Notification Table**********************************************//
//	public static String noti_type_user = "complaint";
	public static String noti_type_depot = "";
	public static String noti_type_division = "";
	public static String noti_type_state = "";
	public static String noti_type_super = "";
	
	public static String noti_msg_user = "Your complaint has been created successfully!";
	public static String noti_msg_user1 = "Your complaint is under review by our administrator.";
	public static String noti_msg_user2 = "Your complaint has been resovled";
	
	public static String noti_msg_depot = "User created a complaint.";
	
	
	public static String noti_type = "complaint";
	
	
	
}
