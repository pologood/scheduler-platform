package com.sncfc.scheduler.server.pojo;


import java.util.HashMap;
import java.util.Map;

/**
 * 增、删、改操作结果
 * @author xuhuan
 *
 */
public class BaseResultBean  {
	protected boolean success;
	protected String errorCode;
	protected String errorMsg;

	protected Map result = new HashMap();

    public Map getResult() {
        return result;
    }

    public void setResult(Map result) {
        result.putAll(result);
    }
    public void setResult(Pager pager) {
        result.put("pager",pager);
    }
    public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

    public void success() {
        this.success = true;
    }
    public void failure() {
        this.success = false;
    }
}
