package com.kepler.rominfo.presentation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GradeController {

    @RequestMapping(value = "/grades", method = RequestMethod.GET)
    public String grades() {
        return "grades";
    }

    @RequestMapping(value = "/myGrades", method = RequestMethod.GET)
    public String myGrades() {
        return "myGrades";
    }

    @RequestMapping(value = "/validation", method = RequestMethod.GET)
    public String validation() {
        return "validation";
    }
}
