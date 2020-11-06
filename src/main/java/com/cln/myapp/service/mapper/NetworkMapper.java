package com.cln.myapp.service.mapper;


import com.cln.myapp.domain.*;
import com.cln.myapp.service.dto.NetworkDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Network} and its DTO {@link NetworkDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NetworkMapper extends EntityMapper<NetworkDTO, Network> {

    @Mapping(source = "networkParent.id", target = "networkParentId")
    NetworkDTO toDto(Network network);

    @Mapping(source = "networkParentId", target = "networkParent")
    Network toEntity(NetworkDTO networkDTO);

    default Network fromId(Long id) {
        if (id == null) {
            return null;
        }
        Network network = new Network();
        network.setId(id);
        return network;
    }
}
