package org.seckill.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Zhou Wenzhe
 * @date 2019/5/11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
@Slf4j
public class SeckillServiceTest {

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> seckillList = seckillService.getSeckillList();
        log.info("seckillList={}",seckillList);
    }

    @Test
    public void queryById() {
        long id = 1000;
        Seckill seckill = seckillService.queryById(id);
        log.info("seckill={}",seckill);
    }

    @Test
    public void testSeckillLogic() {
        Exposer exposer = seckillService.exportSeckillUrl(1000L);
        if (exposer.isExposed()) {
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(1000L, 17638167592L, exposer.getMd5());
                log.info("seckillExecution={}" + seckillExecution);
            } catch (RepeatKillException e) {
                log.error(e.getMessage());
            } catch (SeckillCloseeException e) {
                log.error(e.getMessage());
            }
        }else {
            //秒杀未开启
            log.info("exposer={}",exposer);
        }
    }

}