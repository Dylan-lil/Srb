package com.atguigu.srb.sms;

import com.atguigu.srb.sms.service.SmsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dylan
 * Date: 2022/7/21
 * Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class SrbApplicationTests {
    @Resource
    private SmsService smsService;

    @Test
    void contextLoads() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "0022");
        //输入注册容联云的手机号测试
//        smsService.send(smsDTO.getMobile(), "18566439537", map);

    }
}