package com.cln.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * The Employee entity.
 */
@Entity
@Table(name = "profil")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "profil")
public class Profil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The firstname attribute.
     */
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_day")
    private Instant birthDay;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = "profils", allowSetters = true)
    private Network network;

    @ManyToMany(mappedBy = "profils")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Profil firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Profil lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Profil email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Profil phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public Profil birthDay(Instant birthDay) {
        this.birthDay = birthDay;
        return this;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public User getUser() {
        return user;
    }

    public Profil user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Network getNetwork() {
        return network;
    }

    public Profil network(Network network) {
        this.network = network;
        return this;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Profil roles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Profil addRoles(Role role) {
        this.roles.add(role);
        role.getProfils().add(this);
        return this;
    }

    public Profil removeRoles(Role role) {
        this.roles.remove(role);
        role.getProfils().remove(this);
        return this;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profil)) {
            return false;
        }
        return id != null && id.equals(((Profil) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profil{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            "}";
    }
}
