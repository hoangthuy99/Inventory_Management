package com.ra.inventory_management.filter;


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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response); // enable bypass
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

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserGoogle userGoogle = userGoogleRepository.findByEmail(email).orElse(null);

                if (userGoogle != null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userGoogle,
                            null,
                            userGoogle.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    Users userDetails = (Users) userDetailsService.loadUserByUsername(username);

                    if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } else {
                        if (!response.isCommitted()) {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                            return;
                        }
                    }
                }
            }
            filterChain.doFilter(request, response); // enable bypass

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }

    }

    private boolean isBypassToken(@NotNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/app/auth/login", "POST"),
                Pair.of("/app/auth/oauth-login", "POST"),
                Pair.of("/app/auth/oauth-register", "POST"),
                Pair.of("/uploads/branch/", "GET")
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
