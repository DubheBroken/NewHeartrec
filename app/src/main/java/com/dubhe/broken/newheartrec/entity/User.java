package com.dubhe.broken.newheartrec.entity;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private Long phone;
    private String name;
    private String password;
    private String texts;
    private String records;
    private String paints;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public String getPaints() {
        return paints;
    }

    public void setPaints(String paints) {
        this.paints = paints;
    }
}
