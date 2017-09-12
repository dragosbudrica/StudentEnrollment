package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.services.EnrollmentService;
import com.kepler.rominfo.service.services.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GradeAction extends ActionSupport {

    private UserService userService;
    private EnrollmentService enrollmentService;
    private CourseService courseService;
    private String courseCode;
    private List<String> userIds = new ArrayList<>();
    private String userId;
    private String result;
    private List<User> enrolledStudents = new ArrayList<>();
    private List<CourseDto> professorCoursesWithEnrolledStudents = new ArrayList<>();
    private List<CourseDto> studentCoursesWithGrades = new ArrayList<>();

    public List<CourseDto> getProfessorCoursesWithEnrolledStudents() {
        return professorCoursesWithEnrolledStudents;
    }

    public void setProfessorCoursesWithEnrolledStudents(List<CourseDto> professorCoursesWithEnrolledStudents) {
        this.professorCoursesWithEnrolledStudents = professorCoursesWithEnrolledStudents;
    }

    public List<CourseDto> getStudentCoursesWithGrades() {
        return studentCoursesWithGrades;
    }

    public void setStudentCoursesWithGrades(List<CourseDto> studentCoursesWithGrades) {
        this.studentCoursesWithGrades = studentCoursesWithGrades;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Autowired
    public GradeAction(UserService userService, EnrollmentService enrollmentService, CourseService courseService) {
        this.userService = userService;
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
    }


    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<User> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<User> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String enrolledStudents() {
        try {
            enrolledStudents = userService.getEnrolledStudents(Long.parseLong(courseCode));
            return SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }
    }

    public String validateGrade() {
        try {
            enrollmentService.validateStudentsGrades(Long.parseLong(courseCode), userIds);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }

    public String invalidateGrade() {
        try {
            enrollmentService.invalidateStudentsGrades(Long.parseLong(courseCode), userIds);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }

    public String professorCoursesWithEnrolledStudents() {
        try {
            Map<String, Object> session = ActionContext.getContext().getSession();
            User user = (User) session.get("user");
            professorCoursesWithEnrolledStudents = courseService.getProfessorCoursesWithEnrolledStudents(user.getEmail());

        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    public String studentCoursesWithGrades() {
        try {
            Map<String, Object> session = ActionContext.getContext().getSession();
            User user = (User) session.get("user");
            studentCoursesWithGrades = courseService.getStudentCoursesWithGrades(user.getEmail());

        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    public String editResult() {
        try {
            enrollmentService.editResult(Long.parseLong(courseCode), Long.parseLong(userId), result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }

    public String removeResult() {
        try {
            enrollmentService.removeResult(Long.parseLong(courseCode), Long.parseLong(userId));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }


}
