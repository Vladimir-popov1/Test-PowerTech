package TestPowerTech;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
public class CheckFeeTagComission {
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
        AuthorizationPP1 authorization = new AuthorizationPP1();
        authorization.authorizePP1(driver); //Авторизация ПП1
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
        Reports reports = new Reports();
        reports.reportsPP1(driver);//Репорт //report gen
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
        Reports reports1 = new Reports();
        reports.reportsPP1(driver);//Репорт //report gen
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




//
//
//
//
//
//
//
//
//
//AMP fee = (invoiced unfilled impressions for publisher/AMP ad tags) * $0.0247
//
//ad server fee = (invoiced impressions for ad server tags) * $0.0247
//
//VAST fee = (invoiced impressions for VAST) * $0.18
//
//
