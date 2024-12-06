package com.xzgedu.supercv.admin.service;

import com.xzgedu.supercv.admin.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    public boolean checkIfAdmin(long uid) {
        return adminRepo.checkIsAdmin(uid);
    }

    public boolean grantAdmin(long uid) {
        return adminRepo.grantAdmin(uid);
    }

    public boolean deleteAdmin(long uid) {
        return adminRepo.deleteAdmin(uid);
    }
}
