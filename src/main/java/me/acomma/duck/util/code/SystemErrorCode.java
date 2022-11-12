package me.acomma.duck.util.code;

/**
 * 系统错误码定义为整数，取值范围为 0 ~ 99。
 */
public enum SystemErrorCode implements ErrorCode<Integer> {
    /**
     * 请求执行成功，没有出现错误或异常时使用的错误码。
     */
    SUCCESS(0, "操作成功"),

    /**
     * 作为兜底的错误码，但是要防止滥用。
     */
    SYSTEM_ERROR(1, "系统错误"),

    /**
     * 一般是用户传入的参数非法引起的，请仔细检查入参格式、范围是否一一对应。
     */
    INVALID_PARAMETER(2, "参数无效"),

    /**
     * 连接数据库失败或者执行 SQL 失败时使用的错误码。
     */
    ACCESS_DATABASE_FAILED(3, "访问数据库失败"),

    /**
     * 解析或生成 JSON 格式数据失败时使用的错误码。
     */
    PROCESS_JSON_FAILED(4, "处理 JSON 数据失败");

    /**
     * 错误码。
     */
    private final Integer code;

    /**
     * 错误消息。
     */
    private final String message;

    SystemErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer value() {
        return code();
    }

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
