package com.iwcn.iwcn_p1;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.iwcn.iwcn_p1.ControllerRestTest;
import com.iwcn.iwcn_p1.model.Event;

@DisplayName("REST tests - Ticket Controller")
public class ConcurrentTest extends ControllerRestTest {

    @Test
    @DisplayName("Create concurrent tickets")
    public void createConcurrentTickets() throws Exception {

        Event eventBefore = when()
                .get("/api/v1/events/")
                .then()
                .extract().as(Event[].class)[2];

        AtomicInteger statusCreated = new AtomicInteger();
        AtomicInteger statusConflict = new AtomicInteger();

        IntStream.range(0, 100).parallel().forEach(i -> {

            int status = given()
                    .auth()
                    .basic("user_" + i, "pass")
                    .when()
                    .post("/api/tickets/?eventId={eventId}", eventBefore.getId())
                    .then()
                    .extract().statusCode();

            if (status == HttpStatus.SC_CREATED)
                statusCreated.incrementAndGet();
            else
                statusConflict.incrementAndGet();

        });

        Event eventAfter = when()
                .get("/api/v1/events/")
                .then()
                .extract().as(Event[].class)[2];

        assertEquals(eventAfter.getMaxSeats(), eventAfter.getReservedSeats());
        assertEquals(50, statusCreated.get());
        assertEquals(50, statusConflict.get());

    }

}