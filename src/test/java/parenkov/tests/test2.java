package parenkov.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import parenkov.config.App;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class test2 {

    @BeforeAll
    static void configureBaseUrl() {
        clearBrowserCookies();
    }

    @Test
    public void addProductAndCheckViaUi() {

        // Добавить продукт через API

//        String cookies = "44712ECB9A0CE200F25B4C7CD" +
//                "F522450E47F13DED111B5945B9F76D4A9FB667B0F73647382E0" +
//                "50F6671EFD100FA42A10759140A7FCD2E88FA113642ADB844A1" +
//                "2704796A8C4F97D82CE7C2118A7EFD1AE8165B7A2DD466C5C68" +
//                "EA2FEA363F89EB402DCE4DE1C2B894159C3035610CFA45E274D" +
//                "66FE879B72442B8FD0E1E5B184F";

        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .cookie("Nop.customer=69589107-6373-41bd-891d-47fb44277adc;")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/catalog/31/1/1")
                        .then()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                        .extract().response();

        System.out.println(response.path("updatetopcartsectionhtml").toString());

        // Подложить куку

        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");

        getWebDriver().manage().addCookie(
                new Cookie("Nop.customer", "69589107-6373-41bd-891d-47fb44277adc"));

        // Проверить что продукт добавился через UI

        open("http://demowebshop.tricentis.com/cart");
        $(".product-name").shouldHave(Condition.text("14.1-inch Laptop"));
    }
}

