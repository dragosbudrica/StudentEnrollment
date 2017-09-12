package com.kepler.rominfo.persistence.mappers;

import com.kepler.rominfo.persistence.models.Lecture;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by Dragos on 13.06.2017.
 */

@Repository
public interface LectureMapper {
    void createLecture(@Param("courseCode") long courseCode, @Param("name") String name);
    List<Lecture> getLectures(@Param("courseCode") long courseCode);
    void uploadPDF(@Param("file") byte[] file, @Param("lectureId") long lectureId);
    Lecture getLectureByNameAndCourseCode(@Param("courseCode") long courseCode, @Param("lectureName") String lectureName);
    Lecture getLectureById(long lectureId);
    void removeLectureAttachment(@Param("lectureId") long lectureId);
}
