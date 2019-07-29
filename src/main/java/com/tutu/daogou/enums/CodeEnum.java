package com.tutu.daogou.enums;



/**
 * Created by James on 16/8/12.
 */
public enum CodeEnum {

    // 系统级
    SUCCESS(10200, "成功"),
    FAIL(10400, "失败"),
    SERVER_BUSY(10404, "服务器繁忙"),
    DEAL_RESULT_ERROR(10405, "结果处理失败"),
    SERVER_PROCESS_ERROR(10002,"服务器处理错误"),
    NOT_LOGIN(10801,"未登陆"),

    // 数据库级
    DB_OPERATOR_ERROR(20001, "DB_OPERATOR_ERROR"),
    DB_ADD_ERROR(20002, "DB_ADD_ERROR"),
    DB_MODIFY_ERROR(20003, "DB_MODIFY_ERROR"),
    DB_DELETE_ERROR(20004, "DB_DELETE_ERROR"),
    DB_SELECT_ERROR(20005, "DB_SELECT_ERROR"),
    DB_NO_RECORD_CHANGED(20006, "DB_NO_RECORD_CHANGED"),
    DB_NO_RECORD_SELECTED(20007, "DB_NO_RECORD_SELECTED"),

    // 参数级
    PARAM_NOT_NULL(30401, "参数不能为空"),
    PARAM_ICORRECT(30402, "参数错误"),
    PARAM_FORMAT_ICORRECT(30403, "参数格式错误"),
    PARAM_NOT_NULL_OR_PARAM_ERROR(30404, "参数不能为空或参数错误"),

    // 格式校验
    PHONE_FORMAT_ERROR(50406, "手机号格式错误"),
    CAPTCHA_FORMAT_ERROR(50407, "验证码格式不正确"),

    //逻辑校验
//    NOT_FIND_APP(40000, "NOT_FIND_APP"),
    NO_FIND_USER(40001, "用户不存在"),
    USER_AUTH_FINISH(40002, "用户授权已完成"),
    CODE_NO_CORRECT(40003, "验证码不正确"),
    CODE_EXPIRE(40004, "验证码已超时"),
//    NO_APP_INFO(40005, "NO_APP_INFO"),
//    NO_FIND_DATA(40006, "NO_FIND_DATA"),
//    DATA_IS_EXSITS(40007, "DATA_IS_EXSITS"),


    //用户权限
    USER_NOT_EXSITS(70001,"用户不存在"),
    PASSWORD_WRONG(70002,"密码错误"),
    USER_EXSITS(70003,"用户已存在"),
    COPYRECORD_NOT_EXSITS(70004,"商品记录不存在"),
    DELIVERYADDRESS_NOT_EXSITS(70005,"收货地址不存在"),
    ORDER_NOT_EXSITS(70006,"订单不存在"),
    AUTHORIZATION_FAIL(70007,"授权失败"),
    GETSCORE_FAIL(70008,"获取分数失败"),
    USERCREDIT_NOT_EXSITS(70009,"用户未授信"),
    USER_NOT_FACE_VERIFY(70010, "未作人脸识别"),
    USER_FACE_VERIFY_FAIL(70011, "人脸识别失败"),
    USER_NOT_CREDITE(70012, "用户无授信额度"),
    USER_AUDIT_NOT_FINISH(70013, "用户审核尚未完成"),
    USER_AUDIT_PERIOD_WAIT(70014, "用户审核周期未到"),
    USER_IDCARD_EXISTS(70015,"该身份证已绑定其他手机号，请联系客服处理"),
    USER_NOT_ALLOWANCECARD(70016, "未授信津贴卡"),
    USER_NOT_STAGINGCARD(70017, "未授信分期卡"),
    INVITED_USER_NOT_EXSITS(70018,"邀请用户不存在,请检查邀请码"),
    WEXIN_EXSITS(70019,"微信已绑定其他用户"),
    PHONE_EXSITS(70020,"手机号已经绑定其他微信号"),
    WEXIN_NOT_EXSITS(70021,"微信未绑定用户"),
    // 银行卡绑定
    USER_NOT_CARD(80001, "用户未绑定银行卡"),

    // 还款计划.
//    USER_REPAY_PLAN_EXSITS(50001, "用户已有未还完的分期账单"),
    USER_REPAY_PLAN_EXSITS(50001, "用户已有分期账单"),
    USER_CREDIT_NOT_ENOUGH(50002, "用户分期卡额度不足"),
    USER_BILL_EXSITS(50003, "用户账单存在"),
    USER_ORDER_NOT_EXSITS(50004, "用户没有此订单"),
    USER_ORDER_IS_DYA_LIMIT(50005, "活动期内下单名额有限制，今天已满，请明日再来~"),
    USER_ALLOWANCECARD_NOT_ENOUGH(50003, "用户津贴卡额度不足"),

    //优惠券
    COUPON_NOT_EXSITS(60001,"优惠券不存在"),
    COUPON_NOT_FOR_USER(60002,"优惠券不属于此用户"),
    COUPON_NOT_VALID(60003,"优惠券不在有效期内"),
    COUPON_NOT_SUPPORT(60004, "优惠券不支持叠加"),
    COUPON_NOT_SUPPORT_MANY(60005, "优惠券只支持使用一张"),
    COUPON_NOT_FULL_REDUCTION(60006,"优惠券不符合满减条件"),
    COUPON_EXCEEDS_COMMODITY_PRICE(60007, "优惠券总额超过了商品价格"),
    COUPON_NOT_SOURCE(60008, "优惠券不能叠加"),


    //抓取
    URL_NOT_SUPPORT(90001, "不支持该链接"),

    PLATFORM_NOT_SUPPORT(90002, "不支持该平台"),
    ;
    public String note;
    public Integer code;
    public String languageKey; //对应翻译的key

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    private CodeEnum(Integer code, String note) {
        this.code = code;
        this.note = note;
    }

    public static String getByCode(Integer code ) {
        CodeEnum[] enums = CodeEnum.values();
        for (int i = 0; i < enums.length; i++) {
            if (enums[i].getCode().intValue() == code) {
                return enums[i].getNote();
            }
        }
        return "";
    }
}
