package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.Lecture;
import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.presentation.utils.Utils;
import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.services.LectureService;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
public class ProfessorCoursesView implements Serializable {
    @ManagedProperty("#{courseService}")
    private CourseService courseService;

    @ManagedProperty("#{lectureService}")
    private LectureService lectureService;

    @ManagedProperty("#{studentCoursesView}")
    private StudentCoursesView studentCoursesView;

    @ManagedProperty("#{addCourseView}")
    private AddCourseView addCourseView;

    private List<CourseDto> professorCourses = new ArrayList<>();
    private List<Lecture> listOfLectures = new ArrayList<>();
    private CourseDto selectedCourse;

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void setProfessorCourses(List<CourseDto> professorCourses) {
        this.professorCourses = professorCourses;
    }

    public void setAddCourseView(AddCourseView addCourseView) {
        this.addCourseView = addCourseView;
    }

    public void setLectureService(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public void setStudentCoursesView(StudentCoursesView studentCoursesView) {
        this.studentCoursesView = studentCoursesView;
    }

    public CourseDto getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(CourseDto selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public List<CourseDto> getProfessorCourses() {
        if (professorCourses.size() == 0) {
            try {
                User user = Utils.getUserFromSession();
                professorCourses = courseService.getProfessorCourses(user.getEmail());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return professorCourses;
    }

    public List<Lecture> getListOfLectures() {
        return studentCoursesView.getListOfLectures(selectedCourse);
    }

    public void upload(FileUploadEvent event) throws IOException {
        Long lectureId;
        try {
            lectureId = (Long) event.getComponent().getAttributes().get("lectureId");
            lectureService.uploadFile(event.getFile().getContents(), lectureId);
            Utils.addMessage("The file was uploaded successfully!", "The file was uploaded successfully!", FacesMessage.SEVERITY_INFO);
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.addMessage("The upload process failed!", ex.getMessage(), FacesMessage.SEVERITY_INFO);
        }
    }

    public StreamedContent downloadPDF(Lecture lecture) {
        return studentCoursesView.downloadPDF(lecture);
    }

    public void onRowEdit(RowEditEvent event) {
        String newCourseName;
        String newCategory;
        long courseCode;
        try {
            newCourseName = ((CourseDto) event.getObject()).getCourseName();
            newCategory = ((CourseDto) event.getObject()).getCategory();
            courseCode = ((CourseDto) event.getObject()).getCourseCode();
            courseService.editCourseName(courseCode, newCourseName, newCategory);
            Utils.addMessage("Edit Successful!", "Edit Successful!", FacesMessage.SEVERITY_INFO);
        } catch (Exception ex) {
            Utils.addMessage("Edit Failed!", ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Utils.addMessage("Edit Canceled!", "The edit process was canceled!", FacesMessage.SEVERITY_WARN);
    }

    public Map<String, String> getCategories() {
        return addCourseView.getCategories();
    }

    public void remove(CourseDto courseDto) {
        try {
            courseService.deleteCourse(courseDto.getCourseCode());
            professorCourses.remove(courseDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeAddCourseDialog() {
        User user = Utils.getUserFromSession();
        professorCourses = courseService.getProfessorCourses(user.getEmail());
    }

    public void removeAttachment(Lecture lecture) {
        try {
            lectureService.removeLectureAttachment(lecture.getLectureId());
            getListOfLectures();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
