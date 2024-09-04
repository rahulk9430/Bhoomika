package com.sopra.UserManagement.Controller;

import com.sopra.UserManagement.Entity.User;
import com.sopra.UserManagement.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/api/user/profile")
    public User findUserByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByJwt(jwt);
        return user;
    }
}
