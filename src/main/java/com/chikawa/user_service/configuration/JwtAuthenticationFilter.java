package com.chikawa.user_service.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomJwtDecoder customJwtDecoder;

    public JwtAuthenticationFilter(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // ✅ Decode JWT bằng CustomJwtDecoder
                var jwt = customJwtDecoder.decode(token);
                String email = jwt.getSubject();              // lấy subject/email
                String jti = jwt.getClaimAsString("jti");    // lấy jti nếu cần

                // Create Authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,   // principal
                                token,   // credentials → lưu JWT
                                List.of() // authorities nếu cần
                        );

                // Đưa vào SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // nếu decode thất bại, bỏ qua và để filter tiếp tục
                logger.warn("Invalid JWT token: {}"+ e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
