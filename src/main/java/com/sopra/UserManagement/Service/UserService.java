package com.sopra.UserManagement.Service;

import com.sopra.UserManagement.Entity.User;

public interface UserService {

    public User findUserById(Long userId) throws Exception;

    public User findUserByJwt(String jwt) throws Exception;
}
