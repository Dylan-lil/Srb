package com.atguigu.srb.core;

import com.atguigu.common.util.MD5;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author dylan
 * Date: 2022/7/26
 * Description:
 */
public class MD5Tests {

    @Resource
    private RegisterVO registerVO;

    @Test
    public void test1(){
        System.out.println(MD5.encrypt("123456"));
    }
}