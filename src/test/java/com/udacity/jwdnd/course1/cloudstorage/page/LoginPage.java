package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private final WebDriver driver;

    @FindBy(id = "loginUsername")
    private WebElement loginUsername;
    @FindBy(id = "loginPassword")
    private WebElement loginPassword;
    @FindBy(id = "loginSubmit")
    private WebElement loginSubmit;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void attemptLogin(String username, String password) {
        loginUsername.clear();
        loginUsername.sendKeys(username);
        loginPassword.clear();
        loginPassword.sendKeys(password);
        loginSubmit.click();
    }

    public boolean isSignupMessageDisplayed() {
        return driver.findElements(By.id("signupSuccess")).size() == 1;
    }

    public boolean isErrorMessageDisplayed() {
        return this.driver.findElements(By.id("loginError")).size() == 1;
    }

    public boolean isLogoutMessageDisplayed() {
        return this.driver.findElements(By.id("loginLogout")).size() == 1;
    }
}
