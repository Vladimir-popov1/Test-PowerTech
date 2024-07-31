package TestPowerTech;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestSuite {

    private static final double TOLERANCE_PERCENTAGE = 4.0; // 4% tolerance

    @Nested
    class Authorization {
        private WebDriver driver;
        private Connection connection;

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
        public void authorizationAP() throws InterruptedException {
            driver.get("https://dev.analytics.nextmillmedia.com/platforms");
            var passwordInput = driver.findElement(By.name("password"));  // выбор поля пароль
            var usernameInput = driver.findElement(By.name("username"));  // выбор поля логин
            usernameInput.sendKeys("nmadmin"); // вставка в поле username
            passwordInput.sendKeys("ptfhBZn8_C3fA5RSU"); // вставка в поле pass
            var loginButton = driver.findElement(By.cssSelector("button[type='submit']")); // выбор поля Login
            loginButton.click(); // клик кнопки login
        }

        @Test
        @Order(2)
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

        @Test
        @Order(3)
        public void authorizationPP1() {
            driver.get("https://test.nextmillennium.io/logs");
            var passwordInput = driver.findElement(By.name("password"));
            var usernameInput = driver.findElement(By.name("username"));
            usernameInput.sendKeys("nmadmin");
            passwordInput.sendKeys("ptfhBZn8_C3fA5RSU");
            var loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();
        }

        @Test
        @Order(4) // Скрапп по всем платформам
        public void scraper() throws InterruptedException {
            authorizationPP1();
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

        @Test
        @Order(6)
        //Сравнение данных в reports(user)pp1 с reports pp2
        public void checkInfoReportsPP1PP2() throws InterruptedException {
            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(30));
            authorizationPP1();
            reports();
            Thread.sleep(30000);
            var accounts = driver.findElement(By.cssSelector("a[href='/accounts']")); // выбор поля аккаунт
            accounts.click();
            var searchAccounts = driver.findElement(By.cssSelector("input[type='search']")); // выбор поля поиска
            searchAccounts.sendKeys("techtest"); // вставка нужного юзера
            var clickReports = driver.findElement(By.cssSelector("a[href*='/reports2?userid=']")); // check reports
            clickReports.click(); // click reports

            var newDays = LocalDate.now(); // Вычисление текущей даты и даты три дня назад
            var randomDays = newDays.minusDays(2); // даты 2 дня назад
            var randomDaysTwo = newDays.minusDays(2); // даты 2 дня назад
            var formatterOne = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // Форматирование дат в строковый формат
            String newDaysAgoStr = randomDays.format(formatterOne);// ---
            String newDaysOneStr = randomDaysTwo.format(formatterOne);// ---
            var startDateReports = driver.findElement(By.name("date_from")); // Поле даты репорта
            var lastDateReports = driver.findElement(By.name("date_to")); // Поле даты репорта
            startDateReports.clear();
            startDateReports.sendKeys(newDaysAgoStr); // вставка числа в даты 1
            lastDateReports.clear();
            lastDateReports.sendKeys(newDaysOneStr); // вставка числа в даты 2
            var apply1 = driver.findElement(By.cssSelector("button[id=btnApplyDates]")); // кнопка apply
            apply1.click(); // клик apply

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

            // Пример использования собранных метрик
            for (String metric : metrics.keySet()) {
                System.out.println(metric + ": " + metrics.get(metric));
            }

            authorizationPP2();
            Thread.sleep(10000);
            var clickBurg = driver.findElement(By.cssSelector("#app > main > div.mobile-app-menu-bar.svelte-q1bp0t > div > div > div:nth-child(3) > button")); // выбор кнопки бутера
            if (clickBurg.isDisplayed() && clickBurg.isEnabled()) {
                clickBurg.click();
                System.out.println("Кнопка бутера была успешно кликнута.");
            } else {
                System.out.println("Кнопка бутера не кликабельна или не отображена на странице.");
            }
            var clickReports1 = driver.findElement(By.cssSelector("a[href='/reports']"));// кнопка репорта
            if (clickReports1.isDisplayed() && clickReports1.isEnabled()) {
                clickReports1.click();
                System.out.println("Кнопка 'REPORTS' была успешно кликнута.");
            } else {
                System.out.println("Кнопка 'REPORTS' не кликабельна или не отображена на странице.");
            }
            try {
                var overviev1 = driver.findElement(By.cssSelector("#app > main > div.active-item.w-full.px-20.py-20.bg-white.xl\\:hidden.svelte-q1bp0t > a:nth-child(6) > div.pl-10.svelte-q1bp0t.pt-15 > a > div")); // выбор кнопки overviev
                if (overviev1.isDisplayed() && overviev1.isEnabled()) {
                    overviev1.click();
                    System.out.println("Кнопка overviev1 была успешно кликнута.");
                } else {
                    System.out.println("Кнопка overviev1 не кликабельна или не отображена на странице.");
                }
            } catch (NoSuchElementException e) {
                System.out.println("Элемент overviev1 не найден, попытка кликнуть customRange.");
            }
            Thread.sleep(10000); // Выдержка перед кликом на customRange
            var customRange = driver.findElement(By.cssSelector("div[class='flex justify-between items-center']")); // click customRange
            if (customRange.isDisplayed() && customRange.isEnabled()) {
                customRange.click();
            } else {
                System.out.println("Кнопка customRange не кликабельна или не отображена на странице.");
            }


            // Расчет времени (текущее - 2 дня)
            LocalDate currentDate = LocalDate.now();
            int currentDay = currentDate.getDayOfMonth();
            int targetDay = currentDay - 2;

            // Формирование селектора для нужного дня
            String daySelector = String.format("#dp-6 > div > div.container__main > div > div > div.container__days > a:nth-child(%d)", targetDay + 1);
            // Ожидание и выбор нужного дня
            Actions actions = new Actions(driver);
            var dayElement = wait1.until(ExpectedConditions.elementToBeClickable(By.cssSelector(daySelector)));
            actions.doubleClick(dayElement).
                    perform();
            Thread.sleep(5000);
            var okButton = driver.findElement(By.xpath("//button[@class='button-apply']")); // Просмотр кнопки ОК
            okButton.click(); // Клик кнопки ОК
            Thread.sleep(10000);
            String[] pp2Selectors = {
                    "//div[contains(@class, 'metrics-card')]//div[contains(text(), 'Revenue')]/following-sibling::div",
                    "//div[contains(@class, 'metrics-card')]//div[contains(text(), 'Ad Opportunities')]/following-sibling::div",
                    "//div[contains(@class, 'metrics-card')]//div[contains(text(), 'Ad Requests')]/following-sibling::div",
                    "//div[contains(@class, 'metrics-card')]//div[contains(text(), 'Impressions')]/following-sibling::div",
                    "//div[contains(@class, 'metrics-card')]//div[contains(text(), 'CPM')]/following-sibling::div"
            };
            String[] pp2MetricNames = {"Revenue", "Ad Opportunities", "Ad Requests", "Impressions", "CPM"};
            Map<String, String> pp2Metrics = new HashMap<>();
            for (
                    int i = 0;
                    i < pp2Selectors.length; i++) {
                List<WebElement> elements = driver.findElements(By.xpath(pp2Selectors[i]));
                if (elements.size() > 0) {
                    String value = wait1.until(ExpectedConditions.visibilityOf(elements.get(0))).getText().trim().replace("$", "").replace(",", ".");
                    pp2Metrics.put(pp2MetricNames[i], value);
                } else {
                    System.out.println("Элемент с селектором " + pp2Selectors[i] + " не найден.");
                }
            }
            // Сравнение метрик
            compareMetrics(metrics, pp2Metrics);
        }

        public void compareMetrics(Map<String, String> metrics, Map<String, String> pp2Metrics) {
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
                        System.out.println(metric1 + " и " + metric2 + " совпадают: " + value1 + " = " + value2);
                    } else {
                        System.out.println(metric1 + " и " + metric2 + " не совпадают: " + value1 + " = " + value2);
                    }
                } else {
                    System.out.println("Одно из значений для метрик " + metric1 + " или " + metric2 + " отсутствует.");
                }
            }
            // Отдельная проверка Ad Opportunities
            String adOpportunities = pp2Metrics.get("Ad Opportunities");
            if (adOpportunities != null) {
                System.out.println("Ad Opportunities: " + adOpportunities);
            } else {
                System.out.println("Метрика Ad Opportunities не найдена.");
            }
        }

        private boolean areValuesWithinOnePercent(String value1, String value2) {
            try {
                // Преобразование значений в числа
                double num1 = Double.parseDouble(value1.replace(".", ""));
                double num2 = Double.parseDouble(value2.replace(".", ""));

                // Вычисление разницы
                double difference = Math.abs(num1 - num2);

                // Вычисление допустимого отклонения в 1%
                double allowableDifference = 0.01 * Math.min(num1, num2);

                // Проверка, попадает ли разница в допустимый диапазон
                return difference <= allowableDifference;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка преобразования значений: " + value1 + " или " + value2);
                return false;
            }
        }

        @Test
        @Order(7)
        public void checkScraperInfo() throws InterruptedException {  //Проверка данных с аналитики SSPs и PP1 Logs(view scraper logs)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            authorizationAP();
            var clickLogs = driver.findElement(By.cssSelector("a[href='/platforms']")); // выбор кнопки SSPs
            clickLogs.click(); // клик SSPs
            var newDay = LocalDate.now(); // Вычисление текущей даты и даты три дня назад
            var threeDays3 = newDay.minusDays(2); // даты 2 дня назад
            var threeDays4 = newDay.minusDays(2); // даты 2 дня назад
            var formatter3 = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // Форматирование дат в строковый формат
            var threeDaysAgoStr3 = threeDays3.format(formatter3); // ---
            var threeDaysOneStr4 = threeDays4.format(formatter3); // ---
            var startDateField = driver.findElement(By.name("date_from")); // Поле даты генератора
            var endDateField = driver.findElement(By.name("date_to")); // Поле даты генератора
            startDateField.clear(); // clear Date
            startDateField.sendKeys(threeDaysAgoStr3); // вставка числа в даты 1
            endDateField.clear(); // clear Date
            endDateField.sendKeys(threeDaysOneStr4); // вставка числа в даты 2
            var apply = driver.findElement(By.cssSelector("button[id='btnApplyDates']")); // elements apply
            apply.click(); // клик кнопки apply
            var analyticsPlatformsRevenueMap = new HashMap<String, Double>();
            var pp1LogsMap = new HashMap<String, Double>();
            var naPlatforms = new HashSet<String>();

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
                System.out.println("Normalized platform: " + normalizedPlatform + ", Revenue: " + revenue + " for platform " + platform);
            }
