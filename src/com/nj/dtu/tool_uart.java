package com.nj.dtu;

import gnu.io.*;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.Semaphore;

public class tool_uart implements SerialPortEventListener {
    // 检测系统中可用的通讯端口类
    public CommPortIdentifier commPortId;
    // 枚举类型
    public Enumeration<CommPortIdentifier> portList;
    // RS232串口
    public static SerialPort serialPort;
    // 输入流
    public InputStream inputStream;
    // 输出流
    public static OutputStream outputStream;

    static tool_data_process data_process;
	  final static Semaphore semp = new Semaphore(1);
    
    private ParamConfig param; 
    
    public static Socket socket;
    public static int type = 0; //0未选择 1串口  2网口
    Thread sendThrea;
    send_net senSun;

    

    public tool_uart(tool_data_process dtu_tool_data_process) {
        data_process = dtu_tool_data_process;
    }

    @SuppressWarnings("unchecked")
    public int init(ParamConfig paramConfig) throws Exception {
    	if(type ==2){
    		JOptionPane.showMessageDialog(null, "请先关闭网口！");
    		return 0;
    	}
    	param = paramConfig;
        // 获取系统中所有的通讯端口
        portList = CommPortIdentifier.getPortIdentifiers();
        // 记录是否含有指定串口
        boolean isExsist = false;
        int ret = 0;
        // 循环通讯端口
        while (portList.hasMoreElements()) {
            commPortId = portList.nextElement();
            // 判断是否是串口
            if (commPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // 比较串口名称是否是指定串口
                if (paramConfig.getSerialNumber().equals(commPortId.getName())) {
                    // 串口存在
                    isExsist = true;
                    // 打开串口
                    try {
                        // open:（应用程序名【随意命名】，阻塞时等待的毫秒数）
                        serialPort = (SerialPort) commPortId.open(Object.class.getSimpleName(), 2000);
                        // 设置串口监听
                        serialPort.addEventListener(this);
                        // 设置串口数据时间有效(可监听)
                        serialPort.notifyOnDataAvailable(true);
                        // 设置串口通讯参数:波特率，数据位，停止位,校验方式
                        serialPort.setSerialPortParams(paramConfig.getBaudRate(), paramConfig.getDataBit(),
                                paramConfig.getStopBit(), paramConfig.getCheckoutBit());
                        ret = 1;
                    } catch (PortInUseException e) {
                        JOptionPane.showMessageDialog(null, "端口被占用！");
                    } catch (TooManyListenersException e) {
                        JOptionPane.showMessageDialog(null, "监听器过多！");
                    } catch (UnsupportedCommOperationException e) {
                        JOptionPane.showMessageDialog(null, "不支持的COMM端口操作异常！");
                    }
                    break;
                }
            }
        }
        // 若不存在该串口则抛出异常
        if (!isExsist) {
        	JOptionPane.showMessageDialog(null, "不存在该串口!");
        }

        return ret;
    }
    public void initNet(String host,int port){
    	if(type ==1){
    		JOptionPane.showMessageDialog(null, "请先关闭串口！");
    		return;
    	}
    	    type=2;
         	socket = new Socket();
    	    SocketAddress endpoint = new InetSocketAddress(host, port); 
    	    try {
    	      socket.connect(endpoint, 5000);
    	      new Thread(new Runnable() {
					
					@Override
					public void run() {
						 try {
							 data_process.g_tool_ui_all.Panel_load.connect = true;
								
								Thread.sleep(10);
								sendComm(data_process.send_cmd_run_shell_2(data_process.CMD_GET_MD5,
										"/zr_bin/root_config.sh export"));	
								Thread.sleep(100);
								sendComm(data_process.send_cmd_run_shell_2(data_process.CMD_GET_CHECK_USER,
										"/zr_bin/root_config.sh list_role_check"));	
								
							Thread.sleep(10);
							data_process.g_tool_ui_all.stop();
				    	     data_process.g_tool_ui_all.Panel_load.tool_open_net_button.setText("关闭网口");
				    	     Thread.sleep(30);
								sendComm(data_process.send_cmd_config_request());
								Thread.sleep(30);
								sendComm(data_process.send_cmd_status_rx_tx_runtime());			
								Thread.sleep(30);
								sendComm(data_process.send_cmd_run_shell_2(data_process.CMD_GET_LOG,
										"/zr_bin/ini_config get LOG:logon"));	
				    	     
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
					}
				}).start();
    	     
    	    } catch (IOException e) {
    	    	  data_process.g_tool_ui_all.stop();
    	    		 data_process.g_tool_ui_all.Panel_load.connect = false;
    	    	 JOptionPane.showMessageDialog(null, "连接超时");
    	    	  
    	    }

    }
    public void closeNet(){
    	if(type ==2){
    	   type=0;
    	try {
    		
    		if(socket.isConnected()){
			socket.close();
			data_process.g_tool_ui_all.Panel_load.tool_open_net_button.setText("打开网口");
    		}
		} catch (IOException e) {
			e.printStackTrace();
			data_process.g_tool_ui_all.Panel_load.tool_open_net_button.setText("打开网口");
		}
    	}
    }

    /**
     * 实现接口SerialPortEventListener中的方法 读取从串口中接收的数据
     */
    @SuppressWarnings("deprecation")
    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI: // 通讯中断
            case SerialPortEvent.OE: // 溢位错误
            case SerialPortEvent.FE: // 帧错误
            case SerialPortEvent.PE: // 奇偶校验错误
            case SerialPortEvent.CD: // 载波检测
            case SerialPortEvent.CTS: // 清除发送
            case SerialPortEvent.DSR: // 数据设备准备好
            case SerialPortEvent.RI: // 响铃侦测
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 输出缓冲区已清空
                break;
            case SerialPortEvent.DATA_AVAILABLE: // 有数据到达
                //调用读取数据的方法
                try {
                    String read_buf = readComm();
                    data_process.data_input(read_buf);
                } catch (Exception e) {
                    System.out.println("串口读错误！！");
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public String[] getComName() throws Exception {
        String[] comNameList = new String[10];
        int comIndex = 0;

        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            commPortId = portList.nextElement();
            if (commPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                comNameList[comIndex] = commPortId.getName();
                comIndex++;
            }
        }
        String[] result = new String[comIndex];
        for(int i= 0;i<comIndex;i++){
        	result[i] = comNameList[i];
        }
        
        return result;
    }

    /**
     * 关闭串口
     *
     * @throws
     * @Description: 关闭串口
     * @param:
     * @return: void
     */
    public int closeSerialPort() throws Exception {
    	type=0;
        int ret = 1;
        if (serialPort != null) {
            serialPort.notifyOnDataAvailable(false);
            serialPort.removeEventListener();
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    ret = 0;
                    JOptionPane.showMessageDialog(null, "不支持的COMM端口操作异常！");
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    ret = 0;
                    JOptionPane.showMessageDialog(null, "关闭输出流时发生IO异常");
                }
            }
            serialPort.close();
            serialPort = null;
        }
        return ret;
    }

    /**
     * 读取串口返回信息
     *
     * @return: void
     */
    public String readComm() throws Exception {
        String data_read = "";
        try {
            inputStream = serialPort.getInputStream();
            // 通过输入流对象的available方法获取数组字节长度
            byte[] readBuffer = new byte[inputStream.available()];
            // 从线路上读取数据流
            int len = 0;

            while ((len = inputStream.read(readBuffer)) != -1) {
                data_read = new String(bytesToHexString(readBuffer).getBytes(), "gb2312");
                inputStream.close();
                inputStream = null;
                break;
            }
        } catch (IOException e) {
        	
            int n = JOptionPane.showConfirmDialog(null, "读取信息时发生IO异常,是否关闭串口", "", JOptionPane.YES_NO_OPTION);
            if (n == 0) {
            	data_process.g_tool_ui_all.Panel_load.tool_open_uart_button.setText("打开串口");
            	 closeSerialPort();   	
            }
        
      
         //  throw new Exception("发送信息到串口时发生IO异常");
        }
        return data_read;
    }

    /**
     * 发送信息到串口
     *
     * @throws
     * @param: data
     * @return: void
     */
    public static  void sendComm(String data) throws Exception {
    	
        byte[] writerBuffer = null;

        //转为HEX
        try {
            writerBuffer = hexToByteArray(data);
        } catch (NumberFormatException e) {
            throw new Exception("命令格式错误！");
        }

        byte[] send_buffer = new byte[5000];
        int j = 0;

        send_buffer[j++] = writerBuffer[0];
        send_buffer[j++] = writerBuffer[1];
        for (int i = 2; i < writerBuffer.length - 2; i++) {
            if (writerBuffer[i] == (byte) 0x5a) {
                send_buffer[j++] = 0;
                send_buffer[j++] = writerBuffer[i];
                send_buffer[j++] = 0;           
            } else if (writerBuffer[i] == (byte) 0xa5) {
                send_buffer[j++] = 0;
                send_buffer[j++] = writerBuffer[i];
                send_buffer[j++] = 0;           
            } else {
                send_buffer[j++] = writerBuffer[i];
            }
        }

        send_buffer[j++] = writerBuffer[writerBuffer.length - 2];
        send_buffer[j++] = writerBuffer[writerBuffer.length - 1];

        byte[] send_buffer_2 = new byte[j];

        for (int i = 0; i < j; i++) {
            send_buffer_2[i] = send_buffer[i];
            //System.out.print(" " + String.format("%02x", send_buffer_2[i]));
        }
        if(type ==2){
    		new Thread(new send_net(data_process,socket,send_buffer_2)).start();
    		return;
    	}
   
        try {
        	
            int i = 0;
            for (; i < 3; i++) {
            	
                outputStream = serialPort.getOutputStream();
                outputStream.write(send_buffer_2);
                outputStream.flush();
                if (data_process.wait_ack(data.substring(4, data.length() - 1)) == 1) {
                    break;
                }
             
            }
         
        } catch (NullPointerException e) {
        	 JOptionPane.showMessageDialog(null, "请先配置串口或网口");
           // throw new Exception("找不到串口。");
        } catch (IOException e) {
       	 JOptionPane.showMessageDialog(null, "发送信息到串口时发生IO异常");
          //  throw new Exception("发送信息到串口时发生IO异常");
        }
    }


    //数组的头是5a 5a，尾巴是a5 a5，那么中间也有可能出现这两个数据，目前的处理办法是将5a，a5 ，前后都插入00，这样保障不会连续出现这两个标志
    public void sendCommByte(byte[] writerBuffer) throws Exception {
        byte[] send_buffer = new byte[5000];
        int j = 0;

        send_buffer[j++] = writerBuffer[0];
        send_buffer[j++] = writerBuffer[1];
        for (int i = 2; i < writerBuffer.length - 2; i++) {
            if (writerBuffer[i] == (byte) 0x5a) {
                send_buffer[j++] = 0;
                send_buffer[j++] = writerBuffer[i];
                send_buffer[j++] = 0;            
            } else if (writerBuffer[i] == (byte) 0xa5) {
                send_buffer[j++] = 0;
                send_buffer[j++] = writerBuffer[i];
                send_buffer[j++] = 0;          
            } else {
                send_buffer[j++] = writerBuffer[i];
            }
        }

        send_buffer[j++] = writerBuffer[writerBuffer.length - 2];
        send_buffer[j++] = writerBuffer[writerBuffer.length - 1];

        byte[] send_buffer_2 = new byte[j];

        for (int i = 0; i < j; i++) {
            send_buffer_2[i] = send_buffer[i];
            //System.out.print(" " + String.format("%02x", send_buffer_2[i]));
        }

        try {
            outputStream = serialPort.getOutputStream();
            outputStream.write(send_buffer_2);
            outputStream.flush();
        } catch (NullPointerException e) {
       	 JOptionPane.showMessageDialog(null, "找不到串口");
         // throw new Exception("找不到串口。");
      } catch (IOException e) {
     	 JOptionPane.showMessageDialog(null, "发送信息到串口时发生IO异常");
        //  throw new Exception("发送信息到串口时发生IO异常");
      }
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            // 奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            // 偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param byte[]
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
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

    public static final String bytesToString(byte[] bArray) {
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
