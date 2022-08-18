package com.atguigu.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author dylan
 * Date: 2022/7/31
 * Description:
 */
@Data
@ApiModel(description = "投标信息")
public class InvestVO {

    private Long lendId;

    //投标金额
    private String investAmount;

    //用户id
    private Long investUserId;

    //用户姓名
    private String investName;
}