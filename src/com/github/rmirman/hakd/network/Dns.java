package com.github.rmirman.hakd.network;

import java.util.Vector;

public class Dns {

	private String ip = assignIp(0);


	private static Vector<String> dnsList = new Vector<String>(2, 1);


	public static String assignIp(int region){ // assigns an ip to an object that requests one, also checks it and adds it to the dns list
		String ip = null;
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
		}while(dnsList.indexOf(ip)!= -1);
		dnsList.add(ip);
		dnsList.add("");
		return ip;
	}

	public static boolean addUrl(String ip, String url){ // registers a url to an ip // ip can only be a player's ip if they buy it
		if(url.matches("")){
			if(dnsList.indexOf(url) == -1&&dnsList.indexOf(ip) != -1){ 
				dnsList.set(dnsList.indexOf(ip)+1, url);
				return true; // "you have successfully registered the url " + url + " for the ip " + ip;
			}
		}
		return false; // "Sorry, either that URL is already registered, or a bug)."
	}

	public static int findNetwork(String address){
		if(!address.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")){
			address = dnsList.get(dnsList.indexOf(address)+1);
		}
		if(dnsList.indexOf(address) != -1){
			return dnsList.indexOf(address)/2;
		}
		return -1;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public static Vector<String> getDnsList() {
		return dnsList;
	}

	public static void setDnsList(Vector<String> dnsList) {
		Dns.dnsList = dnsList;
	}
}
