package aiims.cf.tms_api.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Grievence 
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	private String refNo;
	

	@ManyToOne
	private Department department;
	

	@ManyToOne
	private Category category;
	
	private String concern;
	

	@ManyToOne
	private User userRaisedBy;
	
	private String grievanceStatus;
	private Date lastUpdate;
	private Date raisedOn;
	

	@ManyToOne
	private User currentUser;
	
	@Column(nullable = true)
	private String image1;
	@Column(nullable = true)
	private String image2;
	
	private int otp;
	private Date otpsenton;
//	@OneToMany
//	private List<RemarksResponse> remarksResponse = new ArrayList<RemarksResponse>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public User getUserRaisedBy() {
		return userRaisedBy;
	}
	public void setUserRaisedBy(User userRaisedBy) {
		this.userRaisedBy = userRaisedBy;
	}
	public String getGrievanceStatus() {
		return grievanceStatus;
	}
	public void setGrievanceStatus(String grievanceStatus) {
		this.grievanceStatus = grievanceStatus;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Date getRaisedOn() {
		return raisedOn;
	}
	public void setRaisedOn(Date raisedOn) {
		this.raisedOn = raisedOn;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
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
	public int getOtp() {
		return otp;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}
	public Date getOtpsenton() {
		return otpsenton;
	}
	public void setOtpsenton(Date otpsenton) {
		this.otpsenton = otpsenton;
	}
	
}
