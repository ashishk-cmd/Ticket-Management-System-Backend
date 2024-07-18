package aiims.cf.tms_api.models;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="users")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable=false)
	private String fullName;
	@Column(nullable=false,unique = true)
	private String employeeId;
	@Column(nullable=false,unique = true)
	private String contactNo;
	private String email;
	@Transient
	private Lob profile;
	@Transient
	private Lob idCard;
	@Column(nullable=false)
	private String status;
	private Boolean enabled;
	@Column(nullable=false)
	private String password;
	@Column(nullable=false)
	private Date registeredOn;
	private Date otpSentOn; 
	private int otp;
	
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name="user_role",
				joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name="role",referencedColumnName = "id")
			)
	private Set<Role> roles = new HashSet<>();

	@OneToOne
	private Department department;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authories = this.roles.stream().map((role)-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
		return authories;
	}

	@Override
	public String getUsername() {
		return this.employeeId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Lob getProfile() {
		return profile;
	}

	public void setProfile(Lob profile) {
		this.profile = profile;
	}

	public Lob getIdCard() {
		return idCard;
	}

	public void setIdCard(Lob idCard) {
		this.idCard = idCard;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}

	public Date getOtpSentOn() {
		return otpSentOn;
	}

	public void setOtpSentOn(Date otpSentOn) {
		this.otpSentOn = otpSentOn;
	}

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", fullName=" + fullName + ", employeeId=" + employeeId + ", contactNo=" + contactNo
				+ ", email=" + email + ", profile=" + profile + ", idCard=" + idCard + ", status=" + status
				+ ", enabled=" + enabled + ", password=" + password + ", registeredOn=" + registeredOn + ", otpSentOn="
				+ otpSentOn + ", otp=" + otp + ", roles=" + roles + ", department=" + department + "]";
	}
    
	
}
