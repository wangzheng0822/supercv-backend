package com.xzgedu.supercv.oss.service;

import com.xzgedu.supercv.oss.repo.StsToken;
import com.xzgedu.supercv.oss.repo.StsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StsService {
    @Autowired
    private StsRepo stsRepo;

    @Autowired
    private StsClient stsClient;

    public StsToken getStsToken(Long uid) {
        if (uid == null) {
            return stsClient.requestStsToken(null);
        }

        StsToken stsToken = stsRepo.getStsToken(uid);
        if (stsToken != null && stsToken.getExpiration()>System.currentTimeMillis()+600000l) {
            return stsToken;
        }
        StsToken newStsToken = stsClient.requestStsToken(uid);
        newStsToken.setUid(uid);
        if (stsToken == null) {
            stsRepo.addStsToken(newStsToken);
        } else {
            stsRepo.updateStsToken(newStsToken);
        }
        return newStsToken;
    }
}
