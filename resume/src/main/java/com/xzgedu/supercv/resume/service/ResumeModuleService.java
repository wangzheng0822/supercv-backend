package com.xzgedu.supercv.resume.service;

import com.xzgedu.supercv.resume.domain.ResumeModule;
import com.xzgedu.supercv.resume.repo.ResumeModuleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeModuleService {

    @Autowired
    private ResumeModuleRepo resumeModuleRepo;

    public ResumeModule getResumeModuleById(long id) {
        return resumeModuleRepo.getResumeModuleById(id);
    }

    public List<ResumeModule> getResumeModulesByResumeId(long resumeId) {
        return resumeModuleRepo.getResumeModulesByResumeId(resumeId);
    }

    public boolean addResumeModule(ResumeModule module) {
        return resumeModuleRepo.addResumeModule(module);
    }

    public boolean updateResumeModule(ResumeModule module) {
        return resumeModuleRepo.updateResumeModule(module);
    }

    public boolean deleteResumeModule(long id) {
        return resumeModuleRepo.deleteResumeModule(id);
    }
}
