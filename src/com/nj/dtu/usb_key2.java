package com.nj.dtu;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

import java.io.File;

import com.sun.jna.Library;

public interface usb_key2 extends Library {
	String filePath = usb_key2.class.getResource("").getPath().replaceFirst("/","").replaceAll("%20"," ")+"epass_login_dll.dll";
	usb_key2 INSTANCE = (usb_key2) Native.loadLibrary(filePath, usb_key2.class);
	
	int epass_login(String pass,int length);
	
	
	//�����Կ��
	
	
    public static void main(String[] args) {
    	System.out.println("2222222222222222222222222");
    	int a = usb_key2.INSTANCE.epass_login("22", 2);
    	System.out.print(a);
    	System.out.print("333333");

    }
}	