package org.seckill.dto;

import lombok.Data;

/**
 * 暴露秒杀地址DTO
 * @author Zhou Wenzhe
 * @date 2019/5/11
 */
@Data
public class Exposer {

    //是否暴露接口地址
    private boolean isExpose;

    //加密措施
    private String md5;

    private long seckillId;

    //系统当前时间(毫秒)
    private long now;

    //开启时间
    private long start;

    //结束时间
    private long end;

    public Exposer(boolean isExpose, long seckillId) {
        this.isExpose = isExpose;
        this.seckillId = seckillId;
    }

    public Exposer(boolean isExpose, String md5, long seckillId) {
        this.isExpose = isExpose;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean isExpose,long seckillId, long now, long start, long end) {
        this.isExpose = isExpose;
        this.now = now;
        this.start = start;
        this.end = end;
        this.seckillId = seckillId;
    }
}
