package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.service.*;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Controller

public class HomeController {

    private final FilesService filesService;
    private final UserService userService;
    private final CredentialService credentialService;
    private final NoteService noteService;

    private final EncryptionService encryptionService;

    public HomeController(FilesService filesService, UserService userService,
                          NoteService noteService, CredentialService credentialService,
                          EncryptionService encryptionService)
    {
        this.filesService = filesService;
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.credentialService = credentialService;
        this.noteService = noteService;



    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }

    @RequestMapping("/home")
    @GetMapping()
    public String getHomePage(@ModelAttribute("note") NoteForm note, @ModelAttribute("credential") CredentialForm credentialForm,
                              Authentication authentication, Model model) {
        // Find who is logged in
        Integer userId = getUserId(authentication);
        System.out.println("Getting HomePage for user" + userId);
        // Get the file names for the user
        // Pass the user's files' names as a model attribute 'file' to access by home.html
        model.addAttribute("notes", noteService.getUserNotes(userId));
        model.addAttribute("files", filesService.getUserFiles(userId));
        model.addAttribute("credentials", credentialService.getUserCredentials(userId));

        // return home.html
        return "home";
    }



}







