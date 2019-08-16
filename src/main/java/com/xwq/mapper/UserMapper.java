package com.xwq.mapper;

import com.xwq.annotation.Mapper;
import com.xwq.annotation.Param;
import com.xwq.model.User;

@Mapper
public interface UserMapper {
    void insert(String username, String password);

    void deleteById(@Param("id") Long id);

    void updatePassword(@Param("password") String password, @Param("id") Long id);

    User getById(@Param("id") Long id);
}
