package com.api.usafety_backend.configs;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.usafety_backend.util.Constantes;

import io.jsonwebtoken.ExpiredJwtException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(Constantes.HEADER_AUTHORIZATION);
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith(Constantes.PREFIXO_TOKEN)) {
            authToken = header.replace(Constantes.PREFIXO_TOKEN, "");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                logger.error("Ocorreu um erro ao buscar o nome de usuário do token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("O token está expirado", e);
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthenticationToken(authToken,
                        SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                logger.info("Usuario " + username + " esta autenticado");

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }

}
