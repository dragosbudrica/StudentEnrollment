package com.kepler.rominfo.service.services;

import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.persistence.mappers.CategoryMapper;
import com.kepler.rominfo.persistence.mappers.CourseMapper;
import com.kepler.rominfo.persistence.mappers.LectureMapper;
import com.kepler.rominfo.persistence.mappers.UserMapper;
import com.kepler.rominfo.persistence.models.Category;
import com.kepler.rominfo.persistence.models.Course;
import com.kepler.rominfo.persistence.models.Professor;
import com.kepler.rominfo.persistence.models.Student;
import com.kepler.rominfo.service.utils.CourseAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CourseService {

    private CategoryMapper categoryMapper;
    private CourseMapper courseMapper;
    private UserMapper userMapper;
    private LectureMapper lectureMapper;

    public static final String BEGINNING_OF_SCHOOL = "2016-10-01";
    public static final String END_OF_SCHOOL = "2017-07-01";

    @Autowired
    public CourseService(UserMapper userMapper, CourseMapper courseMapper, LectureMapper lectureMapper, CategoryMapper categoryMapper) {
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
        this.lectureMapper = lectureMapper;
        this.categoryMapper = categoryMapper;
    }

    public List<CourseDto> getAllCourses() {
        List<CourseDto> allCourses = new ArrayList<>();
        for (Course currentCourse : courseMapper.getAllCourses()) {
            CourseDto courseDto = putCourseDtoProperties(currentCourse);
            allCourses.add(courseDto);
        }

        allCourses.sort(new Comparator<CourseDto>() {
            public int compare(CourseDto o1, CourseDto o2) {
                return o1.getCourseName().compareTo(o2.getCourseName());
            }
        });
        return allCourses;
    }

    public List<CourseDto> getStudentCourses(String email) {
        Student student = userMapper.findStudentByEmail(email);
        List<CourseDto> studentCourses = new ArrayList<>();

        for (Course currentCourse : courseMapper.getStudentCourses(student.getStudentId())) {
            CourseDto courseDto = putCourseDtoProperties(currentCourse);
            studentCourses.add(courseDto);
        }

        studentCourses.sort(new Comparator<CourseDto>() {
            public int compare(CourseDto o1, CourseDto o2) {
                return o1.getCourseName().compareTo(o2.getCourseName());
            }
        });

        return studentCourses;
    }

    public List<CourseDto> getStudentCoursesWithGrades(String email) {
        Student student = userMapper.findStudentByEmail(email);
        List<CourseDto> studentCoursesWithGrades = new ArrayList<>();

        for (Course currentCourse : courseMapper.getStudentCoursesWithGrades(student.getStudentId())) {
            CourseDto courseDto = putCourseDtoProperties(currentCourse);
            courseDto.setResult(currentCourse.getResult());
            courseDto.setValidated(currentCourse.isValidated());
            studentCoursesWithGrades.add(courseDto);
        }

        return studentCoursesWithGrades;
    }

    public List<CourseDto> getProfessorCourses(String email) {
        Professor professor = userMapper.findProfessorByEmail(email);
        List<CourseDto> professorCourses = new ArrayList<>();

        for (Course currentCourse : courseMapper.getProfessorCourses(professor.getProfessorId())) {
            CourseDto courseDto = putCourseDtoProperties(currentCourse);
            professorCourses.add(courseDto);
        }

        professorCourses.sort(new Comparator<CourseDto>() {
            public int compare(CourseDto o1, CourseDto o2) {
                return o1.getCourseName().compareTo(o2.getCourseName());
            }
        });

        return professorCourses;
    }

    public Course getCourseByName(String courseName) {
        return courseMapper.getCourseByName(courseName);
    }

    public Course getCourseByCode(long courseCode) {
        return courseMapper.getCourseByCode(courseCode);
    }

    public void setTime(String courseName, Date startTime) {
        courseMapper.updateCourseSchedule(courseName, startTime);
    }

    public void addCourse(String courseName, String category, int numberOfLectures, String description, String email) throws CourseAlreadyExistsException {
        Professor professor = userMapper.findProfessorByEmail(email);
        Course course = new Course();
        Category cat = categoryMapper.getCategoryByName(category);
        course.setCourseName(courseName);
        course.setCategory(cat);
        course.setCategoryId(cat.getCategoryId());
        course.setNumberOfLectures(numberOfLectures);
        course.setDescription(description);
        course.setProfessorId(professor.getProfessorId());
        List<CourseDto> allCourses = getAllCourses();
        for (CourseDto currentCourse : allCourses) {
            if (currentCourse.getCourseName().equals(courseName)) {
                throw new CourseAlreadyExistsException("Course already exists!");
            }
        }
        courseMapper.addCourse(course);
        for (int i = 0; i < numberOfLectures; i++) {
            lectureMapper.createLecture(course.getCourseCode(), "Lecture " + (i + 1));
        }
    }



    public List<CourseDto> getOnlyCoursesWithDates() {

        List<Course> allCourses = courseMapper.getOnlyCoursesWithDates();
        List<CourseDto> courses = new ArrayList<>();
        Calendar cal = Calendar.getInstance(); // creates calendar

        for (Course currentCourse : allCourses) {
            CourseDto courseDto = new CourseDto();
            courseDto.setCourseName(currentCourse.getCourseName());
            courseDto.setStartTime(currentCourse.getStartTime());
            cal.setTime(currentCourse.getStartTime()); // sets start time
            cal.add(Calendar.HOUR_OF_DAY, 2); // adds two hours
            courseDto.setEndTime(cal.getTime());
            courses.add(courseDto);
        }
        return courses;
    }

    public List<CourseDto> getAllRecurrentCourses(CourseDto course) {
        List<CourseDto> events = new ArrayList<>();
        CourseDto recurringEvent;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(course.getStartTime());
        Date newStartTime;
        Date newEndTime;

        try {
            Date beg = sdf.parse(BEGINNING_OF_SCHOOL);
            Date end = sdf.parse(END_OF_SCHOOL);
            while (c.getTime().compareTo(beg) > 0) {
                newStartTime = c.getTime();
                newEndTime = getEndTime(c, newStartTime, 2);
                recurringEvent = new CourseDto(course.getCourseName(), newStartTime, newEndTime);
                events.add(recurringEvent);
                setupNewRecurringEvent(c, newStartTime, -7);
            }
            setupNewRecurringEvent(c, course.getStartTime(), 7);
            while (c.getTime().compareTo(end) < 0) {
                newStartTime = c.getTime();
                newEndTime = getEndTime(c, newStartTime, 2);
                recurringEvent = new CourseDto(course.getCourseName(), newStartTime, newEndTime);
                events.add(recurringEvent);
                setupNewRecurringEvent(c, newStartTime, 7);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return events;
    }

    public Date getEndTime(Calendar c, Date newStartTime, int amount) {
        c.setTime(newStartTime);
        c.add(Calendar.HOUR_OF_DAY, amount);
        return c.getTime();
    }

    public void setupNewRecurringEvent(Calendar c, Date newStartTime, int amount) {
        c.setTime(newStartTime);
        c.add(Calendar.DAY_OF_YEAR, amount);
    }

    public List<String> getAllCourseTitles() {

        List<Course> myCourses = courseMapper.getAllCourses();
        List<String> courses = new ArrayList<>();

        for (Course currentCourse : myCourses) {
            String courseName = currentCourse.getCourseName();
            courses.add(courseName);
        }
        return courses;
    }

    public void updateEvent(String courseName, String startTime) throws ParseException {
        Date formattedStartDate = formatStartTime(startTime);
        courseMapper.updateCourseSchedule(courseName, formattedStartDate);
    }

    private Date formatStartTime(String startTime) throws ParseException {
        String[] tokens = startTime.split("T");
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(tokens[0] + " " + tokens[1] + ":00.000");
    }

    public CourseDto getUpdatedCourseDto(Course course) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseName(course.getCourseName());
        courseDto.setStartTime(course.getStartTime());
        cal.setTime(course.getStartTime()); // sets start time
        cal.add(Calendar.HOUR_OF_DAY, 2); // adds two hours
        courseDto.setEndTime(cal.getTime());
        return courseDto;
    }

    public void editCourseName(long courseCode, String courseName, String category) {
        Category cat = categoryMapper.getCategoryByName(category);
        courseMapper.editCourse(courseCode, courseName, cat.getCategoryId());
    }

    public void deleteCourse(long courseCode) {
        courseMapper.deleteCourse(courseCode);
    }

    public List<CourseDto> getProfessorCoursesWithEnrolledStudents(String email) {
        Professor professor = userMapper.findProfessorByEmail(email);
        List<CourseDto> professorCoursesWithEnrolledStudents = new ArrayList<>();

        for (Course currentCourse : courseMapper.getCoursesWithEnrolledStudents(professor.getProfessorId())) {
            CourseDto courseDto = putCourseDtoProperties(currentCourse);
            courseDto.setNumberOfEnrolledStudents(currentCourse.getNumberOfEnrolledStudents());
            professorCoursesWithEnrolledStudents.add(courseDto);
        }

        professorCoursesWithEnrolledStudents.sort(new Comparator<CourseDto>() {
            public int compare(CourseDto o1, CourseDto o2) {
                return o1.getCourseName().compareTo(o2.getCourseName());
            }
        });

        return professorCoursesWithEnrolledStudents;
    }

    private CourseDto putCourseDtoProperties(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseCode(course.getCourseCode());
        courseDto.setCourseName(course.getCourseName());
        courseDto.setCategory(course.getCategory().getCategoryName());
        courseDto.setProfessor(course.getUser().getFullName());
        courseDto.setStartTime(course.getStartTime());
        return courseDto;
    }
}
