package com.dk.microgis.error;

public enum ErrorCode implements BaseErrorInfoInterface {
    SUCCESS(0, "成功"),
    UNKNOWN_ERROR(1, "未知错误"),
    SERVICE_TEMPORARILY_UNAVAILABLE(2, "服务暂不可用"),
    UNSUPPORTED_OPENAPI_METHOD(3, "未知的方法"),
    OPEN_API_REQUEST_LIMIT_REACHED(4, "接口调用次数已达到设定的上限"),
    UNAUTHORIZED_CLIENT_IP_ADDRESS(5, "请求来自未经授权的IP地址"),
    NO_PERMISSION_TO_ACCESS_USER_DATA(6, "无权限访问该用户数据"),
    NO_PERMISSION_TO_ACCESS_DATA_FOR_THIS_REFERER(7, "来自该refer的请求无访问权限"),

    INVALID_PARAMETER(100, "请求参数无效"),
    INVALID_API_KEY(101, "api key无效"),
    SESSION_KEY_INVALID_OR_NO_LONGER_VALID(102, "session key无效"),
    INVALID_USED_CALL_ID_PARAMETER(103, "call_id参数无效"),
    INCORRECT_SIGNATURE(104, "无效签名"),
    TOO_MANY_PARAMETERS(105, "请求参数过多"),
    UNSUPPORTED_SIGNATURE_METHOD(106, "未知的签名方法"),
    INVALID_USED_TIMESTAMP_ARAMETER(107, "timestamp参数无效"),
    INVALID_USER_ID(108, "无效的user id"),
    INVALID_USER_INFO_FIELD(109, "无效的用户资料字段名"),
    ACCESS_TOKEN_INVALID_OR_NO_LONGER_VALID(110, "无效的access token"),


    ACCESS_TOKEN_EXPIRED(111, "access token过期"),
    SESSION_KEY_EXPIRED(112, "session key过期"),
    INVALID_IP(114, "无效的ip参数"),


    USER_NOT_VISIBLE(210, "用户不可见"),
    UNSUPPORTED_PERMISSION(210, "获取未授权的字段"),
    NO_PERMISSION_TO_ACCESS_USER_EMAIL(212, "没有权限获取用户的email"),




    UNKNOWN_DATA_STORE_API_ERROR(800, "未知的存储操作错误"),
    INVALID_OPERATION(801, "无效的操作方法"),
    DATA_STORE_ALLOWABLE_QUOTA_WAS_EXCEEDED(802, "数据存储空间已超过设定的上限"),
    SPECIFIED_OBJECT_CANNOT_BE_FOUND(803, "指定的对象不存在"),
    SPECIFIED_OBJECT_ALREADY_EXISTS(804, "指定的对象已存在"),
    A_DATABASE_ERROR_OCCURRED_PLEASE_TRY_AGAIN(805, "数据库操作出错，请重试"),


    NO_SUCH_APPLICATION_EXISTS(900, "访问的应用不存在"),
    BEGIN_BATCH_ALREADY_CALLED_PLEASE_MAKE_SURE_TO_CALL_END_BATCH_FIRST(950, "批量操作已开始，请先调用end_batch接口结束前一个批量操作"),
    END_BATCH_CALLED_BEFORE_START_BATCH(951, "结束批量操作的接口调用不应该在start_batch接口之前被调用"),
    EACH_BATCH_API_CAN_NOT_CONTAIN_MORE_THAN_20_ITEMS(952, "每个批量调用不能包含多于20个接口调用"),
    METHOD_IS_NOT_ALLOWED_IN_BATCH_MODE(953, "该接口不适合在批量调用操作中被使用"),


    DATA_IS_NULL(3005, "数据为空"),


    DATA_ERROR(4009, "数据有误");

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final int code;
    private final String msg;


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }


}
