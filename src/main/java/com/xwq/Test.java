package com.xwq;

import com.xwq.model.User;
import com.xwq.service.UserService;

public class Test {
    public static void main(String[] args) {
        UserService userService = new UserService();

        // insert
    /*    User u = new User();
        u.setUsername("admin");
        u.setPassword("123");
        userService.insert(u);*/

        // update
//        userService.updatePassword(1L, "admin123");

        //select
        User u = userService.getById(2L);
        System.out.println(u);

        // delete
//        userService.deleteById(1L);
    }
}
