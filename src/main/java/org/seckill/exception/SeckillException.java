package org.seckill.exception;

/**
 * @author Zhou Wenzhe
 * @date 2019/5/11
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
