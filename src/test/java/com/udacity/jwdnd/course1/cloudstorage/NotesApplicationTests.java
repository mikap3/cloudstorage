package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.NotesPage;
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
class NotesApplicationTests {

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
	void testAddNote() {

		WebDriverWait wait = new WebDriverWait(driver, 3);
		NotesPage notesPage = null;
		ResultPage resultPage = null;

		String title = "Buy groceries";
		String description = "apples, bread, milk, cornflakes";

		driver.get(baseUrl + "/notes");
		notesPage = new NotesPage(driver);

		Assertions.assertEquals(0, notesPage.getAllNotes().size());

		Note noteAdd = new Note();
		noteAdd.setTitle(title);
		noteAdd.setDescription(description);
		notesPage.addNote(noteAdd);
		wait.until(ExpectedConditions.urlToBe(baseUrl + "/result"));
		resultPage = new ResultPage(driver);
		Assertions.assertTrue(resultPage.isSaved());
		driver.get(baseUrl + "/notes");
		notesPage = new NotesPage(driver);

		List<Note> noteAddResults = notesPage.getAllNotes();
		Assertions.assertEquals(1, noteAddResults.size());
		Note noteAddResult = noteAddResults.get(0);
		Assertions.assertEquals(title, noteAddResult.getTitle());
		Assertions.assertEquals(description, noteAddResult.getDescription());
	}

	@Test
	void testEditNote() {

		WebDriverWait wait = new WebDriverWait(driver, 3);
		NotesPage notesPage = null;
		ResultPage resultPage = null;

		Integer noteId = null;
		String title = "Buy groceries";
		String description = "apples, bread, milk, cornflakes";

		driver.get(baseUrl + "/notes");
		notesPage = new NotesPage(driver);

		Assertions.assertEquals(0, notesPage.getAllNotes().size());

		Note noteAdd = new Note();
		noteAdd.setTitle(title);
		noteAdd.setDescription(description);
		notesPage.addNote(noteAdd);
		wait.until(ExpectedConditions.urlToBe(baseUrl + "/result"));
		resultPage = new ResultPage(driver);
		Assertions.assertTrue(resultPage.isSaved());
		driver.get(baseUrl + "/notes");
		notesPage = new NotesPage(driver);

		List<Note> noteAddResults = notesPage.getAllNotes();
		Assertions.assertEquals(1, noteAddResults.size());
		Note noteAddResult = noteAddResults.get(0);
		Assertions.assertEquals(title, noteAddResult.getTitle());
		Assertions.assertEquals(description, noteAddResult.getDescription());

		noteId = noteAddResult.getNoteId();
		title = "Buy sweets";
		description = "cookies, chocolate";

		Note noteEdit = new Note();
		noteEdit.setNoteId(noteId);
		noteEdit.setTitle(title);
		noteEdit.setDescription(description);
		notesPage.editNote(noteEdit);
		wait.until(ExpectedConditions.urlToBe(baseUrl + "/result"));
		resultPage = new ResultPage(driver);
		Assertions.assertTrue(resultPage.isSaved());
		driver.get(baseUrl + "/notes");
		notesPage = new NotesPage(driver);

		List<Note> noteEditResults = notesPage.getAllNotes();
		Assertions.assertEquals(1, noteEditResults.size());
		Note noteEditResult = noteEditResults.get(0);
		Assertions.assertEquals(noteId, noteEditResult.getNoteId());
		Assertions.assertEquals(title, noteEditResult.getTitle());
		Assertions.assertEquals(description, noteEditResult.getDescription());
	}

	@Test
	void testDeleteNote() {

		WebDriverWait wait = new WebDriverWait(driver, 3);
		NotesPage notesPage = null;
		ResultPage resultPage = null;

		Integer noteId = null;
		String title = "Buy groceries";
		String description = "apples, bread, milk, cornflakes";

		driver.get(baseUrl + "/notes");
		notesPage = new NotesPage(driver);

		Assertions.assertEquals(0, notesPage.getAllNotes().size());

		Note noteAdd = new Note();
		noteAdd.setTitle(title);
		noteAdd.setDescription(description);
		notesPage.addNote(noteAdd);
		wait.until(ExpectedConditions.urlToBe(baseUrl + "/result"));
		resultPage = new ResultPage(driver);
		Assertions.assertTrue(resultPage.isSaved());
		driver.get(baseUrl + "/notes");
		notesPage = new NotesPage(driver);

		List<Note> noteAddResults = notesPage.getAllNotes();
		Assertions.assertEquals(1, noteAddResults.size());
		Note noteAddResult = noteAddResults.get(0);
		noteId = noteAddResult.getNoteId();

		notesPage.deleteNote(noteId);
		wait.until(ExpectedConditions.urlToBe(baseUrl + "/result"));
		resultPage = new ResultPage(driver);
		Assertions.assertTrue(resultPage.isDeleted());
		driver.get(baseUrl + "/notes");
		notesPage = new NotesPage(driver);

		Assertions.assertEquals(0, notesPage.getAllNotes().size());
	}
}
