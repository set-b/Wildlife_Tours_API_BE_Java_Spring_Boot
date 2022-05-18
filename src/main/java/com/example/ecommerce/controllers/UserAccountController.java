package com.example.ecommerce.controllers;

import static com.example.ecommerce.constants.StringConstants.CONTEXT_USER_ACCOUNTS;
import static com.example.ecommerce.constants.StringConstants.DELETE_REQUEST;
import static com.example.ecommerce.constants.StringConstants.POST_REQUEST;
import static com.example.ecommerce.constants.StringConstants.QUERY_REQUEST;
import static com.example.ecommerce.constants.StringConstants.UPDATE_REQUEST;

import com.example.ecommerce.models.UserAccount;
import com.example.ecommerce.services.UserAccountService;
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

@Api(value = "UserAccount account Controller")
@RestController
@RequestMapping(CONTEXT_USER_ACCOUNTS)
public class UserAccountController {

  //todo add useraccount services
  private final Logger logger = LoggerFactory.getLogger(UserAccountController.class);

  @Autowired
  private UserAccountService userAccountService;

  /**
   * Queries UserAccounts.
   *
   * @param userAccount the UserAccount(s) matching the userAccount's supplied information.
   * @return a list of UserAccounts, containing Objects which match the information supplied, or a
   * list of all UserAccounts if the query is empty.
   */
  @GetMapping
  @Operation(summary = "Query UserAccounts",
      description = "get all userAccounts, or userAccounts filtered according to custom query",
      responses = {
          @ApiResponse(responseCode = "200", description = "List of UserAccounts")
      })
  public ResponseEntity<List<UserAccount>> queryUserAccounts(UserAccount userAccount) {
    logger.info(new Date() + QUERY_REQUEST + userAccount.toString());

    return new ResponseEntity<>(userAccountService.queryUserAccounts(userAccount), HttpStatus.OK);
  }

  /**
   * Retrieves the UserAccount tht has the given id.
   *
   * @param id the id of the UserAccount to be retrieved.
   * @return a UserAccount Object with the given id, if it exists.
   */
  @GetMapping("/{id}")
  @Operation(summary = "get UserAccount by userAccount id",
      parameters = {
          @Parameter(name = "id", required = true,
              description = "The id of the userAccount to be retrieved", allowEmptyValue = false),
      },
      responses = {
          @ApiResponse(responseCode = "200", description = "UserAccount with identical id"),
          @ApiResponse(responseCode = "400", description = "Id must be positive"),
          @ApiResponse(responseCode = "404", description = "UserAccount with given id not found")
      })
  public ResponseEntity<UserAccount> getUserAccountById(@PathVariable Long id) {
    logger.info(new Date() + QUERY_REQUEST + "userAccount with id " + id);

    return new ResponseEntity<>(userAccountService.getUserAccountById(id), HttpStatus.OK);
  }

  /**
   * Saves a UserAccount Object with the data/state provided by the userAccount to the database.
   *
   * @param userAccount the UserAccount to be saved.
   * @return the dta of the UserAccount that was saved to the database, if successful.
   */
  @PostMapping
  @Operation(summary = "post UserAccount", description = "Creates a userAccount from the request body",
      responses = {
          @ApiResponse(responseCode = "201", description = "UserAccount created"),
          @ApiResponse(responseCode = "400", description = "Invalid UserAccount data"),
      })
  public ResponseEntity<UserAccount> postUserAccount(@Valid @RequestBody UserAccount userAccount) {
    logger.info(new Date() + POST_REQUEST + "userAccount");

    return new ResponseEntity<UserAccount>(userAccountService.addUserAccount(userAccount),
        HttpStatus.CREATED);
  }

  /**
   * Finds and replaces a UserAccount Object with another, based on the id and data/state supplied
   * by the userAccount.
   *
   * @param id          the id of the UserAccount to be updated.
   * @param userAccount the UserAccount data which will replace the old UserAccount data.
   * @return the successfully updated UserAccount
   */
  @PutMapping("/{id}")
  @Operation(summary = "put UserAccount", description = "Updates a UserAccount based on the request body",
      responses = {
          @ApiResponse(responseCode = "200", description = "UserAccount updated successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid UserAccount data"),
          @ApiResponse(responseCode = "404", description = "UserAccount not found")
      })
  public ResponseEntity<UserAccount> updateUserAccountById(@PathVariable Long id,
      @Valid @RequestBody UserAccount userAccount) {
    logger.info(new Date() + UPDATE_REQUEST + "userAccount with id " + id);

    return new ResponseEntity<UserAccount>(
        userAccountService.updateUserAccountById(id, userAccount),
        HttpStatus.OK);
  }

  /**
   * Finds and deletes a UserAccount from the database with the id given by the userAccount.
   *
   * @param id the id of the UserAccount to be deleted.
   * @return a deleted status, if the id exists in the database.
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "delete UserAccount", description = "Deletes a userAccount with matching id",
      responses = {
          @ApiResponse(responseCode = "204", description = "No Content. UserAccount deleted"),
          @ApiResponse(responseCode = "404", description = "UserAccount not found"),
          @ApiResponse(responseCode = "400", description = "id must be positive")
      })
  public ResponseEntity<UserAccount> deleteUserAccountById(@PathVariable Long id) {
    logger.info(new Date() + DELETE_REQUEST + "userAccount with id " + id);

    userAccountService.deleteUserAccountById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
