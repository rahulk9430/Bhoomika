package com.sopra.UserManagement.Service;

import com.sopra.UserManagement.Exception.EmailAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopra.UserManagement.Entity.Customer;
import com.sopra.UserManagement.Repository.CustomerRepository;
import java.util.List;


@Service
public interface CustomerService {



	List<Customer> getAllCustomers();
	Customer getCustomerById(Long id);
	Customer saveCustomer(Customer customer);
	void deleteCustomer(Long id);
}
