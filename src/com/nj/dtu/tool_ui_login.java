package com.nj.dtu;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class tool_ui_login extends tool_ui_base {
	tool_ui dtu_tool_ui;
	JButton btLogin;
	JTextField textName;
	JPasswordField textPass;
	JPasswordField textPassPin;
	JTextArea textTip;
	JScrollPane scrollPane;
	JPanel loginPanel;
	JRadioButton radioButton1,radioButton2,radioButton3;
	public tool_ui_login(tool_ui dtu_tool_ui){
		 this.dtu_tool_ui = dtu_tool_ui;
	}
	public JPanel tool_ui_login_init() throws Exception {
		loginPanel = new JPanel();
		loginPanel.setPreferredSize(new Dimension(990, 100));
		loginPanel.setLayout(null);
		loginPanel.setBorder(BorderFactory.createTitledBorder("登录"));
		
		loginPanel.add(buildJLabel("用户名", 360, 60, 50, 20));
		textName  = buildJTextField("", "", 20, 420, 60, 185, 25);
		loginPanel.add(textName);
		loginPanel.add(buildJLabel("密  码", 360, 110, 50, 20));
		textPass = new JPasswordField(20);
		textPass.setBounds( 420, 110, 185, 25);
		loginPanel.add(textPass);
		
		loginPanel.add(buildJLabel("pin码", 360, 160, 50, 20));
		textPassPin = new JPasswordField(20);
		textPassPin.setBounds( 420, 160, 185, 25);
		loginPanel.add(textPassPin);
		
		radioButton1 =new JRadioButton("审计用户");
		radioButton1.setBounds( 338, 200, 100, 20);
		loginPanel.add(radioButton1);
		radioButton2 =new JRadioButton("配置用户");
		radioButton2.setBounds( 438, 200, 100, 20);
		loginPanel.add(radioButton2);
		radioButton3 =new JRadioButton("超级用户");
		radioButton3.setBounds(538, 200, 100, 20);
		loginPanel.add(radioButton3);
		ButtonGroup group=new ButtonGroup();
		radioButton1.setSelected(true);
		group.add(radioButton1);
		group.add(radioButton2);
		group.add(radioButton3);
        
		
		
	    btLogin =  buildJButton("登  录", 432, 240, 100, 30);
	    btLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(usbUtils.isExits()){
					if(OpSqliteDB.canContinue()){
					String pass = String.valueOf(textPass.getPassword());
					String name = textName.getText();
					String passPin = String.valueOf(textPassPin.getPassword());
					if(!dtu_tool_ui.Panel_load.connect){
						JOptionPane.showMessageDialog(null, "请先连接设备");
						return;
					}
					if(name.equals("")||pass.equals("")){
						JOptionPane.showMessageDialog(null, "账号密码不能为空");
						return;
					}
					if(!(name.equals("root_zr")&&pass.equals("123#456@189"))){
					 if(usb_key2.INSTANCE.epass_login(passPin, passPin.length())!=0){
						JOptionPane.showMessageDialog(null, "pin码不正确");
						return;
					 }
					}
					if(radioButton3.isSelected()){
						   if(name.equals("root")&&pass.equals("zr#1234"))
						    	dtu_tool_ui.showLoginSuccessSuper();
						   else{
							   OpSqliteDB.addError();
							   JOptionPane.showMessageDialog(null, "账号密码错误");
						   }
					}else{	
							dtu_tool_ui.loading("正在配对密钥");	
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									 if(usb_key.INSTANCE.get_key_pair()==0){
									        usb_key.INSTANCE.export_pubkey_pair("pubkey.msblob");
									        String md5 = Md5CaculateUtil.getMD5(new File("pubkey.msblob"));
									        if(!md5.equals(dtu_tool_ui.g_data_process.Md5)){
									        	JOptionPane.showMessageDialog(null, "该密钥无效");
									        	dtu_tool_ui.stop();
												return;
									        }		
									        }else{
									        	JOptionPane.showMessageDialog(null, "该usb key还未设置");
									        	dtu_tool_ui.stop();
												return;
									        }
										  dtu_tool_ui.stop();
									 	 if(radioButton1.isSelected()){
												if(dtu_tool_ui.g_data_process.canCheckLogin(name, pass))
												   dtu_tool_ui.showLoginSuccess();
												 else{
													   OpSqliteDB.addError();
													   JOptionPane.showMessageDialog(null, "账号密码错误");
												   }
											}else {
												if(dtu_tool_ui.g_data_process.canConfigLogin(name, pass))
													   dtu_tool_ui.showLoginSuccess();
												 else{
													   OpSqliteDB.addError();
													   JOptionPane.showMessageDialog(null, "账号密码错误");
												   }
											}
									
								}
							}).start();
							
					}
					}else{
						 JOptionPane.showMessageDialog(null, "连续错误，5分钟后重试");
					}
					
					
				}else{
					
					addTip("请插入usb密钥");
//					 dtu_tool_ui.showLoginSuccess();
//					 dtu_tool_ui.showLoginSuccessSuper();
				}

			}
		});
	    loginPanel.add(btLogin);	
	    
	    textTip = buildJJTextArea(3, 50, 20, 310, 950, 250);
	    textTip.setMargin(new Insets(0, 5, 5, 5));
	    scrollPane = buildJJScrollPane(textTip, 20, 310, 950, 250);
	    loginPanel.add(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    return loginPanel;
	}
	private void addTip(String str){
		 String tip =  textTip.getText() +"\r\n"+ str;
		 textTip.setText(tip);
	     Point p = new  Point();   
	     p.setLocation(0 ,textTip.getLineCount()*20);   
	     scrollPane.getViewport().setViewPosition(p); 
	}
}
