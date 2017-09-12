package com.kepler.rominfo.presentation.controllers.restcontrollers;

import com.kepler.rominfo.persistence.models.Category;
import com.kepler.rominfo.service.services.CategoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestCategoryController {
    private static final Log LOGGER = LogFactory.getLog(RestCategoryController.class);

    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/getAllCategories", method = RequestMethod.GET)
    public List<Category> getAllCategories() {
        List<Category> allCategories = new ArrayList<>();
        try {
            allCategories = categoryService.getAllCategories();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return allCategories;
    }
}
