package com.company.ems.security.jwt;

import com.company.ems.common.UserStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    private static final Set<String> LOCKED_USER_ALLOWED_PATHS = Set.of(
            "/auth/login",
            "/auth/me",
            "/auth/change-password"
    );
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/uploads/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        if (jwt.isBlank() || "null".equalsIgnoreCase(jwt) || "undefined".equalsIgnoreCase(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final String userEmail = jwtService.extractUsername(jwt);
            if (userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (!jwtService.isTokenValid(jwt, userDetails)) {
                filterChain.doFilter(request, response);
                return;
            }
            if (userDetails instanceof UserPrincipal userPrincipal) {
                String requestURI = request.getRequestURI();
                requestURI = requestURI.replaceFirst("/api/v\\d+", "");
                if (userPrincipal.getStatus() == UserStatus.LOCKED &&
                        LOCKED_USER_ALLOWED_PATHS.stream().noneMatch(requestURI::startsWith)) {
                    writeJsonResponse(response,
                            HttpServletResponse.SC_FORBIDDEN,
                            "Please change your password to access the system");
                    return;
                }
                if (userPrincipal.getStatus() == UserStatus.INACTIVE) {
                    writeJsonResponse(response,
                            HttpServletResponse.SC_FORBIDDEN,
                            "Your account is deactivated. Please contact administration.");
                    return;
                }
            }
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (ExpiredJwtException e) {
            writeJsonResponse(response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "JWT token has expired. Please login again.");
            return;
        } catch (MalformedJwtException | SignatureException e) {
            writeJsonResponse(response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or corrupted authorization token.");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void writeJsonResponse(HttpServletResponse response,
                                   int status,
                                   String message) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("""
            {
              "success": false,
              "message": "%s",
              "data": null
            }
        """.formatted(message));
    }
} 