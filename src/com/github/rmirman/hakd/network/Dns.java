package com.github.rmirman.hakd.network;

import java.lang.Math;
import java.util.Vector;

public class Dns {
	
	private String ip = assignIp(0);
	
	
	private static Vector<String> dnsList = new Vector<String>(2, 1);
	
	
	public static String assignIp(int region){ // assigns an ip to an object that requests one, also checks it and adds it to the dns list
		String ip = null;
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
		dnsList.add(ip);
		return ip;
	}

	public static boolean checkIp(String ipCheck){ // checks if the ip has been taken on the dns list, also checks if the list is full
		for(int i=0; i<dnsList.size()-1; i+=2){
			if (dnsList.get(i).equals(ipCheck)){
				return true;
			}
		}
		return false;
	}

	public static String getIp(String url){ // returns the ip given the url
		for(int i=0; i<dnsList.size()-1; i+=2){
			if (dnsList.get(i+1).equals(url)){
				return dnsList.get(i);
			}
		}
		return "127.0.0.1";
	}
	
	public static boolean addUrl(String ip, String url){ // registers a url to an ip
		for(int i=0; i<dnsList.size()-1; i+=2){ // ip can only be a player's ip if they buy it
			if(dnsList.get(i).equals(url)){
				return false; // "Sorry, either that URL is already registered, or the dns list is full(which would be a bug)."
			}
		}
		for(int i=0; i<dnsList.size()-1; i+=2){
			if(dnsList.get(i).equals(ip)){
				dnsList.add(i+1, url);
				return true; // "you have successfully registered the url " + url + " for the ip " + ip;
			}
		}
		return false;
	}

	public static int findNetwork(String ip){
		for(int i=0; i<Network.getNetwork().size()-1; i++){
			if(Network.getNetwork().get(i) == null){
			}else if(Network.getNetwork().get(i).getIp().equals(ip)){
				return i;
			}
		}
		return 0;
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
