package com.kepler.rominfo.persistence.models;

import java.io.Serializable;

public class Lecture implements Serializable {
    private long lectureId;
    private String name;
    private byte[] attachment;

    public long getLectureId() {
        return lectureId;
    }

    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }
}
