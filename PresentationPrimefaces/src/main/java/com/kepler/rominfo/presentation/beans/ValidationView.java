package com.kepler.rominfo.presentation.beans;

import com.kepler.rominfo.persistence.models.User;
import com.kepler.rominfo.presentation.utils.UserWithState;
import com.kepler.rominfo.service.dto.CourseDto;
import com.kepler.rominfo.service.services.CourseService;
import com.kepler.rominfo.service.services.EnrollmentService;
import com.kepler.rominfo.service.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

@ManagedBean
@ViewScoped
public class ValidationView implements Serializable {

    private static final String SELECT_ALL = "Select All";
    private static final String DESELECT_ALL = "Deselect All";
    private static final String SELECT_ONLY_ABSENTS = "Select Only Absents";
    private static final String SELECT_ONLY_GRADES = "Select Only Grades";
    private static final String SELECT_VALIDATED_RESULTS = "Select Validated Results";
    private static final String SELECT_INVALIDATED_RESULTS = "Select Invalidated Results";

    @ManagedProperty("#{userService}")
    private UserService userService;

    @ManagedProperty("#{courseService}")
    private CourseService courseService;

    @ManagedProperty("#{enrollmentService}")
    private EnrollmentService enrollmentService;

    public void setEnrollmentService(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    private List<User> enrolledStudents = new ArrayList<>();

    private List<UserWithState> enrolledStudentsWithStates = new ArrayList<>();

    private List<CourseDto> allCourses = new ArrayList<>();

    public List<CourseDto> getAllCourses() {
        if (allCourses.size() == 0) {
            try {
                allCourses = courseService.getAllCourses();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return allCourses;
    }

    public List<UserWithState> getEnrolledStudentsWithStates() {
        return enrolledStudentsWithStates;
    }

    private void fetchEnrolledStudents() {
        try {
            if (selectedCourse != null) {
                enrolledStudents = userService.getEnrolledStudents(selectedCourse.getCourseCode());
                enrolledStudentsWithStates.clear();
                for (User student : enrolledStudents) {
                    UserWithState userWithState = new UserWithState();
                    userWithState.setUser(student);
                    userWithState.setState(false);
                    enrolledStudentsWithStates.add(userWithState);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setEnrolledStudentsWithStates(List<UserWithState> enrolledStudentsWithStates) {
        this.enrolledStudentsWithStates = enrolledStudentsWithStates;
    }

    private Map<String, String> options = new LinkedHashMap<>();

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    private String selectedOption;

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    private CourseDto selectedCourse;

    private boolean somebodyEnrolled;

    private boolean somebodyWithoutGrade;

    public void setSomebodyEnrolled(boolean somebodyEnrolled) {
        this.somebodyEnrolled = somebodyEnrolled;
    }

    public void setSomebodyWithoutGrade(boolean somebodyWithoutGrade) {
        this.somebodyWithoutGrade = somebodyWithoutGrade;
    }

    public CourseDto getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(CourseDto selectedCourse) {
        this.selectedCourse = selectedCourse;
        this.somebodyWithoutGrade = false;
        fetchEnrolledStudents();
        somebodyEnrolled = enrolledStudents.size() != 0;
        for (User student : enrolledStudents) {
            if (student.getResult() == null) {
                somebodyWithoutGrade = true;
                break;
            }
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setEnrolledStudents(List<User> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public Map<String, String> getOptions() {
        if (options.size() == 0) {
            options.put(SELECT_ALL, SELECT_ALL);
            options.put(DESELECT_ALL, DESELECT_ALL);
            options.put(SELECT_ONLY_ABSENTS, SELECT_ONLY_ABSENTS);
            options.put(SELECT_ONLY_GRADES, SELECT_ONLY_GRADES);
            options.put(SELECT_VALIDATED_RESULTS, SELECT_VALIDATED_RESULTS);
            options.put(SELECT_INVALIDATED_RESULTS, SELECT_INVALIDATED_RESULTS);
        }

        return options;
    }

    public boolean isSomebodyEnrolled() {
        return somebodyEnrolled;
    }

    public boolean nobodyEnrolled() {
        return selectedCourse != null && !somebodyEnrolled;
    }

    public boolean isSomebodyWithoutGrade() {
        return somebodyWithoutGrade;
    }

    public boolean isEveryoneWithGrade() {
        return selectedCourse != null && !somebodyWithoutGrade;
    }

    public void onOptionChange() {
        switch (selectedOption) {
            case "Select All":
                for (UserWithState userWithState : enrolledStudentsWithStates) {
                    userWithState.setState(true);
                }
                break;
            case "Deselect All":
                for (UserWithState userWithState : enrolledStudentsWithStates) {
                    userWithState.setState(false);
                }
                break;
            case "Select Only Absents":
                for (UserWithState userWithState : enrolledStudentsWithStates) {
                    if (userWithState.getUser().getResult().equals("Absent")) {
                        userWithState.setState(true);
                    } else {
                        userWithState.setState(false);
                    }
                }
                break;
            case "Select Only Grades":
                for (UserWithState userWithState : enrolledStudentsWithStates) {
                    if (!userWithState.getUser().getResult().equals("Absent")) {
                        userWithState.setState(true);
                    } else {
                        userWithState.setState(false);
                    }
                }
                break;
            case "Select Validated Results":
                for (UserWithState userWithState : enrolledStudentsWithStates) {
                    if (userWithState.getUser().isValidated()) {
                        userWithState.setState(true);
                    } else {
                        userWithState.setState(false);
                    }
                }
                break;
            case "Select Invalidated Results":
                for (UserWithState userWithState : enrolledStudentsWithStates) {
                    if (!userWithState.getUser().isValidated()) {
                        userWithState.setState(true);
                    } else {
                        userWithState.setState(false);
                    }
                }
                break;
            default:
                break;
        }
    }

    public boolean isValidateBtnDisabled() {
        for (UserWithState userWithState : enrolledStudentsWithStates) {
            if (userWithState.isState() && !userWithState.getUser().isValidated()) {
                return false;
            }
        }

        return true;
    }

    public boolean isInvalidateBtnDisabled() {
        for (UserWithState userWithState : enrolledStudentsWithStates) {
            if (userWithState.isState() && userWithState.getUser().isValidated()) {
                return false;
            }
        }

        return true;
    }

    public void validate() {
        Long courseCode = selectedCourse.getCourseCode();
        List<String> userIds = new ArrayList<>();
        try {

            for (UserWithState userWithState : enrolledStudentsWithStates) {
                if (userWithState.isState()) {
                    userIds.add(String.valueOf(userWithState.getUser().getUserId()));
                }
            }

            enrollmentService.validateStudentsGrades(courseCode, userIds);

            for (UserWithState userWithState : enrolledStudentsWithStates) {
                if (userWithState.isState()) {
                    userWithState.setState(false);
                    userWithState.getUser().setValidated(true);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void invalidate() {
        Long courseCode = selectedCourse.getCourseCode();
        List<String> userIds = new ArrayList<>();
        try {

            for (UserWithState userWithState : enrolledStudentsWithStates) {
                if (userWithState.isState()) {
                    userIds.add(String.valueOf(userWithState.getUser().getUserId()));
                }
            }

            enrollmentService.invalidateStudentsGrades(courseCode, userIds);

            for (UserWithState userWithState : enrolledStudentsWithStates) {
                if (userWithState.isState()) {
                    userWithState.setState(false);
                    userWithState.getUser().setValidated(false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
