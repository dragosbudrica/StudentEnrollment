package com.kepler.rominfo.presentation.controllers.restcontrollers;

import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.utils.CourseAlreadyExistsException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RestCourseController {
    private static final Log LOGGER = LogFactory.getLog(RestCourseController.class);

    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }


    @RequestMapping(value = "/getAllCourses", method = RequestMethod.GET)
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    @RequestMapping(value = "/getProfessorCourses", method = RequestMethod.GET)
    public List<CourseDto> getProfessorCourses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return courseService.getProfessorCourses(user.getEmail());
    }

    @RequestMapping(value = "/getStudentCourses", method = RequestMethod.GET)
    public List<CourseDto> getStudentCourses(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return courseService.getStudentCourses(user.getEmail());
    }

    @RequestMapping(value = "/editCourse", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void editCourse(@RequestBody Map<String, Object> params) {
        String courseCode = (String) params.get("courseCode");
        String courseName = (String) params.get("courseName");
        String category = (String) params.get("category");
        try {
            courseService.editCourseName(Long.parseLong(courseCode), courseName, category);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/deleteCourse", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteCourse(@RequestBody Map<String, Object> params) {
        String courseCode = (String) params.get("courseCode");
        try {
            courseService.deleteCourse(Long.parseLong(courseCode));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/addNewCourse", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public String addCourse(@RequestBody Map<String, Object> params, HttpSession session) {
        String result;
        String courseName = (String) params.get("courseName");
        String category = (String) params.get("category");
        String numberOfLectures = (String) params.get("numberOfLectures");
        String description = (String) params.get("description");
        try {
            User user = (User) session.getAttribute("user");
            courseService.addCourse(courseName, category, Integer.parseInt(numberOfLectures), description, user.getEmail());
            result = "Course creation successful!";
        } catch (CourseAlreadyExistsException ex) {
            result = ex.getMessage();
        }
        return result;
    }
}
