package com.kepler.rominfo.persistence.mappers;

import com.kepler.rominfo.persistence.models.Course;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Dragos on 15.05.2017.
 */
@Repository
public interface CourseMapper {
    Course getCourseByCode(long courseCode);
    List<Course> getAllCourses();
    List<Course> getStudentCourses(@Param("studentId") long studentId);
    List<Course> getStudentCoursesWithDates(@Param("studentId") long studentId);
    List<Course> getProfessorCourses(@Param("professorId") long professorId);
    List<Course> getCoursesWithEnrolledStudents(@Param("professorId") long professorId);
    List<Course> getOnlyCoursesWithDates();
    Course getCourseByName(@Param("courseName") String courseName);
    void updateCourseSchedule(@Param("courseName") String courseName, @Param("startTime") Date startTime);
    void addCourse(@Param("course") Course course);
    void editCourse(@Param("courseCode") long courseCode, @Param("courseName") String courseName, @Param("categoryId") long categoryId);
    void deleteCourse(@Param("courseCode") long courseCode);
    List<Course> getStudentCoursesWithGrades(@Param("studentId") long studentId);
}
