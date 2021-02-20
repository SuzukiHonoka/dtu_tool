package com.nj.dtu;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;


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
		loginPanel.setBorder(BorderFactory.createTitledBorder("��¼"));	
		loginPanel.add(buildJLabel("����û�", 30, 30, 80, 20));	
		listCheck = new JList<String>();
		listCheck.addMouseListener(this);
		JScrollPane jsp=new JScrollPane(listCheck);
		jsp.setBounds(30, 50, 130, 220);		
		setListCheck(dtu_tool_ui.g_data_process.mapCheck);
		loginPanel.add(jsp);
		loginPanel.add(buildJLabel("�����û�", 180, 30, 80, 20));
		listConfig = new JList<String>();
		listConfig.addMouseListener(this);
		JScrollPane jsp2=new JScrollPane(listConfig);
		jsp2.setBounds(180, 50, 130, 220);
		setListConfig(dtu_tool_ui.g_data_process.mapConfig);
		loginPanel.add(jsp2);
		
		loginPanel.add(buildJLabel("�û���", 360, 60, 50, 20));
		textName  = buildJTextField("", "", 20, 420, 60, 185, 25);
		loginPanel.add(textName);
		loginPanel.add(buildJLabel("��  ��", 360, 110, 50, 20));
		textPass = new JPasswordField(20);
		textPass.setBounds( 420, 110, 185, 25);
		loginPanel.add(textPass);
		
		radioButton1 =new JRadioButton("����û�");
		radioButton1.setBounds( 385, 160, 100, 20);
		loginPanel.add(radioButton1);
		radioButton2 =new JRadioButton("�����û�");
		radioButton2.setBounds( 485, 160, 100, 20);
		loginPanel.add(radioButton2);
		ButtonGroup group=new ButtonGroup();
		radioButton1.setSelected(true);
		group.add(radioButton1);
		group.add(radioButton2);
	
        
	    btLogin =  buildJButton("���", 360, 200, 100, 30);
	    btLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(dtu_tool_ui.g_data_process.Md5.equals("")){
					JOptionPane.showMessageDialog(null, "���벻���ڣ��뵼��");
					return;
				}
				String pass = String.valueOf(textPass.getPassword());
				String name = textName.getText();
				if(name.equals("")||pass.equals("")){
					JOptionPane.showMessageDialog(null, "�˺����벻��Ϊ��");
					return;
				}
				if(isSpecialChar(name)||isSpecialChar(pass)){
					JOptionPane.showMessageDialog(null, "���ܰ���:;");
					return;
				}
				if(pass.length()<8){
					JOptionPane.showMessageDialog(null, "���벻��С��8λ");
					return;
				}
				if(!usbUtils.checkPasswordRule(pass)){
					JOptionPane.showMessageDialog(null, "������������ĸ�����֡����� �����ַ�");
					return;
				}
				
				if(dtu_tool_ui.g_data_process.mapCheck.containsKey(name)||dtu_tool_ui.g_data_process.mapConfig.containsKey(name)){
					JOptionPane.showMessageDialog(null, "���û��Ѵ���");
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
	    JButton get = buildJButton("��ȡ�û���Ϣ", 470, 200, 135, 30);
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
	    creatKey =  buildJButton("����usb-key��Կ��", 700, 60, 180, 25);
	    creatKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dtu_tool_ui.loading("���ڴ���");
				 new Thread() {
						public void run() {
							if(!usbUtils.isExits()){
								JOptionPane.showMessageDialog(null, "�����usb key");
								dtu_tool_ui.stop();
					    		return;
					    	}
						
							 if(usb_key.INSTANCE.get_key_pair()==0){
								
							  //�� 0;�� 1
					            int n = JOptionPane.showConfirmDialog(null, "��Կ�Ѵ��ڣ��Ƿ����´���?", "", JOptionPane.YES_NO_OPTION);
					            if(n==0){
					            	dtu_tool_ui.loading("���ڴ���");
						        	usb_key.INSTANCE.add_key_pair();
						        	usb_key.INSTANCE.add_key_pair();					      
						        	JOptionPane.showMessageDialog(null, "�����ɹ�");
					            }
							
						        }else{
						        	dtu_tool_ui.loading("���ڴ���");
						        	usb_key.INSTANCE.add_key_pair();
						        	usb_key.INSTANCE.add_key_pair();
						        
						        	JOptionPane.showMessageDialog(null, "�����ɹ�");
						        }
							 dtu_tool_ui.stop();
						}
					}.start();
				
			

			}
		});
	    loginPanel.add(creatKey);	
	    importKey =  buildJButton("���빫Կ���豸", 700, 100, 180, 25);
	    importKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dtu_tool_ui.loading("���ڵ���");
				 new Thread() {
						public void run() {
							if(!usbUtils.isExits()){
								 JOptionPane.showMessageDialog(null,"�����usb key");
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
						        	 JOptionPane.showMessageDialog(null,"usbkey��������Կ���ȴ���");
						        }
						}
					}.start();
				
			        
			}
		});
	    loginPanel.add(importKey);	
	    
	    removeKey =  buildJButton("ɾ���豸��Կ", 700, 140, 180, 25);
	    removeKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dtu_tool_ui.loading("����ɾ��");
				 new Thread() {
						public void run() {
							if(!usbUtils.isExits()){
								 JOptionPane.showMessageDialog(null,"�����usb key");
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
						        	 JOptionPane.showMessageDialog(null,"usbkey��������Կ");
						        }
						}
					}.start();
			}
		});
	    loginPanel.add(removeKey);
	    
	    JButton getMD5= buildJButton("��֤��Կ����",700, 180, 180, 25);
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
//			tipLabel.setText("�豸δ������Կ");
//		else
//			tipLabel.setText("�豸�ѵ�����Կ");
//	}
	public boolean isSpecialChar(String str) {
       if(str.contains(":")||str.contains("��")||str.contains("��")||str.contains(";"))
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
	        //�� 0;�� 1
            int n = JOptionPane.showConfirmDialog(null, "ȷ��ɾ�� "+name+"?", "", JOptionPane.YES_NO_OPTION);
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
