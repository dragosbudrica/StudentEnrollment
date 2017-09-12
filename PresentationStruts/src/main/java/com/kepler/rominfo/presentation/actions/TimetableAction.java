package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.service.services.CourseService;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimetableAction extends ActionSupport {

    private CourseService courseService;

    private boolean rendered;

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    private List<CourseDto> events = new ArrayList<CourseDto>();

    public void setEvents(List<CourseDto> events) {
        this.events = events;
    }

    public List<CourseDto> getEvents() {
        return events;
    }

    public String events() {
        try {
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
        } catch(Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }
}
