package aiims.cf.tms_api.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aiims.cf.tms_api.exceptions.ApiException;
import aiims.cf.tms_api.models.Role;
import aiims.cf.tms_api.models.User;
import aiims.cf.tms_api.payload.requests.EmployeeRequest;
import aiims.cf.tms_api.payload.requests.JwtAuthRequest;
import aiims.cf.tms_api.payload.requests.OtpRequest;
import aiims.cf.tms_api.payload.requests.PassKeyRequest;
import aiims.cf.tms_api.payload.responses.JwtAuthResponse;
import aiims.cf.tms_api.payload.responses.UserResponse;
import aiims.cf.tms_api.repositories.UserRepo;
import aiims.cf.tms_api.security.JwtTokenHelper;
import aiims.cf.tms_api.services.AdminServices;
import aiims.cf.tms_api.utils.CryptoUtil;
import jakarta.validation.Valid;
@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AdminServices adminServices;
	@Autowired
	private UserRepo userRepository;
	 
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@Valid @RequestBody JwtAuthRequest request) throws Exception{
		try {
			String pass = CryptoUtil.decrypt(request.getPassword().trim());
			request.setPassword(pass.trim());
			String Username = request.getUsername().toUpperCase();
			this.authenticate(Username,request.getPassword());
			
			UserDetails userDetails  = userDetailsService.loadUserByUsername(Username);
			String token = jwtTokenHelper.generateToken(userDetails);
			
			JwtAuthResponse response = new JwtAuthResponse();
			User user = this.userRepository.findByEmployeeId(Username).orElse(null);
			if(user != null) {
				Set<Role> role = this.adminServices.getRole(user);
				response.setEmpId(user.getEmployeeId());
				response.setUsername(user.getFullName());
				response.setToken(token);
				response.setRole(role);		
				return ResponseEntity.ok(response);
		    } else {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		    }
		} catch (Exception e) {
			throw new ApiException("Invalid username or password !!");
		}
		
	}

	private void authenticate(String username,String password) throws Exception {
		UsernamePasswordAuthenticationToken authenticationToken = new  UsernamePasswordAuthenticationToken(username,password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new ApiException("Invalid username or password !!");
		}
			
	}
	
	
	//register new user 
	@PostMapping("/forgetPassword")
	public ResponseEntity<Object> forgetUserPassword(@Valid @RequestBody EmployeeRequest user){
		try {
			UserResponse forgetUserPassword = this.adminServices.forgetUserPassword(user);
			return new ResponseEntity<Object>(forgetUserPassword,HttpStatus.OK);				
		} catch (Exception e) {
			return new ResponseEntity<Object>(e,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/verifyOtp")
	public ResponseEntity<Object> VerifyOtp(@Valid @RequestBody OtpRequest user){
		try {
			UserResponse VerifyOtp = this.adminServices.VerifyOtp(user);
			return new ResponseEntity<Object>(VerifyOtp,HttpStatus.OK);				
		} catch (Exception e) {
			return new ResponseEntity<Object>(e,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/updatePassword")
	public ResponseEntity<Object> updatePassword(@Valid @RequestBody PassKeyRequest user){
		try {
			UserResponse updatePassword = this.adminServices.updatePassword(user);
			return new ResponseEntity<Object>(updatePassword,HttpStatus.OK);				
		} catch (Exception e) {
			return new ResponseEntity<Object>(e,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}
