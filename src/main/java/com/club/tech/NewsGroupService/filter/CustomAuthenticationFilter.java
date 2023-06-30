package com.club.tech.NewsGroupService.filter;

import com.club.tech.NewsGroupService.service.JwtHelperService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
  private final UserDetailsService userDetailsService;
  private final JwtHelperService jwtHelperService;

  public CustomAuthenticationFilter(
      UserDetailsService userDetailsService, JwtHelperService jwtHelperService) {
    this.userDetailsService = userDetailsService;
    this.jwtHelperService = jwtHelperService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String requestHeader = request.getHeader("authorization");

    log.info("Header : %s".formatted(requestHeader));

    String username = null;
    String token = null;

    if (requestHeader != null && requestHeader.startsWith("Bearer")) {
      token = requestHeader.substring(7);
      username = jwtHelperService.getUsernameFromToken(token);

    } else {
      log.info("Invalid Header Value !! ");
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      // fetch user detail from username
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
      Boolean validateToken = jwtHelperService.validateToken(token, userDetails);

      if (validateToken) {

        // set the authentication
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

      } else {
        log.info("Validation fails !!");
      }
    }

    filterChain.doFilter(request, response);
  }
}
