package com.sopra.UserManagement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is required")
    private String fullName;

    @NotNull(message = "Date of Birth is required")
    @Past(message = "Date of Birth must be a past date")
    private LocalDate dob;

    @NotEmpty(message = "Gender is required")
    private String gender;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PhoneNo> phones = new ArrayList<>();

    public void addPhoneNo(PhoneNo phoneNo) {
        phones.add(phoneNo);
        phoneNo.setCustomer(this);
    }

    public void removePhoneNo(PhoneNo phoneNo) {
        phones.remove(phoneNo);
        phoneNo.setCustomer(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotEmpty(message = "Name is required") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotEmpty(message = "Name is required") String fullName) {
        this.fullName = fullName;
    }

    public @NotNull(message = "Date of Birth is required") @Past(message = "Date of Birth must be a past date") LocalDate getDob() {
        return dob;
    }

    public void setDob(@NotNull(message = "Date of Birth is required") @Past(message = "Date of Birth must be a past date") LocalDate dob) {
        this.dob = dob;
    }

    public @NotEmpty(message = "Gender is required") String getGender() {
        return gender;
    }

    public void setGender(@NotEmpty(message = "Gender is required") String gender) {
        this.gender = gender;
    }

    public @NotEmpty(message = "Email is required") @Email(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "Email is required") @Email(message = "Email should be valid") String email) {
        this.email = email;
    }

    public List<PhoneNo> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneNo> phones) {
        this.phones = phones;
    }
}