package com.xzgedu.supercv.vip;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.UserAccountSetUp;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.vip.domain.Vip;
import com.xzgedu.supercv.vip.service.VipService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class VipIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VipService vipService;

    @Autowired
    private UserAccountSetUp userAccountSetUp;

    private AuthToken authToken;

    @BeforeEach
    public void setup() {
        this.authToken = userAccountSetUp.createRandomUserAndLogin();
    }

    @Test
    public void getVipInfo_Success() throws Exception {
        long uid = authToken.getUid();

        //第一次购买vip
        vipService.renewVip(uid, 30, 10, 20);

        MvcResult result = mockMvc.perform(get("/v1/vip/info")
                        .header("uid", String.valueOf(uid))
                        .header("Origin", "https://www.supercv.cn")
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Vip> responseData = new ObjectMapper().readValue(content, new TypeReference<ResponseData<Vip>>() {
        });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        Vip vip = (Vip) responseData.getData();
        assertEquals(uid, vip.getUid());
        assertEquals(10, vip.getAiAnalysisLeftNum());
        assertEquals(20, vip.getAiOptimizationLeftNum());
    }
}
