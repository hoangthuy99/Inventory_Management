package com.ra.inventory_management.filter;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.ra.inventory_management.model.entity.UserGoogle;
import com.ra.inventory_management.model.entity.Users;
import com.ra.inventory_management.reponsitory.UserGoogleRepository;
import com.ra.inventory_management.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Qualifier("userDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserGoogleRepository userGoogleRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Bỏ qua các endpoint không cần xác thực token
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            final String authToken = authHeader.substring(7);
            final String username = jwtTokenUtil.extractUsername(authToken);
            final String email = jwtTokenUtil.extractEmail(authToken);

            // Nếu chưa có xác thực
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Kiểm tra xem có phải là người dùng đăng nhập qua Firebase
                if (request.getServletPath().startsWith("/app/auth/oauth-login")) {
                    try {
                        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(authToken);
                        String firebaseUid = decodedToken.getUid();

                        // Kiểm tra nếu người dùng đã tồn tại trong hệ thống của bạn
                        UserGoogle userGoogle = userGoogleRepository.findByEmail(email).orElse(null);
                        if (userGoogle != null) {
                            // Nếu có user Google, cho phép tiếp tục
                            filterChain.doFilter(request, response);
                            return;
                        }
                    } catch (Exception e) {
                        // Nếu không xác thực được token Firebase
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase Token");
                        return;
                    }
                }

                // Nếu là user bình thường (JWT token)
                Users userDetails = (Users) userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return;
                }
            }

            // Cuối cùng cho phép tiếp tục
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }



    private boolean isBypassToken(@NotNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/app/auth/login", "POST"),
                Pair.of("/app/auth/oauth-login", "POST"),
                Pair.of("/app/auth/oauth-register", "POST"),
                Pair.of("/uploads", "GET"),
                Pair.of("/swagger-ui", "GET"),
                Pair.of("/v3/api-docs", "GET")
        );
        System.out.println(request.getServletPath());
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())
            ) {
                return true;
            }
        }

        return false;
    }
}
