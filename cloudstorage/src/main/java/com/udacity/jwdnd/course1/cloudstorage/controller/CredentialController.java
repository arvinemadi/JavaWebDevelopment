package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
//@RequestMapping("credential")
public class CredentialController {

    private UserMapper userMapper;
    private FilesService fileService;
    private CredentialService credentialsService;
    private NoteService noteService;

    public CredentialController(NoteService noteService, UserMapper userMapper, FilesService fileService, CredentialService credentialsService) {
        this.userMapper = userMapper;
        this.fileService = fileService;
        this.credentialsService = credentialsService;
        this.noteService = noteService;
    }

    @PostMapping("credential/edit")
    public String postCredential(Model model, @ModelAttribute("credential") CredentialForm credential, Authentication authentication) {
        System.out.println("Adding Credentials:" + credential);
        System.out.println("url:" + credential.getUrl());
        System.out.println("username:" + credential.getUsername());

        String username = authentication.getName();
        int userId = userMapper.getUser(username).getUserId();
        Credential newcredential = new Credential();
        newcredential.setUserId(userId);
        newcredential.setUserName(credential.getUsername());
        newcredential.setUrl(credential.getUrl());
        newcredential.setPassWord(credential.getPassword());
        String credentialIdStr = credential.getId();
        int isSuccessful;
        if (credential.getId().isBlank()) {
            // newcredential.setCredentialId(Integer.parseInt(credential.getId()));
            isSuccessful = credentialsService.addCredential(newcredential);
        } else {
            Credential existingCredential = credentialsService.getCredential(Integer.parseInt(credentialIdStr));
            existingCredential.setUserId(userId);
            existingCredential.setUserName(credential.getUsername());
            existingCredential.setUrl(credential.getUrl());
            existingCredential.setPassWord(credential.getPassword());
            isSuccessful = credentialsService.updateCredential(existingCredential);
        }
        System.out.println("Successful in adding new credential");

        model.addAttribute("tab", "nav-notes-tab");
        model.addAttribute("credentials", credentialsService.getUserCredentials(userId));
//        Note[] currentNotes =  noteService.getUserNotes(userId);
//        for (Note availabe_note : currentNotes)
//        {
//            System.out.println("Title of available note:" + availabe_note.getNoteTitle());
//        }
        model.addAttribute("notes", noteService.getUserNotes(userId));
        model.addAttribute("files", fileService.getUserFiles(userId));
        System.out.println("Going back to homepage");
        model.addAttribute("success", true);
        Boolean isSuccess = isSuccessful > 0;
        model.addAttribute("result", "success");
        return "result";
    }

//    @GetMapping(value = "/credential/{credentialId}")
//    public Credential getCredential(@PathVariable Integer credentialId) {
//        return credentialsService.getCredential(credentialId);
//    }

    @RequestMapping(value = "credentials/delete/{id}")
    private String deleteCredential(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes, Model model) {

        boolean isSuccessful = Integer.valueOf(id) > 0;


        System.out.println("deleteCredential: " + id);
        if (isSuccessful) {

            credentialsService.deleteCredential(Integer.valueOf(id));
        }
        model.addAttribute("result", "success");
        model.addAttribute("success", "true");
        return "result";
    }
}