// Проверка наличия нужной даты в PP1 перед сбором данных
            boolean foundRequiredDateInPP1 = false;
            authorizationPP1();
            driver.get("https://test.nextmillennium.io/scraperlogs");
            List<WebElement> rowsPP1 = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("table > tbody > tr")));
            for (int i = rowsPP1.size() - 1; i >= 0; i--) {
                var revenueCell = driver.findElement(By.cssSelector("table tbody tr:nth-child(" + (i + 1) + ") > td:nth-child(4)"));
                var platformCell = driver.findElement(By.cssSelector("table tbody tr:nth-child(" + (i + 1) + ") > td:nth-child(2)"));
                var datePeriodCell = driver.findElement(By.cssSelector("table tbody tr:nth-child(" + (i + 1) + ") > td:nth-child(3)"));
                var revenue = revenueCell.getText().trim();
                var platform = platformCell.getText().trim();
                var datePeriod = datePeriodCell.getText().trim();
                LocalDate datePP1 = LocalDate.parse(datePeriod, DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Парсинг даты PP1
                LocalDate expectedDateInPP1 = threeDays3;
                if (datePP1.equals(threeDays3)) {
                    foundRequiredDateInPP1 = true;
                    var normalizedPlatform = normalizePlatformName(platform);
                    if (revenue.contains("N/A")) {
                        naPlatforms.add(normalizedPlatform);
                    } else if (!revenue.isEmpty()) {
                        pp1LogsMap.put(normalizedPlatform, Double.parseDouble(revenue));
                    }
                    System.out.println("Normalized platform: " + normalizedPlatform + ", Revenue: " + revenueCell.getText().trim() + " for platform " + platformCell.getText().trim());
                }
            }

            // Add the logic to combine specific platforms into "Exchange Bidders"
            var exchangeBiddersKey = "Exchange Bidders";
            pp1LogsMap.putIfAbsent(exchangeBiddersKey, 0.0);
            analyticsPlatformsRevenueMap.putIfAbsent(exchangeBiddersKey, 0.0);

            // Combine revenues into "Exchange Bidders"
            combinePlatforms(analyticsPlatformsRevenueMap, exchangeBiddersKey, "OneTag EB", "Index EB", "Magnite EB", "OpenX EB", "Pubmatic EB", "Sovrn EB");
            combinePlatforms(pp1LogsMap, exchangeBiddersKey, "OneTag EB", "Index EB", "Magnite EB", "OpenX EB", "Pubmatic EB", "Sovrn EB");

            // Exclude EB revenues from individual platform totals
            excludePlatforms(analyticsPlatformsRevenueMap, "OneTag EB", "Index EB", "Magnite EB", "OpenX EB", "Pubmatic EB", "Sovrn EB");
            excludePlatforms(pp1LogsMap, "OneTag EB", "Index EB", "Magnite EB", "OpenX EB", "Pubmatic EB", "Sovrn EB");

            // Check if Exchange_Bidders within tolerance
            boolean exchangeBiddersWithinTolerance = isWithinTolerance(
                    analyticsPlatformsRevenueMap.get(exchangeBiddersKey),
                    pp1LogsMap.get(exchangeBiddersKey),
                    TOLERANCE_PERCENTAGE
            );

            // Print matching platforms and their revenues
            System.out.println("\nMatching Platforms:\n");
            pp1LogsMap.forEach((platform, pp1Revenue) -> {
                if (analyticsPlatformsRevenueMap.containsKey(platform)) {
                    var analyticsRevenue = analyticsPlatformsRevenueMap.get(platform);
                    boolean withinTolerance = isWithinTolerance(analyticsRevenue, pp1Revenue, TOLERANCE_PERCENTAGE);
                    if (withinTolerance) {
                        System.out.printf("Platform: %s\n  Analytics Revenue: %.2f\n  PP1 Revenue: %.2f\n  Within Tolerance: Yes\n\n",
                                platform, analyticsRevenue, pp1Revenue);
                    }
                }
            });

            // Print non-matching platforms and their revenues
            System.out.println("\nNon-matching Platforms:\n");
            pp1LogsMap.forEach((platform, pp1Revenue) -> {
                if (analyticsPlatformsRevenueMap.containsKey(platform)) {
                    var analyticsRevenue = analyticsPlatformsRevenueMap.get(platform);
                    boolean withinTolerance = isWithinTolerance(analyticsRevenue, pp1Revenue, TOLERANCE_PERCENTAGE);
                    if (!withinTolerance) {
                        System.out.printf("Platform: %s\n  Analytics Revenue: %.2f\n  PP1 Revenue: %.2f\n  Within Tolerance: No\n\n",
                                platform, analyticsRevenue, pp1Revenue);
                    }
                }
            });

//            // Print Exchange_Bidders if not within tolerance
//            if (!exchangeBiddersWithinTolerance) {
//                System.out.printf("\nPlatform: %s\n  Analytics Revenue: %.2f\n  PP1 Revenue: %.2f\n  Within Tolerance: No\n\n",
//                        exchangeBiddersKey,
//                        analyticsPlatformsRevenueMap.get(exchangeBiddersKey),
//                        pp1LogsMap.get(exchangeBiddersKey));
//            }

            System.out.println("\nPlatforms with 'N/A' revenue in both analytics and PP1:\n");
            naPlatforms.forEach(platform -> {
                if (!pp1LogsMap.containsKey(platform) && !analyticsPlatformsRevenueMap.containsKey(platform)) {
                    System.out.println("Platform " + platform + " has 'N/A' revenue in both analytics and pp1");
                }
            });

            driver.quit();
        }

        private boolean isWithinTolerance(double value1, double value2, double tolerancePercentage) {
            double tolerance = Math.max(value1, value2) * (tolerancePercentage / 100);
            return Math.abs(value1 - value2) <= tolerance;
        }

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

        private void excludePlatforms(Map<String, Double> revenueMap, String... platformsToExclude) {
            for (String platform : platformsToExclude) {
                revenueMap.remove(platform);
            }
        }

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


        @Test
        @Order(8)
        /*Проверка вычислений комиссий fee (заскрапленны данные по ADX + высталены статусы комисии в edit acc enabled/disabled
        + report данных и информация с таблицы userSettings + rowsadx1 сравнены данные с reports(User)
         */
        public void checkFeeTagComission() throws InterruptedException, SQLException {
            DecimalFormat df = new DecimalFormat("#0.00");
            //1-й сценарий FALSE
            String dbUrl = "jdbc:mysql://nmm-ue1-test-rds-pp1-mysql.cyfcbagzyvft.us-east-1.rds.amazonaws.com:3306/db"; //Host BD
            String username = "ppuser"; //username
            String password = "uTBHF35D9QqvLMh1X1FBX93nGgnqpgBc"; //pass
            Connection connection = null;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            authorizationPP1();
            //scrap info
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
            var runAdx = driver.findElement(By.xpath("//a[text()='Run Adx scraper']"));
            runAdx.click();
            Thread.sleep(10000);
            var accounts = driver.findElement(By.cssSelector("a[href='/accounts']")); // выбор поля аккаунт
            accounts.click();
            var searchAccounts = driver.findElement(By.cssSelector("input[type='search']")); // выбор поля поиска
            searchAccounts.sendKeys("techtest");
            var edit = driver.findElement(By.cssSelector("a[href=\"/editaccount?id=16730\"]")); // check edit
            edit.click();
            var feesDropDown = driver.findElement(By.cssSelector("#formAccountEdit > div:nth-child(9) > div:nth-child(3) > div")); //Дропдаун fees
            feesDropDown.click(); //Дропдаун fees клик
            var enabledClick = driver.findElement(By.cssSelector("#bs-select-3-0")); //выбор Enabled
            enabledClick.click(); //Enabled клик
            var save = driver.findElement(By.cssSelector("#formAccountEdit > button")); //выбор save
            save.click();
            reports(); //report gen
            // Подключение к базе данных с попыткой повторного подключения
            boolean isConnected = false;
            int retryCount = 0;
            while (!isConnected && retryCount < 3) {
                try {
                    connection = DriverManager.getConnection(dbUrl, username, password);
                    isConnected = true;
                } catch (SQLException e) {
                    retryCount++;
                    System.err.println("Не удалось подключиться к базе данных. Попытка " + retryCount + " из 3.");
                    e.printStackTrace();
                    Thread.sleep(5000); // Задержка перед повторной попыткой
                }
            }

            if (!isConnected) {
                throw new SQLException("Не удалось подключиться к базе данных после 3 попыток.");
            }

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM db.userSettings WHERE userId = 16730 AND settingKey = 'disable_adx_fees'";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                boolean disableAdxFees = resultSet.getBoolean("settingValue");
                if (!disableAdxFees) {
                    System.out.println("Проверка пройдена: disable_adx_fees = false");
                } else {
                    System.out.println("Проверка не пройдена: disable_adx_fees = true");
                }
            } else {
                System.out.println("Проверка не пройдена: Запись не найдена");
            }
            Thread.sleep(10000);
            var accounts1 = driver.findElement(By.cssSelector("a[href='/accounts']")); // выбор поля аккаунт
            accounts1.click();
            var searchAccounts1 = driver.findElement(By.cssSelector("input[type='search']")); // выбор поля поиска
            searchAccounts1.sendKeys("techtest"); // вставка нужного юзера
            var clickReports = driver.findElement(By.cssSelector("a[href*='/reports2?userid=']")); // check reports
            clickReports.click(); // click reports

            var newDays = LocalDate.now(); // Вычисление текущей даты и даты три дня назад
            var randomDays = newDays.minusDays(2); // даты 2 дня назад
            var randomDaysTwo = newDays.minusDays(2); // даты 2 дня назад
            var formatterOne = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // Форматирование дат в строковый формат
            String newDaysAgoStr = randomDays.format(formatterOne);// ---
            String newDaysOneStr = randomDaysTwo.format(formatterOne);// ---
            var startDateReports = driver.findElement(By.name("date_from")); // Поле даты репорта
            var lastDateReports = driver.findElement(By.name("date_to")); // Поле даты репорта
            startDateReports.clear();
            startDateReports.sendKeys(newDaysAgoStr); // вставка числа в даты 1
            lastDateReports.clear();
            lastDateReports.sendKeys(newDaysOneStr); // вставка числа в даты 2
            var apply1 = driver.findElement(By.cssSelector("button[id=btnApplyDates]")); // кнопка apply
            apply1.click(); // клик apply
            var dropDown = driver.findElement(By.cssSelector("body > div.reports-tabular.container > div.table-wrapper > form > div > div.wrapper-block > div.inline-block.table-select-ad-units_wrapper > div")); //кнопка дропдауна
            dropDown.click(); // клик дропдауна
            String[] placementsToSelect = {
                    "cas.sk_320x50_stickybottom_hb",
                    "crownheights_320x50_stickybotttom_video_hb",
                    "gamewith.jp_rail_hb"
            };

            // Цикл по каждому значению и его выбор
            for (String placementsText : placementsToSelect) {
                var placements = driver.findElement(By.xpath("//span[contains(text(), '" + placementsText + "')]"));
                placements.click();
            }
            dropDown.click(); // клик дропдауна
            var apply2 = driver.findElement(By.cssSelector("button[id=btnApplyDates]")); // кнопка apply
            apply2.click(); // клик apply
            // Нахождение всех строк таблицы
            List<WebElement> oddRows = driver.findElements(By.cssSelector("#tblData > tbody > tr.odd"));
            List<WebElement> evenRows = driver.findElements(By.cssSelector("#tblData > tbody > tr.even"));

            // Объединение списков
            oddRows.addAll(evenRows);

            // Создание списка для хранения результатов
            List<String> results = new ArrayList<>();

