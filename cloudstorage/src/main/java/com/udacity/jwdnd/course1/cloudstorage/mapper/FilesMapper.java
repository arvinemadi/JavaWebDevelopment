package com.udacity.jwdnd.course1.cloudstorage.mapper;



import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

import java.sql.Blob;
import java.util.List;

@Mapper
public interface FilesMapper {
    @Select("SELECT filename FROM FILES WHERE userid = #{userId}")
    String[] getUserFileNames(Integer userId);


    // it may be better not to load the fileData when doing this, may update later
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    UserFile[] getUserFiles(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(UserFile file);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    UserFile getUserFile(Integer fileId);


    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    void deleteFile(Integer fileId);

}



