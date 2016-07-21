package com.llc.service.impl;

import com.llc.dao.UserDao;
import com.llc.model.User;
import com.llc.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by llc on 16/7/20.
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

    @Resource
    UserDao userDao;

    public String getPassword(String username) {
        return userDao.getPassword(username);
    }

    public User getUserByUsername(String username){
        return this.userDao.getUserByUsername(username);
    }
}
