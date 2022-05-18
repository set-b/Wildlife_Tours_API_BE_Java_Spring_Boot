package com.example.ecommerce.controllers;

import static com.example.ecommerce.constants.StringConstants.CONTEXT_TOUR_BOOKINGS;
import static com.example.ecommerce.constants.StringConstants.DELETE_REQUEST;
import static com.example.ecommerce.constants.StringConstants.POST_REQUEST;
import static com.example.ecommerce.constants.StringConstants.QUERY_REQUEST;
import static com.example.ecommerce.constants.StringConstants.UPDATE_REQUEST;

import com.example.ecommerce.models.TourBooking;
import com.example.ecommerce.services.TourBookingService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Tour Booking Controller")
@RestController
@RequestMapping(CONTEXT_TOUR_BOOKINGS)
public class TourBookingController {

  private final Logger logger = LoggerFactory.getLogger(TourBookingController.class);

  @Autowired
  private TourBookingService tourBookingService;

  /**
   * Queries TourBookings.
   *
   * @param tourBooking the TourBooking(s) matching the user's supplied information.
   * @return a list of TourBookings, containing Objects which match the information supplied, or a
   * list of all TourBookings if the query is empty.
   */
  @GetMapping
  @Operation(summary = "Query TourBookings",
      description = "get all tourBookings, or tourBookings filtered according to custom query",
      responses = {
          @ApiResponse(responseCode = "200", description = "List of TourBookings")
      })
  public ResponseEntity<List<TourBooking>> queryTourBookings(TourBooking tourBooking) {
    logger.info(new Date() + QUERY_REQUEST + tourBooking.toString());

    return new ResponseEntity<>(tourBookingService.queryTourBookings(tourBooking), HttpStatus.OK);
  }

  /**
   * Retrieves the TourBooking tht has the given id.
   *
   * @param id the id of the TourBooking to be retrieved.
   * @return a TourBooking Object with the given id, if it exists.
   */
  @GetMapping("/{id}")
  @Operation(summary = "get TourBooking by tourBooking id",
      parameters = {
          @Parameter(name = "id", required = true,
              description = "The id of the tourBooking to be retrieved", allowEmptyValue = false),
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "TourBooking with identical id"),
          @ApiResponse(responseCode = "400", description = "Id must be positive"),
          @ApiResponse(responseCode = "404", description = "TourBooking with given id not found")
      })
  public ResponseEntity<TourBooking> getTourBookingById(@PathVariable Long id) {
    logger.info(new Date() + QUERY_REQUEST + "tourBooking with id " + id);

    return new ResponseEntity<>(tourBookingService.getTourBookingById(id), HttpStatus.OK);
  }

  @GetMapping("/tourcode/{tourcode}")
  @Operation(summary = "get TourBooking by tourBooking tour code",
      parameters = {
          @Parameter(name = "tour code", required = true,
              description = "The tour code of the tourBooking to be retrieved", allowEmptyValue = false),
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "TourBooking with identical id"),
          @ApiResponse(responseCode = "400", description = "Id must be positive"),
          @ApiResponse(responseCode = "404", description = "TourBooking with given id not found")
      })
  public ResponseEntity<TourBooking> getTourBookingByTourCode(@PathVariable String tourCode) {
    logger.info(new Date() + QUERY_REQUEST + "tourBooking with tour code " + tourCode);

    return new ResponseEntity<>(tourBookingService.getTourBookingByTourCode(tourCode), HttpStatus.OK);
  }

  /**
   * Saves a TourBooking Object with the data/state provided by the user to the database.
   *
   * @param tourBooking the TourBooking to be saved.
   * @return the dta of the TourBooking that was saved to the database, if successful.
   */
  @PostMapping
  @Operation(summary = "post TourBooking", description = "Creates a tourBooking from the request body",
      responses = {
          @ApiResponse(responseCode = "201", description = "TourBooking created"),
          @ApiResponse(responseCode = "400", description = "Invalid TourBooking data"),
      })
  public ResponseEntity<TourBooking> postTourBooking(@Valid @RequestBody TourBooking tourBooking) {
    logger.info(new Date() + POST_REQUEST + "tourBooking");

    return new ResponseEntity<TourBooking>(tourBookingService.addTourBooking(tourBooking),
        HttpStatus.CREATED);
  }

  /**
   * Finds and replaces a TourBooking Object with another, based on the id and data/state supplied
   * by the user.
   *
   * @param id          the id of the TourBooking to be updated.
   * @param tourBooking the TourBooking data which will replace the old TourBooking data.
   * @return the successfully updated TourBooking
   */
  @PutMapping("/{id}")
  @Operation(summary = "put TourBooking", description = "Updates a TourBooking based on the request body",
      responses = {
          @ApiResponse(responseCode = "200", description = "TourBooking updated successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid TourBooking data"),
          @ApiResponse(responseCode = "404", description = "TourBooking not found")
      })
  public ResponseEntity<TourBooking> updateTourBookingById(@PathVariable Long id,
      @Valid @RequestBody TourBooking tourBooking) {
    logger.info(new Date() + UPDATE_REQUEST + "tourBooking with id " + id);

    return new ResponseEntity<TourBooking>(
        tourBookingService.updateTourBookingById(id, tourBooking),
        HttpStatus.OK);
  }

  /**
   * Finds and deletes a TourBooking from the database with the id given by the user.
   *
   * @param id the id of the TourBooking to be deleted.
   * @return a deleted status, if the id exists in the database.
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "delete TourBooking", description = "Deletes a tourBooking with matching id",
      responses = {
          @ApiResponse(responseCode = "204", description = "No Content. TourBooking deleted"),
          @ApiResponse(responseCode = "404", description = "TourBooking not found"),
          @ApiResponse(responseCode = "400", description = "id must be positive")
      })
  public ResponseEntity<TourBooking> deleteTourBookingById(@PathVariable Long id) {
    logger.info(new Date() + DELETE_REQUEST + "tourBooking with id " + id);

    tourBookingService.deleteTourBookingById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
