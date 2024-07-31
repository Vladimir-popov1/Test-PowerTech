package TestPowerTech;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Scraper {
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
    public void scraper() throws InterruptedException {
        AuthorizationPP1 authorization = new AuthorizationPP1();
        authorization.authorizePP1(driver);
        var clickLogs = driver.findElement(By.cssSelector("a[href='/logs']"));
        clickLogs.click();
        var todayStr1 = LocalDate.now();
        var threeDaysAgo1 = todayStr1.minusDays(2);
        var threeDaysAgo2 = todayStr1.minusDays(2);
        var formatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String dayStrOne = threeDaysAgo1.format(formatter2);
        String threeDaysAgoStr2 = threeDaysAgo2.format(formatter2);
        var startDateScrapper = driver.findElement(By.name("date_scrape"));
        var endDateScrapper = driver.findElement(By.name("date_scrape2"));
        startDateScrapper.sendKeys(threeDaysAgoStr2);
        endDateScrapper.sendKeys(dayStrOne);
        var runScraper = driver.findElement(By.cssSelector("button[class='btn btn-primary dropdown-toggle']"));
        runScraper.click();
        var list = driver.findElements(By.xpath("//a[starts-with(text(), 'Run')]"));
        for (var i = 0; i < list.size(); i++) {
            try {
                list = driver.findElements(By.xpath("//a[starts-with(text(), 'Run')]"));
                list.get(i).click();
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void scraperNew(WebDriver driver) throws InterruptedException {
        AuthorizationPP1 authorization = new AuthorizationPP1();
        authorization.authorizePP1(driver);
        var clickLogs = driver.findElement(By.cssSelector("a[href='/logs']"));
        clickLogs.click();
        var todayStr1 = LocalDate.now();
        var threeDaysAgo1 = todayStr1.minusDays(2);
        var threeDaysAgo2 = todayStr1.minusDays(2);
        var formatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String dayStrOne = threeDaysAgo1.format(formatter2);
        String threeDaysAgoStr2 = threeDaysAgo2.format(formatter2);
        var startDateScrapper = driver.findElement(By.name("date_scrape"));
        var endDateScrapper = driver.findElement(By.name("date_scrape2"));
        startDateScrapper.sendKeys(threeDaysAgoStr2);
        endDateScrapper.sendKeys(dayStrOne);
        var runScraper = driver.findElement(By.cssSelector("button[class='btn btn-primary dropdown-toggle']"));
        runScraper.click();
        var list = driver.findElements(By.xpath("//a[starts-with(text(), 'Run')]"));
        for (var i = 0; i < list.size(); i++) {
            try {
                list = driver.findElements(By.xpath("//a[starts-with(text(), 'Run')]"));
                list.get(i).click();
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    }



