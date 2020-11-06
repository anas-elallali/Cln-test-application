package com.cln.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.cln.myapp.domain.Role} entity.
 */
public class RoleDTO implements Serializable {
    
    private Long id;

    private String name;

    private Set<ProfilDTO> profils = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProfilDTO> getProfils() {
        return profils;
    }

    public void setProfils(Set<ProfilDTO> profils) {
        this.profils = profils;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleDTO)) {
            return false;
        }

        return id != null && id.equals(((RoleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", profils='" + getProfils() + "'" +
            "}";
    }
}
