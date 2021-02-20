package com.nj.dtu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.JOptionPane;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;

import net.sf.json.JSONObject;

public class tool_data_process extends tool_ui_base {
	// ����ʵ��
	tool_uart g_uart_process;
	// ȫ�ֵ��豸��Ϣ
	tool_device_info g_dtu_device_info;
	// ȫ�ֵ��豸��Ϣ
	tool_ui g_tool_ui_all;

	// �������ݱ���
	String data = "";
	String frame_data = "";

	// Э����ر���
	String FRAME_BEGIN = "5A5A";
	String FRAME_END = "A5A5";
	final String FRAME_ACK_TYPE = "93";
	final String FRAME_DATA_TYPE = "9F";
	final String FRAME_SYNC_TYPE = "97";
	final String FRAME_ACK_LEN = "0001";
	final String FRAME_ACK_LOAD = "06";
	// ��Ƭͷ�Ĵ�С
	final int FRAME_FRAG_HEAD_LEN = 15;
	final String fRAME_CMD_TRUE = "01";

	// Ӧ�����޷��� ����
	public int frame_expected_id = 1;

	// ���Ľ���
	String frame_head = "";
	String frame_type = "";
	String frame_id = "";
	// String tmpACKxuhao= "";
	// String tmpNCKxuhao= "";
	String frame_data_len = "";
	String frame_data_load = "";
	String frame_data_subcmd_load = "";
	String frame_data_check = "";
	String frame_data_check_ack = "";
	String frame_cmd_id = "";

	int frame_input_frag_ack_upload_flag = 0;
	int frame_input_frag_ack_flag = 0;
	String frame_data_frag_loadString;
	// ���յ��ķ�Ƭ��ID
	String frame_input_frag_ack_id;

	// ���ķ��������� CMD_ID,Ϊ16����
	final String CMD_CONFIG_REQUEST = "01"; /* ���ò������� */
	final String CMD_PEIDIAN_REQUEST = "03"; /* ������� */
	final String CMD_SYS_RUNTIME_REQUEST = "05"; /* ϵͳ����ʱ�� */
	final String CMD_SYS_FIRMWARE_SET = "06"; /* ����ָ�� */
	final String CMD_CONFIG_SET = "11"; /* PC�·����� */
	final String CMD_CONFIG_UPLOAD = "12"; /* ���������ļ� */
	final String CMD_CONFIG_DOWNLOAD = "13"; /* ���������ļ� */
	final String CMD_SYS_FIRMWARE_UPLOAD = "14"; /* ���������ļ� */
	final String CMD_SYS_FIRMWARE_UPLOAD_O = "20"; /* ���������ļ� */
	final String CMD_CERTIFICATE_REQUEST = "15"; /* ����֤�� */
	final String CMD_CERTIFICATE_UPLOAD = "16"; /* ����֤�� */
	final String CMD_CERTIFICATE_UPLOAD_O = "22"; /* ����ǩ��֤�� */
	final String CMD_PING_REQUEST = "17"; /* ������ */
	final String CMD_LOG_REQUEST = "18"; /* ��־���� */
	final String CMD_MD5_SEND = "19"; /* MD5���� */
	final String CMD_FRAG_DATA_ACK = "20"; /* ��ƬӦ��ACK */
	final String CMD_MD5_CHECK = "21"; /* MD5 check */

	final String CMD_SET_REBOOT = "30"; /*  */
	final String CMD_SET_GET_VERSION = "31"; /*  */
	// �з���ֵ��shellִ��
	final String CMD_RUN_SHELL_2 = "98"; /*  */
	final String CMD_RUN_SHELL = "99"; /*  */
	// ACKCMD_ID
	final String CMD_ACK = "06"; /* MD5 check */

	final String CMD_REBOOT = "00"; /* ���� */
	final String CMD_ENC = "01"; /* ����֤����Ϣ */
	final String CMD_SIG = "02"; /* ǩ��֤����Ϣ */
	final String CMD_IN = "03"; /* ����ǩ��֤�� */
	final String CMD_P10 = "04"; /* ����ǩ��֤�� */
	final String CMD_VER = "05"; /* cat version */
	final String CMD_INI_OUT = "06"; /* ���������ļ� */
	final String CMD_INI_IN = "07"; /* ���������ļ� */
	final String CMD_RUN_DATE = "08"; /* ���� */
	final String CMD_SET_DATE = "09"; /* ϵͳʱ������ */
	final String CMD_HW_DATE = "10"; /* ����Ӳ��ʱ�� */
	final String CMD_CREATE = "11"; /* ��������֤����Ϣ */
	final String CMD_STATE = "12"; /* ״̬�Զ��� */
	final String CMD_SET_LOG = "13"; /* set log */
	final String CMD_GET_LOG = "14"; /* get log */
	final String CMD_GET_MD5 = "15"; /* get MD5  ��֤ */
	final String CMD_IMPORT_KEY = "16"; /* ������Կ */
	final String CMD_ADD_USER = "17"; /* ����û� */
	final String CMD_GET_CONFIG_USER = "18"; /* ��ȡ�����û� */
	final String CMD_GET_CHECK_USER = "19"; /* ��ȡ����û� */
	final String CMD_REMOVE_USER = "20"; /* ɾ���û� */
	final String CMD_REMOVE_KEY = "21"; /* ɾ����Կ */
	
