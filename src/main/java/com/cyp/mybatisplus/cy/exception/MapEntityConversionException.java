package com.cyp.mybatisplus.cy.exception;

/**
 * MapEntityConversionException
 *
 * @author cyp
 * @since 2025/5/20
 */
@SuppressWarnings("unused")
public class MapEntityConversionException extends RuntimeException {
    public MapEntityConversionException(String message) {
        super(message);
    }

    public MapEntityConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapEntityConversionException(Throwable cause) {
        super("无法将 Map 列表映射为实体类对象", cause);
    }
}
