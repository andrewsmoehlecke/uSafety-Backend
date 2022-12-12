package com.api.usafety_backend.configs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.api.usafety_backend.util.Constantes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider implements Serializable {

    private final Constantes constantes = new Constantes();

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(constantes.SENHA_TOKEN.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(constantes.CARGOS, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Expiração deve ser melhorada
                .setExpiration(new Date(System.currentTimeMillis() + constantes.EXPIRACAO_TOKEN ^ 6))
                .signWith(SignatureAlgorithm.HS256, constantes.SENHA_TOKEN)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(final String token, final Authentication existingAuth,
            final UserDetails userDetails) {

        return new UsernamePasswordAuthenticationToken(userDetails, "", new ArrayList<>());
    }

    @Autowired
    private TokenHandler tokenHandler;

    /**
     * Adiciona no cabeçalho um token válido para o usuário recebido na
     * autenticação.
     *
     * @param response       HttpServletResponse onde será inserido o token.
     * @param authentication Autenticação com o usuário autorizado.
     */
    public void addAuthentication(HttpServletResponse response, Authentication authentication) {
        final UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        response.addHeader(constantes.HEADER_AUTHORIZATION, tokenHandler.createTokenForUser(user));
    }

    /**
     * Pega o token do HttpServletRequest, abre o token e pega o usuário e suas role
     * e devolve em um objeto
     * {@link Authentication}.
     *
     * @param request {@code HttpServletRequest} com o token no cabeçalho.
     * @return Objeto {@link Authentication} com as informações do usuário.
     */
    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(constantes.HEADER_AUTHORIZATION);

        if (!token.isBlank()) {
            final UserPrincipal user = tokenHandler.parseUserFromToken(token);

            if (user != null) {
                Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                return auth;
            }
        }
        return null;
    }

}
