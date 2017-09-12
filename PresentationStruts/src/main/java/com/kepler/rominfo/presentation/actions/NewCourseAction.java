package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.utils.CourseAlreadyExistsException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class NewCourseAction extends ActionSupport {
    private static final Log LOGGER = LogFactory.getLog(NewAccountAction.class);

    private CourseService courseService;

    private String newCourseResult;

    public String getNewCourseResult() {
        return newCourseResult;
    }

    public void setNewCourseResult(String newCourseResult) {
        this.newCourseResult = newCourseResult;
    }

    private String courseName;
    private String category;
    private String numberOfLectures;
    private String description;

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

    public String getNumberOfLectures() {
        return numberOfLectures;
    }

    public void setNumberOfLectures(String numberOfLectures) {
        this.numberOfLectures = numberOfLectures;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public String newCourse() {
        try {
            Map<String, Object> session = ActionContext.getContext().getSession();
            User user = (User) session.get("user");
            courseService.addCourse(courseName, category, Integer.parseInt(numberOfLectures), description, user.getEmail());
            newCourseResult = "Course creation successful!";
        } catch(CourseAlreadyExistsException ex){
            newCourseResult = ex.getMessage();
            return ERROR;
        }

        return SUCCESS;
    }
}
