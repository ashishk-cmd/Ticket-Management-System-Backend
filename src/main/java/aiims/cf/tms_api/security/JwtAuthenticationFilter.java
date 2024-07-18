package aiims.cf.tms_api.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Import the LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    // Initialize the logger
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Use the logger
        logger.info("JwtAuthenticationFilter is processing the request.");

        // 1.get token
    	final String requestToken = request.getHeader("Authorization");
    	
        //Bearer
        
        logger.info("Request Token: {}", requestToken);
        
        String username = null;
        
        String token = null;
        
        if(requestToken!=null && requestToken.startsWith("Bearer")) {
            
            token = requestToken.substring(7);
            try {
                username = this.jwtTokenHelper.getUsernameFromToken(token);                
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT token: {}", e.getMessage());
            } catch(ExpiredJwtException e) {
                logger.error("JWT token has expired: {}", e.getMessage());
            } catch(MalformedJwtException e) {
                logger.error("Invalid JWT token: {}", e.getMessage());
            }
        }
        else {
            logger.warn("JWT token does not begin with Bearer");
        }
        
        
        // once we get the token, now validate
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            if(this.jwtTokenHelper.validateToken(token, userDetails)) {
                
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                logger.warn("Invalid JWT token");
            }
            
            
        } else {
            logger.warn("Username is null or context is not null.");
        }
        
        
        filterChain.doFilter(request, response);
    }
    

}
