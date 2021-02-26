package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT fileid, filename, contenttype, filesize, userid FROM FILES WHERE userid = #{authUserId}")
    List<File> getAllFiles(@Param("authUserId") int authUserId);

    @Select("SELECT COUNT(fileid) FROM FILES WHERE filename = #{filename} AND userId = #{authUserId}")
    int countFilesWithFilename(@Param("filename") String filename, @Param("authUserId") int authUserId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId} AND userId = #{authUserId}")
    File getFile(@Param("fileId") int fileId, @Param("authUserId") int authUserId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{file.filename}, #{file.contentType}, #{file.fileSize}, #{authUserId}, #{file.fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "file.fileId")
    int addFile(@Param("file") File file, @Param("authUserId") int authUserId);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId} AND userId = #{authUserId}")
    int removeFile(@Param("fileId") int fileId, @Param("authUserId") int authUserId);
}
