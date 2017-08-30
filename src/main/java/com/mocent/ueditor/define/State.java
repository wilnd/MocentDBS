package com.mocent.ueditor.define;

import java.util.Map;

/**
 * <p>
 * 处理状态接口
 * </p>
 * @author hadoop
 * @since 2017-08-20
 */
public interface State {

	public boolean isSuccess();

	public void putInfo(String name, String val);

	public void putInfo(String name, long val);

	public String toJSONString();
	
	public Map<String, Object> toJSONObject();

}
