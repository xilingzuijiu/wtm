package com.weitaomi.systemconfig.dataFormat;


import com.weitaomi.systemconfig.constant.ActionConstants;
import com.weitaomi.systemconfig.exception.ResultCode;

import java.io.Serializable;

/**
 * AJAX调用返回对象
 * Created by Jemry.Liu on 2015/4/8.
 */
public class AjaxResult implements Serializable {

    /**
     * 请求结果是否成功
     */
    private String ErrorCode = ResultCode.SUCCESS.getCode();

    /**
     * 请求返回信息
     */
    private String Message = ActionConstants.DEFAULT_SUCCESS_RETURNMSG;

    /**
     * 请求结果
     */
    private Object Data = null;


    /**
     * Instantiates a new Ajax result.
     */
    public AjaxResult(){}

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    /**
     * 获取正确结果模板
     *
     * @param message 请求返回信息
     * @param obj 请求结果
     * @return AjaxResult
     */
    public static AjaxResult getOK(String message, Object obj){
        AjaxResult result = new AjaxResult();
        result.setMessage(message);
        result.setData(obj);
        return result;
    }
    /**
     * 获取正确结果模板
     *
     * @param obj 请求结果
     * @return AjaxResult
     */
    public static AjaxResult getOK(Object obj){
        AjaxResult result = new AjaxResult();
        result.setMessage(ActionConstants.DEFAULT_SUCCESS_RETURNMSG);
        result.setData(obj);
        return result;
    }

    /**
     * 获取错误结果模板
     *
     * @param message 请求返回信息
     * @param obj 请求结果
     * @return AjaxResult
     */
    public static AjaxResult getError(ResultCode errorCode, String message, Object obj){
        AjaxResult result = new AjaxResult();
        result.setErrorCode(errorCode.getCode());
        result.setMessage(message);
        result.setData(obj);
        return result;
    }

    /**
     * 获取正确结果模板
     *
     * @return AjaxResult
     */
    public static AjaxResult getOK(){
        return getOK(ActionConstants.DEFAULT_SUCCESS_RETURNMSG,null);
    }
    /**
     * 获取错误结果模板
     *
     * @return AjaxResult
     */
    public static AjaxResult getError(){
        return getOK(ActionConstants.DEFAULT_FAILED_RETURNMSG,null);
    }


    /**
     * 获取错误结果模板
     *
     * @return AjaxResult
     */
    public static AjaxResult getError(ResultCode resultCode){
        return getError(resultCode, resultCode.getMsg(), "");
    }

    @Override
    public String toString() {
        return "AjaxResult{" +
                "ErrorCode='" + ErrorCode + '\'' +
                ", Message='" + Message + '\'' +
                ", Data=" + Data +
                '}';
    }
}
