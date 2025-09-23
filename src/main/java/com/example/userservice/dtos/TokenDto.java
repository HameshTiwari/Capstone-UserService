package com.example.userservice.dtos;

import com.example.userservice.models.Role;
import com.example.userservice.models.Token;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TokenDto {
    private String tokenValue;
    /*private Date expiryDate;
    private String email;
    private List<Role> roles;*/

    public static TokenDto from(Token token){
        if(token==null){
            return null;
        }
        TokenDto tokenDto=new TokenDto();
        tokenDto.setTokenValue(token.getTokenValue());

        return tokenDto;
    }
}
