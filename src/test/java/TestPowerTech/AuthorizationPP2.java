package TestPowerTech;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AuthorizationPP2 {
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
    @Order(1)
    public void authorizationPP2() throws InterruptedException {
        driver.get("https://pp2-test-front.nextmillennium.io/login");
        Thread.sleep(5000);
        var emailPP2 = driver.findElement(By.name("email"));  // выбор поля email
        var passwordPP2 = driver.findElement(By.name("close"));  // выбор поля pass
        emailPP2.sendKeys("vladimir.popov@nextmillennium.io"); // вставка в поле email
        passwordPP2.sendKeys("azsxdc2303"); // вставка в поле pass
        var singIn = driver.findElement(By.cssSelector("button[type='submit']")); // выбор поля sing in
        singIn.click(); // клик кнопки sing in
    }
    public void authorizePP2(WebDriver driver) throws InterruptedException {
            driver.get("https://pp2-test-front.nextmillennium.io/login");
            Thread.sleep(5000);
            var emailPP2 = driver.findElement(By.name("email"));  // выбор поля email
            var passwordPP2 = driver.findElement(By.name("close"));  // выбор поля pass
            emailPP2.sendKeys("vladimir.popov@nextmillennium.io"); // вставка в поле email
            passwordPP2.sendKeys("azsxdc2303"); // вставка в поле pass
            var singIn = driver.findElement(By.cssSelector("button[type='submit']")); // выбор поля sing in
            singIn.click(); // клик кнопки sing in
    }
}

