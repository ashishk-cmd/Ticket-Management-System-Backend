package aiims.cf.tms_api.config;

public class AppConstants {

	public static final String PAGE_NUMBER="0";
	public static final String PAGE_SIZE="10";
	public static final String SORT_BY="postId";
	public static final String SORT_DIR="asc";

	public static final String ACTIVE= "ACTIVE";
	public static final String INACTIVE = "INACTIVE";
	
	public static final String NORMAL_USER= "ROLE_NORMAL";
	public static final String ADMIN_USER= "ROLE_ADMIN";
	public static final String HELPDESK_USER= "ROLE_HELPDESK";
	
	public static final String NEW_USER_STATUS= "NEW";
	public static final String ACTIVE_USER_STATUS= "ACTIVE";
	public static final String INACTIVE_USER_STATUS= "INACTIVE";
	
	public static final String NEW_GRIEVANCE_STATUS= "NEW";
	public static final String FORWARDED_GRIEVANCE_STATUS= "FORWARDED";
	public static final String ACCEPTED_GRIEVANCE_STATUS= "ACCEPTED";
	public static final String CLOSED_GRIEVANCE_STATUS= "CLOSED";
	public static final String ACTIVE_GRIEVANCE_STATUS= "ACTIVE";

	public static final String ASSIGNED= "ASSIGNED";
	public static final String NOTASSIGNED= "NOTASSIGNED";
	
	//for testing
//	public static final String AUTHENTICATEURL = "https://aiimsdelhi.uat.dcservices.in/HFMSWebServices/employee/authenticate";
//	public static final String EMPLOYEEDATAURL = "https://aiimsdelhi.uat.dcservices.in/HFMSWebServices/employee/Data/getEmployeeData/";
	//for live
	public static final String AUTHENTICATEURL = "https://aiimsdelhi.prd.dcservices.in/HFMSWebServices/employee/authenticate";
	public static final String EMPLOYEEDATAURL = "https://aiimsdelhi.prd.dcservices.in/HFMSWebServices/employee/Data/getEmployeeData/";
	public static final String CLIENTID = "AIIMS";
	public static final String CLIENTSECRET = "AIIMS@DL";
	public static final String CLIENTSERID = "1";
	              
	public static final String SEARCHMODE = "2";
	public static final String ORGCODE = "33101";
	public static final String roleAdmin = "ROLE_ADMIN";
	
	
}
