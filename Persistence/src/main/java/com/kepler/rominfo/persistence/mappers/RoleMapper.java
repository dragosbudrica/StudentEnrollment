package com.kepler.rominfo.persistence.mappers;

import com.kepler.rominfo.persistence.models.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper {
    Role getRoleByName(@Param("roleName") String roleName);
}
