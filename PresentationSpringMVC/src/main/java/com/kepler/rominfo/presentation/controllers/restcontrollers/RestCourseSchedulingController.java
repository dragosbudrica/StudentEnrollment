package com.kepler.rominfo.presentation.controllers.restcontrollers;

import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.utils.InvalidDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class RestCourseSchedulingController {

    private static final String UNIVERSITY_START_DATE = "2016-10-01";
    private static final String UNIVERSITY_END_DATE = "2017-06-30";

    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @RequestMapping(value = "/getCourseEvents", method = RequestMethod.GET)
    public List<CourseDto> events() {
        List<CourseDto> events = new ArrayList<>();
        try {
            for (CourseDto currentCourseDto : courseService.getOnlyCoursesWithDates()) {
                List<CourseDto> reccurentCourses = courseService.getAllRecurrentCourses(currentCourseDto);
                events.addAll(reccurentCourses);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return events;
    }

    @RequestMapping(value = "/getCourseTitles", method = RequestMethod.GET)
    public List<String> courseTitles() {
        List<String> courseTitles = null;
        try {
            courseTitles = courseService.getAllCourseTitles();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return courseTitles;
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.PUT)
    public List<CourseDto> schedule(@RequestBody Map<String, Object> params) {
        List<CourseDto> events = new ArrayList<>();
        String courseName = (String) params.get("courseName");
        String startTime = (String) params.get("startTime");

        try {
            if (validateDate(startTime)) {
                courseService.updateEvent(courseName, startTime);
                for (CourseDto currentCourseDto : courseService.getOnlyCoursesWithDates()) {
                    List<CourseDto> reccurentCourses = courseService.getAllRecurrentCourses(currentCourseDto);
                    events.addAll(reccurentCourses);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return events;
    }

    private boolean validateDate(String startTime) throws InvalidDateException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date universityStartDate = null;
        Date univeristyEndDate = null;
        Date date = null;
        try {
            universityStartDate = sdf.parse(UNIVERSITY_START_DATE);
            univeristyEndDate = sdf.parse(UNIVERSITY_END_DATE);
            date = sdf.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        /* dayOfWeek=7 => SATURDAY
         dayOfWeek=1 => SUNDAY*/
        return !date.before(universityStartDate) && !date.after(univeristyEndDate) && dayOfWeek != 1 && dayOfWeek != 7;
    }

}
