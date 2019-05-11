package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * 接口测试类
 * 1.配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test，junit
 *
 * @author Zhou Wenzhe
 * @date 2019/5/10
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() {
        long seckillID = 1000;
        Date killTime = new Date();
        int i = seckillDao.reduceNumber(seckillID, killTime);
        System.out.println(i);
    }

    @Test
    public void queryId() {
        long seckillId = 1000;
        Seckill seckill = seckillDao.queryId(seckillId);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {

        List<Seckill> seckills = seckillDao.queryAll(0, 10);
        System.out.println(seckills);
    }
}