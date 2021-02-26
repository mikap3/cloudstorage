package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ResultPage {

    private final WebDriver driver;

    public ResultPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isSaved() {
        return driver.findElements(By.id("resultSaved")).size() == 1;
    }

    public boolean isDeleted() {
        return driver.findElements(By.id("resultDeleted")).size() == 1;
    }

    public boolean isError() {
        return driver.findElements(By.id("resultError")).size() == 1;
    }
}
