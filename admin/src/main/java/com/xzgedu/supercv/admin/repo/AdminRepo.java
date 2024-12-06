package com.xzgedu.supercv.admin.repo;

import com.xzgedu.supercv.admin.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepo {

    @Autowired
    private AdminMapper adminMapper;

    public boolean checkIsAdmin(long uid) {
        return adminMapper.selectAdminByUid(uid) != null;
    }

    public boolean grantAdmin(long uid) {
        return adminMapper.insertAdmin(uid) == 1;
    }

    public boolean deleteAdmin(long uid) {
        return adminMapper.deleteAdmin(uid) == 1;
    }
}
