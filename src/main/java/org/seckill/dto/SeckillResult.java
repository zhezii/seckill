package org.seckill.dto;

import lombok.Data;

/**
 * @author Zhou Wenzhe
 * @date 2019/5/11
 */
@Data
public class SeckillResult<T> {

    //是否成功
    private boolean success;

    private T data;

    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }
}
