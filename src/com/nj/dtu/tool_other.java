package com.nj.dtu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class tool_other extends tool_ui_base {
	// 串口实例
	tool_uart serialPort;
	// 数据交互实例
	tool_data_process g_data_process;
	tool_device_info g_device_info;
	JCheckBox jCheckBox;
	JSpinner year;
	JButton tool_set_button;
	JLabel text_log_type;
	JPanel otherPanel;

	public tool_other(tool_uart serial_port, tool_data_process data_process, tool_device_info device_info) {
		serialPort = serial_port;
		g_data_process = data_process;
		g_device_info = device_info;
	}

	public JPanel otherPanel() {
		if (otherPanel != null) {
			year.setValue(new Date());
			return otherPanel;
		}
		otherPanel = new JPanel();
		otherPanel.setPreferredSize(new Dimension(550, 300));
		otherPanel.setLayout(null);
		otherPanel.setBorder(BorderFactory.createTitledBorder("其他"));

		// 标签起始位置
		int x_lable_location = 40;
		int y_lable_location = 40;

		tool_set_button = buildJButton("设置系统时间", x_lable_location, y_lable_location, 150, 25);
		otherPanel.add(tool_set_button);
		// 获得时间日期模型
		SpinnerDateModel model = new SpinnerDateModel();
		// 获得JSPinner对象
		year = new JSpinner(model);
		year.setValue(new Date());
		// 设置时间格式
		JSpinner.DateEditor editor = new JSpinner.DateEditor(year, "yyyy-MM-dd HH:mm:ss");
		year.setEditor(editor);
		x_lable_location += 160;
		year.setBounds(x_lable_location, y_lable_location, 219, 25);
		otherPanel.add(year);
		tool_set_button_listener();
		
		jCheckBox = new JCheckBox("LOG");
		jCheckBox.setBounds(40, 80, 70, 50);
		jCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
		jCheckBox.addItemListener(new ItemListener() {

		    public void itemStateChanged(ItemEvent e) {
		    	 try {
		 			if(jCheckBox.isSelected()){
		 				serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_SET_LOG,
		 						"/zr_bin/ini_config set LOG:logon 1 "));	
		 			}else{
		 				serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_SET_LOG,
		 						"/zr_bin/ini_config set LOG:logon 0 "));
		 			}
		 			text_log_type.setText(jCheckBox.isSelected()?"已打开":"已关闭");
		 		} catch (Exception e1) {
		 			// TODO Auto-generated catch block
		 			e1.printStackTrace();
		 		}
		    }
		});
		otherPanel.add(jCheckBox);
		text_log_type = buildJLabel("",  110, 80, 80, 50);
		otherPanel.add(text_log_type);
		return otherPanel;

	}
	
    //获取设备配置参数
    public void tool_set_button_listener()  {
    	tool_set_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(year.getValue());
                    System.out.println(time);
                	serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_SET_DATE,
							"date -s \""+time+"\""));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
