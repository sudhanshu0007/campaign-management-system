package com.sampana.cms.security.model.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

import com.sampana.cms.security.exceptions.InvalidJwtTokenException;
import com.sampana.cms.security.exceptions.JwtExpiredTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * 
 * @author Sudhanshu Chaturvedi
 *
 */
public class RawAccessJwtToken implements JwtToken {
    private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);
            
    private String token;
    
    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    /**
     * Parses and validates JWT Token signature.
     * 
     * @throws BadCredentialsException
     * @throws JwtExpiredTokenException
     * 
     */
    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            logger.error("Invalid JWT Token", ex.getMessage());
            throw new InvalidJwtTokenException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            logger.info("JWT Token is expired", expiredEx.getMessage());
            throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
        }
    }

    @Override
    public String getToken() {
        return token;
    }
}
