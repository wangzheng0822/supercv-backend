package com.xzgedu.supercv.resume.service;

import com.xzgedu.supercv.resume.domain.ResumeModuleItem;
import com.xzgedu.supercv.resume.repo.ResumeModuleItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeModuleItemService {

    @Autowired
    private ResumeModuleItemRepo resumeModuleItemRepo;

    public List<ResumeModuleItem> getResumeModuleItemsByModuleId(long moduleId) {
        return resumeModuleItemRepo.getResumeModuleItemsByModuleId(moduleId);
    }

    public List<ResumeModuleItem> getResumeModuleItemsByModuleIds(List<Long> moduleIds) {
        return resumeModuleItemRepo.getResumeModuleItemsByModuleIds(moduleIds);
    }

    public boolean addResumeModuleItem(ResumeModuleItem subModule) {
        return resumeModuleItemRepo.addResumeModuleItem(subModule);
    }

    public boolean updateResumeModuleItem(ResumeModuleItem subModule) {
        return resumeModuleItemRepo.updateResumeModuleItem(subModule);
    }

    public boolean deleteResumeModuleItem(long id) {
        return resumeModuleItemRepo.deleteResumeModuleItem(id);
    }
}