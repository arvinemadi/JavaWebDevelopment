package com.udacity.jwdnd.course1.cloudstorage.model;

import java.sql.Blob;

public class UserFile {
    int fileId;
    String fileName;
    String contentType;
    String fileSize;
    int userId;
    byte[] fileData;

    public UserFile(int fileId, String fileName, String contentType, String fileSize, int userId) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.userId = userId;

    }

    public UserFile()
    {

    }

    public int getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public int getUserId() {
        return userId;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
