package com.stream.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stream.app.Services.VideoService;

@SpringBootTest
class SpringStreamBackendApplicationTests {

	@Autowired
	VideoService videoService;

	@Test
	void contextLoads() {
		// videoService.processVideo("44a42624-3d1d-403c-a34c-8a0e93d251cd");
	}

}
