package com.kepler.rominfo.service.services;

import com.kepler.rominfo.persistence.mappers.LectureMapper;
import com.kepler.rominfo.persistence.models.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LectureService {

    private LectureMapper lectureMapper;

    @Autowired
    public LectureService(LectureMapper lectureMapper) {
        this.lectureMapper = lectureMapper;
    }

    public List<Lecture> getLectures(long courseCode) {
        return lectureMapper.getLectures(courseCode);
    }

    public void uploadFile(byte[] file, long lectureId) {
        try {
            if (file != null) {
                lectureMapper.uploadPDF(file, lectureId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Lecture getLectureByNameAndCourseCode(long courseCode, String lectureName) {
            return lectureMapper.getLectureByNameAndCourseCode(courseCode, lectureName);
    }

    public Lecture getLectureById(long lectureId) {
        return lectureMapper.getLectureById(lectureId);
    }

    public void removeLectureAttachment(long lectureId) {
        lectureMapper.removeLectureAttachment(lectureId);
    }
}
