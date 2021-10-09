package com.example;

import com.example.dto.PostsResponseDto;
import com.example.dto.PostsSaveRequestDto;
import com.example.dto.PostsUpdateRequestDto;
import com.example.model.Posts;
import com.example.model.PostsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MvcStudyApplicationTests {

	@LocalServerPort
	private int port;

	@Test
	void contextLoads() {
	}

	@Autowired
	DataSource dataSource;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PostsRepository postsRepository;

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

	@Test
	public void testPostSave_ok(){
		//given
		String title = "title";
		String content = "content";
		PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
				.title(title)
				.content(content)
				.author("jwon")
				.build();

		String url = "http://localhost:" + port + "/api/v1/posts";

		//when
		ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isGreaterThan(0L);


		postsRepository.findById(responseEntity.getBody().longValue()).map(post -> {
			assertThat(post.getTitle()).isEqualTo(title);
			assertThat(post.getContent()).isEqualTo(content);
			return null;
		});

	}

	@Test
	@DisplayName("타이틀을 title 로 해서 에러 추력 확인")
	public void testPostSave_title_null_fail(){
		//given
		String title = null;
		String content = "content";
		PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
				.title(title)
				.content(content)
				.author("jwon")
				.build();

		String url = "http://localhost:" + port + "/api/v1/posts";

		//when
		ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url, requestDto, Object.class);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);


	}

	@Test
	public void PostsUpdate_ok() throws Exception {
		//given
		Posts savePosts = postsRepository.save(Posts.builder()
				.title("title update")
				.content("content")
				.author("author")
				.build());

		Long updateId = savePosts.getId();
		String expectedTitle = "title2";
		String expectedContent = "content2";
		PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
				.title(expectedTitle)
				.content(expectedContent)
				.build();

		String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

		HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

		//when
		ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isGreaterThan(0L);

		List<Posts> all = postsRepository.findAll();
		assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
		assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
	}

	@Test
	public void PostsList_ok() throws Exception {

		//given
		Posts savePosts = postsRepository.save(Posts.builder()
				.title("title")
				.content("content")
				.author("author")
				.build());

		String url = "http://localhost:" + port + "/api/v1/posts";


		ResponseEntity<PostsResponseDto[]> responseEntity
				= restTemplate.getForEntity(url, PostsResponseDto[].class);

		List<PostsResponseDto> list = Arrays.asList(responseEntity.getBody());


		//when
		assertThat(list.size()).isGreaterThan(0);
		list.forEach(postsResponseDto -> {
			System.out.println(postsResponseDto.toString());
		});

	}

}
