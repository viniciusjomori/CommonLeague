package br.com.jrr.apiTest.Security;

import org.springframework.security.core.userdetails.UserDetails;

import br.com.jrr.apiTest.App.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public abstract class UserDetailsAdapter extends BaseEntity implements UserDetails {

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

