package com.xzgedu.supercv.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.UserAccountSetUp;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.common.exception.SendSmsCodeFailedException;
import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.user.service.SmsClient;
import com.xzgedu.supercv.user.service.Captcha;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class SmsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SmsClient smsClient;

    @MockBean
    private Captcha captcha;

    @Autowired
    private UserAccountSetUp userAccountSetUp;

    private AuthToken authToken;

    @BeforeEach
    void setUp() {
        // 清理或初始化数据
        this.authToken = userAccountSetUp.createRandomUserAndLogin();
    }

    @Test
    void sendSmsCode_Successful_SendSmsCode() throws Exception {
        when(captcha.verifyTicket(any(), any(), any())).thenReturn(true);
        doNothing().when(smsClient).sendSmsCode(any(), any());

        MvcResult result = mockMvc.perform(post("/v1/smscode/send")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", String.valueOf(authToken.getUid()))
                        .param("telephone", "13800138000")
                        .param("ticket", "valid_ticket")
                        .param("rand_str", "valid_randStr")
                        .param("user_ip", "127.0.0.1")
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<?> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<?>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
    }

    @Test
    void sendSmsCode_SendSmsCodeFailed_ReturnsError() throws Exception {
        when(captcha.verifyTicket(any(), any(), any())).thenReturn(true);
        doThrow(new SendSmsCodeFailedException("Failed to send SMS code")).when(smsClient).sendSmsCode(anyString(), anyString());

        MvcResult result = mockMvc.perform(post("/v1/smscode/send")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", String.valueOf(authToken.getUid()))
                        .param("telephone", "13800138000")
                        .param("ticket", "valid_ticket")
                        .param("rand_str", "valid_randStr")
                        .param("user_ip", "127.0.0.1")
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<?> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<?>>() {
        });
        assertEquals(ErrorCode.SMS_CODE_SEND_FAILED.getCode(), responseData.getCode());
    }

    @Test
    void sendSmsCode_VerifyTicketFailed_ReturnsError() throws Exception {
        when(captcha.verifyTicket(any(), any(), any())).thenReturn(false);
        doNothing().when(smsClient).sendSmsCode(any(), any());

        MvcResult result = mockMvc.perform(post("/v1/smscode/send")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("uid", String.valueOf(authToken.getUid()))
                        .param("telephone", "13800138000")
                        .param("ticket", "valid_ticket")
                        .param("rand_str", "valid_randStr")
                        .param("user_ip", "127.0.0.1")
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<?> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<?>>() {
        });
        assertEquals(ErrorCode.NO_PERMISSION.getCode(), responseData.getCode());
    }
}