// Цикл по каждой строке для извлечения данных
            for (WebElement row : oddRows) {
                // Извлечение имени
                var nameElement = row.findElement(By.cssSelector("td:nth-child(2)"));
                String name = nameElement.getText();

                // Извлечение данных по доходу
                var revenueElement = row.findElement(By.cssSelector("td:nth-child(6)"));
                String revenue = revenueElement.getText().replace(",", "").replace("$", "").trim();

                // Извлечение данных из базы данных для сравнения
                String dbQuery = "SELECT * FROM db.rowsadx1 WHERE `date` = ? AND `tag` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(dbQuery);
                preparedStatement.setDate(1, java.sql.Date.valueOf(randomDays));
                preparedStatement.setString(2, name);
                ResultSet dbResultSet = preparedStatement.executeQuery();

                if (dbResultSet.next()) {
                    double estimatedRevenue = dbResultSet.getDouble("estimatedrevenue");
                    int adImpressionsInvoicedVAST = dbResultSet.getInt("adImpressionsInvoicedVAST");
                    int adImpressionsUnfilledAMP = dbResultSet.getInt("adImpressionsUnfilledAMP");
                    int adImpressionsInvoicedAdServer = dbResultSet.getInt("adImpressionsInvoicedAdServer");

                    double vastFee = adImpressionsInvoicedVAST * 0.18 / 1000;
                    double ampFee = adImpressionsUnfilledAMP * 0.0247 / 1000;
                    double adServerFee = adImpressionsInvoicedAdServer * 0.0247 / 1000;
                    double dbTotalFee = vastFee + ampFee + adServerFee;

                    double dbFinalRevenue = estimatedRevenue - dbTotalFee;
                    dbFinalRevenue -= dbFinalRevenue * 0.01;

                    // Форматирование результата
                    String formattedDbFinalRevenue = df.format(dbFinalRevenue);
                    String result = name + ": UI PP1 = " + revenue + ", feeComission BD = " + formattedDbFinalRevenue;
                    double uiRevenue = Double.parseDouble(revenue);
                    if (Math.abs(uiRevenue - dbFinalRevenue) < 0.01) {
                        result += " (Данные соответствуют)";
                    } else {
                        result += " (Данные не соответствуют)";
                    }
                    results.add(result);
                }
            }

            for (String result : results) {
                System.out.println(result);
            }
            //2-й сценарий TRUE
            var accounts2 = driver.findElement(By.cssSelector("a[href='/accounts']")); // выбор поля аккаунт
            accounts2.click();
            var searchAccounts2 = driver.findElement(By.cssSelector("input[type='search']")); // выбор поля поиска
            searchAccounts2.sendKeys("techtest");
            var edit1 = driver.findElement(By.cssSelector("a[href=\"/editaccount?id=16730\"]")); // check edit
            edit1.click();
            var feesDropDown1 = driver.findElement(By.cssSelector("#formAccountEdit > div:nth-child(9) > div:nth-child(3) > div")); //Дропдаун fees
            feesDropDown1.click(); //Дропдаун fees клик
            var disabledClick = driver.findElement(By.cssSelector("#bs-select-3-1")); //выбор Disabled
            disabledClick.click(); //Disabled клик
            var save1 = driver.findElement(By.cssSelector("#formAccountEdit > button")); //выбор save
            save1.click();
            reports(); //report gen
            Thread.sleep(10000);
            Statement statement1 = connection.createStatement();
            String query1 = "SELECT * FROM db.userSettings WHERE userId = 16730 AND settingKey = 'disable_adx_fees'";
            ResultSet resultSet1 = statement1.executeQuery(query1);

            // Обработка результатов запроса
            if (resultSet1.next()) {
                String settingValue = resultSet1.getString("settingValue");

                // Проверка значения settingValue и вывод соответствующего сообщения
                if (settingValue.equalsIgnoreCase("true")) {
                    System.out.println("Проверка пройдена: disable_adx_fees = true");
                } else {
                    System.out.println("Проверка не пройдена: disable_adx_fees = false");
                }
            } else {
                System.out.println("Проверка не пройдена: Запись не найдена");
            }
