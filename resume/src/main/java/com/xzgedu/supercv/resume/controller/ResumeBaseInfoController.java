package com.xzgedu.supercv.resume.controller;

import com.xzgedu.supercv.resume.domain.ResumeBaseInfo;
import com.xzgedu.supercv.resume.domain.ResumeBaseInfoItem;
import com.xzgedu.supercv.resume.service.ResumeBaseInfoItemService;
import com.xzgedu.supercv.resume.service.ResumeBaseInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/resume/baseinfo/")
@Tag(name = "简历基本信息")
public class ResumeBaseInfoController {

    @Autowired
    private ResumeBaseInfoService resumeBaseInfoService;

    @Autowired
    private ResumeBaseInfoItemService resumeBaseInfoItemService;

    @Operation(summary = "更新基本信息")
    @PostMapping("/update")
    public boolean updateBaseInfo(@RequestHeader("uid") long uid,
                                  @RequestParam("resume_id") long resumeId,
                                  @RequestParam(value = "head_img_url") String headImgUrl,
                                  @RequestParam(value = "head_img_enabled") Boolean headImgEnabled,
                                  @RequestParam(value = "head_img_layout") Integer headImgLayout,
                                  @RequestParam(value = "item_layout") Integer itemLayout,
                                  @RequestParam(value = "enabled") Boolean enabled) {
        ResumeBaseInfo baseInfo = new ResumeBaseInfo();
        baseInfo.setResumeId(resumeId);
        baseInfo.setHeadImgUrl(headImgUrl);
        baseInfo.setHeadImgEnabled(headImgEnabled);
        baseInfo.setHeadImgLayout(headImgLayout);
        baseInfo.setItemLayout(itemLayout);
        baseInfo.setEnabled(enabled);
        return resumeBaseInfoService.updateResumeBaseInfo(baseInfo);
    }

    @Operation(summary = "添加基本信息项")
    @PostMapping("/item/add")
    public boolean addBaseInfoItem(@RequestHeader("uid") long uid,
                                   @RequestParam("resume_id") long resumeId,
                                   @RequestParam("baseinfo_id") long baseInfoId,
                                   @RequestParam("key") String key,
                                   @RequestParam("value") String value,
                                   @RequestParam("sort_value") Integer sortValue) {
        ResumeBaseInfoItem item = new ResumeBaseInfoItem();
        item.setBaseInfoId(baseInfoId);
        item.setKey(key);
        item.setValue(value);
        item.setSortValue(sortValue);
        return resumeBaseInfoItemService.addResumeBaseInfoItem(item);
    }

    @Operation(summary = "更新基本信息项")
    @PostMapping("/item/update")
    public boolean updateBaseInfoItem(@RequestHeader("uid") long uid,
                                      @RequestParam("resume_id") long resumeId,
                                      @RequestParam("baseinfo_id") long baseInfoId,
                                      @RequestParam("item_id") long itemId,
                                      @RequestParam("key") String key,
                                      @RequestParam("value") String value,
                                      @RequestParam("sort_value") Integer sortValue) {
        ResumeBaseInfoItem item = new ResumeBaseInfoItem();
        item.setId(itemId);
        item.setBaseInfoId(baseInfoId);
        item.setKey(key);
        item.setValue(value);
        item.setSortValue(sortValue);
        return resumeBaseInfoItemService.updateResumeBaseInfoItem(item);
    }

    @Operation(summary = "删除基本信息项")
    @PostMapping("/item/delete")
    public boolean deleteBaseInfoItem(@RequestHeader("uid") long uid,
                                      @RequestParam("resume_id") long resumeId,
                                      @RequestParam("item_id") long itemId) {
        return resumeBaseInfoItemService.deleteResumeBaseInfoItem(itemId);
    }
}
