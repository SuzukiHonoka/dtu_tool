package com.nj.dtu;


import javax.usb.*;
import java.util.List;


public class usbUtils {
    private static short idVendor = 2414;
    private static short idProduct = 1794;
    public static boolean usbExit = false;
    public static boolean usbExitLastTime = false;



    //数字
    public static final String REG_NUMBER = ".*\\d+.*";
    //小写字母
    public static final String REG_UPPERCASE = ".*[A-Z]+.*";
    //大写字母
    public static final String REG_LOWERCASE = ".*[a-z]+.*";
    //特殊符号
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
