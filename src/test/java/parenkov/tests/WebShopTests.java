package parenkov.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parenkov.config.App;

import static io.restassured.RestAssured.*;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.*;

public class WebShopTests {

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = App.config.apiUrl();
    }

    @Test
    @DisplayName("Регистрация")
    void registration() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .body("__RequestVerificationToken=yharMk0UzLqakit" +
                        "-MPnBjOJc6oyYNnsn8u_CWOwbVk9djHYwKWUFoxe0" +
                        "_2nndWrMgtJaual3dxIZ6SkidpixmRpzv6NucA-EDDWc0k5Wmrk1&" +
                        "Gender=M&FirstName=Alex&LastName=Qwerty&Email=qwerty%40www.co&" +
                        "Password=123456&ConfirmPassword=123456&register-button=Register")
                .when()
                .post("/register")
                .then()
                .statusCode(302);
        get("/registerresult/1")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Добавление товара с пользовательскими параметрами в Shopping Cart")
    void addItemToShoppingCart() {
        step("Авторизоваться на сайте", () -> {
            given()
                    .contentType("application/x-www-form-urlencoded")
                    .body("Email=qwerty%40www.co&Password=123456&RememberMe=false")
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(302);
        });

        step("Перейти в раздел Computers -> Desktops", () -> {
            get("/desktops")
                    .then()
                    .statusCode(200)
                    .body("html.head.title", equalTo("Demo Web Shop. Desktops"));
        });

        step("Выбрать значение фильтра цены 'Over 1200.00'", () -> {
            get("/desktops?price=1200-")
                    .then()
                    .statusCode(200);
        });

        step("Перейти на карточку товара с ценой 1800", () -> {
            get("/build-your-own-expensive-computer-2")
                    .then()
                    .statusCode(200)
                    .body("html.head.title", equalTo("Demo Web Shop. " +
                            "Build your own expensive computer"));
        });

        step("Добавить в Shopping cart товар в количестве 2 шт. с максимальной комплектацией", () -> {
            Response response =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .body("product_attribute_74_5_26=82" +
                                    "&product_attribute_74_6_27=85" +
                                    "&product_attribute_74_3_28=87" +
                                    "&product_attribute_74_8_29=88" +
                                    "&product_attribute_74_8_29=89" +
                                    "&product_attribute_74_8_29=90" +
                                    "&addtocart_74.EnteredQuantity=5")
                            .cookie("NOPCOMMERCE.AUTH=44712ECB9A0CE200" +
                                    "F25B4C7CDF522450E47F13DED111B5945" +
                                    "B9F76D4A9FB667B0F73647382E050F667" +
                                    "1EFD100FA42A10759140A7FCD2E88FA11" +
                                    "3642ADB844A12704796A8C4F97D82CE7C" +
                                    "2118A7EFD1AE8165B7A2DD466C5C68EA2" +
                                    "FEA363F89EB402DCE4DE1C2B894159C30" +
                                    "35610CFA45E274D66FE879B72442B8FD0" +
                                    "E1E5B184F;")
                            .when()
                            .post("/addproducttocart/details/74/1")
                            .then()
                            .statusCode(200)
                            .body("success", is(true))
                            .body("message", is("The product has been added to your " +
                                    "<a href=\"/cart\">shopping cart</a>"))
                            .body("updatetopcartsectionhtml", is("(5)"))
                            .extract().
                            response();
            System.out.println("Quantity: " + response.path("updatetopcartsectionhtml"));
        });
    }
}

    @Test
    @DisplayName("Successful authorization to some demowebshop (API + UI)")
    void loginWithCookieTest() {
        step("Get cookie by api and set it to browser", () -> {
            String authorizationCookie =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", App.config.userEmail())
                            .formParam("Password", App.config.userPassword())
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Set cookie to to browser", () ->
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
        });
    }
}


//        step("Open main page", () ->
//                open(""));
//
//        step("Verify successful authorization", () ->
//                $(".account").shouldHave(text(App.config.userEmail())));
//    }
//
//
//}


//    @Test
//    void shoppingCartTest0() {
//        step("Удалить товар из Shopping Cart", () -> {
//            Response response =
//                    given()
//                            .contentType("multipart/form-data; boundary=----WebKitFormBoundaryALZeBbTrVCtvQTAg")
//                            .when()
//                            .post("/cart")
//                            .then()
//                            .statusCode(200)
//                            .extract()
//                            .response();
//            XmlPath xmlPath = new XmlPath(XmlPath.CompatibilityMode.HTML, response.asString());
//            System.out.println(xmlPath.getString("html.body"));


//                    .statusCode("200")
//                    .body("html.head.title", equalTo("Demo Web Shop. Shopping Cart"),
//                    "html.body",  equalTo("Your Shopping Cart is empty!"));
//            System.out.println(response.asString());
//        });
//
//    }


//            step("Open minimal content, because cookie can be set when site is opened", () ->
//                    open("/Themes/DefaultClean/Content/images/logo.png"));
//
//            step("Set cookie to to browser", () ->
//                    getWebDriver().manage().addCookie(
//                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
//        });
//
//        step("Open main page", () ->
//                open(""));
//
//        step("Verify successful authorization", () ->
//                $(".account").shouldHave(text(App.config.userEmail())));
//    }


// Set cookie to browser
//    @Test
//    void cookies() {
//        step("Get cookie by api and set it to browser", () -> {
//            String authorizationCookie =
//                    given()
//                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
//                            .formParam("Email", App.config.userEmail())
//                            .formParam("Password", App.config.userPassword())
//                            .when()
//                            .post("/login")
//                            .then()
//                            .statusCode(302)
//                            .extract()
//                            .cookie("NOPCOMMERCE.AUTH");
//
//            step("Open minimal content, because cookie can be set when site is opened", () ->
//                    open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png"));
//
//            step("Set cookie to browser", () ->
//                    getWebDriver().manage().addCookie(
//                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
//            System.out.println(authorizationCookie);
//        });
//    }