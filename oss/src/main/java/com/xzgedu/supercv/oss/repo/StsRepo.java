package com.xzgedu.supercv.oss.repo;

import com.xzgedu.supercv.oss.mapper.StsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StsRepo {
    @Autowired
    private StsMapper stsMapper;

    public StsToken getStsToken(long uid) {
        return stsMapper.getStsToken(uid);
    }

    public boolean addStsToken(StsToken stsToken) {
        return stsMapper.insertStsToken(stsToken) == 1;
    }

    public boolean updateStsToken(StsToken stsToken) {
        return stsMapper.updateStsToken(stsToken) == 1;
    }

    public boolean deleteStsToken(long uid) {
        return stsMapper.deleteStsToken(uid) == 1;
    }
}
