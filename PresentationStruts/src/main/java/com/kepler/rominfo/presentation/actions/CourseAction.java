package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.service.services.CourseService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dragos on 10.07.2017.
 */
@Component
public class CourseAction extends ActionSupport {

    private static final Log LOGGER = LogFactory.getLog(CourseAction.class);

    private CourseService courseService;

    private List<CourseDto> professorCourses = new ArrayList<>();
    private List<CourseDto> studentCourses = new ArrayList<>();
    private String courseCode;
    private String courseName;
    private String category;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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

    public List<CourseDto> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(List<CourseDto> studentCourses) {
        this.studentCourses = studentCourses;
    }

    private List<CourseDto> allCourses = new ArrayList<>();

    public List<CourseDto> getAllCourses() {
        return allCourses;
    }

    public void setAllCourses(List<CourseDto> allCourses) {
        this.allCourses = allCourses;
    }

    public void setProfessorCourses(List<CourseDto> professorCourses) {
        this.professorCourses = professorCourses;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public List<CourseDto> getProfessorCourses() {
        return professorCourses;
    }

    public String allCourses() {
        try {
            allCourses = courseService.getAllCourses();

            return SUCCESS;
        } catch (Exception ex) {
            return ERROR;
        }
    }

    public String professorCourses() {
        try {
            Map<String, Object> session = ActionContext.getContext().getSession();
            User user = (User) session.get("user");
            professorCourses = courseService.getProfessorCourses(user.getEmail());

        } catch (Exception ex) {
            return ERROR;
        }

        return SUCCESS;
    }

    public String studentCourses() {
        try {
            Map<String, Object> session = ActionContext.getContext().getSession();
            User user = (User) session.get("user");
            studentCourses = courseService.getStudentCourses(user.getEmail());

        } catch (Exception ex) {
            return ERROR;
        }

        return SUCCESS;
    }

    public String editCourse() {
        try {
            courseService.editCourseName(Long.parseLong(courseCode), courseName, category);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }

    public String deleteCourse() {
        try {
            courseService.deleteCourse(Long.parseLong(courseCode));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }
}
