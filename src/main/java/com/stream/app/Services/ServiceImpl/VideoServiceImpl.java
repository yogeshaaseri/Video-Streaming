package com.stream.app.Services.ServiceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.stream.app.Services.VideoService;
import com.stream.app.entity.Video;
import com.stream.app.repositories.VideoRepository;

import jakarta.annotation.PostConstruct;

@Service
public class VideoServiceImpl implements VideoService {

    @Value("${files.video}")
    String DIR;
    @Value("${files.video.hsl}")
    String HSL_DIR;

    @Autowired
    private VideoRepository videoRepository;

    @PostConstruct
    public void init() {
        File file = new File(DIR);

        try {
            Files.createDirectories(Paths.get(HSL_DIR));
            System.out.println("Folder HSL is creted");

        } catch (IOException e) {

            throw new RuntimeException("hsl Folder is not creted ");
        }

        // File file1 = new File(HSL_DIR);
        // if (!file1.exists()) {
        // file1.mkdir();
        // System.out.println("Folder is created name = HSL_DIR");
        // }

        if (!file.exists()) {
            file.mkdir();
            System.out.println("Folder is creted name = video");
        } else {
            System.err.println("Folder is already created name = video");
        }
    }

    @Override
    public Video save(Video video, MultipartFile multipartFile) {
        // get filename
        try {
            String filename = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            InputStream inputStream = multipartFile.getInputStream();

            // file path
            String cleanFileName = StringUtils.cleanPath(filename);
            // folder path create
            String cleanFolder = StringUtils.cleanPath(DIR);
            // folder path with filename
            Path path = Paths.get(cleanFolder, cleanFileName);
            System.out.println(multipartFile.getContentType());
            System.out.println(path);

            // copy file to the folder
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            // video meta data

            video.setContentType(contentType);
            video.setFilePath(path.toString());

            Video saVideo = videoRepository.save(video);

            // processVideo(saVideo.getVideoId());

            // meta data save
            return video;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Video get(String id) {
        // TODO Auto-generated method stub
        Video video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));

        return video;
    }

    @Override
    public Video getByTitle(String title) {

        return null;
    }

    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }

    // @Override
    // public String processVideo(String videoId) {

    //     Video video = this.get(videoId);
    //     String filePath = video.getFilePath();

    //     // path where to store data:
    //     Path videoPath = Paths.get(filePath);

    //     // String output360p = HSL_DIR + videoId + "/360p/";
    //     // String output720p = HSL_DIR + videoId + "/720p/";
    //     // String output1080p = HSL_DIR + videoId + "/1080p/";

    //     try {
    //         // Files.createDirectories(Paths.get(output360p));
    //         // Files.createDirectories(Paths.get(output720p));
    //         // Files.createDirectories(Paths.get(output1080p));

    //         // ffmpeg command
    //         Path outputPath = Paths.get(HSL_DIR, videoId);

    //         Files.createDirectories(outputPath);

    //         // String ffmpegCmd = String.format(
    //         // "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10
    //         // -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\"
    //         // \"%s/master.m3u8\" ",
    //         // videoPath, outputPath, outputPath
    //         // );

    //         String ffmpegCmd = String.format(
    //                 "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%03d.ts\" \"%s/master.m3u8\"",
    //                 videoPath, outputPath, outputPath);

    //         // StringBuilder ffmpegCmd = new StringBuilder();
    //         // ffmpegCmd.append("ffmpeg -i ")
    //         // .append(videoPath.toString())
    //         // .append(" -c:v libx264 -c:a aac")
    //         // .append(" ")
    //         // .append("-map 0:v -map 0:a -s:v:0 640x360 -b:v:0 800k ")
    //         // .append("-map 0:v -map 0:a -s:v:1 1280x720 -b:v:1 2800k ")
    //         // .append("-map 0:v -map 0:a -s:v:2 1920x1080 -b:v:2 5000k ")
    //         // .append("-var_stream_map \"v:0,a:0 v:1,a:0 v:2,a:0\" ")
    //         // .append("-master_pl_name
    //         // ").append(HSL_DIR).append(videoId).append("/master.m3u8 ")
    //         // .append("-f hls -hls_time 10 -hls_list_size 0 ")
    //         // .append("-hls_segment_filename
    //         // \"").append(HSL_DIR).append(videoId).append("/v%v/fileSequence%d.ts\" ")
    //         // .append("\"").append(HSL_DIR).append(videoId).append("/v%v/prog_index.m3u8\"");

    //         System.out.println("ffmpeg command" + ffmpegCmd);
    //         // file this command
    //         ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd);
    //         processBuilder.inheritIO();
    //         Process process = processBuilder.start();
    //         int exit = process.waitFor();
    //         if (exit != 0) {
    //             throw new RuntimeException("video processing failed!!");
    //         }

    //         return videoId;

    //     } catch (IOException ex) {
    //         throw new RuntimeException("Video processing fail!!");
    //     } catch (InterruptedException e) {
    //         throw new RuntimeException(e);
    //     }
    // }

    @Override
public String processVideo(String videoId) {
    // Fetch video details from the database
    Video video = this.get(videoId);
    String filePath = video.getFilePath();

    // Define input and output paths
    Path videoPath = Paths.get(filePath).toAbsolutePath();
    Path outputPath = Paths.get(HSL_DIR, videoId).toAbsolutePath();

    try {
        // Ensure output directory exists
        Files.createDirectories(outputPath);

        // FFmpeg command for HLS conversion (single resolution)
        String ffmpegCmd = String.format(
            "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%03d.ts\" \"%s/master.m3u8\"",
            videoPath.toString(), outputPath.toString(), outputPath.toString()
        );

        System.out.println("Executing FFmpeg command: " + ffmpegCmd);

        // Execute FFmpeg command
        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd);
        processBuilder.redirectErrorStream(true); // Capture stderr and stdout
        Process process = processBuilder.start();

        // Read and log FFmpeg output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        // Wait for FFmpeg to finish processing
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Video processing failed with exit code: " + exitCode);
        }

        System.out.println("Video processing completed successfully!");
        return videoId;

    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Video processing failed due to IO error: " + e.getMessage(), e);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // Restore interrupted state
        throw new RuntimeException("Video processing was interrupted", e);
    }
}

}
