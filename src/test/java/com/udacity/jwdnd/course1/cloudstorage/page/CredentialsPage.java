package com.udacity.jwdnd.course1.cloudstorage.page;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
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

public class CredentialsPage {

    private final WebDriver driver;

    @FindBy(id = "credentialAdd")
    private WebElement credentialAdd;
    @FindBy(id = "credentialModal")
    private WebElement credentialModal;
    @FindBy(id = "credentialModalId")
    private WebElement credentialModalId;
    @FindBy(id = "credentialModalUrl")
    private WebElement credentialModalUrl;
    @FindBy(id = "credentialModalUsername")
    private WebElement credentialModalUsername;
    @FindBy(id = "credentialModalPassword")
    private WebElement credentialModalPassword;
    @FindBy(id = "credentialModalCancel")
    private WebElement credentialModalCancel;
    @FindBy(id = "credentialModalSave")
    private WebElement credentialModalSave;

    public CredentialsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void addCredential(Credential credential) {
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.elementToBeClickable(credentialAdd));
        credentialAdd.click();
        wait.until(ExpectedConditions.visibilityOf(credentialModal));
        credentialModalUrl.sendKeys(credential.getUrl());
        credentialModalUsername.sendKeys(credential.getUsername());
        credentialModalPassword.sendKeys(credential.getPassword());
        credentialModalSave.click();
    }

    public void editCredential(Credential credential) throws NotFoundException {
        List<WebElement> credentialItems = driver.findElements(By.className("credentialItem"));
        for (WebElement credentialItem : credentialItems) {
            int cid = Integer.parseInt(credentialItem.findElement(By.className("credentialId")).getAttribute("innerHTML"));
            if (cid == credential.getCredentialId()) {
                WebDriverWait wait = new WebDriverWait(driver, 3);
                WebElement credentialEdit = credentialItem.findElement(By.className("credentialEdit"));
                wait.until(ExpectedConditions.elementToBeClickable(credentialEdit));
                credentialEdit.click();
                wait.until(ExpectedConditions.visibilityOf(credentialModal));
                credentialModalUrl.clear();
                credentialModalUrl.sendKeys(credential.getUrl());
                credentialModalUsername.clear();
                credentialModalUsername.sendKeys(credential.getUsername());
                credentialModalPassword.clear();
                credentialModalPassword.sendKeys(credential.getPassword());
                credentialModalSave.click();
                return;
            }
        }
        throw new NotFoundException("credential not found");
    }

    public void deleteCredential(Integer credentialId) throws NotFoundException {
        List<WebElement> credentialItems = driver.findElements(By.className("credentialItem"));
        for (WebElement credentialItem : credentialItems) {
            int cid = Integer.parseInt(credentialItem.findElement(By.className("credentialId")).getAttribute("innerHTML"));
            if (cid == credentialId) {
                WebDriverWait wait = new WebDriverWait(driver, 3);
                WebElement credentialDelete = credentialItem.findElement(By.className("credentialDelete"));
                wait.until(ExpectedConditions.elementToBeClickable(credentialDelete));
                credentialDelete.click();
                return;
            }
        }
        throw new NotFoundException("credential not found");
    }

    public List<Credential> getAllCredentials() {
        List<Credential> credentials = new ArrayList<>();
        List<WebElement> credentialItems = driver.findElements(By.className("credentialItem"));
        for (WebElement credentialItem : credentialItems) {
            Credential credential = new Credential();
            credential.setCredentialId(Integer.valueOf(credentialItem.findElement(By.className("credentialId")).getAttribute("innerHTML")));
            credential.setUrl(credentialItem.findElement(By.className("credentialUrl")).getText());
            credential.setUsername(credentialItem.findElement(By.className("credentialUsername")).getText());
            credential.setPassword(credentialItem.findElement(By.className("credentialPassword")).getText());
            credentials.add(credential);
        }
        return credentials;
    }

    public Credential getModalCredential(Integer credentialId) {
        List<WebElement> credentialItems = driver.findElements(By.className("credentialItem"));
        for (WebElement credentialItem : credentialItems) {
            int cid = Integer.parseInt(credentialItem.findElement(By.className("credentialId")).getAttribute("innerHTML"));
            if (cid == credentialId) {
                WebDriverWait wait = new WebDriverWait(driver, 3);
                WebElement credentialEdit = credentialItem.findElement(By.className("credentialEdit"));
                wait.until(ExpectedConditions.elementToBeClickable(credentialEdit));
                credentialEdit.click();
                wait.until(ExpectedConditions.visibilityOf(credentialModal));
                Credential credential = new Credential();
                credential.setCredentialId(credentialId);
                credential.setUrl(credentialModalUrl.getAttribute("value"));
                credential.setUsername(credentialModalUsername.getAttribute("value"));
                credential.setPassword(credentialModalPassword.getAttribute("value"));
                credentialModalCancel.click();
                wait.until(ExpectedConditions.invisibilityOf(credentialModal));
                return credential;
            }
        }
        throw new NotFoundException("credential not found");
    }
}
