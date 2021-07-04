package com.example.demo.auth;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Claims;
@Service
public class TokenManager {
//Tokenı oluşturan validate eden class
    private static final int validity = 5 * 60 * 1000;
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // keyi şifreleme algoritması 

    
    // Benim için bir token oluşturuldu geri döndürülüyor
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // kullanıcı adı atanmsası
                .setIssuer("dogandemir") // tokenı imzalayan oluşturan
                .setIssuedAt(new Date(System.currentTimeMillis())) // oluşturulma tarihi
                .setExpiration(new Date(System.currentTimeMillis() + validity)) // tokenın geçerlilik süresi
                .signWith(key) // tokenı oluştururken kullacağımız algoritma
                .compact();
    }

    //
    public boolean tokenValidate(String token) {
    	// tokenı verdiğimizde içinden username çıkartılabiliyorsa ve süresi dolmamışsa doğrudur
        if (getUsernameToken(token) != null && isExpired(token)) {
            return true;
        }
        return false;
    }

    public String getUsernameToken(String token) {
    	
    	// ıssue bir claimdir , subject bir claimdir claimslere token ile eriştik subjecti return
    	// ettiğimizde bize adını verir
        Claims claims = getClaims(token);
        return claims.getSubject();// username i tokenımızın içerisinden çıkarmış olduk
    }

    public boolean isExpired(String token) {
        Claims claims = getClaims(token);
        // tokenın bitiş tarihinin şu anki tarihten sonra olması lazım ki doğru olduğunu kabul edelim
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

}
