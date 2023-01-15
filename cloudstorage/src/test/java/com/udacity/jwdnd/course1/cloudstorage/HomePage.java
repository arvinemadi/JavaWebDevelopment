package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class HomePage {

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTab;
    // note of these are avialable until the tab is clicked.
    private WebElement addNoteButton;
    private WebElement noteTitle;
    private WebElement noteDescription;

    @FindBy(id = "note-submit")
    private WebElement submitNoteButton;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialTab;
    private WebElement addCredentialButton;
    private WebElement credentialURL;
    private WebElement credentialUsername;
    private WebElement credentialPassword;

    @FindBy(id = "cred-submit")
    private WebElement submitCredentialButton;

    private WebDriverWait wait;
    private WebDriver webDriver;


    public HomePage(WebDriver webDriver, WebDriverWait wait) {
        this.webDriver = webDriver;
        this.wait = wait;
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        logoutButton.click();
    }

    public void createNote(String noteTitle, String noteDescription) throws InterruptedException {
        Thread.sleep(1000);
        this.noteTab.click();

        this.wait.until(ExpectedConditions.elementToBeClickable(By.id("add-note")));
        addNoteButton = webDriver.findElement(By.id("add-note"));
        this.addNoteButton.click();

        this.noteTitle = webDriver.findElement(By.id("note-title"));
        this.wait.until(ExpectedConditions.visibilityOf(this.noteTitle));
        this.noteTitle.sendKeys(noteTitle);

        this.noteDescription = webDriver.findElement(By.id("note-description"));
        this.noteDescription.sendKeys(noteDescription);
        this.submitNoteButton.click();
    }

    public void isNoteDeleted() throws InterruptedException {
        Thread.sleep(1000);
        this.noteTab.click();

        List rows = webDriver.findElements(By.xpath("//*[@id='userTable']/tbody/tr"));
        Assertions.assertEquals(0, rows.size());
    }

    public void isNoteEdited() throws InterruptedException {
        Thread.sleep(1000);
        this.noteTab.click();
        //Thread.sleep(1000);
        this.wait.until(ExpectedConditions.visibilityOf
                (this.webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/th"))));
        noteTitle = this.webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/th"));
        noteDescription = this.webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/td[2]"));

        Assertions.assertEquals("Tests", noteTitle.getText());
        Assertions.assertEquals("Testing is fun! edited", noteDescription.getText());
    }

    public void isNoteVisible() throws InterruptedException {
        Thread.sleep(1000);
        this.noteTab.click();

        this.wait.until(ExpectedConditions.visibilityOf
                (this.webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/th"))));
        noteTitle = this.webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/th"));
        noteDescription = this.webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/td[2]"));

        Assertions.assertEquals("Test", noteTitle.getText());
        Assertions.assertEquals("Testing is fun!", noteDescription.getText());

    }

    public void editNote(String noteTitle, String noteDescription) throws InterruptedException {
        Thread.sleep(1000);
        this.noteTab.click();

        WebElement editButton = this.webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/td[1]/button"));
        this.wait.until(ExpectedConditions.elementToBeClickable(editButton));
        editButton.click();

        this.noteTitle = webDriver.findElement(By.id("note-title"));
        this.wait.until(ExpectedConditions.visibilityOf(this.noteTitle));
        this.noteTitle.sendKeys(noteTitle);

        this.noteDescription = webDriver.findElement(By.id("note-description"));
        this.noteDescription.sendKeys(noteDescription);
        this.submitNoteButton.click();
    }

    public void deleteNote() throws InterruptedException {
        Thread.sleep(1000);
        this.noteTab.click();
        WebElement deleteButton = this.webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/td[1]/a"));
        System.out.println("Clicked the delete button for a note delete....");
        this.wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        deleteButton.click();
    }

    public void createCredential(String url, String username, String password) throws InterruptedException {
        Thread.sleep(1000);
        this.credentialTab.click();

        this.wait.until(ExpectedConditions.elementToBeClickable(By.id("add-cred")));
        addCredentialButton = webDriver.findElement(By.id("add-cred"));
        this.addCredentialButton.click();

        this.credentialURL = webDriver.findElement(By.id("credential-url"));
        this.wait.until(ExpectedConditions.visibilityOf(this.credentialURL));
        this.credentialURL.sendKeys(url);

        this.credentialUsername = webDriver.findElement(By.id("credential-username"));
        this.credentialUsername.sendKeys(username);

        this.credentialPassword = webDriver.findElement(By.id("credential-password"));
        this.credentialPassword.sendKeys(password);

        this.submitCredentialButton.click();

    }

    public void isCredentialVisible() throws InterruptedException {
        Thread.sleep(1000);
        this.credentialTab.click();

        this.wait.until(ExpectedConditions.visibilityOf
                (this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th"))));
        credentialURL = this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th"));
        credentialUsername = this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[2]"));
        credentialPassword = this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[3]"));

        Assertions.assertEquals("rwilcox", credentialUsername.getText());
        Assertions.assertEquals("www.google.com", credentialURL.getText());
        Assertions.assertNotEquals("Password123", credentialPassword.getText());


    }

    public void editCredential(String url, String username, String password) throws InterruptedException {
        Thread.sleep(1000);
        this.credentialTab.click();
        WebElement editButton = this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[1]/button"));
        this.wait.until(ExpectedConditions.elementToBeClickable(editButton));
        editButton.click();

        credentialURL = webDriver.findElement(By.id("credential-url"));
        this.wait.until(ExpectedConditions.visibilityOf(this.credentialURL));
        credentialURL.clear();
        credentialURL.sendKeys(url);

        credentialUsername = webDriver.findElement(By.id("credential-username"));
        credentialUsername.clear();
        credentialUsername.sendKeys(username);

        credentialPassword = webDriver.findElement(By.id("credential-password"));

        Assertions.assertEquals("password123", credentialPassword.getAttribute("value"));

        credentialPassword.clear();
        credentialPassword.sendKeys(password);

        this.submitCredentialButton.click();
    }

    public void isCredentialEdited() throws InterruptedException {
        Thread.sleep(1000);
        this.credentialTab.click();

        this.wait.until(ExpectedConditions.visibilityOf
                (this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th"))));
        credentialURL = this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th"));
        credentialUsername = this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[2]"));
        credentialPassword = this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[3]"));

        Assertions.assertEquals("ryan", credentialUsername.getText());
        Assertions.assertEquals("www.yahoo.com", credentialURL.getText());
    }

    public void deleteCredential() throws InterruptedException {
        Thread.sleep(1000);
        this.credentialTab.click();
        WebElement deleteButton = this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[1]/a"));
        this.wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        deleteButton.click();
    }

    public void isCredentialDeleted() throws InterruptedException {
        Thread.sleep(1000);
        this.credentialTab.click();

        List rows = webDriver.findElements(By.xpath("//*[@id='credentialTable']/tbody/tr"));
        Assertions.assertEquals(0, rows.size());

    }
}
