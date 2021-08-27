package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MvcStudyApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	DataSource dataSource;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void connectionTest() throws Exception{
		Connection connection = dataSource.getConnection();
		assertNotNull(connection);
	}
	@Test
	public void testMainPageLoading_ok(){
		String body = restTemplate.getForObject("/", String.class);
		assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
	}

}
