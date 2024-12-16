package com.xzgedu.supercv.admin.controller.resume;

import com.xzgedu.supercv.resume.domain.Resume;
import com.xzgedu.supercv.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "简历管理")
@RequestMapping("/admin/resume")
@RestController
public class AdminResumeController {

    @Autowired
    private ResumeService resumeService;

    @Operation(summary = "分页查询简历")
    @GetMapping("/list")
    public Map<String, Object> listResumes(@RequestParam("page_no") int pageNo,
                                           @RequestParam("page_size") int pageSize) {
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;
        List<Resume> resumes = resumeService.getResumesPagination(limitOffset, limitSize);
        int count = resumeService.countResumes();
        Map<String, Object> resp = new HashMap<>();
        resp.put("count", count);
        resp.put("resumes", resumes);
        return resp;
    }
}
