package org.jeecg.modules.demo.test01.service.impl;

import org.jeecg.modules.demo.test01.entity.Test01;
import org.jeecg.modules.demo.test01.mapper.Test01Mapper;
import org.jeecg.modules.demo.test01.service.ITest01Service;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 第一个测试
 * @Author: jeecg-boot
 * @Date:   2024-10-31
 * @Version: V1.0
 */
@Service
public class Test01ServiceImpl extends ServiceImpl<Test01Mapper, Test01> implements ITest01Service {

}
