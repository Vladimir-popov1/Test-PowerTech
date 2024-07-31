package TestPowerTech;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AuthorizationAP {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        System.out.println("Test Run");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(2)
    public void authorizationAP() throws InterruptedException {
        driver.get("https://dev.analytics.nextmillmedia.com/platforms");
        var passwordInput = driver.findElement(By.name("password"));  // выбор поля пароль
        var usernameInput = driver.findElement(By.name("username"));  // выбор поля логин
        usernameInput.sendKeys("nmadmin"); // вставка в поле username
        passwordInput.sendKeys("ptfhBZn8_C3fA5RSU"); // вставка в поле pass
        var loginButton = driver.findElement(By.cssSelector("button[type='submit']")); // выбор поля Login
        loginButton.click(); // клик кнопки login
    }

    public void authorizeAP(WebDriver driver) throws InterruptedException {
            driver.get("https://dev.analytics.nextmillmedia.com/platforms");
            var passwordInput = driver.findElement(By.name("password"));  // выбор поля пароль
            var usernameInput = driver.findElement(By.name("username"));  // выбор поля логин
            usernameInput.sendKeys("nmadmin"); // вставка в поле username
            passwordInput.sendKeys("ptfhBZn8_C3fA5RSU"); // вставка в поле pass
            var loginButton = driver.findElement(By.cssSelector("button[type='submit']")); // выбор поля Login
            loginButton.click(); // клик кнопки login
        }
    }


