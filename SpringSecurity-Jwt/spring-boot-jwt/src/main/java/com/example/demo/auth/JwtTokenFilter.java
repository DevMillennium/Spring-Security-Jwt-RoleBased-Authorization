package com.example.demo.auth;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	// Bir tane filtre ekliyoruz içeriye gelen tüm requestleri kontrol edecek 
	// Bu requestler her bir isteğe bakması için OncePerRequestFilter denilen bir classtan türetilmesi gerekiyor
	
    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        /**
         * "Bearer 123hab2355"
         */
    	//requestin içerisinden authorization diye bir headeri alıp bir değişkene koyuyoruz
        final String authHeader = httpServletRequest.getHeader("Authorization");

        String username = null;
        String token = null;


        if (authHeader != null && authHeader.contains("Bearer")) {
            token = authHeader.substring(7);// 7. karakterden itibaren kesip alırsam benim elimde tokenım olucak
            try {
                username = tokenManager.getUsernameToken(token);// bu token doğru mudur
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (username != null && token != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {// son ve bu kullanıcı sisteme hiç girmemişse bu token ile
            if (tokenManager.tokenValidate(token)) { // bu kişinin valid bir tokena sahip olmadığına bakıcaz
                UsernamePasswordAuthenticationToken upassToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(upassToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
