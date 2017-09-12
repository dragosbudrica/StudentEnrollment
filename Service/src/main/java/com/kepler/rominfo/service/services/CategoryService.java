package com.kepler.rominfo.service.services;

import com.kepler.rominfo.persistence.mappers.CategoryMapper;
import com.kepler.rominfo.persistence.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    private CategoryMapper categoryMapper;

    @Autowired
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public List<Category> getAllCategories() {
        return categoryMapper.getAllCategories();
    }


}
