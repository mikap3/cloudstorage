package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.DataAccessException;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
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
public class CredentialsController {

    private final UserService userService;
    private final CredentialService credentialService;

    public CredentialsController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @ModelAttribute("credentials")
    public List<Credential> populateCredentials(Authentication auth) {
        User user = userService.getUserByAuthentication(auth);
        List<Credential> credentials = credentialService.getAllCredentials(user);
        return credentials;
    }

    @ModelAttribute("credentialForm")
    public CredentialForm populateCredentialForm() {
        return new CredentialForm();
    }

    @RequestMapping("/credentials")
    public String credentialsView() {
        return "credentials";
    }

    @RequestMapping(value="/credentials/edit")
    public String credentialsEdit(Authentication auth, @ModelAttribute("credentialForm") CredentialForm credentialForm, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByAuthentication(auth);
        if (credentialForm.getCredentialId() == null)
            credentialService.addCredential(credentialForm, user);
        else
            credentialService.updateCredential(credentialForm, user);
        redirectAttributes.addFlashAttribute("saved", true);
        redirectAttributes.addFlashAttribute("link", "/credentials");
        return "redirect:/result";
    }

    @RequestMapping(value="/credentials/delete", params="id")
    public String credentialsRemove(Authentication auth, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByAuthentication(auth);
        Integer credentialId = Integer.valueOf(req.getParameter("id"));
        credentialService.removeCredential(credentialId, user);
        redirectAttributes.addFlashAttribute("deleted", true);
        redirectAttributes.addFlashAttribute("link", "/credentials");
        return "redirect:/result";
    }

    @ExceptionHandler(DataAccessException.class)
    public String handleError(HttpServletRequest req, RedirectAttributes redirectAttributes, Exception ex) {
        redirectAttributes.addFlashAttribute("error", true);
        redirectAttributes.addFlashAttribute("message", ex.getMessage());
        redirectAttributes.addFlashAttribute("link", "/credentials");
        return "redirect:/result";
    }
}
