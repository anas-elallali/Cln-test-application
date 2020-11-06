package com.cln.myapp.service.mapper;


import com.cln.myapp.domain.*;
import com.cln.myapp.service.dto.RoleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfilMapper.class})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {


    @Mapping(target = "removeProfils", ignore = true)

    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
