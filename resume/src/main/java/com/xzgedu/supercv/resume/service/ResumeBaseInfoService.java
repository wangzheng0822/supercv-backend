package com.xzgedu.supercv.resume.service;

import com.xzgedu.supercv.resume.domain.ResumeBaseInfo;
import com.xzgedu.supercv.resume.repo.ResumeBaseInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumeBaseInfoService {

    @Autowired
    private ResumeBaseInfoRepo resumeBaseInfoRepo;

    public ResumeBaseInfo getResumeBaseInfoById(long id) {
        return resumeBaseInfoRepo.getResumeBaseInfoById(id);
    }

    public ResumeBaseInfo getResumeBaseInfoByResumeId(long resumeId) {
        return resumeBaseInfoRepo.getResumeBaseInfoByResumeId(resumeId);
    }

    public boolean addResumeBaseInfo(ResumeBaseInfo baseInfo) {
        return resumeBaseInfoRepo.addResumeBaseInfo(baseInfo);
    }

    public boolean updateResumeBaseInfo(ResumeBaseInfo baseInfo) {
        return resumeBaseInfoRepo.updateResumeBaseInfo(baseInfo);
    }
}