	String last_p10_name = "";
	Boolean iniAccept = true;
	String Md5 ="";
	Map<String,String> mapCheck = new HashMap<>();
	Map<String,String> mapConfig = new HashMap<>();
	

	public void data_init(tool_uart serialPort, tool_device_info dtu_device_info, tool_ui tool_ui_all) {
		g_uart_process = serialPort;
		g_dtu_device_info = dtu_device_info;
		g_tool_ui_all = tool_ui_all;
	}

	public boolean canCheckLogin(String name,String pass) {
		if(mapCheck.containsKey(name)&&mapCheck.get(name).equals(pass))
			return true;
		return false;   
	}
	public boolean canConfigLogin(String name,String pass) {
		if(mapConfig.containsKey(name)&&mapConfig.get(name).equals(pass))
			return true;
		return false;   
	}

	// �ҵ�һ�������ı���
	public int data_find_one_frame() {
		int ret = 0;
		int index_begin = data.indexOf(FRAME_BEGIN);
		int index_end = data.indexOf(FRAME_END);

		// �����
		frame_data = "";

		// �ֱ����ͷ��β,û�ҵ���ʲô������
		if (index_begin >= 0 && index_end >= 0 && index_end > index_begin) {
			// �������а���ͷ��β 5A 5A 93 00 02 00 01 06 63 A5 A5
			frame_data = data.substring(index_begin, index_end + 4);
			frame_data = frame_data.substring(4, frame_data.length() - 4);
			frame_data = frame_data.replaceAll("005A00", "5A");
			frame_data = frame_data.replaceAll("00A500", "A5");

			System.out.println("back frame_data�� " + frame_data);

			// ����ʣ�ิ��
			if ((data.length() - 1) > index_end + 3) {
				data = data.substring(index_end + 4, data.length());
			} else {
				data = "";
			}

			ret = 1;
		}
		return ret;
	}

