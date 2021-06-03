package com.pp.service;

import com.pp.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
