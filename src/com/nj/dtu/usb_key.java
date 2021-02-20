package com.nj.dtu;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

import java.io.File;

import com.sun.jna.Library;

public interface usb_key extends Library {
	String filePath = usb_key.class.getResource("").getPath().replaceFirst("/","").replaceAll("%20"," ")+"usbkey.dll";
	//String lobPath = usb_key.class.getResource("").getPath().replaceFirst("/","").replaceAll("%20"," ")+"pubkey.msblob";
	usb_key INSTANCE = (usb_key) Native.loadLibrary(filePath, usb_key.class);
	void test();
	
	
	//添加秘钥对
	int add_key_pair();
	//判断设备是否存在秘钥对
	int get_key_pair();
	// 导出秘钥
	int export_pubkey_pair(String file_path);
	
    public static void main(String[] args) {
    	if(!usbUtils.isExits()){
    		System.out.print("no exit");
    		return;
    	}
    	File file = new File("pubkey.msblob");
    	if(file.exists())
    		System.out.print("exit");
    	else
    		System.out.print("no exit");
    	
    	System.out.println(Md5CaculateUtil.getMD5(new File("pubkey.msblob")));
        if(usb_key.INSTANCE.get_key_pair()==0){
        	System.out.println("start");
        	 System.out.println(usb_key.INSTANCE.export_pubkey_pair("pubkey.msblob"));
        	//if(usb_key.INSTANCE.export_pubkey_pair(lobPath)==0)
        	 System.out.println("end");
        		System.out.println(Md5CaculateUtil.getMD5(new File("pubkey.msblob")));
        		
        }else{
        	usb_key.INSTANCE.add_key_pair();
        }
        

    }
}	