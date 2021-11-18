package com.example;

import com.example.dto.MemberSaveRequestDto;
import com.example.dto.PostsResponseDto;
import com.example.dto.PostsSaveRequestDto;
import com.example.dto.PostsUpdateRequestDto;
import com.example.model.Member;
import com.example.model.MemberRepository;
import com.example.model.Posts;
import com.example.model.PostsRepository;
import com.example.service.MemberService;
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
import javax.transaction.Transactional;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

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

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberService memberService;

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
		String status = "writed";
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
			assertThat(post.getStatus()).isEqualTo(status);
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

		Posts posts = postsRepository
				.findById(updateId)
				.orElseThrow(()-> new IllegalArgumentException());

		assertThat(posts.getTitle()).isEqualTo(expectedTitle);
		assertThat(posts.getContent()).isEqualTo(expectedContent);
	}

	@Test
	@Transactional
	public void PostsDelete_ok() throws Exception {
		//given
		Posts savePosts = postsRepository.save(Posts.builder()
				.title("title delete")
				.content("content")
				.author("author")
				.build());
		//when
		savePosts.deletePosts();

		//then
		Posts getPosts = postsRepository.findById(savePosts.getId())
				.orElseThrow(()->new IllegalArgumentException("Post does not exist"));

		assertThat(getPosts.getStatus()).isEqualTo("deleted");
		assertThat(getPosts.getStatus()).isEqualTo(savePosts.getStatus());

	}


	@Test
	@DisplayName("API 로 게시글 삭제 처리")
	public void testPostDelete(){
		//given
		Posts savePosts = postsRepository.save(Posts.builder()
				.title("title delete")
				.content("content")
				.author("author")
				.build());

		String url = "http://localhost:" + port + "/api/v1/posts/" + savePosts.getId();

		//when
		restTemplate.delete(url, String.class);

		//then
		Posts getPosts = postsRepository.findById(savePosts.getId())
				.orElseThrow(()->new IllegalArgumentException("Post does not exist"));

		assertThat(getPosts.getStatus()).isEqualTo("deleted");

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


	@Test
	public void testMemberSave_ok(){
		//given
		String id = "kw200211";
		String password = "password";
		String name = "김지윤";
		String status = "join";
		MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
				.id(id)
				.password(password)
				.name(name)
				.build();

		String url = "http://localhost:" + port + "/api/v1/member";

		//when
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestDto, String.class);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		memberRepository.findById(requestDto.getId()).map(post -> {
			assertThat(post.getId()).isEqualTo(id);
			assertThat(post.getPassword()).isEqualTo(password);
			assertThat(post.getName()).isEqualTo(name);
			return null;
		});


	}

	@Test
	@Transactional
	public void MemberWithdraw_ok() throws Exception {
		//given
		Member saveMember = memberRepository.save(Member.builder()
				.id("jiyun1")
				.password("password1!")
				.name("김지윤")
				.build());

		Posts savePosts = postsRepository.save(Posts.builder()
				.title("title delete")
				.content("content")
				.author("jiyun1")
				.build());
		//when
		memberService.withdraw(saveMember.getId());

		//then
		Member getMember = memberRepository.findById(saveMember.getId())
				.orElseThrow(()->new IllegalArgumentException("Member does not exist"));

		Posts getPosts = postsRepository.findByAuthor(savePosts.getAuthor())
				.orElseThrow(()->new IllegalArgumentException("Posts does not exist"));

		assertThat(getMember.getStatus()).isEqualTo("withdraw");
		assertThat(getMember.getStatus()).isEqualTo(saveMember.getStatus());
		assertThat(getPosts.getStatus()).isEqualTo("deleted");
		assertThat(getPosts.getStatus()).isEqualTo(savePosts.getStatus());

	}

	@Test
	@DisplayName("API 로 회원탈퇴 처리")
	public void testMemberDelete(){
		//given
		Member saveMember = memberRepository.save(Member.builder()
				.id("kw200211")
				.password("password1!")
				.name("김지윤")
				.build());

		Posts savePosts = postsRepository.save(Posts.builder()
				.title("공지사항 입니다.")
				.content("내일은 쉬는날 입니다.")
				.author("kw200211")
				.build());

		String url = "http://localhost:" + port + "/api/v1/member/" + saveMember.getId();

		//when
		restTemplate.delete(url, String.class);

		//then
		Member getMember = memberRepository.findById(saveMember.getId())
				.orElseThrow(()->new IllegalArgumentException("Member does not exist"));

		Posts getPosts = postsRepository.findByAuthor(savePosts.getAuthor())
				.orElseThrow(()->new IllegalArgumentException("Posts does not exist"));

		assertThat(getMember.getStatus()).isEqualTo("withdraw");
		assertThat(getPosts.getStatus()).isEqualTo("deleted");
	}

	// TODO: 21. 11. 19.
	//  MemberService에서 회원중복 검증 시 argument를 Member로 받는데
	//  Member save 할때 받는 argument는  MamberSaveRequestDto다....
	//  서로 다르다.. 검증 방법을 수정해보자
	//  주말에 꼭 하기
	@Test
	public void 멤버중복테스트() {
	    //given
	    Member member1 = memberRepository.save(Member.builder()
				.id("jiyun")
	    		.password("123")
				.name("김지윤")
				.build());

	    //when
		Member member2 = memberRepository.save(Member.builder()
				.id("jiyun")
				.password("1234")
				.name("박지윤")
				.build());

	    //then

	}

}
