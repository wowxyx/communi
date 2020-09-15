package com.houke.community.dao;

import org.springframework.stereotype.Repository;

@Repository
public class TestDaoImp implements TestDao {
    @Override
    public String select() {
        return "Hello";
    }
}
