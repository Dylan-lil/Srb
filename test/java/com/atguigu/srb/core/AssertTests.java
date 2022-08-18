package com.atguigu.srb.core;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import org.junit.jupiter.api.Test;

/**
 * @author dylan
 * Date: 2022/7/16
 * Description:
 */
public class AssertTests {

    @Test
    public void test1(){
        Object o = null;
        if (o == null){
            throw new IllegalArgumentException("参数错误");
        }
    }

    //断言的用法：更为简洁
    @Test
    public void test2() {
        // 另一种写法
        Object o = null;
        Assert.notNull(o, "用户不存在.");

    }
}