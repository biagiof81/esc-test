package it.biagio.esc.test.web.security;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Singleton
public class TokenProvider {

    private static final Logger LOGGER = Logger.getLogger(TokenProvider.class.getName());

    private static final String AUTHORITIES_KEY = "auth";

    private String secretKey;

    private long tokenValidity;

    @PostConstruct
    public void init() {
        /*
         * KeyGenerator generator; try { generator =
         * KeyGenerator.getInstance("AES", "BC"); generator.init(64); secretKey
         * = generator.generateKey(); } catch (NoSuchAlgorithmException e) {
         * 
         * e.printStackTrace(); } catch (NoSuchProviderException e) {
         * 
         * e.printStackTrace(); }
         */ 
        this.secretKey = "my-secret-jwt-key";
        this.tokenValidity = TimeUnit.MINUTES.toMillis(10);

    }

    public String createToken(String userName, Set<String> authorities) {

        long now = Instant.now().toEpochMilli();

        return Jwts.builder().setSubject(userName)
                .claim(AUTHORITIES_KEY, authorities.stream().collect(Collectors.joining(",")))
                .signWith(SignatureAlgorithm.HS512, secretKey).setExpiration(new Date(now + tokenValidity)).compact();

    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.log(Level.INFO, "Invalid JWT signature: {0}", e.getMessage());
            return false;
        }
    }

    public JWTCredentials getCredential(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

        Set<String> authorities = new HashSet<String>(Arrays.asList(claims.get(AUTHORITIES_KEY).toString().split(",")));

        return new JWTCredentials(claims.getSubject(), authorities);
    }
}
