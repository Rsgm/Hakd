package com.github.rmirman.hakd.network;

import java.lang.Math;

public class Dns {
	
	private String ip = assignIp(0);
	
	
	public static String[][] dnsList = new String[500][2];
	
	
	public static String assignIp(int ipRegion){ // assigns an ip to an object that requests one, also checks it and adds it to the dns list
		String ip = null;
		int region = ipRegion;
		boolean taken = true;
		
		do{
			switch(region){	// creates a realistic ip based on registered ipv4 irl //0 == random, 1 == usa, 2 == europe, 3 == asia
			case 0:	
				ip = (int)(Math.random()*256) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256);
				break;
			case 1:
				ip = (int)(Math.random()*14+63) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256);
				break;
			case 2:
				ip = (int)(Math.random()*15+77) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256);
				break;
			default:
				ip = (int)(Math.random()*256) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256);
				break;
			}
			taken = checkIp(ip);
		}while(taken == true);
		addIp(ip);
		return ip;
	}

	public static boolean checkIp(String ipCheck){ // checks if the ip has been taken on the dns list, also checks if the list is full
		for(int i=0; i<dnsList.length; i++){
			if (dnsList[i][0] == ipCheck){
				return true;
			}
		}
		return false;
	}
	
	private static void addIp(String ip){ // adds the ip to the dns list, !always check the ip before sending it to this just in case!
		for(int i=0; i < dnsList.length; i++){
			if (dnsList[i][0] == null){
				dnsList[i][0] = ip;
				return;
			}
		}
		return;
	}
	
	public static String getIp(String url){ // returns the ip given the url
		for(int i=0; i<dnsList.length; i++){
			if (dnsList[i][1] == url){
				return dnsList[i][0];
			}
		}
		return "127.0.0.1";
	}
	
	public static boolean addUrl(String ip, String url){ // registers a url to an ip
		for(int i=0; i<dnsList.length; i++){ // ip can only be a player's ip if they buy it
			if(dnsList[i][1] == url){
				return false; // "Sorry, either that URL is already registered, or the dns list is full(which would be a bug)."
			}
		}
		for(int i=0; i<dnsList.length; i++){
			if(dnsList[i][0] == ip){
				dnsList[i][1] = url;
				return true; // "you have successfully registered the url " + url + " for the ip " + ip;
			}
		}
		return false;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
