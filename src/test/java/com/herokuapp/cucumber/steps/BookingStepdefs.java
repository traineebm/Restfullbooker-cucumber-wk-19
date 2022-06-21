package com.herokuapp.cucumber.steps;

import com.herokuapp.allbookinginfo.BookingSteps;
import com.herokuapp.utils.TestUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

import java.util.HashMap;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasKey;

public class BookingStepdefs {
    static String username = "admin";
    static String password = "password123";
    static String firstname = "Monica" + TestUtils.getRandomValue();
    static String lastname = "Singh";
    static int totalprice = 50;
    static boolean depositpaid = true;
    static String additionalneeds = "Breakfast";
    static int bookingId;
    static String token;
    static ValidatableResponse response;

    @Steps
    BookingSteps bookingSteps;

    @Given("^I am on homepage to get an authorization on the application$")
    public void iAmOnHomepageToGetAnAuthorizationOnTheApplication() {
    }

    @When("^I send a POST request using a valid payload to authorize application$")
    public void iSendAPOSTRequestUsingAValidPayloadToAuthorizeApplication() {
        response = bookingSteps.authUser(username, password).statusCode(200).log().all();
    }

    @Then("^I get a valid response code (\\d+)$")
    public void iGetAValidResponseCode(int code) {
        response.assertThat().statusCode(code);
    }

    @And("^I verify if I have a token$")
    public void iVerifyIfIHaveAToken() {
        HashMap<?, ?> tokenMap = response.log().all().extract().path("");
        Assert.assertThat(tokenMap, hasKey("token"));
        System.out.println(token);
    }

    @Given("^I am on the homepage to create booking on the application$")
    public void iAmOnTheHomepageToCreateBookingOnTheApplication() {
    }

    @When("^I send a POST request using a valid payload to booking application$")
    public void iSendAPOSTRequestUsingAValidPayloadToBookingApplication() {
        HashMap<String, String> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2022-01-31");
        bookingsDatesData.put("checkout", "2022-02-07");
        response = bookingSteps.createBooking(firstname, lastname, totalprice, depositpaid, bookingsDatesData, additionalneeds).statusCode(200).log().all();
        bookingId = response.log().all().extract().path("bookingid");
        HashMap<?, ?> bookingMap = response.log().all().extract().path("");
        Assert.assertThat(bookingMap, anything(firstname));
    }

    @When("^I send GET request to booking application$")
    public void iSendGETRequestToBookingApplication() {
        response = bookingSteps.getBookingByID(bookingId);
    }

    @And("^I verify if booking created with correct details$")
    public void iVerifyIfBookingCreatedWithCorrectDetails() {
        response.log().all().extract().path("bookingid");
        HashMap<?, ?> bookingMap = response.log().all().extract().path("");
        Assert.assertThat(bookingMap, anything(firstname));
    }

    @When("^I send PUT request to booking application$")
    public void iSendPUTRequestToBookingApplication() {
        HashMap<String, String> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2022-01-31");
        bookingsDatesData.put("checkout", "2022-02-07");
        firstname = firstname + "_updated";
        lastname = lastname + "_updated";
        additionalneeds = "Wi-fi";
        response = bookingSteps.updateBooking(bookingId,firstname, lastname,totalprice,depositpaid,bookingsDatesData,additionalneeds);
    }

    @And("^I verify if booking updated with correct details$")
    public void iVerifyIfBookingUpdatedWithCorrectDetails() {
        response.log().all().statusCode(200);
        HashMap<?,?>bookingMap= response.log().all().extract().path("");
        Assert.assertThat(bookingMap,anything(firstname));
    }

    @When("^I send DELETE request to booking application$")
    public void iSendDELETERequestToBookingApplication() {
        response = bookingSteps.deleteBooking(bookingId).statusCode(201).log().all();
    }

    @And("^I verify if booking deleted from the application$")
    public void iVerifyIfBookingDeletedFromTheApplication() {
        response = bookingSteps.getBookingByID(bookingId).statusCode(404).log().all();
    }
}
