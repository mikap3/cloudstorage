package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(@Param("username") String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) " +
            "VALUES(#{user.username}, #{user.salt}, #{user.password}, #{user.firstName}, #{user.lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "user.userId")
    int registerUser(@Param("user") User user);

    @Delete("DELETE FROM USERS WHERE userid = #{userId}")
    int unregisterUser(@Param("userId") Integer userId);

    @Delete("DELETE FROM USERS")
    void deleteEverything();
}
