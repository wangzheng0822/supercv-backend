package com.xzgedu.supercv.resume.service;

import com.xzgedu.supercv.resume.domain.ResumeBaseInfoItem;
import com.xzgedu.supercv.resume.repo.ResumeBaseInfoItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeBaseInfoItemService {

    @Autowired
    private ResumeBaseInfoItemRepo resumeBaseInfoItemRepo;

    public List<ResumeBaseInfoItem> getResumeBaseInfoItems(long baseInfoId) {
        return resumeBaseInfoItemRepo.getResumeBaseInfoItems(baseInfoId);
    }

    public boolean addResumeBaseInfoItem(ResumeBaseInfoItem item) {
        return resumeBaseInfoItemRepo.addResumeBaseInfoItem(item);
    }

    public boolean updateResumeBaseInfoItem(ResumeBaseInfoItem item) {
        return resumeBaseInfoItemRepo.updateResumeBaseInfoItem(item);
    }

    public boolean deleteResumeBaseInfoItem(long id) {
        return resumeBaseInfoItemRepo.deleteResumeBaseInfoItem(id);
    }
}
