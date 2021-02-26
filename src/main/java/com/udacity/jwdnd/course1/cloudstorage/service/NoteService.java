package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.exception.DataAccessException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNotes(User user) {
        return noteMapper.getAllNotes(user.getUserId());
    }

    public void addNote(NoteForm noteForm, User user) throws DataAccessException {
        Note note = new Note();
        note.setTitle(noteForm.getTitle());
        note.setDescription(noteForm.getDescription());
        if (noteMapper.addNote(note, user.getUserId()) != 1)
            throw new DataAccessException("Failed to add note.");
    }

    public void updateNote(NoteForm noteForm, User user) throws DataAccessException {
        Note note = new Note();
        note.setNoteId(noteForm.getNoteId());
        note.setTitle(noteForm.getTitle());
        note.setDescription(noteForm.getDescription());
        if (noteMapper.updateNote(note, user.getUserId()) != 1)
            throw new DataAccessException("Failed to update note.");
    }

    public void removeNote(Integer noteId, User user) throws DataAccessException {
        if (noteMapper.removeNote(noteId, user.getUserId()) != 1)
            throw new DataAccessException("Failed to remove note.");
    }
}
