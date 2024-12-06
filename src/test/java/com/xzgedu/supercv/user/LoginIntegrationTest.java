package com.xzgedu.supercv.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.common.exception.FetchWxUserInfoFailedException;
import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.domain.WechatUser;
import com.xzgedu.supercv.user.service.UserService;
import com.xzgedu.supercv.user.service.WechatService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @MockBean
    private WechatService wechatService;

    @BeforeEach
    void setUp() {
        // 清理或初始化数据
    }

    @Test
    void wechatLoginCallback_UserExists_ReturnsAuthToken() throws Exception {
        String code = "test_code";
        String key = "test_key";
        String unionId = "u_123456789012";
        String openId = "o_123456789012";
        String nickName = "超能用户1234";
        String headImgUrl = "default_head_img_url";

        WechatUser wxUser = new WechatUser();
        wxUser.setUnionId(unionId);
        wxUser.setOpenId(openId);
        wxUser.setNickName(nickName);
        wxUser.setHeadImgUrl(headImgUrl);

        when(wechatService.fetchUserInfoFromWechat(anyString())).thenReturn(wxUser);

        User user = new User();
        user.setUnionId(unionId);
        user.setOpenId(openId);
        user.setNickName(nickName);
        user.setHeadImgUrl(headImgUrl);
        userService.addUser(user);

        MvcResult result = mockMvc.perform(get("/v1/login/wechat/callback")
                        .param("code", code)
                        .param("key", key)
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<AuthToken> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<AuthToken>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        assertNotNull(responseData.getData().getToken());
    }

    @Test
    void wechatLoginCallback_UserDoesNotExist_CreatesUserAndReturnsAuthToken() throws Exception {
        String code = "test_code";
        String key = "test_key";
        String unionId = "u_123456789012";
        String openId = "o_123456789012";
        String nickName = "超能用户1234";
        String headImgUrl = "default_head_img_url";

        WechatUser wxUser = new WechatUser();
        wxUser.setUnionId(unionId);
        wxUser.setOpenId(openId);
        wxUser.setNickName(nickName);
        wxUser.setHeadImgUrl(headImgUrl);

        when(wechatService.fetchUserInfoFromWechat(anyString())).thenReturn(wxUser);

        MvcResult result = mockMvc.perform(get("/v1/login/wechat/callback")
                        .param("code", code)
                        .param("key", key)
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<AuthToken> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<AuthToken>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        assertNotNull(responseData.getData().getToken());

        User user = userService.getUserByUnionId(unionId);
        assertNotNull(user);
        assertEquals(unionId, user.getUnionId());
        assertEquals(openId, user.getOpenId());
        assertEquals(nickName, user.getNickName());
        assertEquals(headImgUrl, user.getHeadImgUrl());
    }

    @Test
    void wechatLoginCallback_FetchWxUserInfoFailed_ReturnsError() throws Exception {
        String code = "test_code";
        String key = "test_key";

        when(wechatService.fetchUserInfoFromWechat(anyString())).thenThrow(
                new FetchWxUserInfoFailedException("Failed to fetch wx user info"));

        MvcResult result = mockMvc.perform(get("/v1/login/wechat/callback")
                        .param("code", code)
                        .param("key", key)
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<?> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<?>>() {
        });
        assertEquals(ErrorCode.FETCH_WX_USER_INFO_FAILED.getCode(), responseData.getCode());
    }
}
