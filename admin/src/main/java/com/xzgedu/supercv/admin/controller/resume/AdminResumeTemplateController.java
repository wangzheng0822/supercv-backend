package com.xzgedu.supercv.admin.controller.resume;

import com.xzgedu.supercv.common.exception.GenericBizException;
import com.xzgedu.supercv.resume.domain.ResumeTemplate;
import com.xzgedu.supercv.resume.service.ResumeTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "简历模板管理")
@RequestMapping("/admin/resume/template")
@RestController
public class AdminResumeTemplateController {

    @Autowired
    private ResumeTemplateService resumeTemplateService;

    @Operation(summary = "创建简历模板")
    @PostMapping("/add")
    public void addResumeTemplate(@RequestParam("name") String name,
                                  @RequestParam("css_name") String cssName) throws GenericBizException {
        ResumeTemplate template = new ResumeTemplate();
        if (!resumeTemplateService.addTemplate(template)) {
            throw new GenericBizException("Failed to add resume template: " + template);
        }
    }

    @Operation(summary = "更新简历模板")
    @PostMapping("/update")
    public void updateResumeTemplate(@RequestParam("id") long id,
                                     @RequestParam("name") String name,
                                     @RequestParam("css_name") String cssName,
                                     @RequestParam(value = "demo_resume_id", required = false) Long demoResumeId)
            throws GenericBizException {
        ResumeTemplate template = new ResumeTemplate();
        template.setId(id);
        template.setName(name);
        template.setCssName(cssName);
        template.setDemoResumeId(demoResumeId);
        if (!resumeTemplateService.updateTemplate(template)) {
            throw new GenericBizException("Failed to update resume template: " + template);
        }
    }

    @Operation(summary = "删除简历模板")
    @PostMapping("/delete")
    public void deleteResumeTemplate(@RequestParam("id") long resumeTemplateId) throws GenericBizException {
        if (!resumeTemplateService.deleteTemplate(resumeTemplateId)) {
            throw new GenericBizException("Failed to delete resume template: " + resumeTemplateId);
        }
    }
}
