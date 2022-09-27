package AMS.AMSsideproject.web.custom.security;

import AMS.AMSsideproject.domain.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {this.user = user;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities  = new ArrayList<>();

        List<String> list = new ArrayList<>();
        list.add(user.getRole());

        list.stream().forEach( r -> {
            authorities.add(() -> r);
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return true;
    }
}
