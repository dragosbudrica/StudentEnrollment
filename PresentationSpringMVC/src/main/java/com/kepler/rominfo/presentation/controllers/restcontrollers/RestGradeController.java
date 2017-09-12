package com.kepler.rominfo.presentation.controllers.restcontrollers;

import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.persistence.models.*;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.services.EnrollmentService;
import com.kepler.rominfo.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RestGradeController {

    private CourseService courseService;

    private UserService userService;

    private EnrollmentService enrollmentService;

    @Autowired
    public RestGradeController(EnrollmentService enrollmentService, UserService userService, CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @RequestMapping(value = "/getCoursesWithEnrolledStudents", method = RequestMethod.GET)
    public List<CourseDto> getProfessorCourses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return courseService.getProfessorCoursesWithEnrolledStudents(user.getEmail());
    }

    @RequestMapping(value = "/getEnrolledStudents", method = RequestMethod.POST)
    public List<User> getEnrolledStudents(@RequestBody Map<String, Object> params) {
        String courseCode = (String) params.get("courseCode");
        List<User> students = null;
        try {
            students = userService.getEnrolledStudents(Long.parseLong(courseCode));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return students;
    }

    @RequestMapping(value = "/editResult", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void editResult(@RequestBody Map<String, Object> params) {
        String courseCode = (String) params.get("courseCode");
        String userId = (String) params.get("userId");
        String result = (String) params.get("result");
        try {
            enrollmentService.editResult(Long.parseLong(courseCode), Long.parseLong(userId), result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/removeResult", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void removeResult(@RequestBody Map<String, Object> params) {
        String courseCode = (String) params.get("courseCode");
        String userId = (String) params.get("userId");

        try {
            enrollmentService.removeResult(Long.parseLong(courseCode), Long.parseLong(userId));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/getMyCoursesWithGrades", method = RequestMethod.GET)
    public List<CourseDto> getMyCoursesWithGrades(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return courseService.getStudentCoursesWithGrades(user.getEmail());
    }

    @RequestMapping(value = "/validate", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void validate(@RequestBody Map<String, Object> params) {
        String courseCode = (String) params.get("courseCode");
        List userIds = (List) params.get("userIds");

        try {
            enrollmentService.validateStudentsGrades(Long.parseLong(courseCode), userIds);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/invalidate", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void invalidate(@RequestBody Map<String, Object> params) {
        String courseCode = (String) params.get("courseCode");
        List userIds = (List) params.get("userIds");

        try {
            enrollmentService.invalidateStudentsGrades(Long.parseLong(courseCode), userIds);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
