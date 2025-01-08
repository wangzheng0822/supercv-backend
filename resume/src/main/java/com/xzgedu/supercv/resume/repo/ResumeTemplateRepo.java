package com.xzgedu.supercv.resume.repo;

import com.xzgedu.supercv.resume.domain.ResumeTemplate;
import com.xzgedu.supercv.resume.mapper.ResumeTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResumeTemplateRepo {

    @Autowired
    private ResumeTemplateMapper resumeTemplateMapper;

    public boolean addTemplate(ResumeTemplate template) {
        return resumeTemplateMapper.insertTemplate(template) == 1;
    }

    public boolean deleteTemplate(long id) {
        return resumeTemplateMapper.deleteTemplate(id) == 1;
    }

    public boolean updateTemplate(ResumeTemplate template) {
        return resumeTemplateMapper.updateTemplate(template) == 1;
    }

    public boolean updateDemoResumeId(long id, long demoResumeId) {
        return resumeTemplateMapper.updateDemoResumeId(id, demoResumeId) == 1;
    }

    public List<ResumeTemplate> getTemplatesPagination(int limitOffset, int limitSize) {
        return resumeTemplateMapper.selectTemplatesPagination(limitOffset, limitSize);
    }

    public int countTemplates() {
        return resumeTemplateMapper.countTemplates();
    }

    public ResumeTemplate getTemplateById(long id) {
        return resumeTemplateMapper.selectTemplateById(id);
    }
}
