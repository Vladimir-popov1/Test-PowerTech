package TestPowerTech;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AuthorizationPP1 {
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
    @Order(3)
    public void authorizationPP1() throws InterruptedException {
        driver.get("https://test.nextmillennium.io/logs");
        var passwordInput = driver.findElement(By.name("password"));
        var usernameInput = driver.findElement(By.name("username"));
        usernameInput.sendKeys("nmadmin");
        passwordInput.sendKeys("ptfhBZn8_C3fA5RSU");
        var loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
    }

    public void authorizePP1(WebDriver driver) throws InterruptedException {
        driver.get("https://test.nextmillennium.io/logs");
        var passwordInput = driver.findElement(By.name("password"));
        var usernameInput = driver.findElement(By.name("username"));
        usernameInput.sendKeys("nmadmin");
        passwordInput.sendKeys("ptfhBZn8_C3fA5RSU");
        var loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
    }
}
//        driver.get("https://test.nextmillennium.io/logs");
//        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(30));
//
//        try {
//            WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
//            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
//            usernameInput.sendKeys("nmadmin");
//            passwordInput.sendKeys("ptfhBZn8_C3fA5RSU");
//
//            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
//            loginButton.click();
//
//            // Ждем немного, чтобы страница успела загрузиться после входа
//            Thread.sleep(2000);
//
//        } catch (Exception e) {
//            // Логируем HTML страницы для отладки
//            System.out.println("HTML страницы: " + driver.getPageSource());
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    public void authorizePP1(WebDriver driver) throws InterruptedException {
//        driver.get("https://test.nextmillennium.io/logs");
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//
//        try {
//            WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
//            WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
//            usernameInput.sendKeys("nmadmin");
//            passwordInput.sendKeys("ptfhBZn8_C3fA5RSU");
//
//            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
//            loginButton.click();
//
//            // Ждем немного, чтобы страница успела загрузиться после входа
//            Thread.sleep(2000);
//
//        } catch (Exception e) {
//            // Логируем HTML страницы для отладки
//            System.out.println("HTML страницы: " + driver.getPageSource());
//            e.printStackTrace();
//            throw e;
//        }
//    }
//}



