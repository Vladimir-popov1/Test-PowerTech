package TestPowerTech;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class CheckScraperInfo {
    private static final double TOLERANCE_PERCENTAGE = 4.0; // 4% tolerance
    private WebDriver driver;
    private WebDriverWait wait;
    private LocalDate threeDays3;
    private LocalDate threeDays4;

    private Map<String, Double> analyticsPlatformsRevenueMap;
    private Map<String, Double> pp1LogsMap;
    private Set<String> naPlatforms;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        LocalDate newDay = LocalDate.now();
        threeDays3 = newDay.minusDays(2);
        threeDays4 = newDay.minusDays(2);

        // Инициализация коллекций
        analyticsPlatformsRevenueMap = new HashMap<>();
        pp1LogsMap = new HashMap<>();
        naPlatforms = new HashSet<>();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(7)
    public void checkScraperInfo() throws InterruptedException {
        Allure.step("Запуск Scraper", () -> {
            Scraper scraper2 = new Scraper();
            scraper2.scraperNew(driver);
        });

        Allure.step("Авторизация в Analytics", () -> {
            AuthorizationAP authorizationAP = new AuthorizationAP();
            authorizationAP.authorizeAP(driver); // Авторизация AP
        });

        Allure.step("Клик на кнопку SSPs", () -> {
            var clickLogs1 = driver.findElement(By.cssSelector("a[href='/platforms']"));
            clickLogs1.click(); // Клик SSPs
        });

        Allure.step("Установка диапазона дат и применение", () -> {
            var formatter3 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            var threeDaysAgoStr3 = threeDays3.format(formatter3);
            var threeDaysOneStr4 = threeDays4.format(formatter3);

            var startDateField = driver.findElement(By.name("date_from"));
            var endDateField = driver.findElement(By.name("date_to"));
            startDateField.clear();
            startDateField.sendKeys(threeDaysAgoStr3);
            endDateField.clear();
            endDateField.sendKeys(threeDaysOneStr4);

            var apply = driver.findElement(By.cssSelector("button[id='btnApplyDates']"));
            apply.click();
        });

        Allure.step("Сбор данных с платформ аналитики", () -> {
            List<WebElement> rowsAnalytics = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#tblData-0 > tbody > tr")));
            for (int i = 0; i < rowsAnalytics.size() - 1; i++) {
                WebElement revenueCell = driver.findElement(By.cssSelector("#tblData-0 tbody tr:nth-child(" + (i + 1) + ") > td:nth-child(3)"));
                WebElement platformCell = driver.findElement(By.cssSelector("#tblData-0 tbody tr:nth-child(" + (i + 1) + ") > td:nth-child(2)"));
                var revenue = revenueCell.getText().trim().replace("$ ", "").replace(",", "");
                var platform = platformCell.getText().trim();
                String normalizedPlatform = normalizePlatformName(platform);

                if (revenue.contains("N/A")) {
                    naPlatforms.add(normalizedPlatform);
                } else if (!revenue.isEmpty()) {
                    analyticsPlatformsRevenueMap.put(normalizedPlatform, analyticsPlatformsRevenueMap.getOrDefault(normalizedPlatform, 0.0) + Double.parseDouble(revenue));
                }
                System.out.println("Platform Analytics: " + normalizedPlatform + ", Revenue: " + revenue + " for platform " + platform);
                Allure.step("Platform Analytics: " + normalizedPlatform + ", Revenue: " + revenue + " for platform " + platform);
            }
        });

        Allure.step("Проверка наличия нужной даты в PP1 и сбор данных", () -> {
            boolean foundRequiredDateInPP1 = false;
            driver.get("https://test.nextmillennium.io/scraperlogs");
            List<WebElement> rowsPP1 = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("table > tbody > tr")));
            for (int i = rowsPP1.size() - 1; i >= 0; i--) {
                var revenueCell = driver.findElement(By.cssSelector("table tbody tr:nth-child(" + (i + 1) + ") > td:nth-child(4)"));
                var platformCell = driver.findElement(By.cssSelector("table tbody tr:nth-child(" + (i + 1) + ") > td:nth-child(2)"));
                var datePeriodCell = driver.findElement(By.cssSelector("table tbody tr:nth-child(" + (i + 1) + ") > td:nth-child(3)"));
                var revenue = revenueCell.getText().trim();
                var platform = platformCell.getText().trim();
                var datePeriod = datePeriodCell.getText().trim();
                LocalDate datePP1 = LocalDate.parse(datePeriod, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate expectedDateInPP1 = threeDays3;
                if (datePP1.equals(threeDays3)) {
                    foundRequiredDateInPP1 = true;
                    var normalizedPlatform = normalizePlatformName(platform);
                    if (revenue.contains("N/A")) {
                        naPlatforms.add(normalizedPlatform);
                    } else if (!revenue.isEmpty()) {
                        pp1LogsMap.put(normalizedPlatform, Double.parseDouble(revenue));
                    }
                    System.out.println("Platform PP1: " + normalizedPlatform + ", Revenue: " + revenueCell.getText().trim() + " for platform " + platformCell.getText().trim());
                    Allure.step("Platform PP1: " + normalizedPlatform + ", Revenue: " + revenueCell.getText().trim() + " for platform " + platformCell.getText().trim());
                }
            }
        });

        Allure.step("Объединение и исключение платформ + Сравнение данных", () -> {
            var exchangeBiddersKey = "Exchange Bidders";
            pp1LogsMap.putIfAbsent(exchangeBiddersKey, 0.0);
            analyticsPlatformsRevenueMap.putIfAbsent(exchangeBiddersKey, 0.0);

            combinePlatforms(analyticsPlatformsRevenueMap, exchangeBiddersKey, "OneTag EB", "Index EB", "Magnite EB", "OpenX EB", "Pubmatic EB", "Sovrn EB");
            combinePlatforms(pp1LogsMap, exchangeBiddersKey, "OneTag EB", "Index EB", "Magnite EB", "OpenX EB", "Pubmatic EB", "Sovrn EB");

            excludePlatforms(analyticsPlatformsRevenueMap, "OneTag EB", "Index EB", "Magnite EB", "OpenX EB", "Pubmatic EB", "Sovrn EB");
            excludePlatforms(pp1LogsMap, "OneTag EB", "Index EB", "Magnite EB", "OpenX EB", "Pubmatic EB", "Sovrn EB");

            boolean exchangeBiddersWithinTolerance = isWithinTolerance(
                    analyticsPlatformsRevenueMap.get(exchangeBiddersKey),
                    pp1LogsMap.get(exchangeBiddersKey),
                    TOLERANCE_PERCENTAGE
            );

            System.out.println("\nMatching Platforms:\n");
            pp1LogsMap.forEach((platform, pp1Revenue) -> {
                if (analyticsPlatformsRevenueMap.containsKey(platform)) {
                    var analyticsRevenue = analyticsPlatformsRevenueMap.get(platform);
                    boolean withinTolerance = isWithinTolerance(analyticsRevenue, pp1Revenue, TOLERANCE_PERCENTAGE);
                    if (withinTolerance) {
                        System.out.printf("Platform: %s\n  Analytics Revenue: %.2f\n  PP1 Revenue: %.2f\n  Within Tolerance: Yes\n\n",
                                platform, analyticsRevenue, pp1Revenue); //IDE вывод
                        String message = String.format("Platform: %s\n  Analytics Revenue: %.2f\n  PP1 Revenue: %.2f\n  Within Tolerance: Yes\n\n",
                                platform, analyticsRevenue, pp1Revenue);
                        Allure.step(message);//Allure вывод
                    }
                }
            });

            System.out.println("\nNon-matching Platforms:\n");
            pp1LogsMap.forEach((platform, pp1Revenue) -> {
                if (analyticsPlatformsRevenueMap.containsKey(platform)) {
                    var analyticsRevenue = analyticsPlatformsRevenueMap.get(platform);
                    boolean withinTolerance = isWithinTolerance(analyticsRevenue, pp1Revenue, TOLERANCE_PERCENTAGE);
                    if (!withinTolerance) {
                        System.out.printf("Platform: %s\n  Analytics Revenue: %.2f\n  PP1 Revenue: %.2f\n  Within Tolerance: No\n\n",
                                platform, analyticsRevenue, pp1Revenue); //IDE вывод
                        String message = String.format("Platform: %s\n  Analytics Revenue: %.2f\n  PP1 Revenue: %.2f\n  Within Tolerance: No\n\n",
                                platform, analyticsRevenue, pp1Revenue);
                        Allure.step(message); //Allure вывод
                    }
                }
            });

            System.out.println("\nPlatforms with 'N/A' revenue in both analytics and PP1:\n");
            naPlatforms.forEach(platform -> {
                if (!pp1LogsMap.containsKey(platform) && !analyticsPlatformsRevenueMap.containsKey(platform)) {
                    System.out.println("Platform " + platform + " has 'N/A' revenue in both analytics and pp1");
                    String message = "Platform " + platform + " has 'N/A' revenue in both analytics and pp1";
                    Allure.step(message); //Allure вывод
                }
            });
        });
    }

    @Step("Проверка, находится ли значение в пределах допустимого отклонения")
    private boolean isWithinTolerance(double value1, double value2, double tolerancePercentage) {
        double tolerance = Math.max(value1, value2) * (tolerancePercentage / 100);
        return Math.abs(value1 - value2) <= tolerance;
    }

    @Step("Объединение данных по платформам")
    private void combinePlatforms(Map<String, Double> revenueMap, String targetPlatform, String... platformsToCombine) {
        double combinedRevenue = revenueMap.getOrDefault(targetPlatform, 0.0);
        for (String platform : platformsToCombine) {
            if (revenueMap.containsKey(platform)) {
                combinedRevenue += revenueMap.get(platform);
                revenueMap.remove(platform); // Удаляем платформу после объединения
            }
        }
        revenueMap.put(targetPlatform, combinedRevenue);
    }

    @Step("Исключение данных по платформам")
    private void excludePlatforms(Map<String, Double> revenueMap, String... platformsToExclude) {
        for (String platform : platformsToExclude) {
            revenueMap.remove(platform);
        }
    }

    @Step("Нормализация названия платформы")
    private String normalizePlatformName(String platform) {
        if (platform.equalsIgnoreCase("Magnite") || platform.equalsIgnoreCase("Magnite 26294") || platform.equalsIgnoreCase("Magnite 26296")) {
            return "Magnite";
        } else if (platform.equalsIgnoreCase("Index") || platform.equalsIgnoreCase("Index 207985")) {
            return "Index";
        } else if (platform.equalsIgnoreCase("Xandr") || platform.equalsIgnoreCase("Xandr ID 15799")) {
            return "Xandr";
        } else if (platform.equalsIgnoreCase("33Across") || platform.equalsIgnoreCase("33across 001Pg000007PnplIAC")) {
            return "33Across";
        } else if (platform.equalsIgnoreCase("OneTag EB") || platform.equalsIgnoreCase("Index EB") ||
                platform.equalsIgnoreCase("Magnite EB") || platform.equalsIgnoreCase("OpenX EB") ||
                platform.equalsIgnoreCase("Pubmatic EB") || platform.equalsIgnoreCase("Sovrn EB")) {
            return "Exchange Bidders";
        } else {
            return platform;
        }
    }
}


