package com.kepler.rominfo.presentation.actions;

import com.kepler.rominfo.service.services.LectureService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dragos on 14.07.2017.
 */

@Component
public class UploadAction extends ActionSupport {

    private LectureService lectureService;

    @Autowired
    public void setLectureService(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    public String uploadLecture() throws ServletException, IOException {
        MultiPartRequestWrapper request = (MultiPartRequestWrapper) ServletActionContext.getRequest();
        UploadedFile[] files = request.getFiles("file");
        String lectureId = request.getParameter("lectureId");
        File[] filesToUpload = new File[files.length];
        InputStream stream;

        int counter = 0;
        for (UploadedFile file: files) {
            filesToUpload[counter] = ((StrutsUploadedFile)file).getContent();
            counter++;
        }

        try {
            stream = new FileInputStream(filesToUpload[0]);
            byte[] file = IOUtils.toByteArray(stream);
            lectureService.uploadFile(file, Long.parseLong(lectureId));
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }
}
