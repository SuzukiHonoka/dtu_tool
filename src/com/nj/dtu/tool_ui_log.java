package com.nj.dtu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class tool_ui_log extends tool_ui_base {
    //״̬��ѯ���
    JPanel panel_log = new JPanel();

    //����ʵ��
    tool_uart serialPort;
    //���ݽ���ʵ��
    tool_data_process g_data_process;
    tool_device_info g_device_info;

    //��ȡlog
    JTextArea text_field_device_log;
    JScrollPane scroll_device_log;
    JButton tool_button_get_log;
    JButton tool_button_clear_log;

    public tool_ui_log(tool_uart serial_port, tool_data_process data_process, tool_device_info device_info) {
        serialPort = serial_port;
        g_data_process = data_process;
        g_device_info = device_info;
    }

    public JPanel systemLogPanel(JPanel systemLogPanel) throws Exception {
        systemLogPanel = new JPanel();
        systemLogPanel.setPreferredSize(new Dimension(990, 300));
        systemLogPanel.setLayout(null);
        systemLogPanel.setBorder(BorderFactory.createTitledBorder("ϵͳ��־"));

        // ��ǩ��ʼλ��
        int x_lable_location = 20;
        int y_lable_location = 20;

        // �ı�����ʼλ��
        int x_text_location = 100;
        int y_text_location = 10;

        // ����
        int x_step = 10;
        int y_step = 30;

        // �ı��򳤶ȺͿ��
        int x_text_length = 850;
        int y_text_heigth = 250;
        int text_columns = 20;

        // ��ǩ�򳤶ȺͿ��
        int x_lable_length = 80;

        text_field_device_log = buildJJTextArea(3, 50, x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        text_field_device_log.setMargin(new Insets(0, 5, 5, 5));
        scroll_device_log = buildJJScrollPane(text_field_device_log, x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        systemLogPanel.add(scroll_device_log);
        scroll_device_log.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll_device_log.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        text_field_device_log.setEditable(false);

        x_lable_location = x_lable_location + x_text_length + x_step;
        x_text_length = 100;
        y_text_heigth = 25;
        tool_button_get_log = buildJButton("�ռ�log", x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        systemLogPanel.add(tool_button_get_log);

        y_lable_location = y_lable_location + y_step;
        tool_button_clear_log = buildJButton("���", x_lable_location, y_lable_location, x_text_length, y_text_heigth);
        systemLogPanel.add(tool_button_clear_log);

        tool_button_get_log_listener();
        tool_button_clear_log_listener();

        return systemLogPanel;
    }

    //�ռ�log
    public void tool_button_get_log_listener() throws Exception {
        tool_button_get_log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //�ռ�log
                    text_field_device_log.setText(text_field_device_log.getText() + "\n\n\n\n  ############################################################################################################    \n\n\n\n ");
                    serialPort.sendComm(g_data_process.send_cmd_get_log());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    //��� text log
    public void tool_button_clear_log_listener() throws Exception {
        tool_button_clear_log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("�����־  ");
                    text_field_device_log.setText("");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void tool_log_set(String str) {
        String dtu_name = g_device_info.device_name;
        String file_path = System.getProperty("user.dir");
        String file_time = getNowTime();
        String file_name = file_path + "\\" + "dtu-" + dtu_name + "-" + file_time + ".log";
        try {
            appendFile(file_name, str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String tip =  text_field_device_log.getText() +"\r\n"+ str;
        text_field_device_log.setText(tip);
        Point p = new  Point();   
        p.setLocation(0 ,text_field_device_log.getLineCount()*20);   
        scroll_device_log.getViewport().setViewPosition(p); 
    }
}
