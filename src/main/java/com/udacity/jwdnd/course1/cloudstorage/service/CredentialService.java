package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.exception.DataAccessException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getAllCredentials(User user) {
        List<Credential> credentials = credentialMapper.getAllCredentials(user.getUserId());
        for (Credential credential : credentials) {
            credential.setPasscipher(credential.getPassword());
            String key = credential.getKey();
            String ciphertext = credential.getPassword();
            String password = encryptionService.decryptValue(ciphertext, key);
            credential.setPassword(password);
        }
        return credentials;
    }

    public void addCredential(CredentialForm credentialForm, User user) throws DataAccessException {
        Credential credential = new Credential();
        credential.setUrl(credentialForm.getUrl());
        credential.setUsername(credentialForm.getUsername());
        String key = encryptionService.generateKey();
        String password = credentialForm.getPassword();
        String cipher = encryptionService.encryptValue(password, key);
        credential.setKey(key);
        credential.setPassword(cipher);
        if (credentialMapper.addCredential(credential, user.getUserId()) != 1)
            throw new DataAccessException("Failed to add credential.");
    }

    public void updateCredential(CredentialForm credentialForm, User user) throws DataAccessException {
        Credential credential = new Credential();
        credential.setCredentialId(credentialForm.getCredentialId());
        credential.setUrl(credentialForm.getUrl());
        credential.setUsername(credentialForm.getUsername());
        String key = encryptionService.generateKey();
        String password = credentialForm.getPassword();
        String ciphertext = encryptionService.encryptValue(password, key);
        credential.setKey(key);
        credential.setPassword(ciphertext);
        if (credentialMapper.updateCredential(credential, user.getUserId()) != 1)
            throw new DataAccessException("Failed to update credential.");
    }

    public void removeCredential(Integer credentialId, User user) throws DataAccessException {
        if (credentialMapper.removeCredential(credentialId, user.getUserId()) != 1)
            throw new DataAccessException("Failed to remove credential.");
    }
}
