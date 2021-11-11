package com.fse.login.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MyUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;
    private String role;

    public MyUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = true;
        this.authorities = Arrays.stream(user.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        this.role = user.getRole();
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
        return active;
    }
}
