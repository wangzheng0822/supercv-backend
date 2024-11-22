package com.xzgedu.supercv.user.repo;

import com.xzgedu.supercv.user.domain.OkTest;
import com.xzgedu.supercv.user.mapper.OkTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OkTestRepo {

    @Autowired
    private OkTestMapper okTestMapper;

    public OkTest selectOkTest(long id) {
        return okTestMapper.selectOkTest(id);
    }

    public boolean insertOkTest(OkTest okTest) {
        return okTestMapper.insertOkTest(okTest) == 1;
    }

    public boolean deleteOkTest(long id) {
        return okTestMapper.deleteOkTest(id) == 1;
    }

    public boolean updateOkTest(OkTest okTest) {
        return okTestMapper.updateOkTest(okTest) == 1;
    }
}