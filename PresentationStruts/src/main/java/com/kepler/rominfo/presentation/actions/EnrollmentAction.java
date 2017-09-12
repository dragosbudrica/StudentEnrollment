package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.Student;
import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.services.EnrollmentService;
import com.kepler.rominfo.service.services.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Created by Dragos on 11.07.2017.
 */
@Component
public class EnrollmentAction extends ActionSupport {

    private static final Log LOGGER = LogFactory.getLog(EnrollmentAction.class);

    private CourseService courseService;
    private UserService userService;
    private EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentAction(EnrollmentService enrollmentService, UserService userService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
    }

    private String courseCode;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    private String enrollmentResult;

    public void setEnrollmentResult(String enrollmentResult) {
        this.enrollmentResult = enrollmentResult;
    }

    public String getEnrollmentResult() {
        return enrollmentResult;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public String enroll() {
        Map<String, Object> session = ActionContext.getContext().getSession();
        User user = (User) session.get("user");
        Student student = userService.findStudent(user.getEmail());
        Course course = courseService.getCourseByCode(Long.parseLong(courseCode));

        try {
            if(!enrollmentService.alreadyEnrolled(student, course)) {
                enrollmentService.enroll(student, course);
                enrollmentResult = "Enrollment successful!";
            }
            else {
                enrollmentResult = "You're already enrolled at that course!";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            enrollmentResult = ex.getMessage();
            return ERROR;
        }

        return SUCCESS;
    }
}
