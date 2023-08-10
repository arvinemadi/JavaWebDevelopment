package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Customer;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="PET")
public class Pet {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private PetType type;

    private String name;
    private LocalDate birthDate;

    @Column(length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany(targetEntity = Schedule.class)
    private List<Schedule> scheduleList;

    public void addScheduleToScheduleList(Schedule schedule) {
        if (scheduleList == null) {
            scheduleList = new ArrayList<>();
        }
        scheduleList.add(schedule);
    }

    // Constructors
    public Pet() {}
    public Pet(Long id, PetType type, String name, LocalDate birthDate, String notes, Customer customer, List<Schedule> scheduleList) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.birthDate = birthDate;
        this.notes = notes;
        this.customer = customer;
        this.scheduleList = scheduleList;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType petType) {
        this.type = petType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
