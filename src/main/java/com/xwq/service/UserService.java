package com.xwq.service;

import com.xwq.MapperProxy;
import com.xwq.mapper.UserMapper;
import com.xwq.model.User;

public class UserService {

    private UserMapper userMapper;

    public UserService() {
        userMapper = new MapperProxy<>(UserMapper.class).getProxy();
    }

    public void insert(User user) {
        userMapper.insert(user.getUsername(), user.getPassword());
    }

    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    public void updatePassword(Long id, String password) {
        userMapper.updatePassword(password, id);
    }

    public User getById(Long id) {
        return userMapper.getById(id);
    }
}
