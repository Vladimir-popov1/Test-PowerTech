package TestPowerTech;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProstoTest {
    @Test
    public void basicTest() {
        exampleStep();
        assertTrue(true);
    }

    @Step("Пример шага для Allure")
    public void exampleStep() {
        System.out.println("Выполнение примера шага.");
    }
}

