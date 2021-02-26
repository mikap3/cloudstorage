package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AccessRestrictionTests {

    @LocalServerPort
    private Integer port;

    private static WebDriver driver;

    private String baseUrl;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
    }

    @AfterAll
    public static void afterAll() {
        driver.quit();
        driver = null;
    }

    @BeforeEach
    public void beforeEach() {
        this.baseUrl = "http://localhost:" + port;
    }

    @Test
    void testUnauthorizedAccess() {
        WebDriverWait wait = new WebDriverWait(driver, 3);

        // check access restrictions with redirect to login
        driver.get(baseUrl + "/files");
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/login"));
        driver.get(baseUrl + "/notes");
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/login"));
        driver.get(baseUrl + "/credentials");
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/login"));

        driver.get(baseUrl + "/signup");
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/signup"));
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/login"));
    }

    @Test
    void testSignupLogin() {

        String username = "JD";
        String password = "1234";

        WebDriverWait wait = new WebDriverWait(driver, 3);

        driver.get(baseUrl + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.attemptSignup(username, password, "John", "Doe");

        // check for successful signup by waiting for redirect
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/login"));

        LoginPage loginPage = new LoginPage(driver);
        Assertions.assertTrue(loginPage.isSignupMessageDisplayed());
        loginPage.attemptLogin(username, password);

        // check for successful login by waiting for redirect
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/files"));

        try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
        WebElement navLogout = driver.findElement(By.id("navLogout"));
        navLogout.click();

        // check for successful logout by waiting for redirect
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/login?logout"));
        Assertions.assertTrue(loginPage.isLogoutMessageDisplayed());

        driver.get(baseUrl + "/files");
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/login"));
    }

    @Test
    void testInvalidLogin() {

        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.attemptLogin("John", "none");
        Assertions.assertTrue(loginPage.isErrorMessageDisplayed());
    }
}
