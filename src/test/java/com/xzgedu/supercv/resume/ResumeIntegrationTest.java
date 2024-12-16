package com.xzgedu.supercv.resume;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.VipAccountSetUp;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.resume.domain.Resume;
import com.xzgedu.supercv.resume.service.ResumeService;
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

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class ResumeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VipAccountSetUp vipAccountSetUp;

    private AuthToken authToken;

    @Autowired
    private ResumeService resumeService;

    @BeforeEach
    void setUp() {
        this.authToken = vipAccountSetUp.createVipAccountAndLogin();
    }

    @Test
    void getResumesByUid_Success() throws Exception {
        //添加简历
        Resume resume1 = new Resume();
        resume1.setUid(authToken.getUid());
        resume1.setName("resume1");
        resume1.setTemplateId(1625L);
        resume1.setThumbnailUrl("https://www.supercv.cn/resume/1/thumbnail");
        resumeService.addResume(resume1);

        Resume resume2 = new Resume();
        resume2.setUid(authToken.getUid());
        resume2.setName("resume2");
        resume2.setTemplateId(1626L);
        resume2.setThumbnailUrl("https://www.supercv.cn/resume/2/thumbnail");
        resumeService.addResume(resume2);

        MvcResult result = mockMvc.perform(get("/v1/resume/list/mine")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("page_no", "1")
                        .param("page_size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<Map<String, Object>> responseData = new ObjectMapper().readValue(
                content, new TypeReference<ResponseData<Map<String, Object>>>() {
                });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        List<Resume> resumes = (List<Resume>) responseData.getData().get("resumes");
        assertThat(resumes).isNotNull();
        assertThat(resumes).hasSize(2);
    }

    //测试删除简历
    @Test
    public void deleteResume_Success() throws Exception {
        //添加简历
        Resume resume1 = new Resume();
        resume1.setUid(authToken.getUid());
        resume1.setName("resume1");
        resume1.setTemplateId(1625L);
        resume1.setThumbnailUrl("https://www.supercv.cn/resume/1/thumbnail");
        resumeService.addResume(resume1);

        MvcResult result = mockMvc.perform(post("/v1/resume/delete")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("resume_id", String.valueOf(resume1.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<Void> responseData = new ObjectMapper().readValue(
                content, new TypeReference<ResponseData<Void>>() {
                });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());

        Resume actualResume = resumeService.getResumeById(resume1.getId());
        assertNull(actualResume);
    }

    @Test
    public void deleteResume_NotOwner_ReturnsNoPermissionErrorCode() throws Exception {
        //添加简历
        Resume resume1 = new Resume();
        resume1.setUid(2222L);
        resume1.setName("resume1");
        resume1.setTemplateId(1625L);
        resume1.setThumbnailUrl("https://www.supercv.cn/resume/1/thumbnail");
        resumeService.addResume(resume1);

        MvcResult result = mockMvc.perform(post("/v1/resume/delete")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("resume_id", String.valueOf(resume1.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<Void> responseData = new ObjectMapper().readValue(
                content, new TypeReference<ResponseData<Void>>() {
                });
        assertEquals(ErrorCode.NO_PERMISSION.getCode(), responseData.getCode());
    }

    //测试更新简历
    @Test
    public void updateResume_Success() throws Exception {
        //添加简历
        Resume resume1 = new Resume();
        resume1.setUid(authToken.getUid());
        resume1.setName("resume1");
        resume1.setTemplateId(1625L);
        resume1.setThumbnailUrl("https://www.supercv.cn/resume/1/thumbnail");
        resumeService.addResume(resume1);

        MvcResult result = mockMvc.perform(post("/v1/resume/update")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("resume_id", String.valueOf(resume1.getId()))
                        .param("name", "new name")
                        .param("template_id", String.valueOf(1626L))
                        .param("page_margin_horizontal", "1")
                        .param("page_margin_vertical", "2")
                        .param("module_margin", "3")
                        .param("theme_color", "new color")
                        .param("font_size", "14")
                        .param("font_family", "new font")
                        .param("line_height", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseData<Void> responseData = new ObjectMapper().readValue(
                content, new TypeReference<ResponseData<Void>>() {
                });
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());

        Resume actualResume = resumeService.getResumeById(resume1.getId());
        assertNotNull(actualResume);
        assertEquals("new name", actualResume.getName());
        assertEquals(1626L, actualResume.getTemplateId());
        assertEquals(1, actualResume.getPageMarginHorizontal());
        assertEquals(2, actualResume.getPageMarginVertical());
        assertEquals(3, actualResume.getModuleMargin());
        assertEquals("new color", actualResume.getThemeColor());
        assertEquals(14, actualResume.getFontSize());
        assertEquals("new font", actualResume.getFontFamily());
        assertEquals(20, actualResume.getLineHeight());
    }

    //TODO:测试获取简历详情

    //TODO:测试3种创建简历
}
