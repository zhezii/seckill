package org.seckill.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Zhou Wenzhe
 * @date 2019/5/9
 */
@Data
public class Seckill {

    private long seckillId;

    private String name;

    private int number;

    private Date startTime;

    private Date endTime;

    private Date createTime;
}
