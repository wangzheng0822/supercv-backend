package com.xzgedu.supercv.article;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzgedu.supercv.SupercvBackendApplication;
import com.xzgedu.supercv.UserAccountSetUp;
import com.xzgedu.supercv.VipAccountSetUp;
import com.xzgedu.supercv.advice.ResponseData;
import com.xzgedu.supercv.article.domain.Article;
import com.xzgedu.supercv.article.enums.ArticleCateType;
import com.xzgedu.supercv.article.service.ArticleService;
import com.xzgedu.supercv.common.exception.ErrorCode;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupercvBackendApplication.class)
@ActiveProfiles("ut")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // This ensures we use the test datasource
@Transactional
public class ArticleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserAccountSetUp userAccountSetUp;

    @Autowired
    private VipAccountSetUp vipAccountSetUp;

    private AuthToken authToken;

    private AuthToken vipAuthToken;

    @BeforeEach
    public void setUp() {
        this.authToken = userAccountSetUp.createRandomUserAndLogin();
        this.vipAuthToken = vipAccountSetUp.createVipAccountAndLogin();
    }

    @Test
    public void listArticles_Success() throws Exception {
        Article article = new Article();
        article.setUid(1L);
        article.setCateType(ArticleCateType.EXPERT_GUIDE.getValue());
        article.setTitle("test title");
        article.setSubTitle("test sub title");
        article.setCoverImg("test img url");
        article.setContent("test content");
        articleService.addArticle(article);

        MvcResult result = mockMvc.perform(get("/v1/article/list")
                        .header("Origin", "https://www.supercv.cn")
                        .param("cate_type", String.valueOf(ArticleCateType.EXPERT_GUIDE.getValue()))
                        .param("page_no", "1")
                        .param("page_size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseData<Map<String,Object>> responseData = objectMapper.readValue(content, new TypeReference<ResponseData<Map<String,Object>>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        assertNotNull(responseData.getData());
        assertEquals(1, (int) responseData.getData().get("count"));
    }

    @Test
    public void getArticleDetailById_Vip_Success() throws Exception {
        Article article = new Article();
        article.setUid(1L);
        article.setCateType(ArticleCateType.EXPERT_GUIDE.getValue());
        article.setTitle("test title");
        article.setSubTitle("test sub title");
        article.setCoverImg("test img url");
        article.setContent("test content");
        articleService.addArticle(article);

        MvcResult result = mockMvc.perform(get("/v1/article/detail")
                        .header("uid", String.valueOf(vipAuthToken.getUid()))
                        .header("Authorization", "Bearer " + vipAuthToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("article_id", String.valueOf(article.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ResponseData<Article> responseData = objectMapper.readValue(content, new TypeReference<ResponseData<Article>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        Article actualArticle = responseData.getData();
        assertEquals(article.getId(), actualArticle.getId());
        System.out.println(actualArticle.toString());
    }

    @Test
    public void getArticleDetailById_NotVip_ReturnsNoPermissionErrorCode() throws Exception {
        Article article = new Article();
        article.setUid(1L);
        article.setCateType(ArticleCateType.EXPERT_GUIDE.getValue());
        article.setTitle("test title");
        article.setSubTitle("test sub title");
        article.setCoverImg("test img url");
        article.setContent("test content");
        articleService.addArticle(article);

        MvcResult result = mockMvc.perform(get("/v1/article/detail")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("article_id", String.valueOf(article.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ResponseData<Article> responseData = objectMapper.readValue(content, new TypeReference<ResponseData<Article>>() {});
        assertEquals(ErrorCode.NO_PERMISSION.getCode(), responseData.getCode());
    }

    @Test
    public void getArticleDetailById_FreeArticle_Success() throws Exception {
        Article article = new Article();
        article.setUid(1L);
        article.setCateType(ArticleCateType.EXPERT_GUIDE.getValue());
        article.setTitle("test title");
        article.setSubTitle("test sub title");
        article.setCoverImg("test img url");
        article.setContent("test content");
        article.setFree(true);
        articleService.addArticle(article);

        MvcResult result = mockMvc.perform(get("/v1/article/detail")
                        .header("uid", String.valueOf(authToken.getUid()))
                        .header("Authorization", "Bearer " + authToken.getToken())
                        .header("Origin", "https://www.supercv.cn")
                        .param("article_id", String.valueOf(article.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        ResponseData<Article> responseData = objectMapper.readValue(content, new TypeReference<ResponseData<Article>>() {});
        assertEquals(ErrorCode.SUCCESS.getCode(), responseData.getCode());
        Article actualArticle = responseData.getData();
        assertEquals(article.getId(), actualArticle.getId());
        System.out.println(actualArticle.toString());
    }
}
