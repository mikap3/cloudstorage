package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{authUserId}")
    List<Note> getAllNotes(@Param("authUserId") int authUserId);

    @Insert("INSERT INTO NOTES (title, description, userid) " +
            "VALUES(#{note.title}, #{note.description}, #{authUserId})")
    @Options(useGeneratedKeys = true, keyProperty = "note.noteId")
    int addNote(@Param("note") Note note, @Param("authUserId") int authUserId);

    @Update("UPDATE NOTES SET title=#{note.title}, description=#{note.description} " +
            "WHERE noteId = #{note.noteId} AND userId = #{authUserId}")
    int updateNote(@Param("note") Note note, @Param("authUserId") int authUserId);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId} AND userId = #{authUserId}")
    int removeNote(@Param("noteId") int noteId, @Param("authUserId") int authUserId);
}
