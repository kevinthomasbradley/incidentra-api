package com.kevinthomasbradley.incidentapi;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * JWT authentication filter for processing incoming HTTP requests.
 * <p>
 * This filter checks for a JWT token in the Authorization header of each request.
 * If a valid token is found, it extracts the username, loads the user details,
 * validates the token, and sets the authentication in the Spring Security context.
 * </p>
 * <ul>
 *   <li>Extends {@link OncePerRequestFilter} to ensure a single execution per request.</li>
 *   <li>Relies on {@link JwtUtil} for token parsing and validation.</li>
 *   <li>Uses {@link UserDetailsService} to load user information from the database or other source.</li>
 * </ul>
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    /**
     * Utility for JWT operations such as extracting username and validating tokens.
     */
    private final JwtUtil jwtUtil;

    /**
     * Service for loading user-specific data during authentication.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Filters each HTTP request to check for a valid JWT token.
     * <ul>
     *   <li>If the Authorization header is missing or does not start with "Bearer ", the filter chain continues.</li>
     *   <li>If a valid JWT is found, the user's authentication is set in the security context.</li>
     *   <li>Any exceptions during processing are caught and ignored (optionally log or handle as needed).</li>
     * </ul>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7); // Remove "Bearer " prefix
            final String username = jwtUtil.extractUsername(jwt);
            
            // Authenticate only if username is present and no authentication exists in the context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Optionally log the exception for debugging
            // e.printStackTrace();
        }
        
        filterChain.doFilter(request, response);
    }
}
