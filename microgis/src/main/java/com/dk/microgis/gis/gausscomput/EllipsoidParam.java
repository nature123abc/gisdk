/**
 * Copyright 2020--2020
 * Zhongtie Trimble Digital Engineering and Construction Co., Ltd
 *
 * @description  定义或设置椭球参数
 *
 * @author jwang 
 * @date   Jan 10, 2017 11:16:17 AM
 */
package com.dk.microgis.gis.gausscomput;


/**
 * 定义椭球的基本参数类
 *@Component
 */

public class EllipsoidParam {
	
	public EllipsoidParam(double a, double b,double f){
		this.a =a;
		this.b =b;
		this.f =f;
	}
	
public EllipsoidParam(){
		
	}
	private double a;
	
	private double b;
	
	private double f;

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getF() {
		return f;
	}

	public void setF(double f) {
		this.f = f;
	}
}
