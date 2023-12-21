package com.sportevents.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportevents.comment.CommentController;
import com.sportevents.dto.CommentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {

    private int port = 8080;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddComment() throws Exception {
        // given
        CommentDto commentRequest = new CommentDto();
        commentRequest.setAuthorId(1L);
        commentRequest.setContent("test comment");

        String jsonRequest = objectMapper.writeValueAsString(commentRequest);

        HttpHeaders headers = new HttpHeaders();
        String jwt = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImJlNzgyM2VmMDFiZDRkMmI5NjI3NDE2NThkMjA4MDdlZmVlNmRlNWMiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiYWRtaW4iLCJsb2NrZWQiOmZhbHNlLCJyb2xlIjoiQURNSU4iLCJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vc3BvcnRldmVudHMtMjBhOTgiLCJhdWQiOiJzcG9ydGV2ZW50cy0yMGE5OCIsImF1dGhfdGltZSI6MTcwMzExNTAxNCwidXNlcl9pZCI6IjEiLCJzdWIiOiIxIiwiaWF0IjoxNzAzMTE1MDE0LCJleHAiOjE3MDMxMTg2MTQsImVtYWlsIjoiYWRtaW5AbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsiYWRtaW5AbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.J4qfW-ibK-nk672Lx0be10iMe3LVNA-6814aWYnMQr4q_VvF8GtE9hogcHmCp9GFwUOuqXEORuI2fSKnkbhXVjTRFZ2KT987pR-cRIIzjSkFmXXbyTEwIcr9_ykYBOJHVqi_28rAb3ybrPtRHlBBrnNdaKgdmHJCXHrJAEbRVGflidEON38AOS3OOCo3c4ROuBY1DZpKEw7TnEsUr4ggqp7vHUUSjExBa6ZzeOfebfUO6r_ZwiI_9LPRealNJcQH3JygSe_ouYuwji1XngSpb_wqNlFcuusmII9mXDXeY0rScXGF8uO9BzE9mAFCynDM_agdTQKY0_c0727uyRhGIw=";
        headers.setBearerAuth(jwt);
        HttpEntity<?> entity = new HttpEntity<Object>( headers);

        restTemplate.exchange(RequestEntity.get(new URI("http://localhost:" + port + "/api/sport/all")).headers(headers).build(), Object.class);

//        ResponseEntity<String> response = restTemplate.exchange(
//                        "http://localhost:" + port + "/api/sport/all",
//                        HttpMethod.GET,
//                        entity,
//                        String.class
//        );

        // when
//        String response = restTemplate.postForObject("http://localhost:" + port + "/api/comment", commentRequest, String.class);

        // then
//        assertEquals("Comment added successfully", response);
    }




}
