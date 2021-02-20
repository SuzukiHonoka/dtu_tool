package com.nj.dtu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class tool_ui_system extends tool_ui_base {

    //串口实例
    tool_uart serialPort;
    //数据交互实例
    tool_data_process g_data_process;
    tool_device_info g_device_info;

    //按钮
    JButton tool_system_reboot_button;
    JTextField tool_text_field_update_file;
    JButton tool_button_select_update_file;
    JButton tool_button_upload_update_file;
    JFileChooser tool_file_chooser_select_update_file;

    //配置文件导出
    JButton tool_button_get_config_file;
    JTextField tool_text_get_config_file_path;
    JFileChooser tool_file_chooser_select_config_save_file;
    //配置文件上传
    JButton tool_button_upload_config_file;
    JTextField tool_text_upload_config_file_path;
    JFileChooser tool_file_chooser_select_config_upload_file;
    
    JPanel deviceUpgradePanel;
    JPanel configImportExportPanel;
    Boolean updateSuccess = false;
	String update_zip_name ="";
	

    

    public tool_ui_system(tool_uart serial_port, tool_data_process data_process, tool_device_info device_info) {
        serialPort = serial_port;
        g_data_process = data_process;
        g_device_info = device_info;
    }

    //
    public void tooi_ui_update_file_select() {
        // 为按钮绑定监听器
        tool_button_select_update_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 对话框
                tool_file_chooser_select_update_file.setFileSelectionMode(0);//设定只能选择到文件
                int state = tool_file_chooser_select_update_file.showOpenDialog(null);//此句是打开文件选择器界面的触发语句
                if (state == 1) {
                    return;//撤销则返回
                } else {
                    File f = tool_file_chooser_select_update_file.getSelectedFile();//f为选择到的目录
                    tool_text_field_update_file.setText(f.getAbsolutePath());
                    int index =  f.getAbsolutePath().lastIndexOf("-");
                    update_zip_name = f.getAbsolutePath().substring(index+1,f.getAbsolutePath().length()-4);
                  
                }
            }
        });
    }
    public void cat_vaersion_reuslt(String data){
    	g_data_process.g_tool_ui_all.stop();
    	if(data.contains(update_zip_name)){
    		updateSuccess = true;
    		 JOptionPane.showMessageDialog(null, "升级成功！");
    	}else{
    		JOptionPane.showMessageDialog(null, "升级失败！");
    	}
    	
    }
    public void cat_version(){
        ///单起个线程处理显示
        Thread thread = new Thread(){
         @SuppressWarnings("static-access")
		public void run()
         {
             try {
            	 Thread.sleep(10000); 
            	 while(true){
            		 if( updateSuccess)
            			 break;
            		 serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_VER,
         					"cat /etc/version")); 
            		 Thread.sleep(5000); 
            	 }
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                  
          }                                   
        };
        thread.start();
    	try {
    		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    //上传升级文件
    public void tooi_ui_update_file_upload() {
        tool_button_upload_update_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(tool_text_field_update_file.getText().toString().equals("")){
            		JOptionPane.showMessageDialog(null, "请选择文件");
            		return;
            	}
                    tool_button_select_update_file.enable(false);
                    g_data_process.g_tool_ui_all.loading("正在下发文件...");
                    ///单起个线程处理显示
                    Thread thread = new Thread(){
                     public void run()
                     {
                    	  try {
                    		  int ret = g_data_process.send_cmd_upload_update_file(tool_text_field_update_file.getText(), serialPort);
                         
                         if (ret == 1) {
                             //是 0;否 1
                        	 g_data_process.g_tool_ui_all.stop();
                             int n = JOptionPane.showConfirmDialog(null, "确认升级", "", JOptionPane.YES_NO_OPTION);
                             System.out.println("n " + n);
                             //升级系统，发送升级指令
                             if (n == 0) {
                             	 updateSuccess = false;
                             	g_data_process.g_tool_ui_all.loading("正在升级...");
                                 serialPort.sendComm(g_data_process.send_cmd_upgrade_system());
                               
                                 cat_version();
                             }
                         } else {
                        	 g_data_process.g_tool_ui_all.stop();
                             JOptionPane.showMessageDialog(null, "下发升级文件失败");
                         }
                         tool_button_select_update_file.enable(true);
                    	  } catch (NumberFormatException | IOException e1) {
                              // TODO Auto-generated catch block
                              e1.printStackTrace();
                          } catch (Exception e1) {
                              // TODO Auto-generated catch block
                              e1.printStackTrace();
                          }
                              
                      }                                   
                    };
                    thread.start();
                                     

               
            }
        });
    }
    
    

    //下载配置文件  导出
    public void tooi_ui_config_file_get() {
        tool_button_get_config_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int ret;
                    tool_button_get_config_file.enable(false);
                    //是 0;否 1
                    int n = JOptionPane.showConfirmDialog(null, "请选择保存路径和文件名", "", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        // default file.
                     
                      
                        tool_file_chooser_select_config_save_file.setCurrentDirectory(new File("."));      
                        tool_file_chooser_select_config_save_file.setSelectedFile(new File("dtu_system_config.ini"));
                 	
                        
                        int state = tool_file_chooser_select_config_save_file.showSaveDialog(new JPanel());//此句是打开文件选择器界面的触发语句
                        if (state == 1) {
                            return;//撤销则返回
                        } else {
                        	 File f = tool_file_chooser_select_config_save_file.getSelectedFile();// dir为选择到的目录 
                        	
                        
                            tool_text_get_config_file_path.setText(f.getAbsolutePath());
                            g_device_info.device_config_download_config_file_path = f.getAbsolutePath();
                            serialPort.sendComm(g_data_process.send_cmd_config_download());
                        }
                    }
                    tool_button_get_config_file.enable(true);
                } catch (NumberFormatException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }

    //上传配置文件  导入
    public void tooi_ui_config_file_upload() {
        tool_button_upload_config_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int ret;
                    tool_button_upload_config_file.enable(false);
                    //是 0;否 1
                    int n = JOptionPane.showConfirmDialog(null, "请选择配置文件", "", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        tool_file_chooser_select_config_upload_file.setFileSelectionMode(0);//设定只能选择到文件
                        int state = tool_file_chooser_select_config_upload_file.showOpenDialog(null);//此句是打开文件选择器界面的触发语句
                        if (state == 1) {
                            return;//撤销则返回
                        } else {
                            File f = tool_file_chooser_select_config_upload_file.getSelectedFile();
                            tool_text_upload_config_file_path.setText(f.getAbsolutePath());
                            g_device_info.device_config_upload_config_file_path = f.getAbsolutePath();
                            serialPort.sendComm(g_data_process.data_frame_data_ini_file_upload());
                            ///单起个线程处理显示
                            Thread thread = new Thread(){
                             public void run()
                             {
                            	 //获取设备参数
                                 try {
                                	 Thread.sleep(1000); 
                                	 serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_INI_IN,
     	    								" md5sum /etc/config/sys_param_gb.ini"));
            				
            					} catch (Exception e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					}
                                      
                              }                                   
                            };
                            thread.start();
                           

                        }
                    }
                    tool_button_upload_config_file.enable(true);
                } catch (NumberFormatException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }

    //重启系统
    public void tooi_ui_reboot_system() {
        tool_system_reboot_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
//                    serialPort.sendComm(g_data_process.send_cmd_run_shell("00","reboot"));
                    serialPort.sendComm(g_data_process.send_cmd_run_shell_2("01","gmssl x509 -in /etc/ipsectool/cert_enc.cer  -noout -text"));
                } catch (NumberFormatException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }

    public JPanel systemSetupPanel(JPanel systemSetupPanel) {
        systemSetupPanel.setBorder(BorderFactory.createTitledBorder("系统设置"));

        // 标签起始位置
        int x_lable_location = 40;
        int y_lable_location = 40;

        // 文本框起始位置
        int x_text_location = 100;
        int y_text_location = 20;

        // 步长
        int x_step = 10;
        int y_step = 30;

        // 文本框长度和宽度
        int x_text_length = 130;
        int y_text_heigth = 25;
        int text_columns = 20;

        // 标签框长度和宽度
        int x_lable_length = 80;

        systemSetupPanel.add(buildJLabel("升级:", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));

        //升级功能
        y_lable_location += y_step;
        y_text_location += y_step;
        systemSetupPanel.add(buildJLabel("升级文件", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));

        x_lable_location = x_lable_location + x_lable_length - x_step / 2;
        tool_text_field_update_file = buildJTextField("", "updatefile", text_columns, x_lable_location, y_text_location, x_text_length * 2, y_text_heigth);
        systemSetupPanel.add(tool_text_field_update_file);

        x_lable_location = x_lable_location + x_text_length * 2 + x_step * 3;
        tool_button_select_update_file = buildJButton("选择文件", x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        systemSetupPanel.add(tool_button_select_update_file);
        tool_file_chooser_select_update_file = new JFileChooser();
        tool_file_chooser_select_update_file.setCurrentDirectory(new File(""));
        systemSetupPanel.add(tool_file_chooser_select_update_file);
        tooi_ui_update_file_select();

        x_lable_location = x_lable_location + x_text_length + x_step;
        tool_button_upload_update_file = buildJButton("上传文件", x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        systemSetupPanel.add(tool_button_upload_update_file);
        tooi_ui_update_file_upload();

        y_lable_location = y_lable_location + y_text_heigth + y_step;
        x_lable_location = 20;
        systemSetupPanel.add(buildJLabel("配置文件导入导出:", x_lable_location, y_lable_location, x_lable_length * 5, y_text_heigth));

        y_lable_location = y_lable_location + y_text_heigth + x_step;
        x_text_length = x_text_length / 2;
        tool_button_get_config_file = buildJButton("导出", x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        systemSetupPanel.add(tool_button_get_config_file);
        x_lable_location = x_lable_location + x_text_length + x_step;
        tool_text_get_config_file_path = buildJTextField("", "保存文件路径", text_columns, x_lable_location, y_lable_location, x_text_length * 4, y_text_heigth);
        systemSetupPanel.add(tool_text_get_config_file_path);
        tool_file_chooser_select_config_save_file = new JFileChooser();
        tool_file_chooser_select_config_save_file.setCurrentDirectory(new File(""));
        systemSetupPanel.add(tool_file_chooser_select_config_save_file);
        tooi_ui_config_file_get();

        x_lable_location = x_lable_location + x_text_length * 4 + x_step * 3;
        tool_button_upload_config_file = buildJButton("导入", x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        systemSetupPanel.add(tool_button_upload_config_file);
        x_lable_location = x_lable_location + x_text_length + x_step;
        tool_text_upload_config_file_path = buildJTextField("", "上传文件路径", text_columns, x_lable_location, y_lable_location, x_text_length * 4, y_text_heigth);
        systemSetupPanel.add(tool_text_upload_config_file_path);
        tool_file_chooser_select_config_upload_file = new JFileChooser();
        tool_file_chooser_select_config_upload_file.setCurrentDirectory(new File(""));
        systemSetupPanel.add(tool_file_chooser_select_config_upload_file);
        tooi_ui_config_file_upload();

        //重启
        tool_system_reboot_button = buildJButton("系统重启", 20, 180, 150, y_text_heigth);
        systemSetupPanel.add(tool_system_reboot_button);
        tooi_ui_reboot_system();

        return systemSetupPanel;
    }

    public JPanel deviceUpgradePanel() {
    	if (deviceUpgradePanel != null) {
     		  return deviceUpgradePanel;
     	  }
    	deviceUpgradePanel = new JPanel();
    	deviceUpgradePanel.setPreferredSize(new Dimension(550, 300));
    	deviceUpgradePanel.setLayout(null);
        deviceUpgradePanel.setBorder(BorderFactory.createTitledBorder("设备升级"));

        // 标签起始位置
        int x_lable_location = 20;
        int y_lable_location = 100;

        // 文本框起始位置
        int x_text_location = 100;
        int y_text_location = 20;

        // 步长
        int x_step = 25;
        int y_step = 30;

        // 文本框长度和宽度
        int x_text_length = 130;
        int y_text_heigth = 25;
        int text_columns = 20;

        // 标签框长度和宽度
        int x_lable_length = 80;

//        deviceUpgradePanel.add(buildJLabel("升级:", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        //升级功能
//        y_lable_location += y_step;
        y_text_location += y_step;
        deviceUpgradePanel.add(buildJLabel("升级文件路径：", x_lable_location, y_lable_location, x_text_length, y_text_heigth));

        x_lable_location = x_lable_location + x_lable_length + x_step;
        tool_text_field_update_file = buildJTextField("", "updatefile", text_columns, x_lable_location, y_lable_location, x_text_length * 3 + y_text_heigth , y_text_heigth);
        deviceUpgradePanel.add(tool_text_field_update_file);

        x_lable_location = x_lable_location + x_text_length + x_step;
        y_lable_location += y_step*2;
        tool_button_select_update_file = buildJButton("选择文件", x_lable_location, y_lable_location, x_text_length - y_step, y_text_heigth);
        deviceUpgradePanel.add(tool_button_select_update_file);
        tool_file_chooser_select_update_file = new JFileChooser();
        tool_file_chooser_select_update_file.setCurrentDirectory(new File(""));
        deviceUpgradePanel.add(tool_file_chooser_select_update_file);
        tooi_ui_update_file_select();

        x_lable_location = x_lable_location + x_text_length + x_step;
        tool_button_upload_update_file = buildJButton("上传文件", x_lable_location, y_lable_location, x_text_length - y_step, y_text_heigth);
        deviceUpgradePanel.add(tool_button_upload_update_file);
        tooi_ui_update_file_upload();

        return deviceUpgradePanel;
    }

    public JPanel configImportExportPanel() {
    	if (configImportExportPanel != null) {
   		  return configImportExportPanel;
   	     }
    	configImportExportPanel = new JPanel();
  	    configImportExportPanel.setPreferredSize(new Dimension(550, 300));
     	configImportExportPanel.setLayout(null);
        configImportExportPanel.setBorder(BorderFactory.createTitledBorder("配置导入导出"));

        // 标签起始位置
        int x_lable_location = 110;
        int y_lable_location = 110;

        // 文本框起始位置
        int x_text_location = 100;
        int y_text_location = 20;

        // 步长
        int x_step = 10;
        int y_step = 50;

        // 文本框长度和宽度
        int x_text_length = 130;
        int y_text_heigth = 25;
        int text_columns = 20;

        // 标签框长度和宽度
        int x_lable_length = 80;

//        y_lable_location = y_lable_location + y_text_heigth + x_step;
        x_text_length = x_text_length / 2;
        tool_button_get_config_file = buildJButton("导出", x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        configImportExportPanel.add(tool_button_get_config_file);
        x_lable_location = x_lable_location + x_text_length + x_step;
        tool_text_get_config_file_path = buildJTextField("", "保存文件路径", text_columns, x_lable_location, y_lable_location, x_text_length * 4, y_text_heigth);
        configImportExportPanel.add(tool_text_get_config_file_path);
        tool_file_chooser_select_config_save_file = new JFileChooser();
        tool_file_chooser_select_config_save_file.setCurrentDirectory(new File(""));
        configImportExportPanel.add(tool_file_chooser_select_config_save_file);
        tooi_ui_config_file_get();

        y_lable_location += y_step;
        x_lable_location = x_lable_location - x_text_length - x_step;
        tool_button_upload_config_file = buildJButton("导入", x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        configImportExportPanel.add(tool_button_upload_config_file);
        x_lable_location = x_lable_location + x_text_length + x_step;
        tool_text_upload_config_file_path = buildJTextField("", "上传文件路径", text_columns, x_lable_location, y_lable_location, x_text_length * 4, y_text_heigth);
        configImportExportPanel.add(tool_text_upload_config_file_path);
        tool_file_chooser_select_config_upload_file = new JFileChooser();
        tool_file_chooser_select_config_upload_file.setCurrentDirectory(new File(""));
        configImportExportPanel.add(tool_file_chooser_select_config_upload_file);
        tooi_ui_config_file_upload();
        return configImportExportPanel;
    }

}
