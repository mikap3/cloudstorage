package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    private final WebDriver driver;

    @FindBy(id = "signupUsername")
    private WebElement signupUsername;
    @FindBy(id = "signupPassword")
    private WebElement signupPassword;
    @FindBy(id = "signupFirstName")
    private WebElement signupFirstName;
    @FindBy(id = "signupLastName")
    private WebElement signupLastName;
    @FindBy(id = "signupSubmit")
    private WebElement signupSubmit;

    public SignupPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void attemptSignup(String username, String password, String firstName, String lastName) {
        signupFirstName.clear();
        signupFirstName.sendKeys(firstName);
        signupLastName.clear();
        signupLastName.sendKeys(lastName);
        signupUsername.clear();
        signupUsername.sendKeys(username);
        signupPassword.clear();
        signupPassword.sendKeys(password);
        signupSubmit.click();
    }

    public boolean isErrorMessageDisplayed() {
        return driver.findElements(By.id("signupError")).size() == 1;
    }
}
