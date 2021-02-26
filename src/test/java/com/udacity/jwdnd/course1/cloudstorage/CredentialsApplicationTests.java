package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.page.CredentialsPage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CredentialsApplicationTests {

    private static WebDriver driver;

    @LocalServerPort
    private int port;

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

        WebDriverWait wait = new WebDriverWait(driver, 3);

        baseUrl = "http://localhost:" + port;

        String username = "JD";
        String password = "1234";

        driver.get(baseUrl + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.attemptSignup(username, password, "John", "Doe");

        // check for successful signup by waiting for redirect
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/login"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.attemptLogin(username, password);

        // check for successful login by waiting for redirect
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/files"));
    }

    @Test
    void testAddCredential() {

        WebDriverWait wait = new WebDriverWait(driver, 3);
        CredentialsPage credentialsPage = null;
        ResultPage resultPage = null;

        String url = "http://localhost:8080/login";
        String username = "admin";
        String password = "password";

        driver.get(baseUrl + "/credentials");
        credentialsPage = new CredentialsPage(driver);

        Assertions.assertEquals(0, credentialsPage.getAllCredentials().size());

        Credential credentialAdd = new Credential();
        credentialAdd.setUrl(url);
        credentialAdd.setUsername(username);
        credentialAdd.setPassword(password);
        credentialsPage.addCredential(credentialAdd);
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/result"));
        resultPage = new ResultPage(driver);
        Assertions.assertTrue(resultPage.isSaved());
        driver.get(baseUrl + "/credentials");
        credentialsPage = new CredentialsPage(driver);

        List<Credential> credentialAddResults = credentialsPage.getAllCredentials();
        Assertions.assertEquals(1, credentialAddResults.size());
        Credential credentialAddResult = credentialAddResults.get(0);
        Assertions.assertEquals(url, credentialAddResult.getUrl());
        Assertions.assertEquals(username, credentialAddResult.getUsername());
        Assertions.assertNotEquals(password, credentialAddResult.getPassword());
    }

    @Test
    void testEditCredential() {

        WebDriverWait wait = new WebDriverWait(driver, 3);
        CredentialsPage credentialsPage = null;
        ResultPage resultPage = null;

        Integer credentialId = null;
        String url = "http://localhost:8080/login";
        String username = "admin";
        String password = "password";

        driver.get(baseUrl + "/credentials");
        credentialsPage = new CredentialsPage(driver);

        Assertions.assertEquals(0, credentialsPage.getAllCredentials().size());

        Credential credentialAdd = new Credential();
        credentialAdd.setUrl(url);
        credentialAdd.setUsername(username);
        credentialAdd.setPassword(password);
        credentialsPage.addCredential(credentialAdd);
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/result"));
        resultPage = new ResultPage(driver);
        Assertions.assertTrue(resultPage.isSaved());
        driver.get(baseUrl + "/credentials");
        credentialsPage = new CredentialsPage(driver);

        List<Credential> credentialAddResults = credentialsPage.getAllCredentials();
        Assertions.assertEquals(1, credentialAddResults.size());
        Credential credentialAddResult = credentialAddResults.get(0);
        credentialId = credentialAddResult.getCredentialId();
        Assertions.assertEquals(url, credentialAddResult.getUrl());
        Assertions.assertEquals(username, credentialAddResult.getUsername());
        Assertions.assertNotEquals(password, credentialAddResult.getPassword());

        Credential credentialAddResultModal = credentialsPage.getModalCredential(credentialId);
        Assertions.assertEquals(password, credentialAddResultModal.getPassword());  // verify password is plain in modal

        url = "http://localhost:8080/login";
        username = "administrator";
        password = "/>455word";

        Credential credentialEdit = new Credential();
        credentialEdit.setCredentialId(credentialId);
        credentialEdit.setUrl(url);
        credentialEdit.setUsername(username);
        credentialEdit.setPassword(password);
        credentialsPage.editCredential(credentialEdit);
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/result"));
        resultPage = new ResultPage(driver);
        Assertions.assertTrue(resultPage.isSaved());
        driver.get(baseUrl + "/credentials");
        credentialsPage = new CredentialsPage(driver);

        List<Credential> credentialEditResults = credentialsPage.getAllCredentials();
        Assertions.assertEquals(1, credentialEditResults.size());
        Credential credentialEditResult = credentialEditResults.get(0);
        Assertions.assertEquals(credentialId, credentialEditResult.getCredentialId());
        Assertions.assertEquals(url, credentialEditResult.getUrl());
        Assertions.assertEquals(username, credentialEditResult.getUsername());
        Assertions.assertNotEquals(password, credentialEditResult.getPassword());
    }

    @Test
    void testDeleteCredential() {

        WebDriverWait wait = new WebDriverWait(driver, 3);
        CredentialsPage credentialsPage = null;
        ResultPage resultPage = null;

        Integer credentialId = null;
        String url = "http://localhost:8080/login";
        String username = "admin";
        String password = "password";

        driver.get(baseUrl + "/credentials");
        credentialsPage = new CredentialsPage(driver);

        Assertions.assertEquals(0, credentialsPage.getAllCredentials().size());

        Credential credentialAdd = new Credential();
        credentialAdd.setUrl(url);
        credentialAdd.setUsername(username);
        credentialAdd.setPassword(password);
        credentialsPage.addCredential(credentialAdd);
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/result"));
        resultPage = new ResultPage(driver);
        Assertions.assertTrue(resultPage.isSaved());
        driver.get(baseUrl + "/credentials");
        credentialsPage = new CredentialsPage(driver);

        List<Credential> credentialAddResults = credentialsPage.getAllCredentials();
        Assertions.assertEquals(1, credentialAddResults.size());
        Credential credentialAddResult = credentialAddResults.get(0);
        credentialId = credentialAddResult.getCredentialId();

        credentialsPage.deleteCredential(credentialId);
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/result"));
        resultPage = new ResultPage(driver);
        Assertions.assertTrue(resultPage.isDeleted());
        driver.get(baseUrl + "/credentials");
        credentialsPage = new CredentialsPage(driver);

        Assertions.assertEquals(0, credentialsPage.getAllCredentials().size());
    }
}
