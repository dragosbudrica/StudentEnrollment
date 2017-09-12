package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.persistence.models.Category;
import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.presentation.utils.Utils;
import com.kepler.rominfo.service.services.CategoryService;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.utils.CourseAlreadyExistsException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class AddCourseView implements Serializable {

    @ManagedProperty("#{courseService}")
    private CourseService courseService;

    @ManagedProperty("#{categoryService}")
    private CategoryService categoryService;

    private String courseName;
    private String category;
    private String description;
    private int numberOfLectures = 3;
    private Map<String,String> categories = new HashMap<>();

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfLectures() {
        return numberOfLectures;
    }

    public void setNumberOfLectures(int numberOfLectures) {
        this.numberOfLectures = numberOfLectures;
    }

    public Map<String, String> getCategories() {
        if (categories.size() == 0) {
            try {
                List<Category> categoryList = categoryService.getAllCategories();
                for (Category category : categoryList) {
                    categories.put(category.getCategoryName(), category.getCategoryName());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return categories;
    }

    public void setCategories(Map<String, String> categories) {
        this.categories = categories;
    }

    public void addCourse() {
        try {
            User user = Utils.getUserFromSession();
            courseService.addCourse(courseName, category, numberOfLectures, description, user.getEmail());
            Utils.addMessage("Course creation successful!", "Course creation successful!", FacesMessage.SEVERITY_INFO);
        } catch (CourseAlreadyExistsException ex) {
            Utils.addMessage(ex.getMessage(), ex.getMessage(), FacesMessage.SEVERITY_WARN);
        }
    }


}
