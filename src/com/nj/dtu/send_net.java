package com.nj.dtu;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class send_net implements Runnable {

	private Socket socket;

	 byte[] send_buffer;

	private tool_data_process process;

	public send_net(tool_data_process process,Socket socket,  byte[] send_buffer) {
	    this.process = process;
		this.socket = socket;
		this.send_buffer = send_buffer;
	}

	@Override
	public void run() {

		sendAndRead();
	}

	public void sendAndRead() {
		try {
					
			socket.getOutputStream().write(send_buffer);
			socket.getOutputStream().flush();
			 String data_read = "";
			  // 通过输入流对象的available方法获取数组字节长度
            byte[] readBuffer = new byte[2048];
            // 从线路上读取数据流
          
            while ((socket.getInputStream().read(readBuffer)) != -1) {
                data_read = new String(bytesToHexString(readBuffer).getBytes(), "gb2312");       
                break;
            }
			process.data_input(data_read);
		
			System.out.println("finish");

		} catch (IOException e) {
//			if (e instanceof SocketTimeoutException) {
//				if (times < 3) {
//					times++;
//					sendAndRead();
//				}else{
//					System.out.print(e.getMessage());
//				}
//			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    /**
     * 数组转换成十六进制字符串
     *
     * @param byte[]
     * @return HexString
     */
    public String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


}
