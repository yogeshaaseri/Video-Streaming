package com.stream.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stream.app.entity.Video;
import java.util.List;


@Repository
public interface VideoRepository extends JpaRepository<Video,String> {

    Optional<Video> findByTitle(String title);
    
}
