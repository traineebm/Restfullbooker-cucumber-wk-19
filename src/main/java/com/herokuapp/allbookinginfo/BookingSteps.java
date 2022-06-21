package com.herokuapp.allbookinginfo;

import com.herokuapp.constants.EndPoints;
import com.herokuapp.model.BookingPojo;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import java.util.HashMap;

public class BookingSteps {
    @Step(" Creating the authorisation of the User username : {0}, password: {1}")
    public ValidatableResponse authUser(String username,
                                        String password) {
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setUsername(username);
        bookingPojo.setPassword(password);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(bookingPojo)
                .when()
                .post(EndPoints.CREATE_AUTH)
                .then();
    }

    @Step("Create Booking  firstname: {0}, lastname: {1}, totalprice: {2}, depositepaid: {3}, bookingsDates:{4}, addtionalsneeds : {5} ")
    public ValidatableResponse createBooking(String firstname, String lastname, int totalprice, boolean depositpaid, HashMap<String, String> bookingsDatesData,
                                             String additionalneeds) {
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(totalprice);
        bookingPojo.setDepositpaid(depositpaid);
        bookingPojo.setBookingdates(bookingsDatesData);
        bookingPojo.setAdditionalneeds(additionalneeds);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(bookingPojo)
                .when()
                .post(EndPoints.GET_BOOKING)
                .then();
    }

    @Step("Update Booking  BookingID: {0},firstname: {1}, lastname: {2}, totalprice: {3}, depositepaid: {4}, bookingsDates:{5}, addtionalsneeds : {6} ")
    public ValidatableResponse updateBooking(int bookingID, String firstname, String lastname, int totalprice,
                                             boolean depositpaid, HashMap<String, String> bookingsDatesData,
                                             String additionalneeds) {
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(totalprice);
        bookingPojo.setDepositpaid(depositpaid);
        bookingPojo.setBookingdates(bookingsDatesData);
        bookingPojo.setAdditionalneeds(additionalneeds);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(bookingPojo)
                .pathParam("bookingID",bookingID)
                .auth().preemptive().basic("admin","password123")
                .when()
                .put(EndPoints.UPDATE_BOOKING_BY_ID)
                .then();
    }

    @Step("Delete Booking  BookingID: {0}")
    public ValidatableResponse deleteBooking(int bookingID) {
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("bookingID",bookingID)
                .auth().preemptive().basic("admin","password123")
                .when()
                .delete(EndPoints.DELETE_BOOKING_BY_ID)
                .then();
    }

    @Step("Get Booking by BookingID: {0}")
    public ValidatableResponse getBookingByID(int bookingID) {
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("bookingID",bookingID)
                .when()
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then();
    }

}
