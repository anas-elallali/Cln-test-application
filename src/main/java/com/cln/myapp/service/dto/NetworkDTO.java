package com.cln.myapp.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.cln.myapp.domain.Network} entity.
 */
public class NetworkDTO implements Serializable {
    
    private Long id;

    private String name;

    private String type;


    private Long networkParentId;
    
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getNetworkParentId() {
        return networkParentId;
    }

    public void setNetworkParentId(Long networkId) {
        this.networkParentId = networkId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetworkDTO)) {
            return false;
        }

        return id != null && id.equals(((NetworkDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NetworkDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", networkParentId=" + getNetworkParentId() +
            "}";
    }
}
