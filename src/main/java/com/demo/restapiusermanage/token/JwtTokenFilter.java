package com.demo.restapiusermanage.token;

import com.demo.restapiusermanage.entity.Role;
import com.demo.restapiusermanage.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if(!hasAuthorizationHeader(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = getAccessToken(request);
        System.out.println("Access token: " + accessToken);
        setAuthenticationContext(accessToken, request);
        filterChain.doFilter(request, response);
    }
    private boolean hasAuthorizationHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        System.out.println("Authorization header:" + header);
        if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }
        return true;
    }
    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String token) {
        User userDetails = new User();
        Claims claims = jwtTokenUtil.parseClaims(token);

        String claimRoles = (String) claims.get("roles");
        System.out.println("claimRoles:" + claimRoles);
        claimRoles = claimRoles.replace("[", "").replace("]", "");

        String[] roleNames = claimRoles.split(",");
        for(String role : roleNames){
            userDetails.addRole(new Role(role));
        }
        String subject = (String) claims.get(claims.SUBJECT);

        String[] jwtSubject = subject.split(",");

        userDetails.setId(Long.parseLong(jwtSubject[0]));
        userDetails.setEmail(jwtSubject[1]);

        return userDetails;
    }


}
