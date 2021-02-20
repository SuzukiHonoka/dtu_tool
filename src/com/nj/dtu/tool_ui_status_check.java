package com.nj.dtu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class tool_ui_status_check extends tool_ui_base {

    //获取RX TX
    JTextField text_field_device_wan_rx;
    JTextField text_field_device_wan_tx;
    JTextField text_field_device_system_runtime;
    JButton tool_status_updata_button;

    //版本信息
    JTextField text_field_device_version;

    //获取ping
    JTextArea text_field_device_ping;
    JScrollPane scroll_device_ping;
    JButton tool_status_ping_button;
    JButton tool_status_ping__clear_button;
    JTextField text_field_device_ping_addr;
    JLabel tool_result_ping_label;

    //串口实例
    tool_uart serialPort;
    //数据交互实例
    tool_data_process g_data_process;
    tool_device_info g_device_info;
    
    JPanel checkStatusPanel;
    JPanel communicationTestPanel;
    
    JTextField text_send;
    JButton button_send;

    public tool_ui_status_check(tool_uart serial_port, tool_data_process data_process, tool_device_info device_info) {
        serialPort = serial_port;
        g_data_process = data_process;
        g_device_info = device_info;
    }

    public JPanel checkStatusPanel() {
    	 if (checkStatusPanel != null) {
     		  return checkStatusPanel;
     	  }
         checkStatusPanel = new JPanel();
         checkStatusPanel.setPreferredSize(new Dimension(550, 300));
         checkStatusPanel.setLayout(null);
        checkStatusPanel.setBorder(BorderFactory.createTitledBorder("状态查询"));

        // 标签起始位置
        int x_lable_location = 40;
        int y_lable_location = 40;

        // 文本框起始位置
        int x_text_location = 120;
        int y_text_location = 40;

        // 步长
        int x_step = 20;
        int y_step = 50;

        // 文本框长度和宽度
        int x_text_length = 185;
        int y_text_heigth = 25;
        int text_columns = 20;

        // 标签框长度和宽度
        int x_lable_length = 80;

        //   *************  设备基本信息    **********************
        checkStatusPanel.add(buildJLabel("上行流量", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        text_field_device_wan_tx = buildJTextField("", "text_field_device_wan_tx", text_columns, x_text_location, y_text_location, x_text_length, y_text_heigth);
        checkStatusPanel.add(text_field_device_wan_tx);
        text_field_device_wan_tx.setEditable(false);

        y_lable_location += y_step;
        y_text_location += y_step;
        checkStatusPanel.add(buildJLabel("下行流量", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        text_field_device_wan_rx = buildJTextField("", "text_field_device_wan_rx", text_columns, x_text_location, y_text_location, x_text_length, y_text_heigth);
        checkStatusPanel.add(text_field_device_wan_rx);
        text_field_device_wan_rx.setEditable(false);

        y_lable_location += y_step;
        y_text_location += y_step;
        checkStatusPanel.add(buildJLabel("运行时间", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        text_field_device_system_runtime = buildJTextField("", "text_field_device_wan_rx", text_columns, x_text_location, y_text_location, x_text_length, y_text_heigth);
        checkStatusPanel.add(text_field_device_system_runtime);
        text_field_device_system_runtime.setEditable(false);

        y_lable_location += y_step;
        y_text_location += y_step;
        checkStatusPanel.add(buildJLabel("版本信息", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        text_field_device_version = buildJTextField("", "text_field_device_version", text_columns, x_text_location, y_text_location, x_text_length, y_text_heigth);
        checkStatusPanel.add(text_field_device_version);
        text_field_device_version.setEditable(false);

        //添加按钮
        y_text_location += y_step;
        tool_status_updata_button = super.buildJButton("获取状态", x_lable_location, y_text_location, 150, 25);
        
        //添加监听事件
        try {
            tool_status_updata_button_listener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        checkStatusPanel.add(tool_status_updata_button);
        text_send = buildJTextField("/zr_bin/atc at+csq", "text_send", text_columns, 330, 100, x_text_length, y_text_heigth);
        button_send = buildJButton("发送", 350, 140,150, 30);
        button_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	serialPort.sendComm(g_data_process.send_cmd_run_shell_2(g_data_process.CMD_STATE,
							text_send.getText()));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        checkStatusPanel.add(text_send);
        checkStatusPanel.add(button_send);    
        return checkStatusPanel;
    }

    public JPanel communicationTestPanel() {
    	 if (communicationTestPanel != null) {
    		  return communicationTestPanel;
    	  }
    	 communicationTestPanel = new JPanel();
        communicationTestPanel.setPreferredSize(new Dimension(550, 300));
        communicationTestPanel.setLayout(null);
        communicationTestPanel.setBorder(BorderFactory.createTitledBorder("通信测试"));
        // 标签起始位置
        int x_lable_location = 20;
        int y_lable_location = 80;

        // 文本框起始位置
        int x_text_location = 20;
        int y_text_location = 40;

        // 步长
        int x_step = 10;
        int y_step = 30;

        // 文本框长度和宽度
        int x_text_length = 100;
        int y_text_heigth = 25;
        int text_columns = 20;

        // 标签框长度和宽度
        int x_lable_length = 80;

        communicationTestPanel.add(buildJLabel("IP地址:", x_text_location, y_text_location, x_text_length, y_text_heigth));
        x_text_location = x_text_location + x_text_length - x_step * 4;
        text_field_device_ping_addr = buildJTextField("", "text_field_device_ping_addr", text_columns, x_text_location, y_text_location, (x_text_length - y_step) * 2, y_text_heigth);
        communicationTestPanel.add(text_field_device_ping_addr);

        x_text_location = x_text_location + x_text_length + x_step * 6;
        tool_status_ping_button = super.buildJButton("ping", x_text_location, y_text_location, x_text_length - x_step * 2, y_text_heigth);
        communicationTestPanel.add(tool_status_ping_button);

        x_text_location = x_text_location + x_text_length + x_step;
        tool_status_ping__clear_button = super.buildJButton("清除", x_text_location - x_step, y_text_location, x_text_length - x_step * 2, y_text_heigth);
        communicationTestPanel.add(tool_status_ping__clear_button);

        tool_result_ping_label = super.buildJLabel("回显结果:", x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        communicationTestPanel.add(tool_result_ping_label);

        y_lable_location = y_lable_location + y_step;
        x_text_length = x_text_length * 5 + x_step * 3;
        y_text_heigth = y_text_heigth * 4 + y_step * 3 - x_step;
        text_field_device_ping = buildJJTextArea(3, 50, x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        scroll_device_ping = buildJJScrollPane(text_field_device_ping, x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        communicationTestPanel.add(scroll_device_ping);
        scroll_device_ping.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll_device_ping.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        text_field_device_ping.setEditable(false);
        //ping
        try {
            tool_status_ping_button_listener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //ping
        try {
            tool_status_ping__clear_button_listener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return communicationTestPanel;
    }

    /**
     * 返回日时分秒
     *
     * @param second
     * @return
     */
    public String secondToTime(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days) {
            return days + "D-" + hours + "H-" + minutes + "M-" + second + "S";
        } else {
            return hours + "H-" + minutes + "M-" + second + "S";
        }
    }

    public void tool_status_set_textfield() {
        text_field_device_wan_rx.setText(g_device_info.device_status_wan_rx);
        text_field_device_wan_tx.setText(g_device_info.device_status_wan_tx);
        text_field_device_system_runtime.setText(secondToTime(Long.parseLong(g_device_info.device_status_run_time.split("\\.")[0])));
    }

    public void tool_softversion_set_textfield() {
        text_field_device_version.setText(g_device_info.device_softversion);
    }

    public void tool_ping_set_textfield() {
        text_field_device_ping.setText(g_device_info.ping_info);
    }

    //获取设备配置参数
    public void tool_status_updata_button_listener() throws Exception {
        tool_status_updata_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //获取设备参数
                    serialPort.sendComm(g_data_process.send_cmd_status_rx_tx_runtime());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    //ping 检测按钮
    public void tool_status_ping_button_listener() throws Exception {
        tool_status_ping_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String ping_url = text_field_device_ping_addr.getText();
                    System.out.println("ping_url:" + ping_url);
                    if (ipCheck(ping_url) == true) {
                        serialPort.sendComm(g_data_process.send_cmd_ping(ping_url));
                        return;
                    }
                    if (url_check(ping_url) == true) {
                        serialPort.sendComm(g_data_process.send_cmd_ping(ping_url));
                        return;
                    }
                    //ip地址和url地址检测错误
                    JOptionPane.showMessageDialog(null, "请输入正确的IP地址或者是url", "警   告  ！！！", 1);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    //ping 清除
    public void tool_status_ping__clear_button_listener() throws Exception {
        tool_status_ping__clear_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //获取设备参数
                    text_field_device_ping.setText("");
                    g_device_info.tool_device_ping_info_clear();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}