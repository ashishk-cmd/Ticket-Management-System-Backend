package aiims.cf.tms_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import aiims.cf.tms_api.security.CustomUserDetailService;
import aiims.cf.tms_api.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig  {
	public static final String[] PUBLIC_URLS= {
			"/api/v1/auth/**",
			"/v3/apis/docs",
			"/v2/api-docs",
			"/swagger-resources/**",
			"/swagger-ui/**",
			"/webjars/**",
			"/admin/**",
			"/admin/addGrievance",
			"/admin/get-department-list",
			"/admin/check-grievance-status",
			"/admin/verify-grievance-status",
			"/admin/verify_user",
			"/admin/get-employeeDetail",
			"/admin/logout",
			"/admin/register",
			"/admin/add-user-roles"
	};
	
	@Autowired
    private JwtAuthenticationFilter authFilter;
	
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
        return http .csrf(csrf -> csrf.disable())
        		    .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(PUBLIC_URLS).permitAll()
//                       .requestMatchers("/admin/**").hasAnyAuthority(AppConstants.roleAdmin)
//                        .requestMatchers("/common/**").authenticated()
                        .anyRequest().authenticated()
                     )
	                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .authenticationProvider(authenticationProvider())
	                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
	                .build();
        
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

	
}
