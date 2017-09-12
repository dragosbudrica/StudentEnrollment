package com.kepler.rominfo.service.services;

import com.kepler.rominfo.persistence.mappers.EnrollmentMapper;
import com.kepler.rominfo.persistence.mappers.UserMapper;
import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class EnrollmentService {
    private EnrollmentMapper enrollmentMapper;
    private UserMapper userMapper;

    @Autowired
    public EnrollmentService(EnrollmentMapper enrollmentMapper, UserMapper userMapper) {
        this.enrollmentMapper = enrollmentMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public void enroll(Student student, Course course) {
        student.getCourses().add(course);
        course.getStudents().add(student);
        enrollmentMapper.addEnrollment(student.getStudentId(), course.getCourseCode());
    }

    public boolean alreadyEnrolled(Student student, Course course) {
        for (Course currentCourse : enrollmentMapper.getCoursesOfStudent(student.getStudentId())) {
            if(currentCourse.getCourseCode() == course.getCourseCode())
                return true;
        }
        return false;
    }

    public void editResult(long courseCode, long userId, String result) {
        Student student = userMapper.findStudentByUserId(userId);
        enrollmentMapper.editResult(courseCode, student.getStudentId(), result);
    }

    public void removeResult(long courseCode, long userId) {
        Student student = userMapper.findStudentByUserId(userId);
        enrollmentMapper.removeResult(courseCode, student.getStudentId());
    }

    public void validateStudentsGrades(long courseCode, List userIds) {
        Student student;
        for(Object userId : userIds) {
            student = userMapper.findStudentByUserId(Long.parseLong((String)userId));
            enrollmentMapper.validate(courseCode, student.getStudentId());
        }
    }

    public void invalidateStudentsGrades(long courseCode, List userIds) {
        Student student;
        for(Object userId : userIds) {
            student = userMapper.findStudentByUserId(Long.parseLong((String)userId));
            enrollmentMapper.invalidate(courseCode, student.getStudentId());
        }
    }
}
