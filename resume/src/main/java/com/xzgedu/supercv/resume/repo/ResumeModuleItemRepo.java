package com.xzgedu.supercv.resume.repo;

import com.xzgedu.supercv.resume.domain.ResumeModuleItem;
import com.xzgedu.supercv.resume.mapper.ResumeModuleItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResumeModuleItemRepo {

    @Autowired
    private ResumeModuleItemMapper resumeModuleItemMapper;

    public List<ResumeModuleItem> getResumeModuleItemsByModuleId(long moduleId) {
        return resumeModuleItemMapper.selectResumeModuleItemsByModuleId(moduleId);
    }

    public List<ResumeModuleItem> getResumeModuleItemsByModuleIds(List<Long> moduleIds) {
        return resumeModuleItemMapper.selectResumeModuleItemsByModuleIds(moduleIds);
    }

    public boolean addResumeModuleItem(ResumeModuleItem moduleItem) {
        return resumeModuleItemMapper.insertResumeModuleItem(moduleItem) == 1;
    }

    public boolean updateResumeModuleItem(ResumeModuleItem moduleItem) {
        return resumeModuleItemMapper.updateResumeModuleItem(moduleItem) == 1;
    }

    public boolean deleteResumeModuleItem(long id) {
        return resumeModuleItemMapper.deleteResumeModuleItem(id) == 1;
    }
}
