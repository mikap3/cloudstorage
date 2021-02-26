package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.exception.DataAccessException;
import com.udacity.jwdnd.course1.cloudstorage.exception.FileInvalidException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getAllFiles(User user) {
        return fileMapper.getAllFiles(user.getUserId());
    }

    public boolean isFilenameAvailable(String filename, User user) {
        int count = fileMapper.countFilesWithFilename(filename, user.getUserId());
        return count == 0;
    }

    public void saveFile(MultipartFile multipartFile, User user) throws FileInvalidException, DataAccessException {
        try {
            if (multipartFile.isEmpty()) {
                throw new FileInvalidException("Failed to store empty file.");
            }
            if (!isFilenameAvailable(multipartFile.getOriginalFilename(), user)) {
                throw new FileInvalidException("Failed to store duplicate file.");
            }
            File file = new File();
            file.setFilename(multipartFile.getOriginalFilename());
            file.setContentType(multipartFile.getContentType());
            file.setFileSize(multipartFile.getSize());
            file.setFileData(multipartFile.getBytes());
            if (fileMapper.addFile(file, user.getUserId()) != 1)
                throw new DataAccessException("Failed to store file.");
        }
        catch (IOException e) {
            throw new FileInvalidException("Failed to store file.", e);
        }
    }

    public File loadFile(Integer fileId, User user) throws DataAccessException {
        File file = fileMapper.getFile(fileId, user.getUserId());
        if (file == null)
            throw new DataAccessException("Failed to retrieve file.");
        return file;
    }

    public void deleteFile(Integer fileId, User user) throws DataAccessException {
        if (fileMapper.removeFile(fileId, user.getUserId()) != 1)
            throw new DataAccessException("Failed to delete file.");
    }
}
