package com.kepler.rominfo.presentation.controllers.restcontrollers;

import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.service.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestTimetableController {

    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @RequestMapping(value = "/getEvents", method = RequestMethod.GET)
    public List<CourseDto> getEvents() {
        List<CourseDto> events = new ArrayList<>();
        boolean timetableUnderConstruction = false;
        for (CourseDto course : courseService.getAllCourses()) {
            if (course.getStartTime() == null) {
                timetableUnderConstruction = true;
                break;
            }
        }

        if (!timetableUnderConstruction) {
            for (CourseDto currentCourseDto : courseService.getOnlyCoursesWithDates()) {
                List<CourseDto> reccurentCourses = courseService.getAllRecurrentCourses(currentCourseDto);
                events.addAll(reccurentCourses);
            }
        }

        return events;
    }
}
