package com.cln.myapp.service.mapper;


import com.cln.myapp.domain.*;
import com.cln.myapp.service.dto.ProfilDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profil} and its DTO {@link ProfilDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, NetworkMapper.class})
public interface ProfilMapper extends EntityMapper<ProfilDTO, Profil> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "network.id", target = "networkId")
    ProfilDTO toDto(Profil profil);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "networkId", target = "network")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "removeRoles", ignore = true)
    Profil toEntity(ProfilDTO profilDTO);

    default Profil fromId(Long id) {
        if (id == null) {
            return null;
        }
        Profil profil = new Profil();
        profil.setId(id);
        return profil;
    }
}
