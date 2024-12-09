package com.xzgedu.supercv.vip.service;

import com.xzgedu.supercv.vip.domain.Vip;
import com.xzgedu.supercv.vip.repo.VipRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VipService {

    @Autowired
    private VipRepo vipRepo;

    public boolean permitValidVip(long uid) {
        Vip vip = vipRepo.getVipByUid(uid);
        return this.checkIfValidVip(vip);
    }

    public boolean permitAiAnalysis(long uid) {
        Vip vip = vipRepo.getVipByUid(uid);

        //拥有VIP并且没有过期
        boolean isValidVip = checkIfValidVip(vip);
        if (!isValidVip) return false;

        //检查是否还有剩余次数
        return vip.getAiAnalysisLeftNum() > 0;
    }

    public boolean permitAiOptimization(long uid) {
        Vip vip = vipRepo.getVipByUid(uid);

        //拥有VIP并且没有过期
        boolean isValidVip = checkIfValidVip(vip);
        if (!isValidVip) return false;

        //检查是否还有剩余次数
        return vip.getAiOptimizationLeftNum() > 0;
    }

    public Vip getVipInfo(Long uid) {
        if (uid == null) return null;
        return vipRepo.getVipByUid(uid);
    }

    public boolean renewVip(long uid, int days, int aiAnalysisNum, int aiOptimizationNum) {
        int aiAnalysisLeftNum = 0;
        int aiOptimizationLeftNum = 0;
        long expireTimeInMillis = System.currentTimeMillis();
        Vip oldVip = vipRepo.getVipByUid(uid);
        if (oldVip != null && oldVip.getExpireTime().after(new Date())) {
            aiAnalysisLeftNum = oldVip.getAiAnalysisLeftNum();
            aiOptimizationLeftNum = oldVip.getAiOptimizationLeftNum();
            expireTimeInMillis = oldVip.getExpireTime().getTime();
        }
        Vip vip = new Vip();
        vip.setUid(uid);
        vip.setAiAnalysisLeftNum(aiAnalysisLeftNum + aiAnalysisNum);
        vip.setAiOptimizationLeftNum(aiOptimizationLeftNum + aiOptimizationNum);
        long millis = days * 3600l * 24l * 1000l;
        vip.setExpireTime(new Date(expireTimeInMillis + millis));
        if (oldVip == null) {
            return vipRepo.addVip(vip);
        } else {
            return vipRepo.updateVip(vip);
        }
    }

    private boolean checkIfValidVip(Vip vip) {
        //检查是否拥有vip
        if (vip == null) return false;

        //检查是否过期
        Date expireTime = vip.getExpireTime();
        if (expireTime == null || expireTime.before(new Date())) {
            return false;
        }

        return true;
    }

   public List<Vip> getVipsByPagination(int limitOffset, int limitSize) {
        return vipRepo.getVipsByPagination(limitOffset, limitSize);
    }

    public int countVips(Date startTime, Date endTime) {
        return vipRepo.countVips(startTime, endTime);
    }
}