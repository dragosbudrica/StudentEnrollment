package com.kepler.rominfo.service.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dragos on 10.07.2017.
 */
public class CourseDto implements Serializable {
    private long courseCode;
    private String courseName;
    private String category;
    private String professor;
    private Date startTime;
    private Date endTime;
    private String description;
    private int numberOfLectures;
    private int numberOfEnrolledStudents;
    private String result;
    private boolean validated;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public int getNumberOfEnrolledStudents() {
        return numberOfEnrolledStudents;
    }

    public void setNumberOfEnrolledStudents(int numberOfEnrolledStudents) {
        this.numberOfEnrolledStudents = numberOfEnrolledStudents;
    }

    public CourseDto() {}

    public CourseDto(String courseName, Date startTime, Date endTime) {
        this.courseName = courseName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getNumberOfLectures() {
        return numberOfLectures;
    }

    public void setNumberOfLectures(int numberOfLectures) {
        this.numberOfLectures = numberOfLectures;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public long getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(long courseCode) {
        this.courseCode = courseCode;
    }
}
