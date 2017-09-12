package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.presentation.utils.Utils;
import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.service.services.CourseService;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class CourseSchedulingView implements Serializable {

    @ManagedProperty("#{courseService}")
    private CourseService courseService;

    @ManagedProperty("#{timetableView}")
    private TimetableView timetableView;

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void setTimetableView(TimetableView timetableView) {
        this.timetableView = timetableView;
    }

    private ScheduleModel eventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        timetableView.fillEventModel(eventModel);
    }

    public List<String> getAllCourseTitles() {
        List<String> courseTitles = null;
        try {
            courseTitles = courseService.getAllCourseTitles();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return courseTitles;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }

    public void addEvent(ActionEvent actionEvent) {
        ScheduleEvent se = null;
        for (ScheduleEvent sev: eventModel.getEvents()) {
            if (sev.getTitle().equals(event.getTitle())) {
                se = sev;
                break;
            }
        }
        if(se != null) {
            event.setId(se.getId());
            eventModel.updateEvent(event);
        }
        else {
            List<ScheduleEvent> events = timetableView.getAllRecurrentCourses(event);
            for (ScheduleEvent ev: events) {
                eventModel.addEvent(ev);
            }
        }
        courseService.setTime(event.getTitle(), event.getStartDate());
        Utils.addMessage("Course scheduled", "Course scheduled", FacesMessage.SEVERITY_INFO);
        event = new DefaultScheduleEvent();
    }

}
