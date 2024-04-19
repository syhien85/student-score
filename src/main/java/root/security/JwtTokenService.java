package root.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class JwtTokenService {

    /*@Value("${jwt.secret}")*/
    private static final String SECRET_KEY = "IT_IS_NOT_LOOKING_GOOD_@#_IT_IS_NOT_LOOKING_GOOD";
    private static final long VALIDITY = 15;

    // để tạo một token cần: secret key và thời gian hết hạn
    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        /*Date now = new Date();
        Date exp = new Date(now.getTime() + VALIDITY * 60 * 1000);*/

        return Jwts
            .builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(VALIDITY, DAYS)))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // đọc body của token (subject)
    public Claims getClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        String subject = getSubject(token);
        return subject.equals(username) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date today = Date.from(Instant.now());
        return getClaims(token).getExpiration().before(today);
    }
}
