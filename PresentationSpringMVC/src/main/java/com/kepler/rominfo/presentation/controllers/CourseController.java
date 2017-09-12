package com.kepler.rominfo.presentation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CourseController {

    @RequestMapping(value = "/allCourses", method = RequestMethod.GET)
    public String allCourses() {
      return "allCourses";
    }

    @RequestMapping(value = "/studentCourses", method = RequestMethod.GET)
    public String studentCourses() {
        return "studentCourses";
    }

    @RequestMapping(value = "/professorCourses", method = RequestMethod.GET)
    public String professorCourses() {
        return "professorCourses";
    }

    @RequestMapping(value = "/newCourse", method = RequestMethod.GET)
    public String newCourse() {
        return "newCourse";
    }
}
