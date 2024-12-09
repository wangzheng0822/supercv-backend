package com.xzgedu.supercv.vip.mapper;

import com.xzgedu.supercv.vip.domain.Vip;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface VipMapper {
    @Results(id = "Vip", value = {
            @Result(property = "uid", column = "uid"),
            @Result(property = "expireTime", column = "expire_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "aiAnalysisLeftNum", column = "ai_analysis_left_num"),
            @Result(property = "aiOptimizationLeftNum", column = "ai_optimization_left_num"),
    })
    @Select("SELECT * FROM vip WHERE uid = #{uid}")
    Vip selectByUid(@Param("uid") long uid);

    @Insert("INSERT INTO vip(uid, expire_time, ai_analysis_left_num, ai_optimization_left_num) " +
            "VALUES(#{uid}, #{expireTime}, #{aiAnalysisLeftNum}, #{aiOptimizationLeftNum})")
    int insertVip(Vip vip);

    @Update("UPDATE vip SET expire_time = #{expireTime}, ai_analysis_left_num = #{aiAnalysisLeftNum}, " +
            "ai_optimization_left_num = #{aiOptimizationLeftNum} WHERE uid = #{uid}")
    int updateVip(Vip vip);

    @ResultMap("Vip")
    @Select("select * from vip order by update_time desc limit #{limitOffset}, #{limitSize}")
    List<Vip> selectVips(@Param("limitOffset") int limitOffset,
                               @Param("limitSize") int limitSize);

    @SelectProvider(type = VipProvider.class, method = "countVips")
    int countVips(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
