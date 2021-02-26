package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{authUserId}")
    List<Credential> getAllCredentials(@Param("authUserId") int authUserId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) " +
            "VALUES(#{credential.url}, #{credential.username}, " +
                    "#{credential.key}, #{credential.password}, " +
                    "#{authUserId})")
    @Options(useGeneratedKeys = true, keyProperty = "credential.credentialId")
    int addCredential(@Param("credential") Credential credential, @Param("authUserId") int authUserId);

    @Update("UPDATE CREDENTIALS SET url=#{credential.url}, username=#{credential.username}, " +
                                    "key=#{credential.key}, password=#{credential.password} " +
            "WHERE credentialId = #{credential.credentialId} AND userId = #{authUserId}")
    int updateCredential(@Param("credential") Credential credential, @Param("authUserId") int authUserId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId} AND userId = #{authUserId}")
    int removeCredential(@Param("credentialId") int credentialId, @Param("authUserId") int authUserId);
}
