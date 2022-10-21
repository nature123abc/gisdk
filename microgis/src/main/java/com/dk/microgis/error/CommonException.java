package com.dk.microgis.error;


/**
 * @author hq
 * @date 2021-04-14 13:49
 * @desc
 */
public class CommonException extends RuntimeException {
    /**
     * 错误码
     */
    protected int errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public CommonException() {
        super();
    }

    public CommonException(BaseErrorInfoInterface errorInfoInterface) {
        super(errorInfoInterface.getMsg());
        this.errorCode = errorInfoInterface.getCode();
        this.errorMsg = errorInfoInterface.getMsg();
    }

    public CommonException(Exception e) {
        super(e);
        this.errorCode = -1;
        this.errorMsg = e.getMessage();
    }

    public CommonException(BaseErrorInfoInterface errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getMsg(), cause);
        this.errorCode = errorInfoInterface.getCode();
        this.errorMsg = errorInfoInterface.getMsg();
    }

    public CommonException(String errorMsg) {
        super(errorMsg);
        this.errorCode = -1;
        this.errorMsg = errorMsg;
    }

    public CommonException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CommonException(int errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public String getErrorCode() {
        return "" + errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
