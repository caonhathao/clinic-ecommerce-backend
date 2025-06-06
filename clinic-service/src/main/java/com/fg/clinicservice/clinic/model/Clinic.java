package com.fg.clinicservice.clinic.model;

import com.fg.clinicservice.clinic_service.model.ClinicService;
import com.fg.clinicservice.doctor.model.Doctor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "clinic")
@Data
@NoArgsConstructor
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", updatable = false, nullable = false)
    private UUID clinicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private ClinicOwner owner;

    @NotNull
    @Column(name = "name", nullable = false)
    private String clinicName;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String clinicPhone;

    @NotNull
    @Column(name = "address", nullable = false, unique = true)
    private String clinicAddress;

    private String description;

    @ElementCollection
    @CollectionTable(name = "clinic_images", joinColumns = @JoinColumn(name = "clinic_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private Status status = Status.CLOSED;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClinicService> clinicServices = new HashSet<>();

    @OneToMany(mappedBy = "clinic")
    private Set<Doctor> doctors = new HashSet<>();

    public enum Status {
        CLOSED,
        OPEN
    }
}