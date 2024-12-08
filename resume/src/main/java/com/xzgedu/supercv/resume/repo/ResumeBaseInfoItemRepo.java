package com.xzgedu.supercv.resume.repo;

import com.xzgedu.supercv.resume.domain.ResumeBaseInfoItem;
import com.xzgedu.supercv.resume.mapper.ResumeBaseInfoItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResumeBaseInfoItemRepo {

    @Autowired
    private ResumeBaseInfoItemMapper resumeBaseInfoItemMapper;

    public List<ResumeBaseInfoItem> getResumeBaseInfoItems(long baseInfoId) {
        return resumeBaseInfoItemMapper.selectResumeBaseInfoItems(baseInfoId);
    }

    public boolean addResumeBaseInfoItem(ResumeBaseInfoItem item) {
        return resumeBaseInfoItemMapper.insertResumeBaseInfoItem(item) == 1;
    }

    public boolean updateResumeBaseInfoItem(ResumeBaseInfoItem item) {
        return resumeBaseInfoItemMapper.updateResumeBaseInfoItem(item) == 1;
    }

    public boolean deleteResumeBaseInfoItem(long id) {
        return resumeBaseInfoItemMapper.deleteResumeBaseInfoItem(id) == 1;
    }
}
