package com.udacity.jwdnd.course1.cloudstorage.page;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class NotesPage {

    private final WebDriver driver;

    @FindBy(id = "noteAdd")
    private WebElement noteAdd;
    @FindBy(id = "noteModal")
    private WebElement noteModal;
    @FindBy(id = "noteModalTitle")
    private WebElement noteModalTitle;
    @FindBy(id = "noteModalDescription")
    private WebElement noteModalDescription;
    @FindBy(id = "noteModalCancel")
    private WebElement noteModalCancel;
    @FindBy(id = "noteModalSave")
    private WebElement noteModalSave;

    public NotesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void addNote(Note note) {
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.elementToBeClickable(noteAdd));
        noteAdd.click();
        wait.until(ExpectedConditions.visibilityOf(noteModal));
        noteModalTitle.sendKeys(note.getTitle());
        noteModalDescription.sendKeys(note.getDescription());
        noteModalSave.click();
    }

    public void editNote(Note note) throws NotFoundException {
        List<WebElement> noteItems = driver.findElements(By.className("noteItem"));
        for (WebElement noteItem : noteItems) {
            int nid = Integer.parseInt(noteItem.findElement(By.className("noteId")).getAttribute("innerHTML"));
            if (nid == note.getNoteId()) {
                WebDriverWait wait = new WebDriverWait(driver, 3);
                WebElement noteEdit = noteItem.findElement(By.className("noteEdit"));
                wait.until(ExpectedConditions.elementToBeClickable(noteEdit));
                noteEdit.click();
                wait.until(ExpectedConditions.visibilityOf(noteModal));
                noteModalTitle.clear();
                noteModalTitle.sendKeys(note.getTitle());
                noteModalDescription.clear();
                noteModalDescription.sendKeys(note.getDescription());
                noteModalSave.click();
                return;
            }
        }
        throw new NotFoundException("note not found");
    }

    public void deleteNote(Integer noteId) throws NotFoundException {
        List<WebElement> noteItems = driver.findElements(By.className("noteItem"));
        for (WebElement noteItem : noteItems) {
            int nid = Integer.parseInt(noteItem.findElement(By.className("noteId")).getAttribute("innerHTML"));
            if (nid == noteId) {
                WebDriverWait wait = new WebDriverWait(driver, 3);
                WebElement noteDelete = noteItem.findElement(By.className("noteDelete"));
                wait.until(ExpectedConditions.elementToBeClickable(noteDelete));
                noteDelete.click();
                return;
            }
        }
        throw new NotFoundException("note not found");
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        List<WebElement> noteItems = driver.findElements(By.className("noteItem"));
        for (WebElement noteItem : noteItems) {
            Note note = new Note();
            note.setNoteId(Integer.valueOf(noteItem.findElement(By.className("noteId")).getAttribute("innerHTML")));
            note.setTitle(noteItem.findElement(By.className("noteTitle")).getText());
            note.setDescription(noteItem.findElement(By.className("noteDescription")).getText());
            notes.add(note);
        }
        return notes;
    }

    public Note getModalNote(Integer noteId) {
        List<WebElement> noteItems = driver.findElements(By.className("noteItem"));
        for (WebElement noteItem : noteItems) {
            int nid = Integer.parseInt(noteItem.findElement(By.className("noteId")).getAttribute("innerHTML"));
            if (nid == noteId) {
                WebDriverWait wait = new WebDriverWait(driver, 3);
                WebElement noteEdit = noteItem.findElement(By.className("noteEdit"));
                wait.until(ExpectedConditions.elementToBeClickable(noteEdit));
                noteEdit.click();
                wait.until(ExpectedConditions.visibilityOf(noteModal));
                Note note = new Note();
                note.setNoteId(noteId);
                note.setTitle(noteModalTitle.getAttribute("value"));
                note.setDescription(noteModalDescription.getAttribute("value"));
                noteModalCancel.click();
                wait.until(ExpectedConditions.invisibilityOf(noteModal));
                return note;
            }
        }
        throw new NotFoundException("note not found");
    }
}
