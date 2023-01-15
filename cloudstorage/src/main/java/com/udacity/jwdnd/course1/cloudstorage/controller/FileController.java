package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.service.*;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Controller
public class FileController {

    private final FilesService filesService;
    private final UserService userService;
    private final CredentialService credentialService;

    private final NoteService noteService;

    public FileController(FilesService filesService, UserService userService,
                          NoteService noteService, CredentialService credentialService)
    {
        this.filesService = filesService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.noteService = noteService;
    }

    @PostMapping("/upload")
    private String uploadFile(Model model, @RequestParam("fileUpload") MultipartFile file, Authentication authentication) throws IOException {
        System.out.println("inside uploading a file to the system");
        int userId = getUserId(authentication);
        System.out.println("logged in user is:" + authentication.getName());
        UserFile[] userFileNames = filesService.getUserFiles(userId);


        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "File is empty");
        } else {
            UserFile newuserFile = new UserFile();
            String newFileContentType = file.getContentType();
            String newFileName = file.getOriginalFilename();
            String FileSize = file.getSize() + "";

            newuserFile.setContentType(newFileContentType);
            newuserFile.setFileName(newFileName);
            newuserFile.setUserId(userId);
            newuserFile.setFileSize(FileSize);


            for (UserFile userFile : userFileNames) {
                String userFileName = userFile.getFileName();
                System.out.println(userFileName + " exists already...");
                if (userFileName.equals(newFileName)) {
                    model.addAttribute("errorMessage", "File already exists!");
                    model.addAttribute("files", userFileName);
                    return "redirect:/home";
                }
            }
            System.out.println("Verified that file does not already exist...");

            try {
                newuserFile.setFileData(file.getBytes());
                System.out.println("Calling the file service to add the file...");
                filesService.addFile(newuserFile);
                model.addAttribute("success", "File saved!");
                System.out.println("Successful in adding the file to database...");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }




        }
        model.addAttribute("tab", "nav-files-tab");
        model.addAttribute("files", filesService.getUserFiles(userId));
        model.addAttribute("notes", noteService.getUserNotes(userId));
        model.addAttribute("credentials", credentialService.getUserCredentials(userId));

        return "redirect:/home";
    }


    @RequestMapping(value = "file/delete/{id}", method = RequestMethod.GET)
    private String deleteFile(@PathVariable(name = "id") String id, RedirectAttributes redirectAttributes) {
        System.out.println("Going to delete file" + id);
        filesService.deleteFile(Integer.valueOf(id));
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/home";
    }

    @RequestMapping(value = {"/file/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> viewFile(@PathVariable(name = "id") String id,
                                           HttpServletResponse response, HttpServletRequest request) {
        System.out.println("Trying to show the file with fileId" + id);
        UserFile file = null;
        file = (UserFile) filesService.getUserFile(Integer.valueOf(id));
        System.out.println("received the file...");
        byte[] fileContents = file.getFileData();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(file.getContentType()));
        String fileName = file.getFileName();
        httpHeaders.setContentDispositionFormData(fileName, fileName);
        httpHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> serverResponse = new ResponseEntity<byte[]>(fileContents, httpHeaders, HttpStatus.OK);
        return serverResponse;
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }





}










