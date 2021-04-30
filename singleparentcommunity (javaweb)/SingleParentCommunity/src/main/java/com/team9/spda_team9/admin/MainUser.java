package com.team9.spda_team9.admin;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MainUser implements UserDetails {
    private Admin admin;
    public MainUser(Admin admin){
        super();
        this.admin = admin;
    }

//methods from UserDetails interface which must be implemented


    //Standard implementation which returns a collection of user authorities which can be used for configuration late
    //it must start with "ROLE_" before adding the actual role: requirement from spring security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + admin.getUsername();
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
    }

     //not tracking the methods from here on so can hardcode to return true
      //must implement because of UserDetails interface

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
        return true;
    }
}
