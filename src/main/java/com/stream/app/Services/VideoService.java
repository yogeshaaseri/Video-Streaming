package com.stream.app.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.stream.app.entity.Video;

public interface VideoService {

    // save the video
    Video save(Video video, MultipartFile multipartFile);

    // get video by id
    Video get(String id);

    // get by title
    Video getByTitle(String title);

    // get All video
    List<Video> getAll();

    // processing Video
    String processVideo(String videoId);

}
