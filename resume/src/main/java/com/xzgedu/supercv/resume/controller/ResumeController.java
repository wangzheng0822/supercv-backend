package com.xzgedu.supercv.resume.controller;

import com.xzgedu.supercv.common.exception.ResumeTemplateNotFoundException;
import com.xzgedu.supercv.resume.domain.Resume;
import com.xzgedu.supercv.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "简历")
@RequestMapping("/v1/resume/")
@RestController
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Operation(summary = "获取用户创建的所有简历（非detail）")
    @GetMapping("/list/mine")
    public List<Resume> getResumesByUid(@RequestHeader("uid") long uid) {
        return resumeService.getResumesByUid(uid);
    }

    @Operation(summary = "获取简历详情")
    @GetMapping("/detail")
    public Resume getResumeDetail(@RequestHeader("uid") long uid,
                                  @RequestParam("resume_id") long resumeId) {
        return resumeService.getResumeDetail(uid, resumeId);
    }

    @Operation(summary = "删除简历")
    @PostMapping("/delete")
    public void deleteResume(@RequestHeader("uid") long uid,
                             @RequestParam("resume_id") long resumeId) {
        resumeService.deleteResume(resumeId);
    }

    @Operation(summary = "改变简历标题、模板、主题颜色、间距等等")
    @PutMapping("/update")
    public boolean updateResume(@RequestHeader("uid") long uid,
                                @RequestParam("resume_id") long resumeId,
                                @RequestParam("name") String name,
                                @RequestParam("template_id") long templateId,
                                @RequestParam("page_margin_horizontal") int pageMarginHorizontal,
                                @RequestParam("page_margin_vertical") int pageMarginVertical,
                                @RequestParam("module_margin") int moduleMargin,
                                @RequestParam("theme_color") String themeColor,
                                @RequestParam("font_size") int fontSize,
                                @RequestParam("font_family") String fontFamily,
                                @RequestParam("line_height") int lineHeight) {
        Resume resume = new Resume();
        resume.setId(resumeId);
        resume.setName(name);
        resume.setTemplateId(templateId);
        resume.setPageMarginHorizontal(pageMarginHorizontal);
        resume.setPageMarginVertical(pageMarginVertical);
        resume.setModuleMargin(moduleMargin);
        resume.setThemeColor(themeColor);
        resume.setFontSize(fontSize);
        resume.setFontFamily(fontFamily);
        resume.setLineHeight(lineHeight);
        return resumeService.updateResume(resume);
    }

    @Operation(summary = "根据模板，创建一个空白简历，简历中包含demo resume的所有内容")
    @PostMapping("/create")
    public Resume createBlankResume(@RequestHeader("uid") long uid,
                                    @RequestParam("template_id") long templateId)
            throws ResumeTemplateNotFoundException {
        return resumeService.createResumeFromTemplateDemo(uid, templateId);
    }

    @Operation(summary = "根据模板，以及用户上传的简历文件，创建一个简历")
    @PostMapping("/create-from-file")
    public Resume createResumeFromExistingFile(@RequestHeader("uid") long uid,
                                               @RequestParam("template_id") long templateId,
                                               @RequestParam("resume_file_url") String resumeFileUrl)
            throws ResumeTemplateNotFoundException {
        return resumeService.createResumeFromExistingFile(uid, templateId, resumeFileUrl);
    }

    @Operation(summary = "拷贝一份简历")
    @PostMapping("/copy")
    public Resume copyResume(@RequestHeader("uid") long uid,
                             @RequestParam("resume_id") long resumeId) {
        return resumeService.copyResume(uid, resumeId);
    }
}
