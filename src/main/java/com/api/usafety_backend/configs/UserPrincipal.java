package com.api.usafety_backend.configs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.api.usafety_backend.entities.Usuario;

public class UserPrincipal implements UserDetails {

    private String username;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private boolean enable;

    public UserPrincipal(Usuario u) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        this.username = u.getUsername();
        this.password = u.getSenha();
        this.enable = u.isAtivo();

        authorities = u.getCargos().stream().map(cargo -> {
            return new SimpleGrantedAuthority("ROLE_".concat(cargo.getCargo()));
        }).collect(Collectors.toList());

        this.authorities = authorities;
    }

    public UserPrincipal(String username, List<String> cargos) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        this.username = username;
        this.password = null;
        this.enable = true;

        authorities = cargos.stream().map(cargo -> {
            return new SimpleGrantedAuthority("ROLE_".concat(cargo));
        }).collect(Collectors.toList());

        this.authorities = authorities;
    }

    public static UserPrincipal create(Usuario u) {
        return new UserPrincipal(u);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

}
