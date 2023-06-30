package com.club.tech.NewsGroupService.service;

import com.club.tech.NewsGroupService.config.JwtSingKeyConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtHelperService {
  private final JwtSingKeyConfig jwtSingKeyConfig;

  public JwtHelperService(JwtSingKeyConfig jwtSingKeyConfig) {
    this.jwtSingKeyConfig = jwtSingKeyConfig;
  }

  public String getToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, username);
  }

  public String createToken(Map<String, Object> claims, String username) {
    int extraTimeInMillis = getExtraTimeInMillis(username);
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + extraTimeInMillis))
        .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getSignatureKey() {
    byte[] keyByte = Decoders.BASE64.decode(jwtSingKeyConfig.signKey());
    return Keys.hmacShaKeyFor(keyByte);
  }

  // retrieve username from jwt token
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  // retrieve expiration date from jwt token
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  // get all information from token we will need the secret key
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(jwtSingKeyConfig.signKey()).parseClaimsJws(token).getBody();
  }

  // check if the token has expired
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  // validate token
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private int getExtraTimeInMillis(String username) {

    int extraTimeInMillis = 1000 * 10 * 60; // set extra 10 mins for non admin user
    if (username.contains("admin")) {
      extraTimeInMillis = 1000 * 20 * 60; // set extra 20 mins for admin user
    }
    return extraTimeInMillis;
  }
}
