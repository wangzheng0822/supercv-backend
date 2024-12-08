package com.xzgedu.supercv.resume.service;

import com.xzgedu.supercv.common.exception.ResumeTemplateNotFoundException;
import com.xzgedu.supercv.resume.domain.Resume;
import com.xzgedu.supercv.resume.domain.ResumeTemplate;
import com.xzgedu.supercv.resume.repo.ResumeRepo;
import com.xzgedu.supercv.resume.repo.ResumeTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepo resumeRepo;

    @Autowired
    private ResumeTemplateRepo resumeTemplateRepo;

    public Resume selectResumeById(long id) {
        return resumeRepo.getResumeById(id);
    }

    public List<Resume> getResumesPagination(int limitOffset, int limitSize) {
        return resumeRepo.getResumesPagination(limitOffset, limitSize);
    }

    public List<Resume> getResumesByUid(long uid) {
        return resumeRepo.getResumesByUid(uid);
    }

    public boolean addResume(Resume resume) {
        return resumeRepo.addResume(resume);
    }

    public boolean deleteResume(long id) {
        return resumeRepo.deleteResume(id);
    }

    /**
     * 改变简历标题、模板、主题颜色、间距等等
     */
    public boolean updateResume(Resume resume) {
        return resumeRepo.updateResume(resume);
    }


    /**
     * 根据模板，创建一个简历，简历中包含demo resume的所有内容
     */
    public Resume createResumeFromTemplateDemo(long uid, long templateId) throws ResumeTemplateNotFoundException {
        ResumeTemplate template = resumeTemplateRepo.getTemplateById(templateId);
        if (template == null || template.getDemoResumeId() == null) {
            throw new ResumeTemplateNotFoundException("Failed to find resume template: " + templateId);
        }
        return copyResume(uid, template.getDemoResumeId());
    }

    /**
     * 根据模板，以及用户上传的简历文件，创建一个简历
     */
    public Resume createResumeFromExistingFile(long uid, long templateId, String resumeFileUrl)
            throws ResumeTemplateNotFoundException {
        ResumeTemplate template = resumeTemplateRepo.getTemplateById(templateId);
        if (template == null || template.getDemoResumeId() == null) {
            throw new ResumeTemplateNotFoundException("Failed to find resume template: " + templateId);
        }

        //TODO：从示例简历直接将specific setting拷贝过来，然后创建default modules，
        // 解析文件，将解析后的内容存到对应的modules，没有对应的，新建module

        return null;
    }

    /**
     * 拷贝简历
     */
    public Resume copyResume(long uid, long resumeId) {
        //TODO: 复制一份简历，并返回
        return null;
    }

    /**
     * 查询完整的简历
     */
    public Resume getResumeDetail(long uid, long id) {
        //TODO: 包含模块、子模块等等内容
        return null;
    }
}
