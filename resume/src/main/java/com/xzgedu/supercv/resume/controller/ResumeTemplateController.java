package com.xzgedu.supercv.resume.controller;

import com.xzgedu.supercv.resume.domain.ResumeTemplate;
import com.xzgedu.supercv.resume.service.ResumeTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "简历模板")
@RestController
@RequestMapping("/v1/resume/template")
public class ResumeTemplateController {

    @Autowired
    private ResumeTemplateService resumeTemplateService;

    @Operation(summary = "分页查询模板")
    @GetMapping("/list")
    public Map<String, Object> listTemplates(@RequestParam("page_no") int pageNo,
                                             @RequestParam("page_size") int pageSize) {
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;
        int count = resumeTemplateService.countTemplates();
        List<ResumeTemplate> resumeTemplates = resumeTemplateService.getTemplatesPagination(limitOffset, limitSize);
        Map<String, Object> resp = new HashMap<>();
        resp.put("count", count);
        resp.put("templates", resumeTemplates);
        return resp;
    }

    //TODO: 稍后删除
    @Operation(summary = "分页查询模板(mock)，用于前端开发首页时临时调用")
    @GetMapping("/list/mock")
    public List<ResumeTemplate> listTemplatesMock(@RequestParam("page_no") int pageNo,
                                                  @RequestParam("page_size") int pageSize) {
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;
        List<ResumeTemplate> mockTemplates = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ResumeTemplate template = new ResumeTemplate();
            template.setId(1625L + i);
            template.setName("简历模板-" + i);
            template.setPageStyle("css_" + i);
            template.setDemoResumeThumbnailUrl("https://static.supercv.cn/image/defaut_resume_thumbnail.png");
            mockTemplates.add(template);
        }
        return mockTemplates.subList(limitOffset, limitSize);
    }
}
