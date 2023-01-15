package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import java.util.Random;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final UserMapper userMapper;

    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserMapper userMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;

    }

    public Credential[] getUserCredentials(Integer userId) {
        return credentialMapper.getUserCredentials(userId);
    }


    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }

    public int addCredential(Credential credential) {

        int id;
        String unencrypedPassword = credential.getPassWord();

        Random random = new Random();
        byte[] key = new byte[16];
        random.nextBytes(key);

        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(unencrypedPassword, encodedKey);

        credential.setKey(encodedKey);
        credential.setPassWord(encryptedPassword);

        id = credentialMapper.insertCredential(credential);
        return id;

    }


    public Credential getCredential(Integer credentialId) {

        Credential credential = credentialMapper.getCredential(credentialId);
        String encodedKey = credential.getKey();
        String encryptedPassword = credential.getPassWord();

        String decryptedPassword = encryptionService.decryptValue(encryptedPassword, encodedKey);

        System.out.println("decrypted Password is " + decryptedPassword);
        credential.setPassWord(decryptedPassword);

        return credential;
    }


    public int updateCredential(Credential credential) {
        int id;
        String unencryptedPassword = credential.getPassWord();
        System.out.println("New desired password unencrypted is: " + unencryptedPassword);
        Random random = new Random();
        byte[] key = new byte[16];
        random.nextBytes(key);

        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(unencryptedPassword, encodedKey);

        credential.setKey(encodedKey);
        credential.setPassWord(encryptedPassword);
        System.out.println("New desired password encrypted is: " + encryptedPassword);

        id = credentialMapper.updateCredential(credential.getCredentialId(), credential.getUserName(), credential.getUrl(), credential.getKey(), credential.getPassWord());

        return id;
    }


}