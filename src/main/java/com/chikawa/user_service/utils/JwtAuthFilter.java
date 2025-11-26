package com.chikawa.user_service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @NonFinal //không bị inject contructor
    @Value("${jwt.signerKey}") //anotation này được sử dụng để đọc biến trong file .yaml
    //https://generate-random.org/
    protected String SIGN_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // ⬇️ Extract userId từ JWT
                Long userId = getUserIdFromToken(token);  // bạn tự implement

                if (userId != null) {
                    UserContextHolder.setUserId(userId);
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear(); // luôn clear
        }
    }

    private Long getUserIdFromToken(String token) {
        // Ví dụ: parse JWT bằng io.jsonwebtoken
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SIGN_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }
}

