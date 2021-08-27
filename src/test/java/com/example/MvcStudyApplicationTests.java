package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MvcStudyApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	DataSource dataSource;

	@Test
	public void connectionTest() throws Exception{
		Connection connection = dataSource.getConnection();
		assertNotNull(connection);
	}

}
