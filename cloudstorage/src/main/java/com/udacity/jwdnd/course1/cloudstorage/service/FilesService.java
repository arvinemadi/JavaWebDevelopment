package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


@Service
public class FilesService {

    private final FilesMapper filesMapper;
    private final UserMapper userMapper;

    public FilesService(FilesMapper filesMapper, UserMapper userMapper) {
        this.filesMapper = filesMapper;
        this.userMapper = userMapper;

    }

//    public String[] getUserFileNames(Integer userId) {
//        return filesMapper.getUserFileNames(userId);
//    }

    public UserFile[] getUserFiles(Integer userId) {
        return filesMapper.getUserFiles(userId);
    }



    public void deleteFile(Integer fileId) {
        filesMapper.deleteFile(fileId);
    }

    public void addFile(UserFile userfile) throws IOException {
        System.out.println("adding file:" + userfile.getFileName());
        filesMapper.insertFile(userfile);

    }




    public Object getUserFile(Integer fileId) {
        System.out.println("in the fileservice to get the file...");
        return filesMapper.getUserFile(fileId);
    }


//    public int createUser(User user) {
//        SecureRandom random = new SecureRandom();
//        byte[] salt = new byte[16];
//        random.nextBytes(salt);
//        String encodedSalt = Base64.getEncoder().encodeToString(salt);
//        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
//        return userMapper.insert(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
//    }
//
//    public User getUser(String username) {
//        return userMapper.getUser(username);
//    }
}