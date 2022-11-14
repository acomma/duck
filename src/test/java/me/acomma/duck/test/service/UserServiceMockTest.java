package me.acomma.duck.test.service;

import me.acomma.duck.mapper.UserMapper;
import me.acomma.duck.model.entity.UserEntity;
import me.acomma.duck.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserServiceMockTest {
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void test1() {
        Long userId = 123L;
        UserEntity mockUser = UserEntity.builder().userId(userId).username("Bob").build();

        Mockito.doReturn(mockUser).when(userMapper).findByUserId(userId);

        UserEntity user = userService.getByUserId(userId);

        Mockito.verify(userMapper, Mockito.times(1)).findByUserId(userId);
        assertEquals(user.getUsername(), "Bob");
    }
}