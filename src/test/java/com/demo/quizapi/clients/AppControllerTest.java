package com.demo.quizapi.clients;

import com.demo.quizapi.dao.AnswerDao;
import com.demo.quizapi.dao.UserDao;
import com.demo.quizapi.dao.UsernameAndPasswordAuthenticationDto;
import com.demo.quizapi.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@Slf4j
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:migration/dev/V2__INSERT_QUESTIONS.sql", "classpath:migration/dev/V3__INSERT_OPTIONS.sql", "classpath:init.sql"})
@Sql(scripts = "classpath:clean-up.sql", executionPhase = AFTER_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepositoryImpl;

    private String token;

    Faker faker = new Faker();

    UserDao userDao = UserDao.builder().firstName(faker.name().firstName()).lastName(faker.name().lastName())
            .email("faker@gmail.com").password("password").build();

    @BeforeEach
    void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        UsernameAndPasswordAuthenticationDto usernameAndPasswordAuthenticationDto = new UsernameAndPasswordAuthenticationDto();
        usernameAndPasswordAuthenticationDto.setUsername("akp.an4f3i@yahoo.com");
        usernameAndPasswordAuthenticationDto.setPassword("passwordf123");
        MvcResult mvcResult = this.mockMvc.perform(post("/api/v1/login")
                .contentType("application/json")
                .content(mapper.writeValueAsString(usernameAndPasswordAuthenticationDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        DataObject dataObject = mapper.readValue(result, DataObject.class);
        token = dataObject.responseData.getAuthorization();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void createUser() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("/api/v1/user")
                .contentType("application/json")
                .content(mapper.writeValueAsString(userDao)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }


    @Test
    @Order(2)
    void getQuestions() throws Exception {
        log.info("the header is ->{}", token);
        this.mockMvc.perform(get("/api/v1/questions").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getQuestion() throws Exception {
        this.mockMvc.perform(get("/api/v1/questions/10").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void answerQuestion() throws Exception {
        AnswerDao answerDao = new AnswerDao();
        answerDao.setOptionId(9);

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("/api/v1/answer").header("Authorization", token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(answerDao)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void getAnswer() throws Exception {
        this.mockMvc.perform(get("/api/v1/answer/1000").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getAllCorrectAnswer() throws Exception {
        this.mockMvc.perform(get("/api/v1/answer/correct").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getAllWrongAnswer() throws Exception {
        this.mockMvc.perform(get("/api/v1/answer/wrong").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getTotalAnswer() throws Exception {
        this.mockMvc.perform(get("/api/v1/answer/total").header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @ToString
    @Getter
    @Setter
    @AllArgsConstructor
    @JsonIgnoreProperties
    static
    class DataObject {
        String message;
        int status;
        ResponseData responseData;

        @JsonProperty("ResponseData")
        public ResponseData getResponseData() {
            return responseData;
        }

         DataObject() {
        }
    }

    @Getter
    @Setter
    @JsonIgnoreProperties
    @ToString
    static
    class ResponseData {
        String Authorization;
        List<Map<String, Object>>  authorities;


         ResponseData() {
        }
        @JsonProperty("Authorization")
        public String getAuthorization() {
            return Authorization;
        }
    }
}