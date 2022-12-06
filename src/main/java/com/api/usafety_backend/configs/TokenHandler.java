package com.api.usafety_backend.configs;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.api.usafety_backend.util.Constantes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenHandler implements Serializable {

    private final Constantes constantes = new Constantes();

    public UserPrincipal parseUserFromToken(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(constantes.SENHA_TOKEN.getBytes()))
                .parseClaimsJws(token)
                .getBody();
        String username = body.getSubject();

        List<String> cargos = body.get("cargos", List.class);

        return new UserPrincipal(username, cargos);
    }

    public String createTokenForUser(UserPrincipal user) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(expirationDate)
                .claim("cargos", user.getAuthorities())
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(constantes.SENHA_TOKEN.getBytes()))
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + constantes.EXPIRACAO_TOKEN);
    }

}
