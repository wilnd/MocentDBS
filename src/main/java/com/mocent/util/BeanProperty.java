package com.mocent.util;

/**
 * <p>
 * Bean属性
 * </p>
 * @author hadoop
 * @since 2017-08-20
 */
public class BeanProperty {
	private final String name;
	private final Class<?> type;
	
	public BeanProperty(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public Class<?> getType() {
		return type;
	}
}
