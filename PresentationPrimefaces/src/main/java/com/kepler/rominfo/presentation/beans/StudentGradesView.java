package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.presentation.utils.Utils;
import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.service.services.CourseService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class StudentGradesView implements Serializable {

    @ManagedProperty("#{courseService}")
    private CourseService courseService;

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    private List<CourseDto> studentCoursesWithGrades = new ArrayList<>();

    public void setStudentCoursesWithGrades(List<CourseDto> studentCoursesWithGrades) {
        this.studentCoursesWithGrades = studentCoursesWithGrades;
    }

    public List<CourseDto> getStudentCoursesWithGrades() {
        if (studentCoursesWithGrades.size() == 0) {
            try {
                User user = Utils.getUserFromSession();
                studentCoursesWithGrades = courseService.getStudentCoursesWithGrades(user.getEmail());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return studentCoursesWithGrades;
    }
}
