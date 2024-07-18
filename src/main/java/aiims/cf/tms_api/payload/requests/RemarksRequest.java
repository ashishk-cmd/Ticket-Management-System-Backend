package aiims.cf.tms_api.payload.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RemarksRequest {

	@NotNull(message="Please provide Grievance Refrence Number.")
	private String grievanceRefNo;
	@NotEmpty(message="Please provide remarks.")
	private String remarks;
	public String getGrievanceRefNo() {
		return grievanceRefNo;
	}
	public void setGrievanceRefNo(String grievanceRefNo) {
		this.grievanceRefNo = grievanceRefNo;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
