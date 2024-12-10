package com.xzgedu.supercv.resume;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.resume.domain.ResumeTemplate;
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

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class ResumeTemplateIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // 清理或初始化数据
    }

    @Test
    void testListTemplatesMock() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/resume/template/list/mock")
                        .header("Origin", "https://www.supercv.cn")
                        .param("page_no", "1")
                        .param("page_size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<List<ResumeTemplate>> responseData = new ObjectMapper().readValue(
                content, new TypeReference<ResponseData<List<ResumeTemplate>>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        List<ResumeTemplate> templates = (List<ResumeTemplate>) responseData.getData();
        assertThat(templates).isNotNull();
        assertThat(templates).hasSize(10); // 根据 page_size 参数，应该返回 10 条数据
        for (int i = 0; i < 10; i++) {
            ResumeTemplate template = templates.get(i);
            assertThat(template.getId()).isEqualTo(1625L + i);
            assertThat(template.getName()).isEqualTo("简历模板-" + i);
            assertThat(template.getCssName()).isEqualTo("css_" + i);
        }
    }
}