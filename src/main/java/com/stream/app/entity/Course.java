package com.stream.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "yt_Course")
public class Course {

    @Id
    private String Id;

    private String title;
}
