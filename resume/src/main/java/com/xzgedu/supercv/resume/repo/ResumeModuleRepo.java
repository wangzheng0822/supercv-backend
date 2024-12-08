package com.xzgedu.supercv.resume.repo;

import com.xzgedu.supercv.resume.domain.ResumeModule;
import com.xzgedu.supercv.resume.mapper.ResumeModuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResumeModuleRepo {

    @Autowired
    private ResumeModuleMapper resumeModuleMapper;

    public ResumeModule getResumeModuleById(long id) {
        return resumeModuleMapper.selectResumeModuleById(id);
    }

    public List<ResumeModule> getResumeModulesByResumeId(long resumeId) {
        return resumeModuleMapper.selectResumeModulesByResumeId(resumeId);
    }

    public boolean addResumeModule(ResumeModule module) {
        return resumeModuleMapper.insertResumeModule(module) == 1;
    }

    public boolean updateResumeModule(ResumeModule module) {
        return resumeModuleMapper.updateResumeModule(module) == 1;
    }

    public boolean deleteResumeModule(long id) {
        return resumeModuleMapper.deleteResumeModule(id) == 1;
    }
}
