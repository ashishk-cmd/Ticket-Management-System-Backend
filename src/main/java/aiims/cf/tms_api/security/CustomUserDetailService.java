package aiims.cf.tms_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import aiims.cf.tms_api.config.AppConstants;
import aiims.cf.tms_api.exceptions.ApiException;
import aiims.cf.tms_api.exceptions.ResourceNotFoundException;
import aiims.cf.tms_api.models.User;
import aiims.cf.tms_api.repositories.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepo.findByEmployeeId(username.toUpperCase()).orElseThrow(()-> new ResourceNotFoundException("User", "Employee Id : ", username.toUpperCase()));
		if(user.getStatus().equalsIgnoreCase("NEW")) {
			user.setStatus(AppConstants.ACTIVE_USER_STATUS);
			this.userRepo.save(user);
		}
		if(user.getEnabled().equals(true)) {
			return user;			
		}else {
			throw new ApiException("Invalid username or password. Contact to your Admin!!");
		}
		
	}
}
