package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.persistence.models.Student;
import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.presentation.utils.Utils;
import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.services.EnrollmentService;
import com.kepler.rominfo.service.services.UserService;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class AllCoursesView implements Serializable {

    @ManagedProperty("#{courseService}")
    private CourseService courseService;

    @ManagedProperty("#{userService}")
    private UserService userService;

    @ManagedProperty("#{enrollmentService}")
    private EnrollmentService enrollmentService;

    private CourseDto selectedCourse;
    private List<CourseDto> allCourses = new ArrayList<>();

    public void setAllCourses(List<CourseDto> allCourses) {
        this.allCourses = allCourses;
    }

    private String enrollmentResult;

    public String getEnrollmentResult() {
        return enrollmentResult;
    }

    public void setEnrollmentResult(String enrollmentResult) {
        this.enrollmentResult = enrollmentResult;
    }

    public void setSelectedCourse(CourseDto selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public CourseDto getSelectedCourse() {
        return selectedCourse;
    }


    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setEnrollmentService(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    public List<CourseDto> getAllCourses() {
        if (allCourses.size() == 0) {
            try {
                allCourses = courseService.getAllCourses();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return allCourses;
    }

    public void enroll(CourseDto courseDto) {
        User user = Utils.getUserFromSession();
        Student student = userService.findStudent(user.getEmail());
        Course course = courseService.getCourseByCode(courseDto.getCourseCode());

        try {
            if(!enrollmentService.alreadyEnrolled(student, course)) {
                enrollmentService.enroll(student, course);
                Utils.addMessage("Status", "Enrollment successful!", FacesMessage.SEVERITY_INFO);
            }
            else {
                Utils.addMessage("Status", "You are already enrolled at that course!", FacesMessage.SEVERITY_WARN);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.addMessage("Enrollment failed!", ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
}
