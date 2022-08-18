package com.atguigu.srb.sms.service;

import java.util.Map;

/**
 * @author: dylan
 * Date:2022/7/21
 */
public interface SmsService {
    void send(String smsDTOMobile, String mobile, Map<String, Object> param);
}
