package com.kepler.rominfo.persistence.mappers;

import com.kepler.rominfo.persistence.models.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceMapper {
    Resource getResourceByName(@Param("resourceName") String resourceName);
}
