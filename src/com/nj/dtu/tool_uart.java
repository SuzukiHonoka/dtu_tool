package com.nj.dtu;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.JOptionPane;

import java.io.*;
import java.lang.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class tool_uart implements SerialPortEventListener {
    // ���ϵͳ�п��õ�ͨѶ�˿���
    public CommPortIdentifier commPortId;
    // ö������
    public Enumeration<CommPortIdentifier> portList;
    // RS232����
    public static SerialPort serialPort;
    // ������
    public InputStream inputStream;
    // �����
    public static OutputStream outputStream;

    static tool_data_process data_process;
	  final static Semaphore semp = new Semaphore(1);
    
    private ParamConfig param; 
    
    public static Socket socket;
    public static int type = 0; //0δѡ�� 1����  2����
    Thread sendThrea;
    send_net senSun;

    

    public tool_uart(tool_data_process dtu_tool_data_process) {
        data_process = dtu_tool_data_process;
    }

    @SuppressWarnings("unchecked")
    public int init(ParamConfig paramConfig) throws Exception {
    	if(type ==2){
    		JOptionPane.showMessageDialog(null, "���ȹر����ڣ�");
    		return 0;
    	}
    	param = paramConfig;
        // ��ȡϵͳ�����е�ͨѶ�˿�
        portList = CommPortIdentifier.getPortIdentifiers();
        // ��¼�Ƿ���ָ������
        boolean isExsist = false;
        int ret = 0;
        // ѭ��ͨѶ�˿�
        while (portList.hasMoreElements()) {
            commPortId = portList.nextElement();
            // �ж��Ƿ��Ǵ���
            if (commPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // �Ƚϴ��������Ƿ���ָ������
                if (paramConfig.getSerialNumber().equals(commPortId.getName())) {
                    // ���ڴ���
                    isExsist = true;
                    // �򿪴���
                    try {
                        // open:��Ӧ�ó�����������������������ʱ�ȴ��ĺ�������
                        serialPort = (SerialPort) commPortId.open(Object.class.getSimpleName(), 2000);
                        // ���ô��ڼ���
                        serialPort.addEventListener(this);
                        // ���ô�������ʱ����Ч(�ɼ���)
                        serialPort.notifyOnDataAvailable(true);
                        // ���ô���ͨѶ����:�����ʣ�����λ��ֹͣλ,У�鷽ʽ
                        serialPort.setSerialPortParams(paramConfig.getBaudRate(), paramConfig.getDataBit(),
                                paramConfig.getStopBit(), paramConfig.getCheckoutBit());
                        ret = 1;
                    } catch (PortInUseException e) {
                        JOptionPane.showMessageDialog(null, "�˿ڱ�ռ�ã�");
                    } catch (TooManyListenersException e) {
                        JOptionPane.showMessageDialog(null, "���������࣡");
                    } catch (UnsupportedCommOperationException e) {
                        JOptionPane.showMessageDialog(null, "��֧�ֵ�COMM�˿ڲ����쳣��");
                    }
                    break;
                }
            }
        }
        // �������ڸô������׳��쳣
        if (!isExsist) {
        	JOptionPane.showMessageDialog(null, "�����ڸô���!");
        }

        return ret;
    }
    public void initNet(String host,int port){
    	if(type ==1){
    		JOptionPane.showMessageDialog(null, "���ȹرմ��ڣ�");
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
				    	     data_process.g_tool_ui_all.Panel_load.tool_open_net_button.setText("�ر�����");
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
    	    	 JOptionPane.showMessageDialog(null, "���ӳ�ʱ");
    	    	  
    	    }

    }
    public void closeNet(){
    	if(type ==2){
    	   type=0;
    	try {
    		
    		if(socket.isConnected()){
			socket.close();
			data_process.g_tool_ui_all.Panel_load.tool_open_net_button.setText("������");
    		}
		} catch (IOException e) {
			e.printStackTrace();
			data_process.g_tool_ui_all.Panel_load.tool_open_net_button.setText("������");
		}
    	}
    }

    /**
     * ʵ�ֽӿ�SerialPortEventListener�еķ��� ��ȡ�Ӵ����н��յ�����
     */
    @SuppressWarnings("deprecation")
    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI: // ͨѶ�ж�
            case SerialPortEvent.OE: // ��λ����
            case SerialPortEvent.FE: // ֡����
            case SerialPortEvent.PE: // ��żУ�����
            case SerialPortEvent.CD: // �ز����
            case SerialPortEvent.CTS: // �������
            case SerialPortEvent.DSR: // �����豸׼����
            case SerialPortEvent.RI: // �������
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // ��������������
                break;
            case SerialPortEvent.DATA_AVAILABLE: // �����ݵ���
                //���ö�ȡ���ݵķ���
                try {
                    String read_buf = readComm();
                    data_process.data_input(read_buf);
                } catch (Exception e) {
                    System.out.println("���ڶ����󣡣�");
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
     * �رմ���
     *
     * @throws
     * @Description: �رմ���
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
                    JOptionPane.showMessageDialog(null, "��֧�ֵ�COMM�˿ڲ����쳣��");
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    ret = 0;
                    JOptionPane.showMessageDialog(null, "�ر������ʱ����IO�쳣");
                }
            }
            serialPort.close();
            serialPort = null;
        }
        return ret;
    }

    /**
     * ��ȡ���ڷ�����Ϣ
     *
     * @return: void
     */
    public String readComm() throws Exception {
        String data_read = "";
        try {
            inputStream = serialPort.getInputStream();
            // ͨ�������������available������ȡ�����ֽڳ���
            byte[] readBuffer = new byte[inputStream.available()];
            // ����·�϶�ȡ������
            int len = 0;

            while ((len = inputStream.read(readBuffer)) != -1) {
                data_read = new String(bytesToHexString(readBuffer).getBytes(), "gb2312");
                inputStream.close();
                inputStream = null;
                break;
            }
        } catch (IOException e) {
        	
            int n = JOptionPane.showConfirmDialog(null, "��ȡ��Ϣʱ����IO�쳣,�Ƿ�رմ���", "", JOptionPane.YES_NO_OPTION);
            if (n == 0) {
            	data_process.g_tool_ui_all.Panel_load.tool_open_uart_button.setText("�򿪴���");
            	 closeSerialPort();   	
            }
        
      
         //  throw new Exception("������Ϣ������ʱ����IO�쳣");
        }
        return data_read;
    }

    /**
     * ������Ϣ������
     *
     * @throws
     * @param: data
     * @return: void
     */
    public static  void sendComm(String data) throws Exception {
    	
        byte[] writerBuffer = null;

        //תΪHEX
        try {
            writerBuffer = hexToByteArray(data);
        } catch (NumberFormatException e) {
            throw new Exception("�����ʽ����");
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
        	 JOptionPane.showMessageDialog(null, "�������ô��ڻ�����");
           // throw new Exception("�Ҳ������ڡ�");
        } catch (IOException e) {
       	 JOptionPane.showMessageDialog(null, "������Ϣ������ʱ����IO�쳣");
          //  throw new Exception("������Ϣ������ʱ����IO�쳣");
        }
    }


    //�����ͷ��5a 5a��β����a5 a5����ô�м�Ҳ�п��ܳ������������ݣ�Ŀǰ�Ĵ���취�ǽ�5a��a5 ��ǰ�󶼲���00���������ϲ�������������������־
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
       	 JOptionPane.showMessageDialog(null, "�Ҳ�������");
         // throw new Exception("�Ҳ������ڡ�");
      } catch (IOException e) {
     	 JOptionPane.showMessageDialog(null, "������Ϣ������ʱ����IO�쳣");
        //  throw new Exception("������Ϣ������ʱ����IO�쳣");
      }
    }

    /**
     * Hex�ַ���תbyte
     *
     * @param inHex ��ת����Hex�ַ���
     * @return ת�����byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * hex�ַ���תbyte����
     *
     * @param inHex ��ת����Hex�ַ���
     * @return ת�����byte������
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            // ����
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            // ż��
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
     * ����ת����ʮ�������ַ���
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
