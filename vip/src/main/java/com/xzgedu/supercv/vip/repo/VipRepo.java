package com.xzgedu.supercv.vip.repo;

import com.xzgedu.supercv.vip.domain.Vip;
import com.xzgedu.supercv.vip.mapper.VipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class VipRepo {

    @Autowired
    private VipMapper vipMapper;

    public boolean addVip(Vip vip) {
        return vipMapper.insertVip(vip) == 1;
    }

    public Vip getVipByUid(long uid) {
        return vipMapper.selectByUid(uid);
    }

    public boolean updateVip(Vip vip) {
        return vipMapper.updateVip(vip) == 1;
    }

    public List<Vip> getVipsByPagination(int limitOffset, int limitSize) {
        return vipMapper.selectVips(limitOffset, limitSize);
    }

    public int countVips(Date startTime, Date endTime) {
        return vipMapper.countVips(startTime, endTime);
    }
}
