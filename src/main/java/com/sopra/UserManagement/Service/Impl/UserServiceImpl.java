package com.sopra.UserManagement.Service.Impl;


import com.sopra.UserManagement.Entity.User;
import com.sopra.UserManagement.Repository.UserRepository;
import com.sopra.UserManagement.SecurityConfig.JwtProvider;
import com.sopra.UserManagement.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtProvider jwtProvider;


    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new Exception("user not found with id "+userId);
    }

    @Override
    public User findUserByJwt(String jwt) throws Exception {
        String email=jwtProvider.getEmailFromJwtToken(jwt);
        if(email==null){
            throw new Exception("Provide valid jwt token...");
        }
        User user =userRepo.findByEmail(email);
        if(user==null){
            throw new Exception("user not found with email..."+email);
        }
        return user;
    }
}