//            var showReports = driver.findElement(By.cssSelector("#formAccountEdit > a")); // check showReports
//            showReports.click(); // click reports
            var accounts4 = driver.findElement(By.cssSelector("a[href='/accounts']")); // выбор поля аккаунт
            accounts4.click();
            var searchAccounts4 = driver.findElement(By.cssSelector("input[type='search']")); // выбор поля поиска
            searchAccounts4.sendKeys("techtest"); // вставка нужного юзера
            var clickReports1 = driver.findElement(By.cssSelector("a[href*='/reports2?userid=']")); // check reports
            clickReports1.click(); // click reports

            var newDays1 = LocalDate.now(); // Вычисление текущей даты и даты три дня назад
            var randomDays1 = newDays1.minusDays(2); // даты 2 дня назад
            var randomDaysTwo1 = newDays1.minusDays(2); // даты 2 дня назад
            var formatterOne1 = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // Форматирование дат в строковый формат
            String newDaysAgoStr1 = randomDays1.format(formatterOne1);// ---
            String newDaysOneStr1 = randomDaysTwo1.format(formatterOne1);// ---
            var startDateReports1 = driver.findElement(By.name("date_from")); // Поле даты репорта
            var lastDateReports1 = driver.findElement(By.name("date_to")); // Поле даты репорта
            startDateReports1.clear();
            startDateReports1.sendKeys(newDaysAgoStr1); // вставка числа в даты 1
            lastDateReports1.clear();
            lastDateReports1.sendKeys(newDaysOneStr1); // вставка числа в даты 2
            var apply3 = driver.findElement(By.cssSelector("button[id=btnApplyDates]")); // кнопка apply
            apply3.click(); // клик apply
            Thread.sleep(5000);
            var dropDown1 = driver.findElement(By.cssSelector("body > div.reports-tabular.container > div.table-wrapper > form > div > div.wrapper-block > div.inline-block.table-select-ad-units_wrapper > div")); //кнопка дропдауна
            dropDown1.click(); // клик дропдауна
            String[] placementsToSelect1 = {
                    "cas.sk_320x50_stickybottom_hb",
                    "crownheights_320x50_stickybotttom_video_hb",
                    "gamewith.jp_rail_hb"
            };

            // Цикл по каждому значению и его выбор
            for (String placementsText : placementsToSelect1) {
                var placements = driver.findElement(By.xpath("//span[contains(text(), '" + placementsText + "')]"));
                placements.click();
            }
            var dropDown2 = driver.findElement(By.cssSelector("body > div.reports-tabular.container > div.table-wrapper > form > div > div.wrapper-block > div.inline-block.table-select-ad-units_wrapper > div")); //кнопка дропдауна
            dropDown2.click(); // клик дропдауна
            var apply4 = driver.findElement(By.cssSelector("button[id=btnApplyDates]")); // кнопка apply
            apply4.click(); // клик apply
            // Нахождение всех строк таблицы
            List<WebElement> oddRows1 = driver.findElements(By.cssSelector("#tblData > tbody > tr.odd"));
            List<WebElement> evenRows1 = driver.findElements(By.cssSelector("#tblData > tbody > tr.even"));

            // Объединение списков
            oddRows1.addAll(evenRows1);

            // Создание списка для хранения результатов
            List<String> results1 = new ArrayList<>();

