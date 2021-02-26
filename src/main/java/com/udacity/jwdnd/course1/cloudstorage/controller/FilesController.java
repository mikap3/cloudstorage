package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.DataAccessException;
import com.udacity.jwdnd.course1.cloudstorage.exception.FileInvalidException;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class FilesController {

    private final UserService userService;
    private final FileService fileService;

    public FilesController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @ModelAttribute("files")
    public List<File> populateFiles(Authentication auth) {
        User user = userService.getUserByAuthentication(auth);
        List<File> files = fileService.getAllFiles(user);
        return files;
    }

    @RequestMapping("files")
    public String filesView() {
        return "files";
    }

    @RequestMapping(value="/files/save")
    public String filesSave(Authentication auth, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByAuthentication(auth);
        fileService.saveFile(file, user);
        redirectAttributes.addFlashAttribute("saved", true);
        redirectAttributes.addFlashAttribute("link", "/files");
        return "redirect:/result";
    }

    @GetMapping(value="/files/load", params="id")
    public ResponseEntity<Resource> filesLoad(Authentication auth, HttpServletRequest req) {
        User user = userService.getUserByAuthentication(auth);
        Integer fileId = Integer.valueOf(req.getParameter("id"));
        File file = fileService.loadFile(fileId, user);
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(resource);
    }

    @RequestMapping(value="/files/delete", params="id")
    public String filesDelete(Authentication auth, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByAuthentication(auth);
        Integer fileId = Integer.valueOf(req.getParameter("id"));
        fileService.deleteFile(fileId, user);
        redirectAttributes.addFlashAttribute("deleted", true);
        redirectAttributes.addFlashAttribute("link", "/files");
        return "redirect:/result";
    }

    @ExceptionHandler({FileInvalidException.class, DataAccessException.class})
    public String handleError(HttpServletRequest req, RedirectAttributes redirectAttributes, Exception ex) {
        redirectAttributes.addFlashAttribute("error", true);
        redirectAttributes.addFlashAttribute("message", ex.getMessage());
        redirectAttributes.addFlashAttribute("link", "/files");
        return "redirect:/result";
    }
}
