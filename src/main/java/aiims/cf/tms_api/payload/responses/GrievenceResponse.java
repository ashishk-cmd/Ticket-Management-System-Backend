package aiims.cf.tms_api.payload.responses;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import aiims.cf.tms_api.models.Category;
import aiims.cf.tms_api.models.Department;

public class GrievenceResponse  
{
	private String refNo;

	private Department department = new Department();
	
	private Category category = new Category();
	
	private String concern;
	
	private UserResponse userRaisedBy = new UserResponse();
	private String grievanceStatus;
//	@JsonIgnore
	private String lastUpdate;
//	@JsonIgnore
	private String raisedOn;

	private UserResponse currentUser = new UserResponse();
	private String image1;
	private String image2;
	
	private List<RemarksResponse> remarksResponse = new ArrayList<RemarksResponse>();

	
	public GrievenceResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GrievenceResponse(String refNo, Department department, Category category, String concern,
			UserResponse userRaisedBy, String grievanceStatus, String lu, String rOn, UserResponse currentUser,
			String image1, String image2, List<RemarksResponse> remarksResponse) {
		super();
		this.refNo = refNo;
		this.department = department;
		this.category = category;
		this.concern = concern;
		this.userRaisedBy = userRaisedBy;
		this.grievanceStatus = grievanceStatus;
		this.lastUpdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lu);
		this.raisedOn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rOn);
		this.currentUser = currentUser;
		this.image1 = image1;
		this.image2 = image2;
		this.remarksResponse = remarksResponse;
	}

	
	@Override
	public String toString() {
		return "GrievenceResponse [refNo=" + refNo + ", department=" + department + ", category=" + category
				+ ", concern=" + concern + ", userRaisedBy=" + userRaisedBy + ", grievanceStatus=" + grievanceStatus
				+ ", lastUpdate=" + lastUpdate + ", raisedOn=" + raisedOn + ", currentUser=" + currentUser + ", image1="
				+ image1 + ", image2=" + image2 + ", remarksResponse=" + remarksResponse + "]";
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getConcern() {
		return concern;
	}

	public void setConcern(String concern) {
		this.concern = concern;
	}

	public UserResponse getUserRaisedBy() {
		return userRaisedBy;
	}

	public void setUserRaisedBy(UserResponse userRaisedBy) {
		this.userRaisedBy = userRaisedBy;
	}

	public String getGrievanceStatus() {
		return grievanceStatus;
	}

	public void setGrievanceStatus(String grievanceStatus) {
		this.grievanceStatus = grievanceStatus;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getRaisedOn() {
		return raisedOn;
	}

	public void setRaisedOn(String raisedOn) {
		this.raisedOn = raisedOn;
	}

	public UserResponse getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(UserResponse currentUser) {
		this.currentUser = currentUser;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public List<RemarksResponse> getRemarksResponse() {
		return remarksResponse;
	}

	public void setRemarksResponse(List<RemarksResponse> remarksResponse) {
		this.remarksResponse = remarksResponse;
	}

	
	
}
