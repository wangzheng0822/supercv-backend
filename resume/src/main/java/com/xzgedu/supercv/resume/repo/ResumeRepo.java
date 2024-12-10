package com.xzgedu.supercv.resume.repo;

import com.xzgedu.supercv.resume.domain.Resume;
import com.xzgedu.supercv.resume.mapper.ResumeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResumeRepo {

    @Autowired
    private ResumeMapper resumeMapper;

    public Resume getResumeById(long id) {
        return resumeMapper.selectResumeById(id);
    }

    public List<Resume> getResumesPagination(int limitOffset, int limitSize) {
        return resumeMapper.selectResumesPagination(limitOffset, limitSize);
    }

    public List<Resume> getResumesByUid(long uid) {
        return resumeMapper.selectResumesByUid(uid);
    }

    public boolean addResume(Resume resume) {
        return resumeMapper.insertResume(resume) == 1;
    }

    public boolean deleteResume(long id) {
        return resumeMapper.deleteResume(id) == 1;
    }

    public boolean updateResume(Resume resume) {
        return resumeMapper.updateResume(resume) == 1;
    }
}
