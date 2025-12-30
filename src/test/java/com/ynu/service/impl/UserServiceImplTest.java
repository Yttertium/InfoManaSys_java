package com.ynu.service.impl;

import com.ynu.entity.User;
import com.ynu.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 测试结束后自动回滚
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void testRegisterAndLoginFlow() {
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("123456");
        user.setEmail("1@example.com");

        // 执行注册
        userService.register(user);

        // 测试登录成功
        User loginSuccess = userService.login("test_user", "123456");
        assertNotNull(loginSuccess, "登录成功后返回的用户对象不应为空");
        assertEquals("1@example.com", loginSuccess.getEmail(), "登录返回的信息应与注册一致");

        // 测试密码错误
        User loginFail = userService.login("test_user", "1234567");
        assertNull(loginFail, "密码错误应该返回 null");

        // 测试用户不存在
        User userNotFound = userService.login("non_exist_user", "123");
        assertNull(userNotFound, "用户不存在应该返回 null");
    }


    //测试重复注册（用户名已存在）
    @Test
    void testRegisterDuplicateUsername() {
        User u1 = new User();
        u1.setUsername("user1");
        u1.setPassword("123");
        u1.setEmail("1@example.com");
        userService.register(u1);

        // 再注册一个同名用户
        User u2 = new User();
        u2.setUsername("user1"); // 同名
        u2.setPassword("456");
        u2.setEmail("2@example.com"); // 邮箱不同

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(u2);
        });
        assertEquals("用户名已存在", exception.getMessage());
    }

    //测试重复注册（邮箱已存在）
    @Test
    void testRegisterDuplicateEmail() {
        User u1 = new User();
        u1.setUsername("user_a");
        u1.setPassword("123");
        u1.setEmail("1@example.com");
        userService.register(u1);

        // 再注册一个同邮箱用户
        User u2 = new User();
        u2.setUsername("user_b"); // 用户名不同
        u2.setPassword("456");
        u2.setEmail("1@example.com"); // 同邮箱

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.register(u2);
        });
        assertEquals("邮箱已存在", exception.getMessage());
    }
}
