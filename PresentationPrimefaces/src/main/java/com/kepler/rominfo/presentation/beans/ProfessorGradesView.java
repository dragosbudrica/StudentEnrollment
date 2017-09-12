package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.Student;
import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.presentation.utils.Utils;
import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.services.EnrollmentService;
import com.kepler.rominfo.service.services.UserService;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
public class ProfessorGradesView implements Serializable {
    @ManagedProperty("#{courseService}")
    private CourseService courseService;

    @ManagedProperty("#{userService}")
    private UserService userService;

    @ManagedProperty("#{enrollmentService}")
    private EnrollmentService enrollmentService;

    public void setEnrollmentService(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    private CourseDto selectedCourse;

    private Long userId;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public CourseDto getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(CourseDto selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private List<CourseDto> professorCoursesWithEnrolledStudents = new ArrayList<>();
    private List<User> enrolledStudents = new ArrayList<>();
    private Map<String, String> possibleResults = new HashMap<String, String>();

    public void setPossibleResults(Map<String, String> possibleResults) {
        this.possibleResults = possibleResults;
    }

    public Map<String, String> getPossibleResults() {
        if(possibleResults.size() == 0) {
            possibleResults.put("Absent", "Absent");
            for(int i = 1; i <= 10; i++) {
                possibleResults.put(String.valueOf(i), String.valueOf(i));
            }
        }

        return possibleResults;
    }

    public void setEnrolledStudents(List<User> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public void setProfessorCoursesWithEnrolledStudents(List<CourseDto> professorCoursesWithEnrolledStudents) {
        this.professorCoursesWithEnrolledStudents = professorCoursesWithEnrolledStudents;
    }

    public List<CourseDto> getProfessorCoursesWithEnrolledStudents() {
        if(professorCoursesWithEnrolledStudents.size() == 0) {
            try {
                User user = Utils.getUserFromSession();
                professorCoursesWithEnrolledStudents = courseService.getProfessorCoursesWithEnrolledStudents(user.getEmail());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return professorCoursesWithEnrolledStudents;
    }

    public List<User> getEnrolledStudents(CourseDto selectedCourse) {
        if(selectedCourse != null) {
            try {
                enrolledStudents = userService.getEnrolledStudents(selectedCourse.getCourseCode());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return enrolledStudents;
    }


    public void onResultChange(ValueChangeEvent event) {
        try {
            User userToBeUpdated = (User) event.getOldValue();
            String newResult = (String) event.getNewValue();

            if (newResult != null && !newResult.equals(userToBeUpdated.getResult())) {
                enrollmentService.editResult(selectedCourse.getCourseCode(), userToBeUpdated.getUserId(), newResult);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
