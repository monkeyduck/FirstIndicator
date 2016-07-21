package com.llc.dao;

import com.llc.model.User;

/**
 * Created by llc on 16/7/20.
 */
public interface UserDao {
    String getPassword(String username);

    User getUserByUsername(String username);
}
