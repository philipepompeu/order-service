package com.github.philipepompeu.order_service.app.dto;

import com.github.philipepompeu.order_service.domains.model.UserEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginDto {
    
    private String username;
    private String password;
    
    public UserLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserEntity toEntity(){

        UserEntity user = new UserEntity();
        
        user.setUserName(this.username);
        user.setPassword(this.password);
        
        return user;
    }
}
