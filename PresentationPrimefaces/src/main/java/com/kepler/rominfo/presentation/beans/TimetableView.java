package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.presentation.utils.Utils;
import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.services.UserService;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
public class TimetableView implements Serializable {

    @ManagedProperty("#{userService}")
    private UserService userService;

    @ManagedProperty("#{courseService}")
    private CourseService courseService;

    private boolean rendered = true;
    private ScheduleModel eventModel;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        boolean timetableUnderConstruction = false;
        for (CourseDto course : courseService.getAllCourses()) {
            if (course.getStartTime() == null) {
                timetableUnderConstruction = true;
                break;
            }
        }

        if (timetableUnderConstruction) {
            rendered = false;
        } else {
            fillEventModel(eventModel);
            rendered = true;
        }
    }

     void fillEventModel(ScheduleModel eventModel) {
        for (CourseDto course : courseService.getOnlyCoursesWithDates()) {
            DefaultScheduleEvent newEvent = new DefaultScheduleEvent(course.getCourseName(), course.getStartTime(), course.getEndTime());
            newEvent.setEditable(false);
            for (ScheduleEvent ev : getAllRecurrentCourses(newEvent)) {
                eventModel.addEvent(ev);
            }
        }
    }

     List<ScheduleEvent> getAllRecurrentCourses(ScheduleEvent event) {
        List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
        DefaultScheduleEvent recurringEvent;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(event.getStartDate());
        Date newStartTime;
        Date newEndTime;

        try {
            Date beg = sdf.parse(CourseService.BEGINNING_OF_SCHOOL);
            Date end = sdf.parse(CourseService.END_OF_SCHOOL);
            while(c.getTime().compareTo(beg) > 0) {
                newStartTime = c.getTime();
                newEndTime = courseService.getEndTime(c, newStartTime, 2);
                recurringEvent = new DefaultScheduleEvent(event.getTitle(), newStartTime, newEndTime);
                events.add(recurringEvent);
                courseService.setupNewRecurringEvent(c, newStartTime, -7);
            }
            courseService.setupNewRecurringEvent(c, event.getStartDate(), 7);
            while(c.getTime().compareTo(end) < 0) {
                newStartTime = c.getTime();
                newEndTime = courseService.getEndTime(c, newStartTime, 2);
                recurringEvent = new DefaultScheduleEvent(event.getTitle(), newStartTime, newEndTime);
                events.add(recurringEvent);
                courseService.setupNewRecurringEvent(c, newStartTime, 7);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return events;
    }

    public String getRole() {
        User user = Utils.getUserFromSession();
        return userService.findUser(user.getEmail()).getRole().getRoleName();
    }
}
