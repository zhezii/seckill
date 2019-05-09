package org.seckill.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Zhou Wenzhe
 * @date 2019/5/9
 */
@Data
public class SuccessKilled {

    private long seckillId;

    private long userPhone;

    private short state;

    private Date createTime;

    //变通
    //多对一
    private Seckill seckill;
}
