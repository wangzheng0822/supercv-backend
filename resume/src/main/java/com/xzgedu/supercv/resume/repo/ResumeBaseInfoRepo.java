package com.xzgedu.supercv.resume.repo;

import com.xzgedu.supercv.resume.domain.ResumeBaseInfo;
import com.xzgedu.supercv.resume.mapper.ResumeBaseInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ResumeBaseInfoRepo {

    @Autowired
    private ResumeBaseInfoMapper resumeBaseInfoMapper;

    public ResumeBaseInfo getResumeBaseInfoById(long id) {
        return resumeBaseInfoMapper.selectResumeBaseInfoById(id);
    }

    public ResumeBaseInfo getResumeBaseInfoByResumeId(long resumeId) {
        return resumeBaseInfoMapper.selectResumeBaseInfoByResumeId(resumeId);
    }

    public boolean addResumeBaseInfo(ResumeBaseInfo baseInfo) {
        return resumeBaseInfoMapper.insertResumeBaseInfo(baseInfo) == 1;
    }

    public boolean updateResumeBaseInfo(ResumeBaseInfo baseInfo) {
        return resumeBaseInfoMapper.updateResumeBaseInfo(baseInfo) == 1;
    }
}
