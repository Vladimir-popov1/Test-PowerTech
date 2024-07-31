package TestPowerTech;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reports {
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
    @Order(5)  //Запуск репортов по пользаку
    public void reports() throws InterruptedException {
        var clickLogs = driver.findElement(By.cssSelector("a[href='/logs']")); //выбор кнопки logs
        clickLogs.click(); // клик logs
        var today = LocalDate.now(); //Вычисление текущей даты и даты три дня назад
        var threeDaysAgo = today.minusDays(2); //даты 2 дня назад
        var threeDaysOne = today.minusDays(2); //даты 2 дня назад
        var formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); //Форматирование дат в строковый формат
        String threeDaysAgoStr = threeDaysAgo.format(formatter);// ---
        String threeDaysOneStr = threeDaysOne.format(formatter);// ---
        var startDateField = driver.findElement(By.name("date_reports")); //Поле даты генератора
        var endDateField = driver.findElement(By.name("date_reports2")); //Поле даты генератора
        startDateField.sendKeys(threeDaysAgoStr); //вставка числа в даты 1
        endDateField.sendKeys(threeDaysOneStr); //вставка числа в даты 2
        var checkUser = driver.findElement(By.cssSelector(".bootstrap-select button[data-id=\"ddlReportsUser\"] span.filter-option"));// Необходимый дропдаун
        checkUser.click(); //Кнопка дропдауна
        var user = driver.findElement(By.xpath("//span[@class='text' and contains(text(), 'techtest')]")); //выбор нужного пользователя Techtest
        user.click(); //Клик
        var generateReports = driver.findElement(By.cssSelector("button[id='btnGenerateReports']"));//выбор кнопки Reports
        generateReports.click();
    }
    public void reportsPP1(WebDriver driver) throws InterruptedException {
        var clickLogs = driver.findElement(By.cssSelector("a[href='/logs']")); //выбор кнопки logs
        clickLogs.click(); // клик logs
        var today = LocalDate.now(); //Вычисление текущей даты и даты три дня назад
        var threeDaysAgo = today.minusDays(2); //даты 2 дня назад
        var threeDaysOne = today.minusDays(2); //даты 2 дня назад
        var formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); //Форматирование дат в строковый формат
        String threeDaysAgoStr = threeDaysAgo.format(formatter);// ---
        String threeDaysOneStr = threeDaysOne.format(formatter);// ---
        var startDateField = driver.findElement(By.name("date_reports")); //Поле даты генератора
        var endDateField = driver.findElement(By.name("date_reports2")); //Поле даты генератора
        startDateField.sendKeys(threeDaysAgoStr); //вставка числа в даты 1
        endDateField.sendKeys(threeDaysOneStr); //вставка числа в даты 2
        var checkUser = driver.findElement(By.cssSelector(".bootstrap-select button[data-id=\"ddlReportsUser\"] span.filter-option"));// Необходимый дропдаун
        checkUser.click(); //Кнопка дропдауна
        var user = driver.findElement(By.xpath("//span[@class='text' and contains(text(), 'techtest')]")); //выбор нужного пользователя Techtest
        user.click(); //Клик
        var generateReports = driver.findElement(By.cssSelector("button[id='btnGenerateReports']"));//выбор кнопки Reports
        generateReports.click();
    }
}

