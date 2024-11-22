package com.xzgedu.supercv.user.service;

import com.xzgedu.supercv.user.domain.OkTest;
import com.xzgedu.supercv.user.repo.OkTestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OkTestService {

    @Autowired
    private OkTestRepo okTestRepo;

    public OkTest selectOkTest(long id) {
        return okTestRepo.selectOkTest(id);
    }

    public boolean insertOkTest(OkTest okTest) {
        return okTestRepo.insertOkTest(okTest);
    }

    public boolean deleteOkTest(long id) {
        return okTestRepo.deleteOkTest(id);
    }

    public boolean updateOkTest(OkTest okTest) {
        return okTestRepo.updateOkTest(okTest);
    }
}