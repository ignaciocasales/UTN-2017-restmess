package com.utn.restmess.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;

/**
 * Created by ignacio on 6/7/17.
 * <p>
 * User class entity.
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userid", nullable = false)
    private long id;

    @NotEmpty(message = "First name is required.")
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @NotEmpty(message = "Last Name is required.")
    @Column(name = "lastname", nullable = false)
    private String lastName;

    @NotEmpty(message = "Address is required.")
    @Column(name = "address", nullable = false)
    private String address;

    @NotEmpty(message = "Phone is required.")
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotEmpty(message = "City is required.")
    @Column(name = "city", nullable = false)
    private String city;

    @NotEmpty(message = "State is required.")
    @Column(name = "state", nullable = false)
    private String state;

    @NotEmpty(message = "Country is required.")
    @Column(name = "country", nullable = false)
    private String country;

    @NotEmpty(message = "Username is required.")
    @Column(name = "username",columnDefinition = "varchar(20)", unique = true, nullable = false)
    private String username;

    @NotEmpty(message = "Password is required.")
    @Column(name = "password", nullable = false)
    private String password;

    @Email(message = "Please provide a valid email address.")
    @NotEmpty(message = "Email is required.")
    @Column(name = "email", columnDefinition = "varchar(50)", unique = true, nullable = false)
    private String email;

    /* Old relationship
    @OneToMany
    @JoinTable(
            name = "usermessages",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "messageid")
    )
    */
    @OneToMany(mappedBy="user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Message> msgList;

    public User() {
    }

    public User(String firstName,
                String lastName,
                String address,
                String phone,
                String city,
                String state,
                String country,
                String username,
                String password,
                String email
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.country = country;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String firstName,
                String lastName,
                String address,
                String phone,
                String city,
                String state,
                String country,
                String username,
                String password,
                String email,
                List<Message> msgList
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.country = country;
        this.username = username;
        this.password = password;
        this.email = email;
        this.msgList = msgList;
    }

    public User(long id,
                String firstName,
                String lastName,
                String address,
                String phone,
                String city,
                String state,
                String country,
                String username,
                String password,
                String email,
                List<Message> msgList
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.country = country;
        this.username = username;
        this.password = password;
        this.email = email;
        this.msgList = msgList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Message> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<Message> msgList) {
        this.msgList = msgList;
    }
}
