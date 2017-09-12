package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.Lecture;
import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.presentation.utils.AlphanumericSorting;
import com.kepler.rominfo.presentation.utils.Utils;
import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.services.LectureService;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
public class StudentCoursesView implements Serializable {

    @ManagedProperty("#{courseService}")
    private CourseService courseService;

    @ManagedProperty("#{lectureService}")
    private LectureService lectureService;

    private List<CourseDto> studentCourses = new ArrayList<>();
    private List<Lecture> listOfLectures = new ArrayList<>();
    private CourseDto selectedCourse;

    private static Comparator<Lecture> alphanumericComparator = new AlphanumericSorting<Lecture>();

    public CourseDto getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(CourseDto selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void setStudentCourses(List<CourseDto> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public void setLectureService(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public void setListOfLectures(List<Lecture> listOfLectures) {
        this.listOfLectures = listOfLectures;
    }

    public List<CourseDto> getStudentCourses() {
        if (studentCourses.size() == 0) {
            try {
               User user = Utils.getUserFromSession();
                studentCourses = courseService.getStudentCourses(user.getEmail());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return studentCourses;
    }

    public List<Lecture> getListOfLectures(CourseDto selectedCourse) {
        if (selectedCourse != null) {
            try {
                listOfLectures = lectureService.getLectures(selectedCourse.getCourseCode());
                listOfLectures.sort(alphanumericComparator);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listOfLectures;
    }

    public StreamedContent downloadPDF(Lecture lecture) {
        InputStream stream = new ByteArrayInputStream(lecture.getAttachment());
        return new DefaultStreamedContent(stream, "application/pdf", lecture.getName().concat(".pdf"));
    }
}
