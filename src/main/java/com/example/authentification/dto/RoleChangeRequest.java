package com.example.authentification.dto;

import lombok.Data;

public class RoleChangeRequest {
    private String role;
    private boolean add;
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean isAdd() {
        return add;
    }
    public void setAdd(boolean add) {
        this.add = add;
    }

}