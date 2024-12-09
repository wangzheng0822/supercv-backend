package com.xzgedu.supercv.resume.controller;

import com.xzgedu.supercv.resume.domain.ResumeModule;
import com.xzgedu.supercv.resume.domain.ResumeModuleItem;
import com.xzgedu.supercv.resume.service.ResumeModuleService;
import com.xzgedu.supercv.resume.service.ResumeModuleItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/resume/module/")
@Tag(name = "简历模块")
public class ResumeModuleController {

    @Autowired
    private ResumeModuleService resumeModuleService;

    @Autowired
    private ResumeModuleItemService resumeModuleItemService;

    @Operation(summary = "添加模块")
    @PostMapping("/add")
    public boolean addModule(@RequestHeader("uid") long uid,
                             @RequestParam("resume_id") long resumeId,
                             @RequestParam("title") String title,
                             @RequestParam(value = "sort_value", required = false) Integer sortValue,
                             @RequestParam(value = "default_module", required = false) Boolean defaultModule,
                             @RequestParam(value = "enabled", required = false) Boolean enabled) {
        ResumeModule module = new ResumeModule();
        module.setResumeId(resumeId);
        module.setTitle(title);
        module.setSortValue(sortValue);
        module.setDefaultModule(defaultModule);
        module.setEnabled(enabled);

        return resumeModuleService.addResumeModule(module);
    }

    @Operation(summary = "更新模块")
    @PutMapping("/update")
    public boolean updateModule(@RequestHeader("uid") long uid,
                                @RequestParam("resume_id") long resumeId,
                                @RequestParam("module_id") long moduleId,
                                @RequestParam("title") String title,
                                @RequestParam(value = "sort_value", required = false) Integer sortValue,
                                @RequestParam(value = "default_module", required = false) Boolean defaultModule,
                                @RequestParam(value = "enabled", required = false) Boolean enabled) {
        ResumeModule module = new ResumeModule();
        module.setId(moduleId);
        module.setResumeId(resumeId);
        module.setTitle(title);
        module.setSortValue(sortValue);
        module.setDefaultModule(defaultModule);
        module.setEnabled(enabled);
        return resumeModuleService.updateResumeModule(module);
    }

    @Operation(summary = "删除模块")
    @DeleteMapping("/delete")
    public boolean deleteModule(@RequestHeader("uid") long uid,
                                @RequestParam("resume_id") long resumeId,
                                @RequestParam("module_id") long moduleId) {
        return resumeModuleService.deleteResumeModule(moduleId);
    }

    @Operation(summary = "添加子模块")
    @PostMapping("/item/add")
    public boolean addModuleItem(@RequestHeader("uid") long uid,
                                 @RequestParam("resume_id") long resumeId,
                                 @RequestParam("module_id") long moduleId,
                                 @RequestParam("title_major") String titleMajor,
                                 @RequestParam(value = "title_minor", required = false) String titleMinor,
                                 @RequestParam(value = "title_date", required = false) String titleDate,
                                 @RequestParam(value = "title_sort_type", required = false) Integer titleSortType,
                                 @RequestParam(value = "title_major_enabled", required = false) Boolean titleMajorEnabled,
                                 @RequestParam(value = "title_minor_enabled", required = false) Boolean titleMinorEnabled,
                                 @RequestParam(value = "title_date_enabled", required = false) Boolean titleDateEnabled,
                                 @RequestParam("content") String content,
                                 @RequestParam(value = "sort_value", required = false) Integer sortValue) {
        ResumeModuleItem moduleItem = new ResumeModuleItem();
        moduleItem.setResumeId(resumeId);
        moduleItem.setModuleId(moduleId);
        moduleItem.setTitleMajor(titleMajor);
        moduleItem.setTitleMinor(titleMinor);
        moduleItem.setTitleDate(titleDate);
        moduleItem.setTitleSortType(titleSortType);
        moduleItem.setTitleMajorEnabled(titleMajorEnabled);
        moduleItem.setTitleMinorEnabled(titleMinorEnabled);
        moduleItem.setTitleDateEnabled(titleDateEnabled);
        moduleItem.setContent(content);
        moduleItem.setSortValue(sortValue);
        return resumeModuleItemService.addResumeModuleItem(moduleItem);
    }

    @Operation(summary = "更新子模块")
    @PutMapping("/item/update")
    public boolean updateModuleItem(@RequestHeader("uid") long uid,
                                    @RequestParam("resume_id") long resumeId,
                                    @RequestParam("module_item_id") long moduleItemId,
                                    @RequestParam("title_major") String titleMajor,
                                    @RequestParam(value = "title_minor", required = false) String titleMinor,
                                    @RequestParam(value = "title_date", required = false) String titleDate,
                                    @RequestParam(value = "title_sort_type", required = false) Integer titleSortType,
                                    @RequestParam(value = "title_major_enabled", required = false) Boolean titleMajorEnabled,
                                    @RequestParam(value = "title_minor_enabled", required = false) Boolean titleMinorEnabled,
                                    @RequestParam(value = "title_date_enabled", required = false) Boolean titleDateEnabled,
                                    @RequestParam("content") String content,
                                    @RequestParam(value = "sort_value", required = false) Integer sortValue) {
        ResumeModuleItem moduleItem = new ResumeModuleItem();
        moduleItem.setId(moduleItemId);
        moduleItem.setResumeId(resumeId);
        moduleItem.setTitleMajor(titleMajor);
        moduleItem.setTitleMinor(titleMinor);
        moduleItem.setTitleDate(titleDate);
        moduleItem.setTitleSortType(titleSortType);
        moduleItem.setTitleMajorEnabled(titleMajorEnabled);
        moduleItem.setTitleMinorEnabled(titleMinorEnabled);
        moduleItem.setTitleDateEnabled(titleDateEnabled);
        moduleItem.setContent(content);
        moduleItem.setSortValue(sortValue);
        return resumeModuleItemService.updateResumeModuleItem(moduleItem);
    }

    @Operation(summary = "删除子模块")
    @DeleteMapping("/item/delete")
    public boolean deleteModuleItem(@RequestHeader("uid") long uid,
                                    @RequestParam("resume_id") long resumeId,
                                    @RequestParam("module_item_id") long moduleItemId) {
        return resumeModuleItemService.deleteResumeModuleItem(moduleItemId);
    }
}
