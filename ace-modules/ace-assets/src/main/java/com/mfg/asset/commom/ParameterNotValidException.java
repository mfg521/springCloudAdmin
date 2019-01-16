package com.mfg.asset.commom;

/**
 * Create By mengfanguang on 2017/12/18 17:58
 */
public class ParameterNotValidException extends CustomRuntimeException {

    /**
     * 参数校验异常的基数
     */
    private static final Long ERRCODE_MIN = 1000_000L;

    public ParameterNotValidException(Long errcode, String message) {
        super(1000_000L + errcode, message);
    }

    public ParameterNotValidException(String message) {
        super(1000_000L, message);
    }
}
