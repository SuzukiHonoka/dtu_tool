package com.nj.dtu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.json.JSONObject;

public class tool_ui_x509 extends tool_ui_base {

    //����ʵ��
    tool_uart serialPort;
    //���ݽ���ʵ��
    tool_data_process g_data_process;
    tool_device_info g_device_info;

    JTextField text_field_x509_contry; //����(C)
    JTextField text_field_x509_province; //ʡ��(S)
    JTextField text_field_x509_location; //����(L)
    JTextField text_field_x509_organization; //��˾(O)
    JTextField text_field_x509_organizationalUnit; //��֯(OU)
    JTextField text_field_x509_cn; //ӵ����(CN)

    //��ť
    JButton tool_button_upload_csr; //��������֤��
    JFileChooser tool_file_chooser_select_req_save_file;
    String tool_text_x509req_save_file;
    JButton tool_button_get_csr;
    JButton tool_button_set_csr;
    JButton tool_button_upload_crt;
    JFileChooser tool_file_chooser_select_csr_file;
    JPanel certConfigPanel;

    public tool_ui_x509(tool_uart serial_port, tool_data_process data_process, tool_device_info device_info) {
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
        certConfigPanel.setBorder(BorderFactory.createTitledBorder("֤������"));

        // ��ǩ��ʼλ��
        int x_lable_location = 40;
        int y_lable_location = 30;
        // �ı�����ʼλ��
        int x_text_location = 120;
        int y_text_location = 50;

        // ����
        int x_step = 10;
        int y_step = 30;

        // �ı��򳤶ȺͿ��
        int x_text_length = 220;
        int y_text_heigth = 25;
        int text_columns = 20;

        // ��ǩ�򳤶ȺͿ��
        int x_lable_length = 80;

        //   *************  ��������֤��    **********************
//        certConfigPanel.add(super.buildJLabel("����֤������:", x_lable_location, y_lable_location, x_lable_length * 2, y_text_heigth));
//        y_lable_location += y_step;
        certConfigPanel.add(buildJLabel("����(C)", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        text_field_x509_contry = buildJTextField("", "contry", text_columns, x_text_location, y_lable_location, x_text_length, y_text_heigth);
        certConfigPanel.add(text_field_x509_contry);

        y_lable_location += y_step;
        y_text_location += y_step;
        certConfigPanel.add(buildJLabel("ʡ��(S)", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        text_field_x509_province = buildJTextField("", "province", text_columns, x_text_location, y_lable_location, x_text_length, y_text_heigth);
        certConfigPanel.add(text_field_x509_province);

        y_lable_location += y_step;
        y_text_location += y_step;
        certConfigPanel.add(buildJLabel("����(L)", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        text_field_x509_location = buildJTextField("", "location", text_columns, x_text_location, y_lable_location, x_text_length, y_text_heigth);
        certConfigPanel.add(text_field_x509_location);

        y_lable_location += y_step;
        y_text_location += y_step;
        certConfigPanel.add(buildJLabel("����(OU)", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        text_field_x509_organizationalUnit = buildJTextField("", "organizationalUnit", text_columns, x_text_location, y_lable_location, x_text_length, y_text_heigth);
        certConfigPanel.add(text_field_x509_organizationalUnit);

        y_lable_location += y_step;
        y_text_location += y_step;
        certConfigPanel.add(buildJLabel("��֯(O)", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        text_field_x509_organization = buildJTextField("", "organization", text_columns, x_text_location, y_lable_location, x_text_length, y_text_heigth);
        certConfigPanel.add(text_field_x509_organization);

        y_lable_location += y_step;
        y_text_location += y_step;
        certConfigPanel.add(buildJLabel("ӵ����(CN)", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
        text_field_x509_cn = buildJTextField("", "cn", text_columns, x_text_location, y_lable_location, x_text_length, y_text_heigth);
        certConfigPanel.add(text_field_x509_cn);

        y_lable_location += y_step;
        y_text_location += y_step;
        tool_button_get_csr = super.buildJButton("��ȡ֤������", x_lable_location, y_lable_location, x_lable_length * 2 - x_step, y_text_heigth);
        certConfigPanel.add(tool_button_get_csr);

        x_lable_location = x_lable_location + x_lable_length * 2;
        tool_button_upload_csr = super.buildJButton("��������֤��", x_lable_location, y_lable_location, x_lable_length * 2 - x_step, y_text_heigth);
        certConfigPanel.add(tool_button_upload_csr);
        tool_file_chooser_select_req_save_file = new JFileChooser();
        tool_file_chooser_select_req_save_file.setCurrentDirectory(new File(""));
        certConfigPanel.add(tool_file_chooser_select_req_save_file);


        y_lable_location += y_step;
        y_text_location += y_step;
        x_lable_location = 40;
        tool_button_set_csr = super.buildJButton("����֤��", x_lable_location, y_lable_location, x_lable_length * 2 - x_step, y_text_heigth);
        certConfigPanel.add(tool_button_set_csr);

        x_lable_location = x_lable_location + x_lable_length * 2;
        tool_button_upload_crt = super.buildJButton("����ǩ��֤��", x_lable_location, y_lable_location, x_lable_length * 2 - x_step, y_text_heigth);
        certConfigPanel.add(tool_button_upload_crt);
        tool_file_chooser_select_csr_file = new JFileChooser();
        tool_file_chooser_select_csr_file.setCurrentDirectory(new File(""));
        certConfigPanel.add(tool_file_chooser_select_csr_file);
        //��Ӽ����¼�
        try {
            get_csr_info_button_listener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //��Ӽ����¼�
        try {
            get_csr_upload_button_listener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //��Ӽ����¼�
        try {
            get_csr_set_button_listener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //��Ӽ����¼�
        try {
            get_crt_upload_button_listener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return certConfigPanel;
    }

    //
    public void get_csr_info_button_listener() throws Exception {
        tool_button_get_csr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    serialPort.sendComm(g_data_process.send_cmd_status_rx_tx_runtime());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    //
    public void get_csr_set_button_listener() throws Exception {
        tool_button_set_csr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
//                	serialPort.sendComm(g_data_process.send_cmd_status_rx_tx_runtime());
                    JSONObject subj = new JSONObject();
                    if (text_field_x509_contry.getText().length() != 2) {
                        JOptionPane.showMessageDialog(null, "������ ���ô���");
                        return;
                    } else {
                        subj.put("country", text_field_x509_contry.getText());
                    }

                    if (text_field_x509_province.getText().length() == 0) {
                        JOptionPane.showMessageDialog(null, "ʡ��(S) ���ô���");
                        return;
                    } else {
                        subj.put("province", text_field_x509_province.getText());
                    }

                    if (text_field_x509_location.getText().length() == 0) {
                        JOptionPane.showMessageDialog(null, "����(L) ���ô���");
                        return;
                    } else {
                        subj.put("location", text_field_x509_location.getText());
                    }

                    if (text_field_x509_organization.getText().length() == 0) {
                        JOptionPane.showMessageDialog(null, "��˾(O) ���ô���");
                        return;
                    } else {
                        subj.put("organization", text_field_x509_organization.getText());
                    }

                    if (text_field_x509_organizationalUnit.getText().length() == 0) {
                        JOptionPane.showMessageDialog(null, "��֯(OU) ���ô���");
                        return;
                    } else {
                        subj.put("organizationalUnit", text_field_x509_organizationalUnit.getText());
                    }

                    if (text_field_x509_cn.getText().length() == 0) {
                        JOptionPane.showMessageDialog(null, "ӵ����(CN) ���ô���");
                        return;
                    } else {
                        subj.put("cn", text_field_x509_cn.getText());
                    }
                    subj.put("mode", "set");
                    System.out.println("subj :" + subj.toString());
                    serialPort.sendComm(g_data_process.send_cmd_csr_upload(subj.toString()));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    //��������֤��
    public void get_csr_upload_button_listener() throws Exception {
        tool_button_upload_csr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JSONObject subj = new JSONObject();

                    subj.put("mode", "get");

                    //�� 0;�� 1
                    int n = JOptionPane.showConfirmDialog(null, "��ѡ�񱣴�·�����ļ���", "", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                      
                        tool_file_chooser_select_req_save_file.setCurrentDirectory(new File("."));         
                        tool_file_chooser_select_req_save_file.setFileSelectionMode(1);//�趨ֻ��ѡ���ļ���
                        int state = tool_file_chooser_select_req_save_file.showOpenDialog(null);//�˾��Ǵ��ļ�ѡ��������Ĵ������
                        if (state == 1) {
                            return;//�����򷵻�
                        } else {
                        	 File dir = tool_file_chooser_select_req_save_file.getSelectedFile();// dirΪѡ�񵽵�Ŀ¼ 
                        	 String name  = "//"+ g_device_info.device_name+"_"+System.currentTimeMillis()+"_site.req";
                        	 File f = new File(dir.getAbsoluteFile()+name);
                            tool_text_x509req_save_file = f.getAbsolutePath();
                            g_device_info.device_x509_req_file_path = f.getAbsolutePath();
                            System.out.println("g_dtu_device_info.device_x509_req_file_path " + g_device_info.device_x509_req_file_path);
                            serialPort.sendComm(g_data_process.send_cmd_csr_upload(subj.toString()));
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    //����ǩ��֤��
    public void get_crt_upload_button_listener() throws Exception {
        tool_button_upload_crt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //�� 0;�� 1
                    int n = JOptionPane.showConfirmDialog(null, "��ѡ��ǩ��֤��", "", JOptionPane.YES_NO_OPTION);
                    if (n == 0) {
                        tool_file_chooser_select_csr_file.setCurrentDirectory(new File("."));
                        tool_file_chooser_select_csr_file.setFileSelectionMode(0);//�趨ֻ��ѡ���ļ�
                        int state = tool_file_chooser_select_csr_file.showOpenDialog(null);//�˾��Ǵ��ļ�ѡ��������Ĵ������
                        if (state == 1) {
                            return;//�����򷵻�
                        } else {
                            File f = tool_file_chooser_select_csr_file.getSelectedFile();
                            g_device_info.device_x509_crt_file_path = f.getAbsolutePath();
                            System.out.println("g_device_info.device_x509_crt_file_path :" + g_device_info.device_x509_crt_file_path);
                            g_data_process.data_frame_data_x509_crt(g_device_info.device_x509_crt_file_path, serialPort);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void tool_status_set_req_info(JSONObject jsonObject) {
        text_field_x509_contry.setText((String) jsonObject.get("contry"));
        text_field_x509_province.setText((String) jsonObject.get("province"));
        text_field_x509_location.setText((String) jsonObject.get("location"));
        text_field_x509_organization.setText((String) jsonObject.get("organization"));
        text_field_x509_organizationalUnit.setText((String) jsonObject.get("organizationalUnit"));
        text_field_x509_cn.setText((String) jsonObject.get("cn"));
       
        if(tool_button_get_csr.hasFocus())
        if(text_field_x509_contry.getText().toString().equals("")&&
        		text_field_x509_province.getText().toString().equals("")&&
        		text_field_x509_location.getText().toString().equals("")&&
        		text_field_x509_organization.getText().toString().equals("")&&
        		text_field_x509_organizationalUnit.getText().toString().equals("")&&
        		text_field_x509_cn.getText().toString().equals("")){
        	 JOptionPane.showMessageDialog(null, "����֤�飬��������֤��");
        }
    }
}
