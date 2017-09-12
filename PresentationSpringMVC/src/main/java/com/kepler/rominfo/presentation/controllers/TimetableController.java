package com.kepler.rominfo.presentation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TimetableController {

    @RequestMapping(value = "/timetable", method = RequestMethod.GET)
    public String timetable() {
        return "timetable";
    }

    @RequestMapping(value = "/courseScheduling", method = RequestMethod.GET)
    public String courseScheduling() {
        return "courseScheduling";
    }
}
