package com.eclipse.test.model;

import java.util.ArrayList;

public class MyModel {
	private String act;
	private String position;
	private Double t_min;
	private Double t_max;
	private Double t_mcr;
	private Double t_sttdev;
	private Double t_mean;
	private Double t_rms;
	private Double t_iqr;
	private Double t_mad;
	private Double t_variance;
	
	public MyModel(String act, String position, Double min, Double max, Double mcr, Double sttdev, 
			Double mean,	Double rms, Double iqr, Double mad, Double variance) {
		super();
		this.act = act;
		this.position = position;
		this.t_min = min;
		this.t_max = max;
		this.t_mcr = mcr;
		this.t_sttdev = sttdev;
		this.t_mean = mean;
		this.t_rms = rms;
		this.t_iqr = iqr;
		this.t_mad = mad;
		this.t_variance = variance;
	}
	
	public MyModel(ArrayList<String> tmpList) {
		super();
		this.act = tmpList.get(1);
		this.position = tmpList.get(2);
		this.t_min = Double.valueOf(tmpList.get(3));
		this.t_max = Double.valueOf(tmpList.get(4));
		this.t_mcr = Double.valueOf(tmpList.get(5));
		this.t_sttdev = Double.valueOf(tmpList.get(6));
		this.t_mean = Double.valueOf(tmpList.get(7));
		this.t_rms = Double.valueOf(tmpList.get(8));
		this.t_iqr = Double.valueOf(tmpList.get(9));
		this.t_mad = Double.valueOf(tmpList.get(10));
		this.t_variance = Double.valueOf(tmpList.get(11));
	}
	
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Double getT_min() {
		return t_min;
	}
	public void setT_min(Double t_min) {
		this.t_min = t_min;
	}
	public Double getT_max() {
		return t_max;
	}
	public void setT_max(Double t_max) {
		this.t_max = t_max;
	}
	public Double getT_mcr() {
		return t_mcr;
	}
	public void setT_mcr(Double t_mcr) {
		this.t_mcr = t_mcr;
	}
	public Double getT_sttdev() {
		return t_sttdev;
	}
	public void setT_sttdev(Double t_sttdev) {
		this.t_sttdev = t_sttdev;
	}
	public Double getT_mean() {
		return t_mean;
	}
	public void setT_mean(Double t_mean) {
		this.t_mean = t_mean;
	}
	public Double getT_rms() {
		return t_rms;
	}
	public void setT_rms(Double t_rms) {
		this.t_rms = t_rms;
	}
	public Double getT_iqr() {
		return t_iqr;
	}
	public void setT_iqr(Double t_iqr) {
		this.t_iqr = t_iqr;
	}
	public Double getT_mad() {
		return t_mad;
	}
	public void setT_mad(Double t_mad) {
		this.t_mad = t_mad;
	}
	public Double getT_variance() {
		return t_variance;
	}
	public void setT_variance(Double t_variance) {
		this.t_variance = t_variance;
	}
}
