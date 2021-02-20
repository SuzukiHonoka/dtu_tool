package com.nj.dtu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Map;


public class tool_ui_set_user extends tool_ui_base implements MouseListener {
	tool_ui dtu_tool_ui;
	JButton btLogin;
	JTextField textName;
	JPanel loginPanel;
	JPasswordField textPass;
	JRadioButton radioButton1,radioButton2;
	JList<String> listCheck,listConfig;
	JButton creatKey,importKey,removeKey;
	String [] sCheck,sConfig;
	JLabel tipLabel;
	public tool_ui_set_user(tool_ui dtu_tool_ui){
		 this.dtu_tool_ui = dtu_tool_ui;
	}
	public JPanel tool_ui_login_init() {
		if(loginPanel!=null)
			return loginPanel;
		loginPanel = new JPanel();
		loginPanel.setPreferredSize(new Dimension(990, 100));
		loginPanel.setLayout(null);
		loginPanel.setBorder(BorderFactory.createTitledBorder("登录"));	
		loginPanel.add(buildJLabel("审计用户", 30, 30, 80, 20));	
		listCheck = new JList<String>();
		listCheck.addMouseListener(this);
		JScrollPane jsp=new JScrollPane(listCheck);
		jsp.setBounds(30, 50, 130, 220);		
		setListCheck(dtu_tool_ui.g_data_process.mapCheck);
		loginPanel.add(jsp);
		loginPanel.add(buildJLabel("配置用户", 180, 30, 80, 20));
		listConfig = new JList<String>();
		listConfig.addMouseListener(this);
		JScrollPane jsp2=new JScrollPane(listConfig);
		jsp2.setBounds(180, 50, 130, 220);
		setListConfig(dtu_tool_ui.g_data_process.mapConfig);
		loginPanel.add(jsp2);
		
		loginPanel.add(buildJLabel("用户名", 360, 60, 50, 20));
		textName  = buildJTextField("", "", 20, 420, 60, 185, 25);
		loginPanel.add(textName);
		loginPanel.add(buildJLabel("密  码", 360, 110, 50, 20));
		textPass = new JPasswordField(20);
		textPass.setBounds( 420, 110, 185, 25);
		loginPanel.add(textPass);
		
		radioButton1 =new JRadioButton("审计用户");
		radioButton1.setBounds( 385, 160, 100, 20);
		loginPanel.add(radioButton1);
		radioButton2 =new JRadioButton("配置用户");
		radioButton2.setBounds( 485, 160, 100, 20);
		loginPanel.add(radioButton2);
		ButtonGroup group=new ButtonGroup();
		radioButton1.setSelected(true);
		group.add(radioButton1);
		group.add(radioButton2);
	
        
	    btLogin =  buildJButton("添加", 360, 200, 100, 30);
	    btLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(dtu_tool_ui.g_data_process.Md5.equals("")){
					JOptionPane.showMessageDialog(null, "密码不存在，请导入");
					return;
				}
				String pass = String.valueOf(textPass.getPassword());
				String name = textName.getText();
				if(name.equals("")||pass.equals("")){
					JOptionPane.showMessageDialog(null, "账号密码不能为空");
					return;
				}
				if(isSpecialChar(name)||isSpecialChar(pass)){
					JOptionPane.showMessageDialog(null, "不能包含:;");
					return;
				}
				if(pass.length()<8){
					JOptionPane.showMessageDialog(null, "密码不能小于8位");
					return;
				}
				if(!usbUtils.checkPasswordRule(pass)){
					JOptionPane.showMessageDialog(null, "密码必须包含字母、数字、符号 三种字符");
					return;
				}
				
				if(dtu_tool_ui.g_data_process.mapCheck.containsKey(name)||dtu_tool_ui.g_data_process.mapConfig.containsKey(name)){
					JOptionPane.showMessageDialog(null, "该用户已存在");
					return;
				}
			
			    String cmd = "";			
				if(radioButton1.isSelected()){
					cmd = "/zr_bin/root_config.sh add_role_check";
				}else{
					cmd = "/zr_bin/root_config.sh add_role_config";
				}
				try {
					dtu_tool_ui.serialPort.sendComm(dtu_tool_ui.g_data_process.send_cmd_run_shell_2(dtu_tool_ui.g_data_process.CMD_ADD_USER,
							cmd+" "+name+" "+pass));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
	  
	    loginPanel.add(btLogin);	
	    JButton get = buildJButton("获取用户信息", 470, 200, 135, 30);
	    get.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					dtu_tool_ui.serialPort.sendComm(dtu_tool_ui.g_data_process.send_cmd_run_shell_2(dtu_tool_ui.g_data_process.CMD_GET_CHECK_USER,
							"/zr_bin/root_config.sh list_role_check"));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	    loginPanel.add(get);	
	    creatKey =  buildJButton("创建usb-key密钥对", 700, 60, 180, 25);
	    creatKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dtu_tool_ui.loading("正在创建");
				 new Thread() {
						public void run() {
							if(!usbUtils.isExits()){
								JOptionPane.showMessageDialog(null, "请插入usb key");
								dtu_tool_ui.stop();
					    		return;
					    	}
						
							 if(usb_key.INSTANCE.get_key_pair()==0){
								
							  //是 0;否 1
					            int n = JOptionPane.showConfirmDialog(null, "密钥已存在，是否重新创建?", "", JOptionPane.YES_NO_OPTION);
					            if(n==0){
					            	dtu_tool_ui.loading("正在创建");
						        	usb_key.INSTANCE.add_key_pair();
						        	usb_key.INSTANCE.add_key_pair();					      
						        	JOptionPane.showMessageDialog(null, "创建成功");
					            }
							
						        }else{
						        	dtu_tool_ui.loading("正在创建");
						        	usb_key.INSTANCE.add_key_pair();
						        	usb_key.INSTANCE.add_key_pair();
						        
						        	JOptionPane.showMessageDialog(null, "创建成功");
						        }
							 dtu_tool_ui.stop();
						}
					}.start();
				
			

			}
		});
	    loginPanel.add(creatKey);	
	    importKey =  buildJButton("导入公钥到设备", 700, 100, 180, 25);
	    importKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dtu_tool_ui.loading("正在导入");
				 new Thread() {
						public void run() {
							if(!usbUtils.isExits()){
								 JOptionPane.showMessageDialog(null,"请插入usb key");
					    		return;
					    	}
							  if(usb_key.INSTANCE.get_key_pair()==0){			   
						        	usb_key.INSTANCE.export_pubkey_pair("pubkey.msblob");
						        	String Md5 = Md5CaculateUtil.getMD5(new File("pubkey.msblob"));
						        	System.out.println(Md5);
						        	try {
										dtu_tool_ui.serialPort.sendComm(dtu_tool_ui.g_data_process.send_cmd_run_shell_2(dtu_tool_ui.g_data_process.CMD_IMPORT_KEY,
													"/zr_bin/root_config.sh import "+Md5));
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
						        		
						        }else{
						        	 JOptionPane.showMessageDialog(null,"usbkey不存在密钥请先创建");
						        }
						}
					}.start();
				
			        
			}
		});
	    loginPanel.add(importKey);	
	    
	    removeKey =  buildJButton("删除设备公钥", 700, 140, 180, 25);
	    removeKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dtu_tool_ui.loading("正在删除");
				 new Thread() {
						public void run() {
							if(!usbUtils.isExits()){
								 JOptionPane.showMessageDialog(null,"请插入usb key");
					    		return;
					    	}
							  if(usb_key.INSTANCE.get_key_pair()==0){			   
						        	usb_key.INSTANCE.export_pubkey_pair("pubkey.msblob");
						        	String Md5 = Md5CaculateUtil.getMD5(new File("pubkey.msblob"));
						        	System.out.println(Md5);
						        	try {
										dtu_tool_ui.serialPort.sendComm(dtu_tool_ui.g_data_process.send_cmd_run_shell_2(dtu_tool_ui.g_data_process.CMD_REMOVE_KEY,
													"/zr_bin/root_config.sh remove "+Md5));
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
						        		
						        }else{
						        	 JOptionPane.showMessageDialog(null,"usbkey不存在密钥");
						        }
						}
					}.start();
			}
		});
	    loginPanel.add(removeKey);
	    
	    JButton getMD5= buildJButton("验证密钥导入",700, 180, 180, 25);
	    getMD5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					dtu_tool_ui.serialPort.sendComm(dtu_tool_ui.g_data_process.send_cmd_run_shell_2(dtu_tool_ui.g_data_process.CMD_GET_MD5,
							"/zr_bin/root_config.sh export"));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	    loginPanel.add(getMD5);	
	    tipLabel = buildJLabel("",  730, 220, 120, 25);
	    loginPanel.add(tipLabel);	
	   
	    return loginPanel;
	}

	
	public void setListCheck(Map<String,String> map){
		sCheck = new String[map.size()];
		map.keySet().toArray(sCheck);
		listCheck.setListData(sCheck);
	
	}
	public void setListConfig(Map<String,String> map){
		sConfig = new String[map.size()];
	    map.keySet().toArray(sConfig);
		listConfig.setListData(sConfig);
	}
