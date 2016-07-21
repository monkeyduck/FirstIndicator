package com.llc.service;

import com.llc.model.User;

/**
 * Created by llc on 16/7/20.
 */
public interface UserService {
    String getPassword(String username);

    User getUserByUsername(String username);
}
