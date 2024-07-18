package aiims.cf.tms_api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Remarks {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	@ManyToOne
	private User toUser;
	@ManyToOne
	private User fromUser;
	@ManyToOne
	private Grievence grievence;
	private String remarks;
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public User getToUser() {
		return toUser;
	}
	public void setToUser(User toUser) {
		this.toUser = toUser;
	}
	public User getFromUser() {
		return fromUser;
	}
	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}
	public Grievence getGrievence() {
		return grievence;
	}
	public void setGrievence(Grievence grievence) {
		this.grievence = grievence;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
