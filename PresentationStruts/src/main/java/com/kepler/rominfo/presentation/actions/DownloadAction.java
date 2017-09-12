package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.persistence.models.Lecture;
import com.kepler.rominfo.service.services.LectureService;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Dragos on 14.07.2017.
 */

@Component
public class DownloadAction extends ActionSupport {

    private InputStream inputStream;
    private String fileName;
    private String contentType;
    private long contentLength;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    private LectureService lectureService;

    @Autowired
    public void setLectureService(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    private String lectureId;

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    public String downloadLecture() {
        try {
            Lecture lecture = lectureService.getLectureById(Long.parseLong(lectureId));
            inputStream = new ByteArrayInputStream(lecture.getAttachment());
            fileName = lecture.getName() + ".pdf";
            return SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }
    }
}
