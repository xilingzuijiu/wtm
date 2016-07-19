package com.weitaomi.systemconfig.exception;

/** 
 * @ClassName: BusinessExcepiton 
 * @Description:  业务异常的自定义封装类
 *  
 */
public class BusinessException extends BaseException {
	private static final long serialVersionUID = -2891888846711081199L;

	public BusinessException(String message) {
		super(message,new Throwable(message));
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
