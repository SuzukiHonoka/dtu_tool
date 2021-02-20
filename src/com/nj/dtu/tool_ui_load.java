package com.nj.dtu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;

public class tool_ui_load extends tool_ui_base {

	JComboBox tool_uart_name_list_box;
	JComboBox tool_baud_rate_box;
	JComboBox tool_data_bit_box;
	JComboBox tool_check_bit_box;
	JComboBox tool_stop_bit_box;
	JButton tool_open_uart_button;
	
	JTextField text_ip;
	JButton tool_open_net_button;

	// ����ʵ��
	tool_uart serialPort;
	// ���ݽ���ʵ��
	tool_data_process g_data_process;
	JPanel connectPanel;
	Thread dateThread;

	Boolean canRun = false;
	Boolean connect = false;
	String[] comItem = {};

	public tool_ui_load(tool_uart serial_port, tool_data_process data_process) {
		serialPort = serial_port;
		g_data_process = data_process;
	}



	private void open() {

		// ���ڲ���
		String uart_name = (String) tool_uart_name_list_box.getSelectedItem();
		int uart_baud_rate = Integer.parseInt((String) tool_baud_rate_box.getSelectedItem());
		int uart_data_bit = Integer.parseInt((String) tool_data_bit_box.getSelectedItem());
		int uart_stop_bit = Integer.parseInt((String) tool_stop_bit_box.getSelectedItem());

		int uart_check_bit = 0;
		String tip = (String) tool_check_bit_box.getSelectedItem();
		if (tip.equals("��У��")) {
			uart_check_bit = 1;
		} else if (tip.equals("żУ��")) {
			uart_check_bit = 2;
		}

		if (uart_name == null || uart_name.equals("")) {
			JOptionPane.showMessageDialog(null, "��ѡ����Ч�Ĵ���");
			return;
		}

		// ���ڲ�����ʼ��
		ParamConfig paramConfig = new ParamConfig(uart_name, uart_baud_rate, uart_check_bit, uart_data_bit,
				uart_stop_bit);

		System.out.println("uart_button_open_listener");
		try {
			if (serialPort.init(paramConfig) == 1) {
				tool_open_uart_button.setText("�رմ���");

				/// ������̴߳�����ʾ
				Thread thread = new Thread() {
					public void run() {
						// ��ȡ�豸����
						try {
							Thread.sleep(10);
							serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_GET_MD5,
									"/zr_bin/root_config.sh export"));	
							Thread.sleep(100);
							serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_GET_CHECK_USER,
									"/zr_bin/root_config.sh list_role_check"));	
							
							
//							Thread.sleep(1000);
//							serialPort.sendComm(g_data_process.send_cmd_config_request());
//							Thread.sleep(100);
//							serialPort.sendComm(g_data_process.send_cmd_status_rx_tx_runtime());			
//							Thread.sleep(100);
//							serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_GET_LOG,
//									"/zr_bin/ini_config get LOG:logon"));	
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				};
				thread.start();
			//	runDate();

				JOptionPane.showMessageDialog(null, "���ڴ򿪳ɹ���");
				connect = true;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void close() {
		System.out.println("uart_button_close_listener");
		try {
			if (serialPort.closeSerialPort() == 1) {
				JOptionPane.showMessageDialog(null, "���ڹرճɹ���");
				connect = false;
				//stopDate();
				tool_open_uart_button.setText("�򿪴���");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void runDate() {
		if (dateThread == null) {
			dateThread = new Thread() {
				@SuppressWarnings("static-access")
				public void run() {
					while(true){
					// ��ȡ�豸����
					try {
						Thread.sleep(5000);
						if(canRun)
						serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_RUN_DATE
								,"date"));
				
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				}
			};
		}
		canRun = true;
		dateThread.start();
	}

	private void stopDate() {
		canRun = false;
		dateThread.interrupt();
	}

	private void com_item_update() {
		String tool_uart_list[] = {};
		try {
			tool_uart_list = serialPort.getComName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(comItem.length);
		System.out.println(tool_uart_list.length);
		if (comItem.length != tool_uart_list.length) {
			update(tool_uart_list);
		} else {
			for (int i = 0; i < comItem.length; i++) {
				if (comItem[i] != tool_uart_list[i]) {
					update(tool_uart_list);
					break;
				}
			}
		}
	}

	private void update(String[] com) {
		DefaultComboBoxModel codeTypeModel = new DefaultComboBoxModel(com);
		tool_uart_name_list_box.setModel(codeTypeModel);
		comItem = com;
	}

	public JPanel tool_ui_load_init() throws Exception {
		if(connectPanel!=null)
			return connectPanel;
		connectPanel = new JPanel();
		connectPanel.setPreferredSize(new Dimension(990, 85));
		connectPanel.setLayout(null);
		connectPanel.setBorder(BorderFactory.createTitledBorder("����"));

		connectPanel.add(buildJLabel("���ں�", 10, 25, 80, 20));
		String tool_uart_list[] = this.serialPort.getComName();
		tool_uart_name_list_box = super.buildJComboBox("", "chuankouhaolv", tool_uart_list, 20, 63, 25, 80, 20);
		connectPanel.add(tool_uart_name_list_box);
		tool_uart_name_list_box.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				com_item_update();
				System.out.println("mouseEntered");
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		connectPanel.add(buildJLabel("������", 180, 25, 80, 20));
		String tool_baud_rate_list[] = { "9600", "19200", "115200" };
		tool_baud_rate_box = super.buildJComboBox("115200", "botelv", tool_baud_rate_list, 20, 235, 25, 80, 20);
		tool_baud_rate_box.setSelectedIndex(2);
		connectPanel.add(tool_baud_rate_box);

		connectPanel.add(buildJLabel("����λ", 345, 25, 80, 20));
		String data_bit[] = { "7", "8" };
		tool_data_bit_box = buildJComboBox("8", "data_bit", data_bit, 20, 400, 25, 80, 20);
		connectPanel.add(tool_data_bit_box);

		connectPanel.add(buildJLabel("У��λ", 510, 25, 80, 20));
		String verfy_bit[] = { "��У��", "żУ��", "��У��" };
		tool_check_bit_box = buildJComboBox("��У��", "data_bit", verfy_bit, 20, 565, 25, 80, 20);
		connectPanel.add(tool_check_bit_box);

		connectPanel.add(buildJLabel("ֹͣλ", 675, 25, 80, 20));
		String stop_bit[] = { "1", "2" };
		tool_stop_bit_box = buildJComboBox("", "stop_bit", stop_bit, 20, 733, 25, 80, 20);
		connectPanel.add(tool_stop_bit_box);

		tool_open_uart_button = buildJButton("�򿪴���", 850, 25, 120, 20);

		connectPanel.add(buildJLabel("ip��ַ", 10, 55, 80, 20));
		text_ip  = buildJTextField("192.168.1.30", "IP ADDRESS", 20, 63, 55, 185, 25);
		tool_open_net_button =  buildJButton("������", 280, 55, 120, 25);
	//	tool_open_net_button.addActionListener();
		connectPanel.add(text_ip);
		connectPanel.add(tool_open_net_button);
		// ��Ӽ����¼�
		uart_button_open_listener();
		net_button_open_listener();
		connectPanel.add(tool_open_uart_button);

		return connectPanel;
	}
	// ���ڴ򿪰�ť�ļ�������
	public void uart_button_open_listener() throws Exception {
		tool_open_uart_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tool_open_uart_button.getText().toString().equals("�򿪴���")) {
					open();
				} else {
					close();
				}

			}
		});
	}
	// ���ڴ򿪰�ť�ļ�������
	public void net_button_open_listener() throws Exception {
		tool_open_net_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tool_open_net_button.getText().toString().equals("������")) {
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							g_data_process.g_tool_ui_all.loading("��������");
							serialPort.initNet(text_ip.getText(), 8000);
						}
					}).start();
				} else {
					serialPort.closeNet();
					connect = false;
					tool_open_net_button.setText("������");
				}
			
				
			}
		});
	}
}
