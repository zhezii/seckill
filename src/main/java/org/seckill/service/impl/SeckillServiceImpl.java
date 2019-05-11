package org.seckill.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseeException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @author Zhou Wenzhe
 * @date 2019/5/11
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    private final String salt = "sdfsdfs$%^&%&";

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill queryById(long seckillId) {
        return seckillDao.queryId(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {

        Seckill seckill = seckillDao.queryId(seckillId);

        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();

        Date nowTime = new Date();

        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        return new Exposer(true,getMD5(seckillId),seckillId);
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点
     * 1:开发团队达成一致约定，明确标注事务方法的编程风格
     * 2:保证事务方法的执行时间尽可能短，不要穿插其他网络的操作
     * 3:不是所有的方法都需要事务,如只有一条修改操作,只读操作
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws RepeatKillException, SeckillException, SeckillCloseeException {
        if (md5 == null || !getMD5(seckillId).equals(md5)) {
            throw new SeckillException("seckill data rewrite");
        }
        try {
            //执行秒杀逻辑:减库存+记录购买行为
            int updateCount = seckillDao.reduceNumber(seckillId, new Date());
            if (updateCount <= 0) {
                //秒杀已结束
                throw new SeckillCloseeException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }

            }
        } catch (SeckillCloseeException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //所有编译器异常转化为运行期异常
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }

    private String getMD5(Long seckillId){
        String base = seckillId + "/" +salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
