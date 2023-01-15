package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.FilesService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    private NoteService noteService;
    private UserMapper userMapper;
    private FilesService fileService;
    private CredentialService credentialsService;

    public NoteController(NoteService noteService, UserMapper userMapper, FilesService fileService, CredentialService credentialsService) {
        this.noteService = noteService;
        this.userMapper = userMapper;
        this.fileService = fileService;
        this.credentialsService = credentialsService;
    }

    @PostMapping("/notes")
    public String postNote(Model model, @ModelAttribute("note") NoteForm note, Authentication authentication) {
        System.out.println("Adding note:" + note);
        System.out.println("description of note:" + note.getDescription());
        System.out.println("title of note:" + note.getTitle());

        String username = authentication.getName();
        int userId = userMapper.getUser(username).getUserId();
        Note noteObj = new Note();

        noteObj.setUserId(userId);
        noteObj.setNoteTitle(note.getTitle());
        noteObj.setNoteDescription(note.getDescription());
        if (!note.getId().isBlank()) {
            noteObj.setNoteId(Integer.parseInt(note.getId()));
            noteService.updateNote(noteObj);
        } else {
            noteService.addNote(noteObj, userId);
        }
        model.addAttribute("success", true);
        model.addAttribute("tab", "nav-notes-tab");
        Note[] currentNotes =  noteService.getUserNotes(userId);
        for (Note availabe_note : currentNotes)
        {
            System.out.println("Title of available note:" + availabe_note.getNoteTitle());
        }
        model.addAttribute("notes", currentNotes);
        model.addAttribute("files", fileService.getUserFiles(userId));
        //model.addAttribute("credentials", credentialsService.getUserCredentials(userId));
        model.addAttribute("result", "success");
        return "result";
    }


    @RequestMapping(value = "note/delete/{id}")
    private String deleteNote(@PathVariable(name = "id") String id, Authentication authentication, RedirectAttributes redirectAttributes, Model model) {
        String username = authentication.getName();
        int userId = userMapper.getUser(username).getUserId();
        boolean isSuccess = Integer.valueOf(id) > 0;
        System.out.println("deleteNote: " + id);
        if (isSuccess) {
            noteService.deleteNote(Integer.parseInt(id));
        }
        model.addAttribute("notes", noteService.getUserNotes(userId));

        model.addAttribute("result", "success");
        return "result";
    }
}