//	public void setTip(){
//		if(dtu_tool_ui.g_data_process.Md5.equals(""))
//			tipLabel.setText("设备未导入密钥");
//		else
//			tipLabel.setText("设备已导入密钥");
//	}
	public boolean isSpecialChar(String str) {
       if(str.contains(":")||str.contains("：")||str.contains("；")||str.contains(";"))
    	   return true;
        return false;
    }
	@Override
	public void mouseClicked(MouseEvent e) {
		 int clickCount = e.getClickCount();
	        if (clickCount == 2) {
	            doubleClick(e);
	        }
		
	}
	 private void doubleClick(MouseEvent e) {
	        Object source = e.getSource();
	        String name = ((JList<String>)source).getSelectedValue();
	        //是 0;否 1
            int n = JOptionPane.showConfirmDialog(null, "确认删除 "+name+"?", "", JOptionPane.YES_NO_OPTION);
            if(n==0){
            String cmd = "";	
            String pass = "";
	        if (source == listCheck) {
	        	cmd = "/zr_bin/root_config.sh remove_role_check";
	        	pass = dtu_tool_ui.g_data_process.mapCheck.get(name);
	        } else if (source == listConfig) {
	        	cmd = "/zr_bin/root_config.sh remove_role_config";
	        	pass = dtu_tool_ui.g_data_process.mapConfig.get(name);
	        }
	        try {
				dtu_tool_ui.serialPort.sendComm(dtu_tool_ui.g_data_process.send_cmd_run_shell_2(dtu_tool_ui.g_data_process.CMD_REMOVE_USER,
						cmd+" "+name+" "+pass));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            }
	    }
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
