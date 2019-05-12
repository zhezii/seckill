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
    private boolean exposed;

    //加密措施
    private String md5;

    private long seckillId;

    //系统当前时间(毫秒)
    private long now;

    //开启时间
    private long start;

    //结束时间
    private long end;

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed,long seckillId, long now, long start, long end) {
        this.exposed = exposed;
        this.now = now;
        this.start = start;
        this.end = end;
        this.seckillId = seckillId;
    }
}
