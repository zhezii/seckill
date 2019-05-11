package org.seckill.exception;

/**
 * 秒杀关闭异常
 * @author Zhou Wenzhe
 * @date 2019/5/11
 */
public class SeckillCloseeException extends SeckillException {

    public SeckillCloseeException(String message) {
        super(message);
    }

    public SeckillCloseeException(String message, Throwable cause) {
        super(message, cause);
    }
}
