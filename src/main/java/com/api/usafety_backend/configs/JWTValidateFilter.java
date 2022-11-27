package com.api.usafety_backend.configs;

import java.io.IOException;
import java.security.SignatureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.api.usafety_backend.entities.Usuario;
import com.api.usafety_backend.repositories.UsuarioRepository;
import com.api.usafety_backend.util.Constantes;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

public class JWTValidateFilter extends BasicAuthenticationFilter {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenProvider tokenProvider;

    private final Logger log = LoggerFactory.getLogger(JWTValidateFilter.class);

    public JWTValidateFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse res,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, x-requested-with, Authorization, Content-Type, "
                + "withCredentials, " + Constantes.HEADER_AUTHORIZATION);
        response.setHeader("Access-Control-Expose-Headers", Constantes.HEADER_AUTHORIZATION);
        response.setHeader("p3p", "CP=\"This is not a P3P policy!\"");

        if (!httpRequest.getMethod().equals("OPTIONS")) {
            try {
                Authentication authentication = tokenProvider.getAuthentication(httpRequest);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException ex) {
                log.warn("O token recebido Está expirado!");
                SecurityContextHolder.getContext().setAuthentication(null);
                response.sendError(440, "Token expired. " + ex.getMessage());
                return;
            } catch (MalformedJwtException se) {
                log.warn("O token recebido é inválido!");
                SecurityContextHolder.getContext().setAuthentication(null);
                response.sendError(HttpStatus.UNAUTHORIZED.value(), se.getMessage());
                return;
            } catch (Exception ex) {
                log.error("Unexpected error", ex);
                return;
            }

            chain.doFilter(request, res);
        }
        SecurityContextHolder.getContext().setAuthentication(null);

        // String atributo = request.getHeader(Constantes.HEADER_AUTHORIZATION);

        // if (atributo == null || !atributo.startsWith(Constantes.PREFIXO_TOKEN)) {
        // chain.doFilter(request, response);
        // return;
        // }

        // String token = atributo.replace(Constantes.PREFIXO_TOKEN, "");

        // UsernamePasswordAuthenticationToken authenticationToken =
        // getAuthenticationToken(token);

        // SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        String usuario = JWT.require(Algorithm.HMAC512(Constantes.SENHA_TOKEN))
                .build()
                .verify(token)
                .getSubject();

        if (usuario != null) {
            return null;
        }

        Usuario u = usuarioRepository.findByUsername(usuario);

        return new UsernamePasswordAuthenticationToken(usuario, null, u.getAuthorities());
    }
}
