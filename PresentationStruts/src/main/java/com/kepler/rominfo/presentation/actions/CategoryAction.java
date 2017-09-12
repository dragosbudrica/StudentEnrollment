package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.persistence.models.Category;
import com.kepler.rominfo.service.services.CategoryService;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryAction extends ActionSupport {

    private CategoryService categoryService;
    private List<Category> allCategories = new ArrayList<>();

    public List<Category> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(List<Category> allCategories) {
        this.allCategories = allCategories;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public String allCategories() {
        try {
            allCategories = categoryService.getAllCategories();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }
}
