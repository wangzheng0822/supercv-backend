package com.xzgedu.supercv.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class LoginDevIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        // 清理或初始化数据
    }

    @Test
    void login_TelephoneExists_ReturnsAuthToken() throws Exception {
        String telephone = "13945678901";
        User user = new User();
        user.setTelephone(telephone);
        user.setOpenId("o_123456789012");
        user.setUnionId("u_123456789012");
        user.setNickName("超能用户1234");
        user.setHeadImgUrl("default_head_img_url");
        userService.addUser(user);

        MvcResult result = mockMvc.perform(post("/v1/login/dev/telephone")
                        .param("telephone", telephone)
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<AuthToken> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<AuthToken>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        assertNotNull(responseData.getData().getToken());
    }

    @Test
    void login_TelephoneDoesNotExist_CreatesUserAndReturnsAuthToken() throws Exception {
        String telephone = "13945678901";

        MvcResult result = mockMvc.perform(post("/v1/login/dev/telephone")
                        .param("telephone", telephone)
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<AuthToken> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<AuthToken>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        assertNotNull(responseData.getData().getToken());

        User user = userService.getUserByTelephone(telephone);
        assertNotNull(user);
        assertEquals(telephone, user.getTelephone());
        assertTrue(user.getOpenId().startsWith("o_"));
        assertTrue(user.getUnionId().startsWith("u_"));
        assertTrue(user.getNickName().startsWith("超能用户"));
        assertEquals("", user.getHeadImgUrl());
    }

    @Test
    void login_InvalidTelephone_ReturnsError() throws Exception {
        String invalidTelephone = "1394567890";

        MvcResult result = mockMvc.perform(post("/v1/login/dev/telephone")
                        .param("telephone", invalidTelephone)
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<?> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<?>>() {});
        assertEquals(ErrorCode.TELEPHONE_INVALID.getCode(), responseData.getCode());
    }
}
