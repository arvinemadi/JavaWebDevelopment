package com.udacity.jwdnd.course1.cloudstorage.service;



import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;

    }

    public Note[] getUserNotes(Integer userId) {
        return noteMapper.getUserNotes(userId);
    }


    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

    public void addNote(Note note, Integer userId) {
        Note newNote = new Note(0, note.getNoteTitle(), note.getNoteDescription(), userId);
        System.out.println("Going to add a note...");
        noteMapper.insertNote(newNote);
        System.out.println("Mapper called successfully...");
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    public void updateNote(Note note)  {
        noteMapper.updateNote(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription());
    }

}
