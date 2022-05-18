package com.example.ecommerce.controllers;

import static com.example.ecommerce.constants.StringConstants.CONTEXT_TOURS;
import static com.example.ecommerce.constants.StringConstants.DELETE_REQUEST;
import static com.example.ecommerce.constants.StringConstants.POST_REQUEST;
import static com.example.ecommerce.constants.StringConstants.QUERY_REQUEST;
import static com.example.ecommerce.constants.StringConstants.UPDATE_REQUEST;

import com.example.ecommerce.models.Tour;
import com.example.ecommerce.services.TourService;
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

@Api(value = "Tour Controller")
@RestController
@RequestMapping(CONTEXT_TOURS)
public class TourController {

  private final Logger logger = LoggerFactory.getLogger(TourController.class);

  @Autowired
  private TourService tourService;

  /**
   * Queries Tours.
   *
   * @param tour the Tour(s) matching the user's supplied information.
   * @return a list of Tours, containing Objects which match the information supplied, or a list of
   * all Tours if the query is empty.
   */
  @GetMapping
  @Operation(summary = "Query Tours",
      description = "get all tours, or tours filtered according to custom query",
      responses = {
          @ApiResponse(responseCode = "200", description = "List of Tours")
      })
  public ResponseEntity<List<Tour>> queryTours(Tour tour) {
    logger.info(new Date() + QUERY_REQUEST + tour.toString());

    return new ResponseEntity<>(tourService.queryTours(tour), HttpStatus.OK);
  }

  /**
   * Retrieves the Tour tht has the given id.
   *
   * @param id the id of the Tour to be retrieved.
   * @return a Tour Object with the given id, if it exists.
   */
  @GetMapping("/{id}")
  @Operation(summary = "get Tour by tour id",
      parameters = {
          @Parameter(name = "id", required = true,
              description = "The id of the tour to be retrieved", allowEmptyValue = false),
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "Tour with identical id"),
          @ApiResponse(responseCode = "400", description = "Id must be positive"),
          @ApiResponse(responseCode = "404", description = "Tour with given id not found")
      })
  public ResponseEntity<Tour> getTourById(@PathVariable Long id) {
    logger.info(new Date() + QUERY_REQUEST + "tour with id " + id);

    return new ResponseEntity<>(tourService.getTourById(id), HttpStatus.OK);
  }

  /**
   * Saves a Tour Object with the data/state provided by the user to the database.
   *
   * @param tour the Tour to be saved.
   * @return the dta of the Tour that was saved to the database, if successful.
   */
  @PostMapping
  @Operation(summary = "post Tour", description = "Creates a tour from the request body",
      responses = {
          @ApiResponse(responseCode = "201", description = "Tour created"),
          @ApiResponse(responseCode = "400", description = "Invalid Tour data"),
      })
  public ResponseEntity<Tour> postTour(@Valid @RequestBody Tour tour) {
    logger.info(new Date() + POST_REQUEST + "tour");

    return new ResponseEntity<Tour>(tourService.addTour(tour), HttpStatus.CREATED);
  }

  /**
   * Finds and replaces a Tour Object with another, based on the id and data/state supplied by the
   * user.
   *
   * @param id   the id of the Tour to be updated.
   * @param tour the Tour data which will replace the old Tour data.
   * @return the successfully updated Tour
   */
  @PutMapping("/{id}")
  @Operation(summary = "put Tour", description = "Updates a Tour based on the request body",
      responses = {
          @ApiResponse(responseCode = "200", description = "Tour updated successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid Tour data"),
          @ApiResponse(responseCode = "404", description = "Tour not found")
      })
  public ResponseEntity<Tour> updateTourById(@PathVariable Long id,
      @Valid @RequestBody Tour tour) {
    logger.info(new Date() + UPDATE_REQUEST + "tour with id " + id);

    return new ResponseEntity<Tour>(tourService.updateTourById(id, tour),
        HttpStatus.OK);
  }

  /**
   * Finds and deletes a Tour from the database with the id given by the user.
   *
   * @param id the id of the Tour to be deleted.
   * @return a deleted status, if the id exists in the database.
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "delete Tour", description = "Deletes a tour with matching id",
      responses = {
          @ApiResponse(responseCode = "204", description = "No Content. Tour deleted"),
          @ApiResponse(responseCode = "404", description = "Tour not found"),
          @ApiResponse(responseCode = "400", description = "id must be positive")
      })
  public ResponseEntity<Tour> deleteTourById(@PathVariable Long id) {
    logger.info(new Date() + DELETE_REQUEST + "tour with id " + id);

    tourService.deleteTourById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
