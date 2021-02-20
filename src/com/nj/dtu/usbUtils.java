package com.nj.dtu;


import java.util.List;

import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;


public class usbUtils {
    private static short idVendor = 2414;
    private static short idProduct = 1794;
    public static boolean usbExit = false;
    public static boolean usbExitLastTime = false;



    //Êý×Ö
    public static final String REG_NUMBER = ".*\\d+.*";
    //Ð¡Ð´×ÖÄ¸
    public static final String REG_UPPERCASE = ".*[A-Z]+.*";
    //´óÐ´×ÖÄ¸
    public static final String REG_LOWERCASE = ".*[a-z]+.*";
    //ÌØÊâ·ûºÅ
    public static final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";
 
    public static boolean checkPasswordRule(String password){
        int i = 0;
        if (password.matches(REG_NUMBER)) i++;
        if (password.matches(REG_LOWERCASE))i++;
        if (password.matches(REG_UPPERCASE)) i++;
        if (password.matches(REG_SYMBOL)) i++;
 
        if (i  < 3 )  return false;
 
        return true;
    }

    public static boolean findDevice(UsbHub hub)
    {
       
        List list = (List) hub.getAttachedUsbDevices();
        for (int i = 0;i<list.size();i++)
        {
        	UsbDevice device = (UsbDevice)list.get(i);
            UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
          
            if (desc.idVendor() == idVendor && desc.idProduct() == idProduct) {
            	usbExitLastTime = usbExit;
            	usbExit = true;
            	return true;}
           
        }
        usbExitLastTime = usbExit;
        usbExit = false;
        return false;
    }
    public static boolean isExits(){
    	try {
			return findDevice(UsbHostManager.getUsbServices()
			        .getRootUsbHub());
		} catch (SecurityException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }

}