// Цикл по каждой строке для извлечения данных
            for (WebElement row : oddRows1) {
                // Извлечение имени
                var nameElement1 = row.findElement(By.cssSelector("td:nth-child(2)"));
                String name1 = nameElement1.getText();

                // Извлечение данных по доходу
                var revenueElement1 = row.findElement(By.cssSelector("td:nth-child(6)"));
                String revenue1 = revenueElement1.getText().replace(",", "").replace("$", "").trim();

                // Извлечение данных из базы данных для сравнения
                String dbQuery1 = "SELECT * FROM db.rowsadx1 WHERE `date` = ? AND `tag` = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(dbQuery1);
                preparedStatement1.setDate(1, java.sql.Date.valueOf(randomDays1));
                preparedStatement1.setString(2, name1);
                ResultSet dbResultSet1 = preparedStatement1.executeQuery();

                if (dbResultSet1.next()) {
                    double estimatedRevenue1 = dbResultSet1.getDouble("estimatedrevenue");
                    // Вычет 1% из дохода
                    double finalRevenueWithDeduction = estimatedRevenue1 - estimatedRevenue1 * 0.01;

                    // Форматирование результата
                    String formattedFinalRevenueWithDeduction = df.format(finalRevenueWithDeduction);
                    String result1 = name1 + ": UI PP1 = " + revenue1 + ", feeComission BD = " + formattedFinalRevenueWithDeduction;
                    double uiRevenue1 = Double.parseDouble(revenue1);
                    if (Math.abs(uiRevenue1 - finalRevenueWithDeduction) < 0.01) {
                        result1 += " (Данные соответствуют)";
                    } else {
                        result1 += " (Данные не соответствуют)";
                    }
                    results1.add(result1);
                }
            }

// Вывод всех результатов после обработки
            for (String result1 : results1) {
                System.out.println(result1);
            }

            resultSet.close();
            statement.close();
            connection.close();
        }
    }
    }










//AMP fee = (invoiced unfilled impressions for publisher/AMP ad tags) * $0.0247
//
//ad server fee = (invoiced impressions for ad server tags) * $0.0247
//
//VAST fee = (invoiced impressions for VAST) * $0.18

















