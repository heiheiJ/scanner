package com.jhyarrow.scanner.util;

public class IP {
	private static IP ip;
	private static String ipAddress = "192.168.18.1";
	
	public static IP getInstance(){
		if(ip == null){
			return new IP();
		}
		return ip;
	}
	
	public String getIpAddress(){
		return ipAddress;
	}
}
