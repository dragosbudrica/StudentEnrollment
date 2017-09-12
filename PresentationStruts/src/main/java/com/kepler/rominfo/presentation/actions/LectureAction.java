package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.Lecture;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.services.LectureService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dragos on 12.07.2017.
 */
@Component
public class LectureAction extends ActionSupport {
    private static final Log LOGGER = LogFactory.getLog(LectureAction.class);

    private LectureService lectureService;

    private List<Lecture> lectures = new ArrayList<>();
    private String courseCode;
    private String lectureId;

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public LectureService getLectureService() {
        return lectureService;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    @Autowired
    public void setLectureService(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public String lectures() {
        try {
            lectures = lectureService.getLectures(Long.parseLong(courseCode));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }

    public String removeLectureAttachment() {
        try {
            lectureService.removeLectureAttachment(Long.parseLong(lectureId));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }
}
