package com.springboot.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
class DemoApplicationTests {

	@MockBean
	private DataSource dataSource;

	@MockBean
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

}

