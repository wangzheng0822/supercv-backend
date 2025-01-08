package com.xzgedu.supercv.resume.service;

import com.xzgedu.supercv.resume.domain.ResumeTemplate;
import com.xzgedu.supercv.resume.repo.ResumeTemplateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeTemplateService {

    @Autowired
    private ResumeTemplateRepo resumeTemplateRepo;

    public boolean addTemplate(ResumeTemplate template) {
        return resumeTemplateRepo.addTemplate(template);
    }

    public boolean deleteTemplate(long id) {
        return resumeTemplateRepo.deleteTemplate(id);
    }

    public boolean updateTemplate(ResumeTemplate template) {
        return resumeTemplateRepo.updateTemplate(template);
    }

    public boolean updateDemoResumeId(long id, long demoResumeId) {
        return resumeTemplateRepo.updateDemoResumeId(id, demoResumeId);
    }

    public List<ResumeTemplate> getTemplatesPagination(int limitOffset, int limitSize) {
        return resumeTemplateRepo.getTemplatesPagination(limitOffset, limitSize);
    }

    public int countTemplates() {
        return resumeTemplateRepo.countTemplates();
    }

    public ResumeTemplate getTemplateById(long id) {
        return resumeTemplateRepo.getTemplateById(id);
    }
}
