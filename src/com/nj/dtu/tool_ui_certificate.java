package com.nj.dtu;

import net.sf.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class tool_ui_certificate extends tool_ui_base {
	// 串口实例
	tool_uart serialPort;
	// 数据交互实例
	tool_data_process g_data_process;
	tool_device_info g_device_info;

	JComboBox box_device_tunnel;
	JTextField text_field_user;
	JTextArea text_field_encry;
	JScrollPane scroll_encry;
	JTextArea text_field_cer;
	JScrollPane scroll_cer;
	JTabbedPane jTabbedpane = new JTabbedPane();// 存放选项卡的组件

	// 按钮
	JButton tool_cer_out_button;
	JButton tool_pio_out_button;
	JButton tool_cer_in_button;
	JButton tool_cer_info_button;


	Boolean isSend = false;

	JPanel certConfigPanel;

	public tool_ui_certificate(tool_uart serial_port, tool_data_process data_process, tool_device_info device_info) {
		serialPort = serial_port;
		g_data_process = data_process;
		g_device_info = device_info;
	}

	public JPanel certConfigPanel() {
		if (certConfigPanel != null) {
			return certConfigPanel;
		}
		certConfigPanel = new JPanel();
		certConfigPanel.setPreferredSize(new Dimension(550, 300));
		certConfigPanel.setLayout(null);
		certConfigPanel.setBorder(BorderFactory.createTitledBorder("证书配置"));

		// 标签起始位置
		int x_lable_location = 20;
		int y_lable_location = 25;
		// 文本框起始位置
		int x_text_location = 100;
		int y_text_location = 50;

		// 步长
		int x_step = 30;
		int y_step = 30;

		// 文本框长度和宽度
		int x_text_length = 100;
		int y_text_heigth = 25;
		int text_columns = 20;

		// 标签框长度和宽度
		int x_lable_length = 80;

		certConfigPanel.add(buildJLabel("隧道编号:", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String isenc1[] = { "隧道1", "隧道2" };
		box_device_tunnel = buildJComboBox("", "device_tunnel", isenc1, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		certConfigPanel.add(box_device_tunnel);

		x_lable_location += x_step * 8;
		x_text_location = x_lable_location + x_lable_length - 15;
		certConfigPanel.add(buildJLabel("使用者:", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_user = buildJTextField("", "user", text_columns, x_text_location, y_lable_location, 200,
				y_text_heigth);
		certConfigPanel.add(text_field_user);

		JPanel jpanelFirst = new JPanel();
		jTabbedpane.addTab("加密证书信息", null, jpanelFirst, "first");
		JPanel jpanelSecond = new JPanel();
		jTabbedpane.addTab("签名证书信息", null, jpanelSecond, "second");
		jTabbedpane.setBounds(20, 55, 550, 210);

		text_field_encry = buildJJTextArea(9, 69, 0, 0, 550, 220);
		scroll_encry = buildJJScrollPane(text_field_encry, 0, 0, 550, 220);
		jpanelFirst.add(scroll_encry);
		scroll_encry.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_encry.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		text_field_encry.setEditable(false);

		text_field_cer = buildJJTextArea(9, 69, 0, 0, 550, 220);
		scroll_cer = buildJJScrollPane(text_field_cer, 0, 0, 550, 220);
		jpanelSecond.add(scroll_cer);
		scroll_cer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_cer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		text_field_cer.setEditable(false);
		certConfigPanel.add(jTabbedpane);

		tool_cer_out_button = buildJButtonSmall("生成请求文件", 20, 270, 120, 25);
		tool_pio_out_button = buildJButtonSmall("导出P10请求文件", 153, 270, 120, 25);
		tool_cer_in_button = buildJButtonSmall("导入签发证书", 286, 270, 120, 25);
		tool_cer_info_button = buildJButtonSmall("获取证书信息", 420, 270, 120, 25);

	//	tool_cer_out_button
		certConfigPanel.add(tool_cer_out_button);
		certConfigPanel.add(tool_pio_out_button);
		certConfigPanel.add(tool_cer_in_button);
		certConfigPanel.add(tool_cer_info_button);

		info_button_listener();
		get_crt_upload_button_listener();
		get_csr_upload_button_listener();
		set_button_listener();
		return certConfigPanel;
	}

	// 获取证书信息
	public void info_button_listener() {
		tool_cer_info_button.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				if(serialPort.serialPort==null){
					JOptionPane.showMessageDialog(null, "请先打开串口！");
					return;
				}
				if(isSend)
					return;
				isSend = true;
				try {
				
	                Thread thread = new Thread(){
	                 public void run()
	                 {
	               
	                     try {
	                    		serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_ENC,
	    								"gmssl x509 -in /etc/ipsectool/cert_enc.cer  -noout -text"));
	                    		Thread.sleep(20); 
	    						serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_SIG,
	    								"gmssl x509 -in /etc/ipsectool/cert_sig.cer  -noout -text"));
	    						Thread.sleep(20); 
								serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_IN,
										"/zr_bin/cert_test.sh"));
								isSend = false;
	                    	
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                          
	                  }                                   
	                };
	                thread.start();
					
					

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	// 导入签发证书
	public void get_crt_upload_button_listener() {
		tool_cer_in_button.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				if(serialPort.serialPort==null){
					JOptionPane.showMessageDialog(null, "请先打开串口！");
					return;
				}
					
				try {
					// 是 0;否 1
					int n = JOptionPane.showConfirmDialog(null, "请选择签发证书", "", JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						JFileChooser tool_file_chooser_select_csr_file = new JFileChooser();
						tool_file_chooser_select_csr_file.setCurrentDirectory(new File("."));
						tool_file_chooser_select_csr_file.setFileSelectionMode(0);
						int state = tool_file_chooser_select_csr_file.showOpenDialog(null);
						if (state == 1) {
							return;// 撤销则返回
						} else {
							File f = tool_file_chooser_select_csr_file.getSelectedFile();
							g_device_info.device_x509_crt_file_path = f.getAbsolutePath();
							System.out.println("g_device_info.device_x509_crt_file_path :"
									+ g_device_info.device_x509_crt_file_path);
							
										 g_data_process.g_tool_ui_all.loading("正在导入");							
							
							new Thread() {	
								public void run() {
									
							try {
								Thread.sleep(100);
								g_data_process.data_frame_data_x509_crt(g_device_info.device_x509_crt_file_path,
										serialPort);
								Thread.sleep(100);
								serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_IN,
										"/zr_bin/cert_test.sh"));
								g_data_process.g_tool_ui_all.stop();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
								}
							}.start();
							
							
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
    //生成证书
    public void set_button_listener() {
    	tool_cer_out_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	  //是 0;否 1
                    int n = JOptionPane.showConfirmDialog(null, "确认生成?", "", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                    JSONObject subj = new JSONObject();
                    subj.put("mode", "set");
                    subj.put("country", "11");
                    g_device_info.device_x509_req_file_path = "";
                    g_data_process.g_tool_ui_all.loading("正在生成...");
					new Thread() {
						@SuppressWarnings("static-access")
						public void run() {
							 try {
								   serialPort.sendComm(g_data_process.send_cmd_csr_upload(subj.toString()));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
						}
					}.start();
                 
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    //导出请求证书1
    public void get_csr_upload_button_listener() {
    	tool_pio_out_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(serialPort.serialPort==null){
					JOptionPane.showMessageDialog(null, "请先打开串口！");
					return;
				}
                try {
                    JSONObject subj = new JSONObject();
                    subj.put("mode", "get");

                    //是 0;否 1
                    int n = JOptionPane.showConfirmDialog(null, "请选择保存路径和文件名", "", JOptionPane.YES_NO_OPTION);
                    Long timestamp = System.currentTimeMillis();
                    String name  ="dtu_"+g_device_info.device_name+"_"+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(timestamp))+"_site.p10";
                    if (n == 0) {
                    	JFileChooser choose = new JFileChooser();
                    	choose.setCurrentDirectory(new File("."));      
                    	choose.setSelectedFile(new File(name));
                 	
                        int state = choose.showSaveDialog(new JPanel());//此句是打开文件选择器界面的触发语句
                        if (state == 1) {
                            return;//撤销则返回
                        } else {
                        	 File f = choose.getSelectedFile();// dir为选择到的目录 
                      
                            g_device_info.device_x509_req_file_path = f.getAbsolutePath();
                            System.out.println("g_dtu_device_info.device_x509_req_file_path " + g_device_info.device_x509_req_file_path);
                            g_data_process.g_tool_ui_all.loading("正在导出...");
        					new Thread() {
        						@SuppressWarnings("static-access")
        						public void run() {
        							 try {
        								 serialPort.sendComm(g_data_process.send_cmd_csr_upload(subj.toString()));
        							}  catch (Exception e) {
        								// TODO Auto-generated catch block
        								e.printStackTrace();
        							} 
        						}
        					}.start();
                           
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }



	public void setInfo(String id,String info) {
		if (id.equals("01")) {
			text_field_encry.setText(info);
		} else {
			text_field_cer.setText(info);
		}

	}
}
