package com.sopra.UserManagement.Controller;

import com.sopra.UserManagement.Entity.PhoneNo;
import com.sopra.UserManagement.Service.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sopra.UserManagement.Entity.Customer;
import com.sopra.UserManagement.Exception.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerService;

    // Get all customers
    @Operation(
            summary = "Retrieve all customers",
            description = "Get a list of all customer objects. The response contains a list of customer objects.",
            tags = { "customer", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Customer.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Get a customer by ID
    @Operation(
            summary = "Retrieve a customer by ID",
            description = "Get a customer object by specifying its ID. The response contains the customer object.",
            tags = { "customer", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(schema = @Schema(implementation = Customer.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Customer not found with id " + id);
        }
    }

    // Create a new customer
    @Operation(
            summary = "Create a new customer",
            description = "Create a new customer object. The response contains the created customer object.",
            tags = { "customer", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = @Content(schema = @Schema(implementation = Customer.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.saveCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    // Update an existing customer
    @Operation(
            summary = "Update an existing customer",
            description = "Update an existing customer object by specifying its ID. The response contains the updated customer object.",
            tags = { "customer", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(schema = @Schema(implementation = Customer.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema()))
    })
//    @PutMapping("/{id}")
//    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
//        Customer existingCustomer = customerService.getCustomerById(id);
//        if (existingCustomer != null) {
//            existingCustomer.setName(customerDetails.getName());
//            existingCustomer.setDob(customerDetails.getDob());
//            existingCustomer.setGender(customerDetails.getGender());
//            existingCustomer.setEmail(customerDetails.getEmail());
//            existingCustomer.setPhones(customerDetails.getPhones());
//            Customer updatedCustomer = customerService.saveCustomer(existingCustomer);
//            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
//        } else {
//            throw new ResourceNotFoundException("Customer not found with id " + id);
//        }
//    }
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestHeader("Authorization") String jwt, @PathVariable Long id, @RequestBody Customer customerDetails) {
        Customer existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer != null) {
            existingCustomer.setFullName(customerDetails.getFullName());
            existingCustomer.setDob(customerDetails.getDob());
            existingCustomer.setGender(customerDetails.getGender());
            existingCustomer.setEmail(customerDetails.getEmail());

            existingCustomer.getPhones().clear();
            for (PhoneNo phone : customerDetails.getPhones()) {
                existingCustomer.addPhoneNo(phone);
            }

            Customer updatedCustomer = customerService.saveCustomer(existingCustomer);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Customer not found with id " + id);
        }
    }

    // Delete a customer by ID
    @Operation(
            summary = "Delete a customer by ID",
            description = "Delete a customer object by specifying its ID. The response indicates the status of the deletion.",
            tags = { "customer", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema()))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@RequestHeader("Authorization") String jwt, @PathVariable Long id) {
        Customer existingCustomer = customerService.getCustomerById(id);
        if (existingCustomer != null) {
            customerService.deleteCustomer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("Customer not found with id " + id);
        }
    }
}
