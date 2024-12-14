package com.xzgedu.supercv.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.UserAccountSetUp;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.order.domain.PaymentChannel;
import com.xzgedu.supercv.order.enums.PaymentChannelType;
import com.xzgedu.supercv.order.service.PaymentChannelService;
import com.xzgedu.supercv.user.domain.AuthToken;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentChannelService paymentChannelService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserAccountSetUp userAccountSetUp;

    private AuthToken authToken;

    @BeforeEach
    public void setUp() {
        this.authToken = userAccountSetUp.createRandomUserAndLogin();
    }

    @Test
    public void getEnabledPaymentChannelTypes_Success() throws Exception {
        PaymentChannel paymentChannel1 = new PaymentChannel();
        paymentChannel1.setType(PaymentChannelType.WXPAY.getValue());
        paymentChannel1.setName("wx-1");
        paymentChannel1.setAppId("wx-app-id-1");
        paymentChannel1.setMchId("wx-mch-id-1");
        paymentChannel1.setSecret("wx-secret-1");
        paymentChannel1.setEnabled(true);
        paymentChannelService.addPaymentChannel(paymentChannel1);

        PaymentChannel paymentChannel2 = new PaymentChannel();
        paymentChannel2.setType(PaymentChannelType.ALIPAY.getValue());
        paymentChannel2.setName("alipay-2");
        paymentChannel2.setAppId("alipay-app-id-2");
        paymentChannel2.setMchId("alipay-mch-id-2");
        paymentChannel2.setSecret("alipay-secret-2");
        paymentChannel2.setEnabled(true);
        paymentChannelService.addPaymentChannel(paymentChannel2);

        PaymentChannel paymentChannel3 = new PaymentChannel();
        paymentChannel3.setType(PaymentChannelType.MOCK_PAYMENT.getValue());
        paymentChannel3.setName("mock-3");
        paymentChannel3.setAppId("mock-app-id-3");
        paymentChannel3.setMchId("mock-mch-id-3");
        paymentChannel3.setSecret("mock-secret-3");
        paymentChannel3.setEnabled(false);
        paymentChannelService.addPaymentChannel(paymentChannel3);

        MvcResult result = mockMvc.perform(get("/v1/payment/channel/type/enabled")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<List<Integer>> responseData = objectMapper.readValue(
                content, new TypeReference<ResponseData<List<Integer>>>() {});

        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        List<Integer> enabledTypes = responseData.getData();
        assertEquals(2, enabledTypes.size());
    }
}
