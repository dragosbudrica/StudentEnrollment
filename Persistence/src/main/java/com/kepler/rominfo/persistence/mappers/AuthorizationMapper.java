package com.kepler.rominfo.persistence.mappers;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationMapper {
    boolean isAuthorized(@Param("roleId") long roleId, @Param("resourceId") long resourceId);
}
