package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.beans.IntrospectionException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private WebDriverWait wait;

	private String baseURL;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.wait = new WebDriverWait(driver, 10);

		baseURL = "http://localhost:" + port;
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	/**
	 *
	 */
	public void signupAndLogin() {
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Ryan", "Wilcox", "rwilcox", "password123");

		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("rwilcox", "password123");
	}


	/**
	 * Attempt to access the homepage without being logged in.
	 */
	@Test
	public void attemptToAccessUnauthorizedPage() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void signupSignoutAuthorization() {

		/**
		 * Sign up, login and then make sure the home page is accessible
		 */
		signupAndLogin();
		Assertions.assertEquals("Home", driver.getTitle());

		/**
		 * Logout, attempt to access the home page, it should be inaccessible
		 */
		driver.get(baseURL + "/home");
		HomePage homePage = new HomePage(driver, wait);
		homePage.logout();
		driver.get(baseURL + "/home");

		Assertions.assertEquals("Login", driver.getTitle());

	}

	@Test
	public void verifyNoteCreation() throws InterruptedException {
		signupAndLogin();
		HomePage homePage = new HomePage(driver, wait);
		homePage.createNote("Test", "Testing is fun!");
		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.id("success")).getText());

		driver.get(baseURL + "/home");

		homePage.isNoteVisible();

		homePage.deleteNote();
	}

	@Test
	public void verifyNoteManipulation() throws InterruptedException {
		signupAndLogin();
		HomePage homePage = new HomePage(driver, wait);
		homePage.createNote("Test", "Testing is fun! ");
		Assertions.assertEquals("Success", driver.findElement(By.id("success")).getText());

		driver.get(baseURL + "/home");

		homePage.editNote("s", "edited");
		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.id("success")).getText());

		driver.get(baseURL + "/home");

		homePage.isNoteEdited();

		homePage.deleteNote();
	}

	@Test
	public void verifyNoteDeletion() throws InterruptedException {
		signupAndLogin();
		HomePage homePage = new HomePage(driver, wait);
		homePage.createNote("Test", "Testing is fun!");
		Assertions.assertEquals("Success", driver.findElement(By.id("success")).getText());

		driver.get(baseURL + "/home");

		homePage.deleteNote();
		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.id("success")).getText());

		driver.get(baseURL + "/home");

		homePage.isNoteDeleted();
	}

	@Test
	public void verifyCredCreationAndEncryption() throws InterruptedException {
		signupAndLogin();
		HomePage homePage = new HomePage(driver, wait);
		homePage.createCredential("www.google.com", "rwilcox", "password123");

		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.id("success")).getText());

		driver.get(baseURL + "/home");

		homePage.isCredentialVisible();

		homePage.deleteCredential();
	}

	@Test
	public void verifyCredentialManipulation() throws InterruptedException {
		signupAndLogin();
		HomePage homePage = new HomePage(driver, wait);
		homePage.createCredential("www.google.com", "rwilcox", "password123");

		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.id("success")).getText());

		driver.get(baseURL + "/home");

		homePage.editCredential("www.yahoo.com", "ryan", "newpassword");

		driver.get(baseURL + "/home");

		homePage.isCredentialEdited();

		homePage.deleteCredential();
	}

	@Test
	public void verifyCredentialDeletion() throws InterruptedException {
		signupAndLogin();
		HomePage homePage = new HomePage(driver, wait);
		homePage.createCredential("www.google.com", "rwilcox", "password123");
		Assertions.assertEquals("Success", driver.findElement(By.id("success")).getText());

		driver.get(baseURL + "/home");

		homePage.deleteCredential();
		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.id("success")).getText());

		driver.get(baseURL + "/home");

		homePage.isCredentialDeleted();
	}

}
