package com.example.userservice.services;

import com.example.userservice.dtos.SendEmailDto;
import com.example.userservice.exceptions.InvalidTokenException;
import com.example.userservice.exceptions.PasswordMismatchException;
import com.example.userservice.models.Role;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.RoleRepository;
import com.example.userservice.repositories.TokenRepository;
import com.example.userservice.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RoleRepository roleRepository;
    private KafkaTemplate<String , String> kafkaTemplate;
    private ObjectMapper objectMapper;

    private UserServiceImpl(UserRepository userRepository ,
                            BCryptPasswordEncoder bCryptPasswordEncoder,
                            TokenRepository tokenRepository,
                            RoleRepository roleRepository,
                            KafkaTemplate<String , String> kafkaTemplate ,
                            ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public User signup(String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        user.setPassword(bCryptPasswordEncoder.encode(password));
        Optional<Role> optionalRole= roleRepository.findByValue("STUDENT");
        user.getRoles().add(optionalRole.orElse(new Role()));


        SendEmailDto emaildto = new SendEmailDto();
        emaildto.setEmail(email);
        emaildto.setSubject("New User");
        emaildto.setBody("Welcome new user "+email);
        /*try {
            //kafkaTemplate.send("sendEmail", objectMapper.writeValueAsString(emaildto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }*/



        return userRepository.save(user);
        //return user;
    }

    @Override
    public Token login(String email, String password) throws PasswordMismatchException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty())
        {
            return null;
        }
        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword()))
        {
            throw new PasswordMismatchException("Invalid Password");
        }
        Token token = new Token();
        token.setUser(user);
        token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date expiryDate = calendar.getTime();


        token.setExpiryDate(expiryDate);

        return tokenRepository.save(token);
    }

    @Override
    public User validateToken(String tokenValue) throws InvalidTokenException {
        Optional<Token> tokenOptional = tokenRepository.findByTokenValueAndExpiryDateAfter(tokenValue, new Date());
        if(tokenOptional.isEmpty())
        {
            throw new InvalidTokenException("Token is Invalid or Expired");
            //return null;
        }
        return tokenOptional.get().getUser();
    }
}
