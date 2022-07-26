package com.dzx.myrpc.server.service.impl;

import com.dzx.myrpc.server.service.StudentService;

public class StudentServiceImpl implements StudentService {
    @Override
    public String study(String book) {
        return "universityStudent study " + book;
    }
}