	// ����ACK����
	public String send_cmd_ack() {
		// String ack = "93"+OctHex(shujuxuhao)+"000106";
		String cmd_len = "0001";
		String cmd_str = FRAME_ACK_TYPE + numToHex16(frame_expected_id) + cmd_len + CMD_ACK;
		String cmd_chk = checkSum(cmd_str);

		System.out.println("send_cmd_ack�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	// ���������ļ�
	public String send_cmd_config_download() {
		String cmd_len = "0001";
		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + cmd_len + CMD_CONFIG_DOWNLOAD;
		String cmd_chk = checkSum(cmd_str);

		System.out.println("send_cmd_config_download�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	// ���ò�������
	public String send_cmd_config_request() {
		String cmd_len = "0001";
		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + cmd_len + CMD_CONFIG_REQUEST;
		String cmd_chk = checkSum(cmd_str);

		System.out.println("data_send_cmd_config_request�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	// //��ȡ֤����Ϣ
	// public String send_cmd_status_rx_tx_runtime() {
	// String cmd_len = "0001";
	// String cmd_str= FRAME_DATA_TYPE + numToHex16(frame_expected_id) + cmd_len
	// + CMD_SYS_RUNTIME_REQUEST;
	// String cmd_chk = checkSum(cmd_str);
	//
	// System.out.println("send_cmd_status_rx_tx_runtime�� " + FRAME_BEGIN +
	// cmd_str + cmd_chk + FRAME_END);
	//
	// return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	// }

	// �豸״̬
	public String send_cmd_status_rx_tx_runtime() {
		String cmd_len = "0001";
		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + cmd_len + CMD_SYS_RUNTIME_REQUEST;
		String cmd_chk = checkSum(cmd_str);

		System.out.println("send_cmd_status_rx_tx_runtime�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	// ���ò������浽�豸
	/*
	 * ���ĸ�ʽ�� 9F ������� ���ݳ��� 12 size�������ļ���С��4�ֽڣ� ��Ƭ�ţ�2�ֽڣ� ƫ������4�ֽڣ� ���ȣ�4�ֽڣ� ��Ƭ�ļ�
	 * CheckSum
	 */
	public String send_cmd_config_upload() {
		/*
		 * ���ĸ�ʽ�� 9F ������� ���ݳ��� 12 size�������ļ���С��4�ֽڣ� ��Ƭ�ţ�1�ֽڣ� ƫ������4�ֽڣ� ���ȣ�4�ֽڣ� ��Ƭ�ļ�
		 * CheckSum
		 */
		try {
			String config_file_md5 = getMD5(file_ini_name);
			// ��ȡ�ļ�
			String cmd_str = "";
			String cmd_frame_id = numToHex_2_byte(frame_expected_id);
			String config_file_content = readFileContent(file_ini_name);
			String cmd_frame_len = numToHex_2_byte(
					convertStringToHex(config_file_content).length() / 2 + FRAME_FRAG_HEAD_LEN);
			String cmd_frame_subid = CMD_CONFIG_UPLOAD;
			String cmd_file_len = numToHex_4_byte(convertStringToHex(config_file_content).length() / 2);
			String cmd_frag_id = "0001";
			String cmd_frag_offset = "00000000";
			String cmd_frag_len = cmd_file_len;

			cmd_str = FRAME_DATA_TYPE + cmd_frame_id + cmd_frame_len + cmd_frame_subid + cmd_file_len + cmd_frag_id
					+ cmd_frag_offset + cmd_frag_len + convertStringToHex(config_file_content);

			String cmd_chk = checkSum(cmd_str);

			System.out.println("send data��" + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

			return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
		} catch (Exception e) {
			System.out.println("send_cmd_config_upload error " + e);
		}

		return "";
	}

	// ����֤������
	public String send_cmd_csr_upload(String str) {
		int cmd_len = 0;

		cmd_len = cmd_len + 1; // �����һ���ֽ�
		cmd_len = cmd_len + str.length();

		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + num_str_2byte(cmd_len)
				+ CMD_CERTIFICATE_REQUEST + strTo16hexstr(str);
		String cmd_chk = checkSum(cmd_str);

		System.out.println("send_cmd_run_shell�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	// ��������
	public String send_cmd_run_shell(String shell_com) {
		int cmd_len = 0;

		cmd_len = cmd_len + 1; // �����һ���ֽ�
		cmd_len = cmd_len + shell_com.length();

		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + num_str_2byte(cmd_len) + CMD_RUN_SHELL
				+ strTo16hexstr(shell_com);
		String cmd_chk = checkSum(cmd_str);

		System.out.println("send_cmd_run_shell�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	// shell
	public String send_cmd_run_shell_2(String shell_com_id, String shell_com) {
		int cmd_len = 0;

		cmd_len = cmd_len + 1; // �����һ���ֽ�
		cmd_len = cmd_len + shell_com_id.length(); // �����һ���ֽ�
		cmd_len = cmd_len + shell_com.length();

		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + num_str_2byte(cmd_len) + CMD_RUN_SHELL_2
				+ strTo16hexstr(shell_com_id) + strTo16hexstr(shell_com);
		
		String cmd_chk = checkSum(cmd_str);
		System.out.println("send_cmd_run_shell�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	// shell
	public void frame_process_shell_2() {
		try {
			String cmd_id = convertHexToString(frame_data_subcmd_load.substring(0, 4));
			System.out.println("cmd_id " + cmd_id);
			int index = frame_data_subcmd_load.indexOf("00");
			String data = convertHexToString(frame_data_subcmd_load.substring(index + 2));
			System.out.println("data " + data);
			if (cmd_id.equals(CMD_ENC) || cmd_id.equals(CMD_SIG)) {
				g_tool_ui_all.Panel_certificate.setInfo(cmd_id, data);

			} else if (cmd_id.equals(CMD_IN)) {
				g_tool_ui_all.Panel_log.tool_log_set(data);
			} else if (cmd_id.equals(CMD_P10)) {
				if (last_p10_name.equals(g_dtu_device_info.device_x509_req_file_path)) // Ŀǰ�ط��������ν��
					return;
				last_p10_name = g_dtu_device_info.device_x509_req_file_path;
				g_tool_ui_all.stop();
				try {

					String md5 = DigestUtils
							.md5Hex(new FileInputStream(new File(g_dtu_device_info.device_x509_req_file_path)));	
					if (data.contains(md5)) {
						JOptionPane.showMessageDialog(null, "�����ɹ���");
					} else {
						JOptionPane.showMessageDialog(null, "����ʧ�ܣ�");
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cmd_id.equals(CMD_VER)) {
				g_tool_ui_all.Panel_system.cat_vaersion_reuslt(data);
			} else if (cmd_id.equals(CMD_INI_OUT)) {
				if (iniAccept)
					return;
				iniAccept = true;
				try {

					String md5 = DigestUtils.md5Hex(
							new FileInputStream(new File(g_dtu_device_info.device_config_download_config_file_path)));
					if (data.contains(md5)) {
						JOptionPane.showMessageDialog(null, "�����ɹ���");
					} else {
						JOptionPane.showMessageDialog(null, "����ʧ�ܣ�");
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cmd_id.equals(CMD_INI_IN)) {
				try {

					String md5 = DigestUtils.md5Hex(
							new FileInputStream(new File(g_dtu_device_info.device_config_upload_config_file_path)));
				
					if (data.contains(md5)) {
						JOptionPane.showMessageDialog(null, "����ɹ���");
					} else {
						JOptionPane.showMessageDialog(null, "����ʧ�ܣ�");
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(cmd_id.equals(CMD_RUN_DATE)){
				g_tool_ui_all.Panel_log.tool_log_set(data);
			
			}else if(cmd_id.equals(CMD_SET_DATE)){
				iniAccept = false;
				try {
					g_uart_process.sendComm(send_cmd_run_shell_2(CMD_HW_DATE,"hwclock -w"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(cmd_id.equals(CMD_HW_DATE)){
				if (iniAccept)
					return;
				iniAccept = true;
				JOptionPane.showMessageDialog(null, "���óɹ���");
			
			}else if(cmd_id.equals(CMD_CREATE)){
				if (iniAccept)
					return;
				g_tool_ui_all.stop();
				iniAccept = true;
				g_tool_ui_all.Panel_log.tool_log_set(data);
			}else if(cmd_id.equals(CMD_REBOOT)){
				g_tool_ui_all.Panel_every.rebootSuccess = true;
				 g_tool_ui_all.stop();
			}else if(cmd_id.equals(CMD_STATE)){
				g_tool_ui_all.Panel_log.tool_log_set(data);
			}else if(cmd_id.equals(CMD_GET_LOG)){
				g_tool_ui_all.Panel_other.jCheckBox.setSelected(data.equals("1"));
				g_tool_ui_all.Panel_other.text_log_type.setText(data.equals("1")?"�Ѵ�":"�ѹر�");
			}else if(cmd_id.equals(CMD_GET_MD5)){
					Md5=data;
					if(Md5.equals(""))
						g_tool_ui_all.Panel_log.tool_log_set("�豸δ������Կ");
				    else
					    g_tool_ui_all.Panel_log.tool_log_set("�豸�ѵ�����Կ");
					
			}else if(cmd_id.equals(CMD_IMPORT_KEY)){
				g_tool_ui_all.stop();
			}else if(cmd_id.equals(CMD_ADD_USER)){
			    if(data.equals("")){
			    	 new Thread() {
							public void run() {
								try {
									g_uart_process.sendComm(send_cmd_run_shell_2(CMD_GET_CHECK_USER,
												"/zr_bin/root_config.sh list_role_check"));	
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
			    	 }.start();
			    
					  JOptionPane.showMessageDialog(null, "��ӳɹ�");
			    }else{
			       JOptionPane.showMessageDialog(null, "���˻��Ѵ���");
			    }
			}else if(cmd_id.equals(CMD_GET_CONFIG_USER)){
				getUserList(data,true);
			}else if(cmd_id.equals(CMD_GET_CHECK_USER)){
				getUserList(data,false);
				try {
					g_uart_process.sendComm(send_cmd_run_shell_2(CMD_GET_CONFIG_USER,
							"/zr_bin/root_config.sh list_role_config"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(cmd_id.equals(CMD_REMOVE_USER)){
				 new Thread() {
						public void run() {
							try {
								g_uart_process.sendComm(send_cmd_run_shell_2(CMD_GET_CHECK_USER,
											"/zr_bin/root_config.sh list_role_check"));	
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
		    	 }.start();
		    
				  JOptionPane.showMessageDialog(null, "ɾ���ɹ�");
			}else if(cmd_id.equals(CMD_REMOVE_KEY)){
				 JOptionPane.showMessageDialog(null, "ɾ���ɹ�");
				g_tool_ui_all.stop();
			}
			

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
    public void getUserList(String test ,boolean isConfig){
   	 String[] stringList =test.split("\n");
   	 if(isConfig)
   		mapConfig = new HashMap<>();
   	 else
   		mapCheck = new HashMap<>(); 
  
   	 for(String one : stringList){	
   		 if(one.indexOf("role:")>=0){ 			
   			 String name = one.substring(one.indexOf(":")+1,one.indexOf(";"));
   			 String password = one.substring(one.indexOf("d:")+2,one.length());
   			 if(isConfig)
   		   		mapConfig.put(name, password);
   		   	 else
   		   		mapCheck.put(name, password);
   		 }
   	 }
   	 if(isConfig)
   		g_tool_ui_all.Panel_set.setListConfig(mapConfig);
     else
        g_tool_ui_all.Panel_set.setListCheck(mapCheck);
   }


	// ��ȡ�汾
	public String send_cmd_get_version() {
		String cmd_len = "0001";
		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + cmd_len + CMD_SET_GET_VERSION;
		String cmd_chk = checkSum(cmd_str);

		System.out.println("send_cmd_get_version�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	// ����ping��ⱨ��
	public String send_cmd_ping(String ping_url) {
		int cmd_len = 0;
		cmd_len = cmd_len + 1;
		cmd_len = cmd_len + ping_url.length();
		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + num_str_2byte(cmd_len) + CMD_PING_REQUEST
				+ strTo16hexstr(ping_url);
		String cmd_chk = checkSum(cmd_str);

		System.out.println("send_cmd_ping�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	// �����ռ�logָ��
	public String send_cmd_get_log() {
		String cmd_len = "0001";
		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + cmd_len + CMD_LOG_REQUEST;
		String cmd_chk = checkSum(cmd_str);

		System.out.println("send_cmd_get_log�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	/*
	 * 1.300msû���յ����·��� 2.���͵������ݱ��ģ�����ȴ�ack���� 3.�жϻ��ƣ�
	 */
	public int wait_ack(String send_frame_data) {
		int ret = 0;
		// �����ݱ��Ĳ���Ҫ�ȴ�Ack
		if (send_frame_data.substring(0, 2).equals(FRAME_DATA_TYPE) != true) {
			System.out.println("now send frame is ack not wait");
			return 1;
		}

		if (send_frame_data.substring(10, 12).equals(CMD_FRAG_DATA_ACK)) {
			System.out.println("��Ƭ���� ����ҪACK");
			return 1;
		}

		int frame_last_expected_id = str16_to_int16(send_frame_data.substring(2, 6));

		int count = 0;
		while (true) {
			try {
				if (count++ > 300) {
					break;
				}
				Thread.sleep(1);
				if (frame_expected_id > frame_last_expected_id) {
					ret = 1;
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("resend wait_ack count:" + count);
		return ret;
	}

	public int wait_frag_expect_id(int frag_expect_id) {
		// 50ms�ж�һ��
		int ret = 0;
		int count = 0;
		while (true) {
			try {
				// 5Sû�յ����ģ�������ʧ��
				if (count++ > 100) {
					break;
				}
				Thread.sleep(50); // ��ʱ2��
				if (frag_expect_id == frame_input_frag_ack_flag) {
					ret = 1;
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	// �����յ���Ƭȷ�ϱ���
	// ��ʽ��CMD_FRAG_DATA_ACK +
	public String send_cmd_frag_ack() {
		String cmd_len = "0005";
		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + cmd_len + CMD_FRAG_DATA_ACK + frame_cmd_id
				+ frame_input_frag_ack_id + fRAME_CMD_TRUE;
		String cmd_chk = checkSum(cmd_str);

		System.out.println("send_cmd_frag_ack�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	// ������������
	public String send_cmd_upgrade_system() {
		String cmd_len = "0001";
		String cmd_str = FRAME_DATA_TYPE + numToHex16(frame_expected_id) + cmd_len + CMD_SYS_FIRMWARE_SET;
		String cmd_chk = checkSum(cmd_str);

		System.out.println("send_cmd_upgrade_system�� " + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

		return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
	}

	/*
	 * 9F �������(2�ֽ�) ���ݳ��� (2�ֽ�) 14 size�������ļ���С��4�ֽڣ� ��Ƭ�ţ�2�ֽڣ� ƫ������4�ֽڣ� ���ȣ�4�ֽڣ�
	 * ��Ƭ�ļ� CheckSum 1 2 2 1 4 2 4 4
	 */
	// ���������ļ����豸
	public int send_cmd_upload_update_file(String file_name, tool_uart serialPort) throws Exception {
		byte[] tmp = new byte[1002 - 4];
		byte[] buffer = new byte[1024];
		int buffer_p = 0;
		int count;
		int i = 0;
		int fragmentation_id = 1;
		int ret = 1;

		// �ӵ�һ���˷�Ƭ��ʼ����ʼ��
		frame_input_frag_ack_flag = 0;

		FileInputStream in = new FileInputStream(file_name);
		File fd = new File(file_name);

		while ((count = in.read(tmp)) != -1) {
			buffer_p = 0;
			buffer[buffer_p] = (byte) 0x5a;
			buffer_p += 1;
			buffer[buffer_p] = (byte) 0x5a;
			buffer_p += 1;
			buffer[buffer_p] = (byte) 0x9f;

			// 2�ֽ�"�������"
			byte[] frame_id_int = intToByteArray4(frame_expected_id);

			for (int j = 0; j < frame_id_int.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = (byte) frame_id_int[j];
			}

			// 2�ֽ�"���ݳ���"
			byte[] frame_data_len = intToByteArray4(FRAME_FRAG_HEAD_LEN + count);

			System.out.println("data len " + (FRAME_FRAG_HEAD_LEN + count));

			for (int j = 0; j < frame_data_len.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = (byte) frame_data_len[j];
			}

			// cmd id 0x14
			buffer_p += 1;
			buffer[buffer_p] = (byte) (Integer.valueOf(CMD_SYS_FIRMWARE_UPLOAD_O) & 0xff);

			// �ļ�����
			byte[] file_len = intToByteArray8((int) fd.length()); // 4�ֽڡ��ļ���С��
			for (int j = 0; j < file_len.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = file_len[j];
			}

			// ��Ƭ��
			byte[] fragmentation_id_2 = intToByteArray4(fragmentation_id); // 2�ֽڡ���Ƭ�š�
			for (int j = 0; j < fragmentation_id_2.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = fragmentation_id_2[j];
			}

			// ƫ����
			byte[] bias = intToByteArray8(count); // 4�ֽڡ�ƫ������
			for (int j = 0; j < bias.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = bias[j];
			}

			// ��Ƭ����
			byte[] changdu = intToByteArray8(count); // 4�ֽڡ���Ƭ���ȡ�
			for (int j = 0; j < changdu.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = changdu[j];
			}

			// ���ݿ���
			for (i = 0; i < count; i++) {
				buffer_p += 1;
				buffer[buffer_p] = tmp[i];
			}

			// У����
			byte check_sum = do_check_sum(buffer, 2, buffer_p + 1);
			System.out.println("check_sum: " + check_sum);
			buffer_p += 1;
			buffer[buffer_p] = check_sum;

			// ����β��
			buffer_p += 1;
			buffer[buffer_p] = (byte) 0xa5;
			buffer_p += 1;
			buffer[buffer_p] = (byte) 0xa5;

			// �������ݽӿ�
			byte[] send_buffer = new byte[buffer_p + 1];
			System.out.println("length: " + (buffer_p + 1));

			for (i = 0; i < buffer_p + 1; i++) {
				send_buffer[i] = buffer[i];
			}

			String data_read = new String(bytesToHexString(send_buffer).getBytes(), "gb2312");
			Thread.sleep(50);
			serialPort.sendComm(data_read);
			// ���ͳɹ�֮��ȴ���Ƭ���ĵ���
			if (wait_frag_expect_id(fragmentation_id) == 1) {
				System.out.println("��Ƭ�����ճɹ�����������");
			} else {
				ret = 0;
				break;
			}
			// ��ƬID + 1
			fragmentation_id += 1;
		}

		in.close();
		return ret;
	}

	// ����ǩ��֤�鵽�豸
	public int data_frame_data_x509_crt(String file_name, tool_uart serialPort) throws Exception {
		byte[] tmp = new byte[1002 - 4];
		byte[] buffer = new byte[1024];
		int buffer_p = 0;
		int count;
		int i = 0;
		int fragmentation_id = 1;
		int ret = 1;

		// �ӵ�һ���˷�Ƭ��ʼ����ʼ��
		frame_input_frag_ack_flag = 0;

		FileInputStream in = new FileInputStream(file_name);
		File fd = new File(file_name);

		while ((count = in.read(tmp)) != -1) {
			buffer_p = 0;
			buffer[buffer_p] = (byte) 0x5a;
			buffer_p += 1;
			buffer[buffer_p] = (byte) 0x5a;
			buffer_p += 1;
			buffer[buffer_p] = (byte) 0x9f;

			// 2�ֽ�"�������"
			byte[] frame_id_int = intToByteArray4(frame_expected_id);

			for (int j = 0; j < frame_id_int.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = (byte) frame_id_int[j];
			}

			// 2�ֽ�"���ݳ���"
			byte[] frame_data_len = intToByteArray4(FRAME_FRAG_HEAD_LEN + count);

			System.out.println("data len " + (FRAME_FRAG_HEAD_LEN + count));

			for (int j = 0; j < frame_data_len.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = (byte) frame_data_len[j];
			}

			// cmd id 0x14
			buffer_p += 1;
			buffer[buffer_p] = (byte) (Integer.valueOf(CMD_CERTIFICATE_UPLOAD_O) & 0xff);

			// �ļ�����
			byte[] file_len = intToByteArray8((int) fd.length()); // 4�ֽڡ��ļ���С��
			for (int j = 0; j < file_len.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = file_len[j];
			}

			// ��Ƭ��
			byte[] fragmentation_id_2 = intToByteArray4(fragmentation_id); // 2�ֽڡ���Ƭ�š�
			for (int j = 0; j < fragmentation_id_2.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = fragmentation_id_2[j];
			}

			// ƫ����
			byte[] bias = intToByteArray8(count); // 4�ֽڡ�ƫ������
			for (int j = 0; j < bias.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = bias[j];
			}

			// ��Ƭ����
			byte[] changdu = intToByteArray8(count); // 4�ֽڡ���Ƭ���ȡ�
			for (int j = 0; j < changdu.length; j++) {
				buffer_p += 1;
				buffer[buffer_p] = changdu[j];
			}

			// ���ݿ���
			for (i = 0; i < count; i++) {
				buffer_p += 1;
				buffer[buffer_p] = tmp[i];
			}

			// У����
			byte check_sum = do_check_sum(buffer, 2, buffer_p + 1);
			System.out.println("check_sum: " + check_sum);
			buffer_p += 1;
			buffer[buffer_p] = check_sum;

			// ����β��
			buffer_p += 1;
			buffer[buffer_p] = (byte) 0xa5;
			buffer_p += 1;
			buffer[buffer_p] = (byte) 0xa5;

			// �������ݽӿ�
			byte[] send_buffer = new byte[buffer_p + 1];
			System.out.println("length: " + (buffer_p + 1));

			for (i = 0; i < buffer_p + 1; i++) {
				send_buffer[i] = buffer[i];
			}

			String data_read = new String(bytesToHexString(send_buffer).getBytes(), "gb2312");
			Thread.sleep(50);
			serialPort.sendComm(data_read);
			// ���ͳɹ�֮��ȴ���Ƭ���ĵ���
			if (wait_frag_expect_id(fragmentation_id) == 1) {
				System.out.println("��Ƭ�����ճɹ�����������");
			} else {
				ret = 0;
				break;
			}
			// ��ƬID + 1
			fragmentation_id += 1;

		}

		in.close();
		return ret;
	}

	// ID+1
	public void frame_id_excepted() {
		frame_expected_id += 1;
	}

	// ID+1
	public void frame_id_init() {
		frame_expected_id = 1;
	}

	// ack ����
	public int frame_ack_process() {
		int ret = 0;

		if (frame_data_len.equals(FRAME_ACK_LEN) && frame_data_load.equals(FRAME_ACK_LOAD)
				&& OctHex(frame_expected_id).equals(frame_id)) {
			frame_id_excepted();
			ret = 1;
		}

		return ret;
	}

	// CMD_SYS_RUNTIME_REQUEST ���Ĵ���
	public int frame_input_tx_tx_runtime_request() {
		int ret = 1;
		String status_json = "";
		try {
			status_json = convertHexToString(frame_data_subcmd_load);
			System.out.println("frame_data_subcmd_load�� " + convertHexToString(frame_data_subcmd_load));
		} catch (Exception e) {
			System.out.println("frame_input_tx_tx_runtime_request ����");
		}

		JSONObject jsonObject = JSONObject.fromObject(status_json);
		g_dtu_device_info.tool_device_status_set((String) jsonObject.get("wan_rx"), (String) jsonObject.get("wan_tx"),
				(String) jsonObject.get("rum_time"));
		g_tool_ui_all.Panel_status_check.tool_status_set_textfield();
	//	g_tool_ui_all.Panel_x509.tool_status_set_req_info(jsonObject);

		return ret;
	}

	// ping����
	public int frame_input_ping_request() {
		int ret = 1;

		try {
			System.out.println("frame_input_ping_request �� " + convertHexToString(frame_data_subcmd_load));
			g_dtu_device_info.tool_device_ping_info(convertHexToString(frame_data_subcmd_load));
			g_tool_ui_all.Panel_status_check.tool_ping_set_textfield();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	// ����汾
	public int frame_input_get_softversion() {
		int ret = 1;

		try {
			System.out.println("frame_input_get_softversion �� " + convertHexToString(frame_data_subcmd_load));
			g_dtu_device_info.tool_device_softversion_info(convertHexToString(frame_data_subcmd_load));
			g_tool_ui_all.Panel_status_check.tool_softversion_set_textfield();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	// ��Ƭ���Ľ��ճɹ���
	public int frame_input_frag_ack() {
		// frame_input_frag_ack_flag =
		// Integer.valueOf(frame_data_load.substring(4,8));
		// ��ȡ����ƬID
		frame_input_frag_ack_flag = Integer.parseInt(frame_data_load.substring(4, 8).replaceAll("^0[x|X]", ""), 16);
		System.out.println("��Ƭ���ճɹ���" + frame_input_frag_ack_flag);
		return 1;
	}

	// CMD_CONFIG_REQUEST ���Ĵ���
	public int frame_input_ini_file_request() {
		int ret = 1;

		// �� frame_data
		String frame_data_ini = frame_data.substring(40, frame_data.length() - 2);
		try {
			frame_data_ini = convertHexToString(frame_data_ini);
			// ���ݱ��浽ini�ļ�
			ParaToFile(frame_data_ini, file_ini_name);
			g_dtu_device_info.tool_device_info_get();
			g_tool_ui_all.Panel_every.tool_ui_panel_every_text_update(g_dtu_device_info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	// ���������ļ�
	public int frame_input_ini_file_download() {
		int ret = 1;

		// �� frame_data
		String frame_data_ini = frame_data.substring(40, frame_data.length() - 2);
		try {
			frame_data_ini = convertHexToString(frame_data_ini);
			// ���ݱ��浽ini�ļ�
			System.out.println("g_dtu_device_info.device_config_download_config_file_path "
					+ g_dtu_device_info.device_config_download_config_file_path);
			ParaToFile(frame_data_ini, g_dtu_device_info.device_config_download_config_file_path);
			iniAccept = false;
			g_uart_process.sendComm(send_cmd_run_shell_2(CMD_INI_OUT, "md5sum /etc/config/sys_param_gb.ini "));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	// ��������֤��
	public int frame_x509_req_upload() {
		int ret = 1;
		if (g_dtu_device_info.device_x509_req_file_path == null
				|| g_dtu_device_info.device_x509_req_file_path.equals("")){
			iniAccept = false;
			try {
				g_uart_process.sendComm(send_cmd_run_shell_2(CMD_CREATE, "gmssl req -in /etc/config/req/site_req.der -noout -text -inform der"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ret;
		}
			
		// �� frame_data
		String frame_data_req = frame_data.substring(40, frame_data.length() - 2);
		System.out.println("frame_data_req: " + frame_data_req);
		try {
			byte[] result = hexToByteArray(frame_data_req);
			System.out.println(
					"g_dtu_device_info.device_x509_req_file_path " + g_dtu_device_info.device_x509_req_file_path);
			// ParaToFile(frame_data_req,g_dtu_device_info.device_x509_req_file_path);
			createFile(g_dtu_device_info.device_x509_req_file_path, result);
			g_uart_process.sendComm(send_cmd_run_shell_2(CMD_P10, "md5sum /etc/config/req/site_req.der"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	// log
	// �������̣� 1. �����ռ�logָ�� 2. ���շ�Ƭ���� 3. �ظ�ACK 4. �ظ����շ�Ƭ�ɹ����� 5. �ص�1 6. ����md5�ļ�
	public int frame_input_log() {
		int ret = 1;
		g_tool_ui_all.Panel_log.tool_log_set(frame_data_frag_loadString);
		// ��ʱ10MS
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			g_uart_process.sendComm(send_cmd_frag_ack());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	// ���������ļ����豸
	public String data_frame_data_ini_file_upload() {
		try {
			String file_down = g_dtu_device_info.device_config_upload_config_file_path;
			// ��ȡ�ļ�
			String cmd_str = "";
			String cmd_frame_id = numToHex_2_byte(frame_expected_id);
			String config_file_content = readFileContent(file_down);
			String cmd_frame_len = numToHex_2_byte(
					convertStringToHex(config_file_content).length() / 2 + FRAME_FRAG_HEAD_LEN);
			String cmd_frame_subid = CMD_CONFIG_UPLOAD;
			String cmd_file_len = numToHex_4_byte(convertStringToHex(config_file_content).length() / 2);
			String cmd_frag_id = "0001";
			String cmd_frag_offset = "00000000";
			String cmd_frag_len = cmd_file_len;

			cmd_str = FRAME_DATA_TYPE + cmd_frame_id + cmd_frame_len + cmd_frame_subid + cmd_file_len + cmd_frag_id
					+ cmd_frag_offset + cmd_frag_len + convertStringToHex(config_file_content);

			String cmd_chk = checkSum(cmd_str);

			System.out.println("send data��" + FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);

			return (FRAME_BEGIN + cmd_str + cmd_chk + FRAME_END);
		} catch (Exception e) {
			System.out.println("send_cmd_config_upload error " + e);
		}
		return "";
	}

	// ͬ��������
	public int frame_sync_process() {
		int ret = 1;

		// �ظ�ACK
		try {
			g_uart_process.sendComm(send_cmd_ack());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// ID+1
		frame_id_init();

		return ret;
	}

	// ���ݱ���
	public int frame_data_process() {
		int ret = 1;

		// �ظ�ACK
		try {
			g_uart_process.sendComm(send_cmd_ack());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// ID+1
		frame_id_excepted();
		// ����CMD id
		frame_parser_cmd_id_process();

		switch (frame_cmd_id) {
		case CMD_CONFIG_REQUEST:
			frame_input_ini_file_request();
			break;
		case CMD_SYS_RUNTIME_REQUEST:
			frame_input_tx_tx_runtime_request();
			break;
		case CMD_FRAG_DATA_ACK:
			frame_input_frag_ack();
			break;
		case CMD_PING_REQUEST:
			frame_input_ping_request();
			break;
		case CMD_SET_GET_VERSION:
			frame_input_get_softversion();
			break;
		case CMD_CONFIG_DOWNLOAD:
			frame_input_ini_file_download();
			break;
		case CMD_LOG_REQUEST:
			frame_input_log();
			break;
		case CMD_CERTIFICATE_REQUEST:
			frame_x509_req_upload();
			break;
		case CMD_RUN_SHELL_2:
			frame_process_shell_2();
			break;
		case CMD_CERTIFICATE_UPLOAD_O:
			JOptionPane.showMessageDialog(null, "����ǩ��֤��ɹ�");
			break;

		}
		return ret;
	}

	// ���Ľ�����Ĭ�Ͻ����ܳɹ�
	public int frame_parser_cmd_id_process() {
		int ret = 1;

		frame_cmd_id = frame_data.substring(10, 12);
		System.out.println("frame_cmd_id�� " + frame_cmd_id);
		// todo���з�ƬID���ж�����ģ�����Ҫ����
		if (frame_cmd_id.equals(CMD_LOG_REQUEST)) {
			frame_input_frag_ack_id = frame_data.substring(20, 24);
			System.out.println("frame_input_frag_ack_id�� " + frame_input_frag_ack_id);
			frame_data_frag_loadString = frame_data.substring(40, frame_data.length() - 2);
			try {
				frame_data_frag_loadString = convertHexToString(frame_data_frag_loadString);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("frame_data_frag_loadString: " +
			// frame_data_frag_loadString);
		}

		return ret;
	}

	// ���Ľ�����Ĭ�Ͻ����ܳɹ�
	/*
	 * 
	 * 
	 * */
	public int frame_parser_process() {
		int ret = 1;

		frame_type = frame_data.substring(0, 2);
		frame_head = frame_data.substring(0, 2);
		frame_id = frame_data.substring(2, 6);
		frame_data_len = frame_data.substring(6, 10);
		frame_data_load = frame_data.substring(10, frame_data.length() - 2);

		if (frame_type.equals(FRAME_SYNC_TYPE) == false) {
			System.out.println("back frame_type�� " + frame_type);
			frame_data_subcmd_load = frame_data.substring(12, frame_data.length() - 2);
		}

		frame_data_check = frame_data.substring(frame_data.length() - 2, frame_data.length());

		return ret;
	}

	// ���ʹ���ack��data��sync������
	public int frame_type_process() {
		int ret = 0;

		switch (frame_type) {
		case FRAME_ACK_TYPE:
			ret = frame_ack_process();
			System.out.println("ack���Ĵ���ɹ�");
			break;
		case FRAME_DATA_TYPE:
			ret = frame_data_process();
			break;
		case FRAME_SYNC_TYPE:
			ret = frame_sync_process();
			break;
		}

		return ret;
	}

	// �������
	public void data_input(String read_data_buf) throws Exception {
		int ret;

		// ���ݸ���
		data = data.concat(read_data_buf);

		while (true) {
			// �ҵ�һ��������֡
			ret = data_find_one_frame();
			if (ret == 0 || frame_data.equals("")) {
				break;
			}

			// ��ʼ�������� 1.У�� 2.���Ľ��� 3. �������ʹ���
			// 1.У��
			if (frame_check() == 0) {
				System.out.println("����У��ʧ�ܣ�" + frame_data);
				continue;
			}

			// 2.���Ľ���
			if (frame_parser_process() == 0) {
				System.out.println("���ݱ��Ľ���ʧ�ܣ� " + frame_data);
				continue;
			}

			// 3.�������ʹ���
			if (frame_type_process() == 0) {
				System.out.println("���ݴ���ʧ�ܣ� " + frame_data);
				continue;
			}

			System.out.println("���Ĵ���ɹ� ��");
		}
	}

	// У��
	public int frame_check() {
		int ret = 0;
		String ret1;
		String ret2;
		byte ret_byte_1;
		byte ret_byte_2;

		ret1 = data_checkSum2(frame_data.substring(0, frame_data.length() - 2));
		ret2 = frame_data.substring(frame_data.length() - 2, frame_data.length());

		ret_byte_1 = hexToByteArray(ret1)[0];
		ret_byte_2 = hexToByteArray(ret2)[0];

		// У��ɹ�
		if ((ret_byte_1 + ret_byte_2 + 0x1) == 0x00) {
			ret = 1;
		}

		// Ĭ�Ϸ���0��У��ʧ��
		return ret;
	}
}