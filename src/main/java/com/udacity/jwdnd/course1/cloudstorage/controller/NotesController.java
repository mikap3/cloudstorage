package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.DataAccessException;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class NotesController {

    private final UserService userService;
    private final NoteService noteService;

    public NotesController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @ModelAttribute("notes")
    public List<Note> populateNotes(Authentication auth) {
        User user = userService.getUserByAuthentication(auth);
        List<Note> notes = noteService.getAllNotes(user);
        return notes;
    }

    @ModelAttribute("noteForm")
    public NoteForm populateNoteForm() {
        return new NoteForm();
    }

    @RequestMapping("/notes")
    public String notesView() {
        return "notes";
    }

    @RequestMapping(value="/notes/edit")
    public String notesEdit(Authentication auth, @ModelAttribute("noteForm") NoteForm noteForm, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByAuthentication(auth);
        if (noteForm.getNoteId() == null)
            noteService.addNote(noteForm, user);
        else
            noteService.updateNote(noteForm, user);
        redirectAttributes.addFlashAttribute("saved", true);
        redirectAttributes.addFlashAttribute("link", "/notes");
        return "redirect:/result";
    }

    @RequestMapping(value="/notes/delete", params="id")
    public String notesRemove(Authentication auth, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByAuthentication(auth);
        Integer noteId = Integer.valueOf(req.getParameter("id"));
        noteService.removeNote(noteId, user);
        redirectAttributes.addFlashAttribute("deleted", true);
        redirectAttributes.addFlashAttribute("link", "/notes");
        return "redirect:/result";
    }

    @ExceptionHandler(DataAccessException.class)
    public String handleError(HttpServletRequest req, RedirectAttributes redirectAttributes, Exception ex) {
        redirectAttributes.addFlashAttribute("error", true);
        redirectAttributes.addFlashAttribute("message", ex.getMessage());
        redirectAttributes.addFlashAttribute("link", "/notes");
        return "redirect:/result";
    }
}
