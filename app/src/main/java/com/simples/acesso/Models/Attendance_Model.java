package com.simples.acesso.Models;

public class Attendance_Model {
    int id;
    int id_service;
    String description;
    boolean checked;

    public Attendance_Model(int id, int id_service, String description, boolean checked) {
        this.id = id;
        this.id_service = id_service;
        this.description = description;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_service() {
        return id_service;
    }

    public void setId_service(int id_service) {
        this.id_service = id_service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
