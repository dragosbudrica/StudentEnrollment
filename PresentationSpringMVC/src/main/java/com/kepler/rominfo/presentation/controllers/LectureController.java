package com.kepler.rominfo.presentation.controllers;

import com.kepler.rominfo.persistence.models.Lecture;
import com.kepler.rominfo.service.services.LectureService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class LectureController {

    private LectureService lectureService;

    @Autowired
    public void setLectureService(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public @ResponseBody
    String doDownload(@RequestParam("lectureId") String lectureId, HttpServletResponse response) {
        String result;

        Lecture lecture = lectureService.getLectureById(Long.parseLong(lectureId));
        InputStream is = new ByteArrayInputStream(lecture.getAttachment());
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename="+lecture.getName()+".pdf");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
            result = "The file was successfully downloaded!";
        } catch (IOException e) {
            e.printStackTrace();
            result = "The download process failed! Reason: " + e.getMessage();
        }

        return result;
    }
}
