package com.udacity.jwdnd.course1.cloudstorage.model;

import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;

public class Credential
{
    Integer credentialId;
    String url;
    String userName;
    String key;
    String passWord;
    Integer userId;



    EncryptionService encryptionService;

    public Credential(Integer credentialId, String url, String userName, String key, String passWord, Integer userId) {

        this.credentialId = credentialId;
        this.url = url;
        this.userName = userName;
        this.key = key;
        this.passWord = passWord;
        this.userId = userId;


    }
    public String getDecrpyptedPassword()

    {
        System.out.println("Decrypting..." + this.passWord);
        this.encryptionService = new EncryptionService();
        String decrypedPassword = encryptionService.decryptValue(this.passWord, this.key);
        System.out.println("Decryped password is ..." + decrypedPassword);
        return decrypedPassword;
    }




    public Credential(){}

    public Integer getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

