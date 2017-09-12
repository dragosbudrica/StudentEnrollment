package com.kepler.rominfo.service.services;

import com.kepler.rominfo.persistence.mappers.RoleMapper;
import com.kepler.rominfo.persistence.mappers.UserMapper;
import com.kepler.rominfo.persistence.models.Professor;
import com.kepler.rominfo.persistence.models.Student;
import com.kepler.rominfo.persistence.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class UserService {

    private UserMapper userMapper;
    private RoleMapper roleMapper;

    @Autowired
    public UserService(UserMapper userMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public void addUser(String firstName, String lastName, long ssn, String email, String password, String role) {
        User user = new User();
        long roleId = roleMapper.getRoleByName(role).getRoleId();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setSsn(ssn);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoleId(roleId);
        userMapper.addUser(user);
        if(role.equals("Student")) {
            User student = userMapper.findByEmail(email);
            userMapper.addStudent(student.getUserId());
        } else {
            User professor = userMapper.findByEmail(email);
            userMapper.addProfessor(professor.getUserId());
        }
    }

    public User findUser(String email) {
        return userMapper.findByEmail(email);
    }

    public Student findStudent(String email) {
        return userMapper.findStudentByEmail(email);
    }

    public Professor findProfessor(String email) { return userMapper.findProfessorByEmail(email); }

    public boolean checkCredentials(User user, String email, String password) {
        return (user.getEmail().equals(email) && user.getPassword().equals(password));
    }

    public List<User> getEnrolledStudents(long courseCode) {
        List<User> enrolledStudents = userMapper.getEnrolledStudents(courseCode);

        enrolledStudents.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        });

        return enrolledStudents;
    }
}

