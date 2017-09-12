package com.kepler.rominfo.persistence.mappers;


import com.kepler.rominfo.persistence.models.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    List<Category> getAllCategories();
    Category getCategoryByName(@Param("categoryName") String categoryName);
}
