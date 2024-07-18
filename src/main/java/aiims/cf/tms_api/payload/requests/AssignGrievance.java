package aiims.cf.tms_api.payload.requests;

import jakarta.validation.constraints.NotNull;

public class AssignGrievance {

	@NotNull(message="Please provide Grievance Refrence Number.")
	private String grievanceRefNo;
	@NotNull(message="Please provide Employee Id.")
	private String empId;
	public String getGrievanceRefNo() {
		return grievanceRefNo;
	}
	public void setGrievanceRefNo(String grievanceRefNo) {
		this.grievanceRefNo = grievanceRefNo;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
}
