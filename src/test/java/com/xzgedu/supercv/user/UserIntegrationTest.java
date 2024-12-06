package com.xzgedu.supercv.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.UserAccountSetUp;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.service.SmsService;
import com.xzgedu.supercv.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @MockBean
    private SmsService smsService;

    @Autowired
    private UserAccountSetUp userAccountSetUp;

    private AuthToken authToken;

    @BeforeEach
    public void setup() {
        this.authToken = userAccountSetUp.createRandomUserAndLogin();
    }

    @Test
    void getUserInfo_UserFound_ReturnsUser() throws Exception {
        long uid = authToken.getUid();
        MvcResult result = mockMvc.perform(get("/v1/user/info")
                        .header("uid", String.valueOf(uid))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<User> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<User>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        assertEquals(uid, responseData.getData().getId());
    }

    @Test
    void getUserInfo_UserNotFound_ReturnsError() throws Exception {
        long uid = authToken.getUid() + 1; //一个不能存在的uid

        MvcResult result = mockMvc.perform(get("/v1/user/info")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<?> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<?>>() {
        });
        assertEquals(ErrorCode.AUTH_FAILED.getCode(), responseData.getCode());
    }

    @Test
    void bindTelephone_Successful() throws Exception {
        long uid = authToken.getUid();
        String telephone = "13945678901";
        String smsCode = "123456";
        doNothing().when(smsService).verifySmsCode(telephone, smsCode);
        mockMvc.perform(post("/v1/user/telephone/bind")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .param("telephone", telephone)
                        .param("sms_code", smsCode)
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User user = userService.getUserById(uid);
        assertNotNull(user);
        assertEquals(telephone, user.getTelephone());
    }

    @Test
    void updateNickName_Successful() throws Exception {
        long uid = authToken.getUid();
        String nickName = "newName";

        mockMvc.perform(post("/v1/user/nick-name/update")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .param("nick_name", nickName)
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User user = userService.getUserById(uid);
        assertNotNull(user);
        assertEquals(nickName, user.getNickName());
    }

    @Test
    void updateHeadImgUrl_Successful() throws Exception {
        long uid = authToken.getUid();
        String headImgUrl = "https://example.com/image.jpg";

        mockMvc.perform(post("/v1/user/head-img-url/update")
                        .header("uid", String.valueOf(uid))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .param("head_img_url", headImgUrl)
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User user = userService.getUserById(uid);
        assertNotNull(user);
        assertEquals(headImgUrl, user.getHeadImgUrl());
    }
}

