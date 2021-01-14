package com.smeshariks.pms;

import com.codeborne.selenide.SelenideElement;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class UiTest {

    @BeforeClass
    public static void setUp() {
        closeWebDriver();
    }

    @Test
    public void userCanCreateNewAccount() {
        open("/signup");
        $(By.name("username")).setValue("testaccount");
        $(By.name("name")).setValue("Тест");
        $(By.name("password")).setValue("12345");
        $(byText("Зарегистрироваться")).click();
    }

    @Test
    public void userCanLogin() {
        open("/login");
        $(By.name("username")).setValue("pinhead");
        $(By.name("password")).setValue("qzwxecasd123");
        $(byText("Войти")).click();
        closeWebDriver();
    }


    @Test
    public void userCanCreateNewProject() {
        open("/login");
        $(By.name("username")).setValue("alien");
        $(By.name("password")).setValue("qzwxecasd123");
        $(byText("Войти")).click();

        open("/project/create");
        $(By.name("title")).setValue("Create test");
        $(By.name("description")).setValue("Тестовое описание");
        $(By.name("tsStart")).setValue("14.01.2021");
        $(By.name("tsStop")).setValue("16.01.2021");
        $(By.name("cost")).setValue("12345");
        $(By.name("comment")).setValue("Комментарий");
        $(byText("Создать проект")).click();
        closeWebDriver();
    }

    @Test
    public void userCanCreateNewTask() {
        open("/login");
        $(By.name("username")).setValue("pinhead");
        $(By.name("password")).setValue("qzwxecasd123");
        $(byText("Войти")).click();

        open("/project/13");
        $(byText("Добавить задачу")).click();
        $(By.name("title")).setValue("Тестовая задача");
        $(By.name("description")).setValue("Описание задачи");
        $(By.name("deadTimeStr")).setValue("15.01.2021");
        SelenideElement dropdownList = $("#executor");
        dropdownList.selectOptionByValue("7");
        $(byText("Создать задачу")).click();
        closeWebDriver();
    }

    @Test
    public void userCanCreateNewMaterial() {
        open("/login");
        $(By.name("username")).setValue("barash");
        $(By.name("password")).setValue("qzwxecasd123");
        $(byText("Войти")).click();

        open("/warehouse");
        $(By.name("addmaterial")).click();
        $(By.name("title")).setValue("Тестовый материал");
        $(By.name("description")).setValue("Тестовое описание");
        $(By.name("balance")).setValue("125");
        $(By.name("minimumVolume")).setValue("123");
        $(By.name("cost")).setValue("10");
        $(byText("Создать")).click();
        closeWebDriver();
    }

    @Test
    public void userCanEditProject() {
        open("/login");
        $(By.name("username")).setValue("alien");
        $(By.name("password")).setValue("qzwxecasd123");
        $(byText("Войти")).click();

        open("/project/14");
        $(By.name("editbtn")).click();
        $(By.name("title")).setValue("Тестирование 123");
        $(By.name("description")).setValue("Тестовый проект 123");
        $(By.name("tsStart")).setValue("14.01.2021");
        $(By.name("tsStop")).setValue("16.01.2021");
        $(By.name("cost")).setValue("543210");
        $(By.name("addinfo")).setValue("Комментарий");
        $(byText("Сохранить")).click();
        closeWebDriver();
    }



}