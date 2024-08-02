package TestPowerTech;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class CheckInfoReportsPP1PP2 {
    private WebDriver driver;
    private Map<String, String> metricsPP1;
    private WebDriverWait wait;
    private Actions actions;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        actions = new Actions(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(6)
    @Story("Сравнение данных в reports(user)pp1 с reports pp2")
    @DisplayName("Сравнение данных в reports(user)pp1 с reports pp2")
    //Сравнение данных в reports(user)pp1 с reports pp2
    public void checkInfoReportsPP1PP2() throws InterruptedException {
        Allure.step("Авторизация в PP1", () -> {
            AuthorizationPP1 authorization = new AuthorizationPP1();
            authorization.authorizePP1(driver); //Авторизация ПП1

        });

        Allure.step("Получене отчетов из PP1", () -> {
            Reports reports = new Reports();
            reports.reportsPP1(driver);
        });

        Allure.step("Выбор аккаунта и отчетов", () -> {
            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(30));
            driver.findElement(By.cssSelector("a[href='/accounts']")).click();
            driver.findElement(By.cssSelector("input[type='search']")).sendKeys("techtest");
            driver.findElement(By.cssSelector("a[href*='/reports2?userid=']")).click();
            Thread.sleep(10000);
            var newDays = LocalDate.now();
            var randomDays = newDays.minusDays(2);
            var formatterOne = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String newDaysAgoStr = randomDays.format(formatterOne);
            String newDaysOneStr = newDays.format(formatterOne);

            WebElement startDateReports = driver.findElement(By.name("date_from"));
            WebElement lastDateReports = driver.findElement(By.name("date_to"));
            startDateReports.clear();
            startDateReports.sendKeys(newDaysAgoStr);
            lastDateReports.clear();
            lastDateReports.sendKeys(newDaysOneStr);
            Thread.sleep(10000);
            driver.findElement(By.cssSelector("button[id=btnApplyDates]")).click();
        });
        Thread.sleep(8000);
        Allure.step("Сбор метрик из отчетов PP1", () -> {
            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(30));
            String[] selectors = {
                    "div.reports-tabular.container > div.row.data-boxes > div:nth-child(4) h3.text-center.info",
                    "div.reports-tabular.container > div.row.data-boxes > div:nth-child(5) h3.text-center.info",
                    "div.reports-tabular.container > div.row.data-boxes > div:nth-child(6) h3.text-center.info",
                    "div.reports-tabular.container > div.row.data-boxes > div:nth-child(7) h3.text-center.info"
            };
            String[] metricNames = {"Earnings", "Bid Requests", "Impressions", "eCPM"};
            Map<String, String> metrics = new HashMap<>();
            for (int i = 0; i < selectors.length; i++) {
                List<WebElement> elements = driver.findElements(By.cssSelector(selectors[i]));
                if (elements.size() > 0) {
                    String value = wait1.until(ExpectedConditions.visibilityOf(elements.get(0))).getText().trim().replace("$ ", "").replace(",", ".");
                    metrics.put(metricNames[i], value);
                } else {
                    System.out.println("Элемент с селектором " + selectors[i] + " не найден.");
                }
            }
            metricsPP1 = metrics; // Сохранение метрик PP1
            metrics.forEach((k, v) -> System.out.println(k + ": " + v));
        });

        Allure.step("Авторизация в PP2 и выбор отчетов", () -> {
            AuthorizationPP2 authorization2 = new AuthorizationPP2();
            authorization2.authorizePP2(driver);
            Thread.sleep(10000);
            WebElement clickBurg = driver.findElement(By.cssSelector("#app > main > div.mobile-app-menu-bar.svelte-q1bp0t > div > div > div:nth-child(3) > button"));
            if (clickBurg.isDisplayed() && clickBurg.isEnabled()) {
                clickBurg.click();
            } else {
                System.out.println("Кнопка бутера не кликабельна или не отображена на странице.");
            }

            WebElement clickReports1 = driver.findElement(By.cssSelector("a[href='/reports']"));
            if (clickReports1.isDisplayed() && clickReports1.isEnabled()) {
                clickReports1.click();
            } else {
                System.out.println("Кнопка 'REPORTS' не кликабельна или не отображена на странице.");
            }

            try {
                WebElement overviev1 = driver.findElement(By.cssSelector("#app > main > div.active-item.w-full.px-20.py-20.bg-white.xl\\:hidden.svelte-q1bp0t > a:nth-child(6) > div.pl-10.svelte-q1bp0t.pt-15 > a > div"));
                if (overviev1.isDisplayed() && overviev1.isEnabled()) {
                    overviev1.click();
                }
            } catch (NoSuchElementException e) {
                System.out.println("Элемент overviev1 не найден, попытка кликнуть customRange.");
            }

            Thread.sleep(10000);
            WebElement customRange = driver.findElement(By.cssSelector("div[class='flex justify-between items-center']"));
            if (customRange.isDisplayed() && customRange.isEnabled()) {
                customRange.click();
                Thread.sleep(10000);
            } else {
                System.out.println("Кнопка customRange не кликабельна или не отображена на странице.");
            }
        });

        Allure.step("Выбор нужной даты и применение фильтра в PP2", () -> {
            LocalDate currentDate = LocalDate.now();
            LocalDate targetDate = currentDate.minusDays(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d"); // Форматируем дату как "день"
            String targetDay = targetDate.format(formatter); // Преобразуем в строку

            // Проверка, является ли текущая дата 1 или 2 числом месяца
            if (currentDate.getDayOfMonth() == 1 || currentDate.getDayOfMonth() == 2) {
                System.out.println("Текущая дата: " + currentDate.getDayOfMonth() + ". Переход на предыдущий месяц.");

                // Переход на предыдущий месяц
                try {
                    WebElement previousMonthButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".button-previous-month")));
                    if (previousMonthButton.isDisplayed()) {
                        previousMonthButton.click();
                        System.out.println("Перешли на предыдущий месяц.");
                    } else {
                        System.out.println("Кнопка перехода на предыдущий месяц не видима.");
                    }
                } catch (Exception e) {
                    System.out.println("Не удалось перейти на предыдущий месяц. Ошибка: " + e.getMessage());
                    return; // Завершаем выполнение, если не удалось перейти на предыдущий месяц
                }
            } else {
                System.out.println("Текущая дата: " + currentDate.getDayOfMonth() + ". Переход на предыдущий месяц не требуется.");
            }

            // Формирование XPath для нужного дня
            String dayXPath = String.format("//*[contains(@class, 'day-item') and text()='%s']", targetDay);

            // Ожидание и выбор нужного дня
            Actions actions = new Actions(driver);
            try {
                WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dayXPath)));
                actions.doubleClick(dayElement).perform();
                System.out.println("Кнопка дня '" + targetDay + "' была успешно кликнута.");
            } catch (Exception e) {
                System.out.println("Кнопка дня '" + targetDay + "' не найдена или не кликабельна. Ошибка: " + e.getMessage());
            }

            // Клик на кнопку OK
            try {
                Thread.sleep(5000);
                WebElement okButton = driver.findElement(By.xpath("//button[@class='button-apply']"));
                okButton.click(); // Клик кнопки ОК
                System.out.println("Кнопка OK была успешно кликнута.");
            } catch (Exception e) {
                System.out.println("Кнопка OK не найдена или не кликабельна. Ошибка: " + e.getMessage());
            }
            Thread.sleep(10000);
        });

        Allure.step("Сбор метрик из отчетов PP2 и сравнение с PP1", () -> {
            String[] pp2Selectors = {
                    "//div[contains(@class, 'metrics-card')]//div[contains(text(), 'Revenue')]/following-sibling::div",
                    "//div[contains(@class, 'metrics-card')]//div[contains(text(), 'Ad Opportunities')]/following-sibling::div",
                    "//div[contains(@class, 'metrics-card')]//div[contains(text(), 'Ad Requests')]/following-sibling::div",
                    "//div[contains(@class, 'metrics-card')]//div[contains(text(), 'Impressions')]/following-sibling::div",
                    "//div[contains(@class, 'metrics-card')]//div[contains(text(), 'CPM')]/following-sibling::div"
            };
            String[] pp2MetricNames = {"Revenue", "Ad Opportunities", "Ad Requests", "Impressions", "CPM"};
            Map<String, String> pp2Metrics = new HashMap<>();
            for (int i = 0; i < pp2Selectors.length; i++) {
                List<WebElement> elements = driver.findElements(By.xpath(pp2Selectors[i]));
                if (elements.size() > 0) {
                    String value = new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(elements.get(0))).getText().trim().replace("$", "").replace(",", ".");
                    pp2Metrics.put(pp2MetricNames[i], value);
                } else {
                    System.out.println("Элемент с селектором " + pp2Selectors[i] + " не найден.");
                }
            }
            compareMetrics(metricsPP1, pp2Metrics);
        });
    }

    public void compareMetrics(Map<String, String> metrics, Map<String, String> pp2Metrics) {
        Allure.step("Сравнение метрик PP1 и PP2", () -> {
            String[] metricPairs = {"Earnings:Revenue", "Bid Requests:Ad Requests", "Impressions:Impressions", "eCPM:CPM"};
            for (String pair : metricPairs) {
                String[] parts = pair.split(":");
                String metric1 = parts[0];
                String metric2 = parts[1];
                String value1 = metrics.get(metric1);
                String value2 = pp2Metrics.get(metric2);
                if (value1 != null && value2 != null) {
                    boolean areValuesClose = areValuesWithinOnePercent(value1, value2);
                    if (areValuesClose) {
                        String message = metric1 + " и " + metric2 + " совпадают: " + value1 + " = " + value2;
                        System.out.println(message);
                        Allure.step(message);
                    } else {
                        String message = metric1 + " и " + metric2 + " не совпадают: " + value1 + " = " + value2;
                        System.out.println(message);
                        throw new AssertionError(message);
                    }
                } else {
                    String message = "Одно из значений для метрик " + metric1 + " или " + metric2 + " отсутствует.";
                    System.out.println(message);
                    throw new AssertionError(message);
                }
            }
            String adOpportunities = pp2Metrics.get("Ad Opportunities");
            if (adOpportunities != null) {
                String message = "Ad Opportunities: " + adOpportunities;
                System.out.println(message);
                Allure.step(message);
            } else {
                String message = "Метрика Ad Opportunities не найдена.";
                System.out.println(message);
                throw new AssertionError(message);
            }
        });
    }

    private boolean areValuesWithinOnePercent(String value1, String value2) {
        try {
            double num1 = Double.parseDouble(value1.replace(".", ""));
            double num2 = Double.parseDouble(value2.replace(".", ""));
            double difference = Math.abs(num1 - num2);
            double allowableDifference = 0.01 * Math.min(num1, num2);
            return difference <= allowableDifference;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка преобразования значений: " + value1 + " или " + value2);
            return false;
        }
    }
}
