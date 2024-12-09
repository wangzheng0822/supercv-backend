package com.xzgedu.supercv.vip.domain;

import com.xzgedu.supercv.common.anotation.ViewData;
import lombok.Data;

import java.util.Date;

@Data
public class Vip {
    private Long uid;
    @ViewData
    private String nickName;
    @ViewData
    private String headImgUrl;

    private Date expireTime;
    private Integer aiAnalysisLeftNum;
    private Integer aiOptimizationLeftNum;
    private Date updateTime;
}