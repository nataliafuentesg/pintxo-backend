package com.pintxo.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    public JwtAuthFilter(JwtService jwt){ this.jwt = jwt; }


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String auth = req.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                io.jsonwebtoken.Claims claims = jwt.parse(token).getBody();
                String email = claims.getSubject();

                var auths = new java.util.ArrayList<org.springframework.security.core.authority.SimpleGrantedAuthority>();
                Object r = claims.get("roles");
                if (r instanceof java.util.Collection<?> col) {
                    for (Object it : col) auths.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_"+it));
                }
                if (!auths.isEmpty()) {
                    var up = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(email, null, auths);
                    up.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(req));
                    org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(up);
                } else {
                    org.springframework.security.core.context.SecurityContextHolder.clearContext();
                }
            } catch (Exception ignored) {
                org.springframework.security.core.context.SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(req, res);
    }
}

