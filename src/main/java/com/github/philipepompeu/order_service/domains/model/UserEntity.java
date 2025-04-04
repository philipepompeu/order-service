package com.github.philipepompeu.order_service.domains.model;

import java.io.Serializable;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity extends BaseEntity implements UserDetails {
   

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;
    

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));        
        
        return authorities; // Aqui você pode adicionar roles/authorities se necessário
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
    
}
