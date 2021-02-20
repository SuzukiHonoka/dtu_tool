package com.nj.dtu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;

public class tool_ui_config_every extends tool_ui_base {

	// �豸������Ϣ
	JTextField text_field_device_name;
	// JTextField text_field_device_type;
	JTextField text_field_device_location;
	JTextField text_field_device_software_version;
	JComboBox box_device_work_mode;
	// eth
	JTextField text_field_device_wan_ipadd;
	JTextField text_field_device_wan_netmask;
	JTextField text_field_device_wan_gateway;
	JTextField text_field_device_lan_ipadd;
	JTextField text_field_device_lan_netmask;
	// JTextField text_field_device_lan_gateway;
	// apn0
	JTextField text_field_device_apn0;
	JTextField text_field_device_code0;
	JTextField text_field_device_name0;
	JTextField text_field_device_pwd0;
	JTextField text_field_device_pin0;
	// apn1
	JTextField text_field_device_apn1;
	JTextField text_field_device_code1;
	JTextField text_field_device_name1;
	JTextField text_field_device_pwd1;
	JTextField text_field_device_pin1;
	// �������
	JComboBox box_device_isenc1;
	JTextField text_field_device_protect_dtuip1;
	JTextField text_field_device_mainip1;
	JTextField text_field_device_protect_mainip1;
	// ҵ�񴮿�
	JComboBox box_device_rate;
	JComboBox box_device_databit;
	JComboBox box_device_verifybit;
	JComboBox box_device_stopbit;
	JComboBox box_device_conbit;
	// ����
	// tcp101
	JTextField text_field_device_tcp101_servers;
	JComboBox box_device_tcp101_localserver;

	// ��ť
	JButton tool_every_updata_button;
	JButton tool_every_save_button;
	JButton tool_system_reboot_button;

	// ����ʵ��
	tool_uart serialPort;
	// ���ݽ���ʵ��
	tool_data_process g_data_process;

	JPanel devicePanel;
	JPanel pcdPanel;
	JPanel tunnelParamPanel;
	JPanel internetAccessPanel;
	JPanel viceCardDialPanel;
	JPanel businessSerialPortPanel;
	JPanel otherPanel;

	Boolean rebootSuccess = false;
	Boolean isChangeNet = false;

	/**
	 * �豸��Ϣģ��
	 *
	 * @param devicePanel
	 * @param serialPort_p
	 * @param data_process
	 * @return
	 */
	public JPanel deviceInfoPanelInit(tool_uart serialPort_p, tool_data_process data_process) {
		if (devicePanel != null) {
			return devicePanel;
		}
		serialPort = serialPort_p;
		g_data_process = data_process;
		devicePanel = new JPanel();
		devicePanel.setPreferredSize(new Dimension(550, 300));
		devicePanel.setLayout(null);
		devicePanel.setBorder(BorderFactory.createTitledBorder("�豸��Ϣ"));

		// ��ǩ��ʼλ��
		int x_lable_location = 40;
		int y_lable_location = 40;
		// �ı�����ʼλ��
		int x_text_location = 120;
		int y_text_location = 70;

		// ����
		int x_step = 20;
		int y_step = 50;

		// �ı��򳤶ȺͿ��
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// ��ǩ�򳤶ȺͿ��
		int x_lable_length = 80;

		// ************* �豸������Ϣ **********************
		// devicePanel.add(super.buildJLabel("�豸��Ϣ:", x_lable_location,
		// y_lable_location, x_lable_length * 2, y_text_heigth));

		// y_lable_location += y_step;
		devicePanel.add(buildJLabel("�豸����", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_name = buildJTextField("", "device_name", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		devicePanel.add(text_field_device_name);

		y_lable_location += y_step;
		y_text_location += y_step;
		devicePanel.add(buildJLabel("�豸����", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String isenc1[] = { "δ֪", "����-������", "����-������", "����-������", "����-������" };
		box_device_work_mode = buildJComboBox("", "device_type", isenc1, text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		devicePanel.add(box_device_work_mode);
		box_device_work_mode.setEditable(false);

		y_lable_location += y_step;
		y_text_location += y_step;
		devicePanel.add(buildJLabel("��װλ��", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_location = buildJTextField("", "device_location", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		devicePanel.add(text_field_device_location);

		y_lable_location += y_step;
		y_text_location += y_step;
		devicePanel.add(buildJLabel("����汾", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_software_version = buildJTextField("", "device_software_version", text_columns,
				x_text_location, y_lable_location, x_text_length, y_text_heigth);
		devicePanel.add(text_field_device_software_version);
		text_field_device_software_version.setEditable(false);
		return devicePanel;
	}

	/**
	 * ��������ģ��
	 *
	 * @param pcdPanel
	 * @param serialPort_p
	 * @param data_process
	 * @return
	 */
	public JPanel principalCardDial(tool_uart serialPort_p, tool_data_process data_process) {
		if (pcdPanel != null) {
			return pcdPanel;
		}
		serialPort = serialPort_p;
		g_data_process = data_process;
		pcdPanel = new JPanel();
		pcdPanel.setPreferredSize(new Dimension(550, 300));
		pcdPanel.setLayout(null);
		pcdPanel.setBorder(BorderFactory.createTitledBorder("��������"));

		// ��ǩ��ʼλ��
		int x_lable_location = 40;
		int y_lable_location = 40;
		// �ı�����ʼλ��
		int x_text_location = 120;
		int y_text_location = 70;

		// ����
		int x_step = 20;
		int y_step = 50;

		// �ı��򳤶ȺͿ��
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// ��ǩ�򳤶ȺͿ��
		int x_lable_length = 80;

		// pcdPanel.add(buildJLabel("�������ţ�", x_lable_location, y_lable_location,
		// x_lable_length * 2, y_text_heigth));

		// y_lable_location += y_step;
		pcdPanel.add(buildJLabel("APN", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_apn0 = buildJTextField("", "apn", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		pcdPanel.add(text_field_device_apn0);

		y_lable_location += y_step;
		y_text_location += y_step;
		pcdPanel.add(buildJLabel("�û���", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_name0 = buildJTextField("", "�û���", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		pcdPanel.add(text_field_device_name0);

		y_lable_location += y_step;
		y_text_location += y_step;
		pcdPanel.add(buildJLabel("����", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_pwd0 = buildJTextField("", "����", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		pcdPanel.add(text_field_device_pwd0);

		y_lable_location += y_step;
		y_text_location += y_step;
		pcdPanel.add(buildJLabel("PIN��", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_pin0 = buildJTextField("", "pin��", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		pcdPanel.add(text_field_device_pin0);

		y_lable_location += y_step;
		y_text_location += y_step;
		pcdPanel.add(buildJLabel("������ʽ", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_code0 = buildJTextField("", "������ʽ", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		pcdPanel.add(text_field_device_code0);
		return pcdPanel;
	}

	/**
	 * �������ģ��
	 *
	 * @param tunnelParamPanel
	 * @param serialPort_p
	 * @param data_process
	 * @return
	 */
	public JPanel tunnelParamPanel(tool_uart serialPort_p, tool_data_process data_process) {
		if (tunnelParamPanel != null) {
			return tunnelParamPanel;
		}
		serialPort = serialPort_p;
		g_data_process = data_process;
		tunnelParamPanel = new JPanel();
		tunnelParamPanel.setPreferredSize(new Dimension(550, 300));
		tunnelParamPanel.setLayout(null);
		tunnelParamPanel.setBorder(BorderFactory.createTitledBorder("�������"));

		// ��ǩ��ʼλ��
		int x_lable_location = 40;
		int y_lable_location = 40;
		// �ı�����ʼλ��
		int x_text_location = 160;
		int y_text_location = 70;

		// ����
		int x_step = 20;
		int y_step = 50;

		// �ı��򳤶ȺͿ��
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// ��ǩ�򳤶ȺͿ��
		int x_lable_length = 120;

		// tunnelParamPanel.add(buildJLabel("�������:", x_lable_location,
		// y_lable_location, x_lable_length * 2, y_text_heigth));

		// y_lable_location += y_step;
		tunnelParamPanel.add(buildJLabel("�Ƿ����", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String isenc1[] = { "δ֪", "��", "��" };
		box_device_isenc1 = buildJComboBox("", "moshi", isenc1, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		tunnelParamPanel.add(box_device_isenc1);

		y_lable_location += y_step;
		y_text_location += y_step;
		tunnelParamPanel
				.add(buildJLabel("�ܱ������ն�IP", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_protect_dtuip1 = buildJTextField("", "�������ն�IP", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		tunnelParamPanel.add(text_field_device_protect_dtuip1);

		y_lable_location += y_step;
		y_text_location += y_step;
		tunnelParamPanel.add(buildJLabel("��վIP", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_mainip1 = buildJTextField("", "��վIP", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		tunnelParamPanel.add(text_field_device_mainip1);

		y_lable_location += y_step;
		y_text_location += y_step;
		tunnelParamPanel
				.add(buildJLabel("�ܱ�������վIP", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_protect_mainip1 = buildJTextField("", "������", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		tunnelParamPanel.add(text_field_device_protect_mainip1);

		return tunnelParamPanel;
	}

	/**
	 * ���ڲ���ģ��
	 *
	 * @param internetAccessPanel
	 * @param serialPort_p
	 * @param data_process
	 * @return
	 */
	public JPanel internetAccessPanel(tool_uart serialPort_p, tool_data_process data_process) {
		if (internetAccessPanel != null) {
			return internetAccessPanel;
		}
		serialPort = serialPort_p;
		g_data_process = data_process;
		internetAccessPanel = new JPanel();
		internetAccessPanel.setPreferredSize(new Dimension(550, 300));
		internetAccessPanel.setLayout(null);
		internetAccessPanel.setBorder(BorderFactory.createTitledBorder("���ڲ���"));

		// ��ǩ��ʼλ��
		int x_lable_location = 40;
		int y_lable_location = 40;
		// �ı�����ʼλ��
		int x_text_location = 120;
		int y_text_location = 70;

		// ����
		int x_step = 20;
		int y_step = 50;

		// �ı��򳤶ȺͿ��
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// ��ǩ�򳤶ȺͿ��
		int x_lable_length = 80;

		// internetAccessPanel.add(buildJLabel("���ڲ���: ", x_lable_location,
		// y_lable_location, x_lable_length * 2, y_text_heigth));
		// y_lable_location += y_step;
		internetAccessPanel.add(buildJLabel("����IP", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_wan_ipadd = buildJTextField("", "device_wan_ipadd", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		internetAccessPanel.add(text_field_device_wan_ipadd);

		y_lable_location += y_step;
		y_text_location += y_step;
		internetAccessPanel.add(buildJLabel("��������", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_wan_netmask = buildJTextField("", "device_wan_netmask", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		internetAccessPanel.add(text_field_device_wan_netmask);

		y_lable_location += y_step;
		y_text_location += y_step;
		internetAccessPanel.add(buildJLabel("��������", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_wan_gateway = buildJTextField("", "device_wan_gateway", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		internetAccessPanel.add(text_field_device_wan_gateway);

		y_lable_location += y_step;
		y_text_location += y_step;
		internetAccessPanel.add(buildJLabel("����IP", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_lan_ipadd = buildJTextField("", "device_lan_ipadd", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		internetAccessPanel.add(text_field_device_lan_ipadd);

		y_lable_location += y_step;
		y_text_location += y_step;
		internetAccessPanel.add(buildJLabel("��������", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_lan_netmask = buildJTextField("", "device_lan_netmask", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		internetAccessPanel.add(text_field_device_lan_netmask);

		// y_lable_location += y_step;
		// y_text_location += y_step;
		// internetAccessPanel.add(buildJLabel("��������", x_lable_location,
		// y_lable_location, x_lable_length, y_text_heigth));
		// text_field_device_lan_gateway = buildJTextField("",
		// "device_lan_gateway", text_columns, x_text_location,
		// y_lable_location, x_text_length, y_text_heigth);
		// internetAccessPanel.add(text_field_device_lan_gateway);

		return internetAccessPanel;
	}

	/**
	 * ��������ģ��
	 *
	 * @param viceCardDialPanel
	 * @param serialPort_p
	 * @param data_process
	 * @return
	 */
	public JPanel viceCardDialPanel(tool_uart serialPort_p, tool_data_process data_process) {
		if (viceCardDialPanel != null) {
			return viceCardDialPanel;
		}
		serialPort = serialPort_p;
		g_data_process = data_process;
		viceCardDialPanel = new JPanel();
		viceCardDialPanel.setPreferredSize(new Dimension(550, 300));
		viceCardDialPanel.setLayout(null);
		viceCardDialPanel.setBorder(BorderFactory.createTitledBorder("��������"));

		// ��ǩ��ʼλ��
		int x_lable_location = 40;
		int y_lable_location = 40;
		// �ı�����ʼλ��
		int x_text_location = 120;
		int y_text_location = 70;

		// ����
		int x_step = 20;
		int y_step = 50;

		// �ı��򳤶ȺͿ��
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// ��ǩ�򳤶ȺͿ��
		int x_lable_length = 80;

		// viceCardDialPanel.add(buildJLabel("��������:", x_lable_location,
		// y_lable_location, x_lable_length * 2, y_text_heigth));

		// y_lable_location += y_step;
		viceCardDialPanel.add(buildJLabel("APN", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_apn1 = buildJTextField("", "apn", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		viceCardDialPanel.add(text_field_device_apn1);

		y_lable_location += y_step;
		y_text_location += y_step;
		viceCardDialPanel.add(buildJLabel("�û���", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_name1 = buildJTextField("", "�û���", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		viceCardDialPanel.add(text_field_device_name1);

		y_lable_location += y_step;
		y_text_location += y_step;
		viceCardDialPanel.add(buildJLabel("����", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_pwd1 = buildJTextField("", "����", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		viceCardDialPanel.add(text_field_device_pwd1);

		y_lable_location += y_step;
		y_text_location += y_step;
		viceCardDialPanel.add(buildJLabel("PIN��", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_pin1 = buildJTextField("", "pin��", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		viceCardDialPanel.add(text_field_device_pin1);

		y_lable_location += y_step;
		y_text_location += y_step;
		viceCardDialPanel.add(buildJLabel("������ʽ", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_code1 = buildJTextField("", "������ʽ", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		viceCardDialPanel.add(text_field_device_code1);

		return viceCardDialPanel;
	}

	/**
	 * ҵ�񴮿�
	 *
	 * @param businessSerialPortPanel
	 * @param serialPort_p
	 * @param data_process
	 * @return
	 */
	public JPanel businessSerialPortPanel(tool_uart serialPort_p, tool_data_process data_process) {
		if (businessSerialPortPanel != null) {
			return businessSerialPortPanel;
		}
		serialPort = serialPort_p;
		g_data_process = data_process;
		businessSerialPortPanel = new JPanel();
		businessSerialPortPanel.setPreferredSize(new Dimension(550, 300));
		businessSerialPortPanel.setLayout(null);
		businessSerialPortPanel.setBorder(BorderFactory.createTitledBorder("ҵ�񴮿�"));

		// ��ǩ��ʼλ��
		int x_lable_location = 40;
		int y_lable_location = 40;
		// �ı�����ʼλ��
		int x_text_location = 120;
		int y_text_location = 70;

		// ����
		int x_step = 20;
		int y_step = 50;

		// �ı��򳤶ȺͿ��
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// ��ǩ�򳤶ȺͿ��
		int x_lable_length = 80;

		// businessSerialPortPanel.add(buildJLabel("ҵ�񴮿�:", x_lable_location,
		// y_lable_location, x_lable_length * 2, y_text_heigth));

		// y_lable_location += y_step;
		businessSerialPortPanel
				.add(buildJLabel("������", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String rate[] = { "δ֪", "1200", "4800", "9600", "19200", "38400", "57600", "115200" };
		box_device_rate = buildJComboBox("", "rate", rate, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		businessSerialPortPanel.add(box_device_rate);

		y_lable_location += y_step;
		y_text_location += y_step;
		businessSerialPortPanel
				.add(buildJLabel("����λ", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String data_bit[] = { "δ֪", "7", "8" };
		box_device_databit = buildJComboBox("", "data_bit", data_bit, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		businessSerialPortPanel.add(box_device_databit);

		y_lable_location += y_step;
		y_text_location += y_step;
		businessSerialPortPanel
				.add(buildJLabel("У��λ", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String verfy_bit[] = { "δ֪", "��У��", "żУ��", "��У��" };
		box_device_verifybit = buildJComboBox("", "data_bit", verfy_bit, text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		businessSerialPortPanel.add(box_device_verifybit);

		y_lable_location += y_step;
		y_text_location += y_step;
		businessSerialPortPanel
				.add(buildJLabel("ֹͣλ", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String stop_bit[] = { "δ֪", "1", "2" };
		box_device_stopbit = buildJComboBox("", "stop_bit", stop_bit, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		businessSerialPortPanel.add(box_device_stopbit);

		y_lable_location += y_step;
		y_text_location += y_step;
		businessSerialPortPanel
				.add(buildJLabel("����λ", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String con_bit[] = { "δ֪", "Ӳ����", "������" };
		box_device_conbit = buildJComboBox("", "con_bit", con_bit, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		businessSerialPortPanel.add(box_device_conbit);

		return businessSerialPortPanel;
	}

	/**
	 * ����
	 *
	 * @param otherPanel
	 * @param serialPort_p
	 * @param data_process
	 * @return
	 */
	public JPanel otherPanel(tool_uart serialPort_p, tool_data_process data_process) {
		if (otherPanel != null) {
			return otherPanel;
		}
		serialPort = serialPort_p;
		g_data_process = data_process;
		otherPanel = new JPanel();
		otherPanel.setPreferredSize(new Dimension(550, 300));
		otherPanel.setLayout(null);
		otherPanel.setBorder(BorderFactory.createTitledBorder("����"));

		// ��ǩ��ʼλ��
		int x_lable_location = 40;
		int y_lable_location = 40;
		// �ı�����ʼλ��
		int x_text_location = 140;
		int y_text_location = 70;

		// ����
		int x_step = 20;
		int y_step = 50;

		// �ı��򳤶ȺͿ��
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// ��ǩ�򳤶ȺͿ��
		int x_lable_length = 80;

		// otherPanel.add(buildJLabel("����:", x_lable_location, y_lable_location,
		// x_lable_length * 2, y_text_heigth));
		// y_lable_location += y_step;
		otherPanel.add(buildJLabel("101������", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_tcp101_servers = buildJTextField("", "text_field_device_tcp101_servers", text_columns,
				x_text_location, y_lable_location, x_text_length, y_text_heigth);
		otherPanel.add(text_field_device_tcp101_servers);

		y_lable_location += y_step;
		y_text_location += y_step;
		otherPanel.add(buildJLabel("���ط�����", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String tcp_101_local[] = { "δ֪", "��", "��" };
		box_device_tcp101_localserver = buildJComboBox("", "tcp_101_local", tcp_101_local, text_columns,
				x_text_location, y_lable_location, x_text_length, y_text_heigth);
		otherPanel.add(box_device_tcp101_localserver);

		return otherPanel;
	}

	public JPanel globalBtnPanel(JPanel globalBtnPanel) throws Exception {
		globalBtnPanel = new JPanel();
		globalBtnPanel.setPreferredSize(new Dimension(120, 300));
		globalBtnPanel.setLayout(null);
		// globalBtnPanel.setBorder(BorderFactory.createTitledBorder(""));

		// ��Ӱ�ť
		tool_every_updata_button = super.buildJButton("��ȡ����", 10, 10, 100, 30);
		tool_every_save_button = super.buildJButton("�����·�", 10, 80, 100, 30);
		// ����
		tool_system_reboot_button = super.buildJButton("ϵͳ����", 10, 170, 100, 30);

		// ��Ӽ����¼�
		tool_every_updata_button_listener();
		tool_every_save_button_listener();
		tooi_ui_reboot_system();
		globalBtnPanel.add(tool_every_updata_button);
		globalBtnPanel.add(tool_every_save_button);
		globalBtnPanel.add(tool_system_reboot_button);

		return globalBtnPanel;
	}

	// ����text�����õ�ini�ļ�
	public int tool_every_save_to_ini() {
		int ret = 1;
		try {
			// ��ȡ�豸����
			IniEditor device_config = new IniEditor();
			device_config.load(file_ini_name);

			// ������Ϣ
			device_config.set("device", "name_device", text_field_device_name.getText());
			// device_config.set("device","type_device",text_field_device_type.getText());
			String type = (String) box_device_work_mode.getSelectedItem();
			if (type.equals("δ֪")) {
				JOptionPane.showMessageDialog(null, "��ѡ���豸����ģʽ");
				ret = 0;
				return ret;
			} else {

				if (type.equals("����-������")) {
					type = "wireless-uart";
				} else if (type.equals("����-������")) {
					type = "wire-uart";
				} else if (type.equals("����-������")) {
					type = "wireless-wire";
				} else if (type.equals("����-������")) {
					type = "wire-wire";
				}

				device_config.set("device", "type_device", type);
			}

			device_config.set("device", "location_device", text_field_device_location.getText());
			device_config.set("device", "version_device", text_field_device_software_version.getText());

			if (ipCheck(text_field_device_wan_ipadd.getText()) == false) {
				JOptionPane.showMessageDialog(null, "����IP��ʽ����");
				ret = 0;
				return ret;
			}
			if (ipCheck(text_field_device_wan_netmask.getText()) == false) {
				JOptionPane.showMessageDialog(null, "���������ʽ����");
				ret = 0;
				return ret;
			}
			// ·������
			if (text_field_device_wan_gateway.getText().equals("")) {
				if (type.equals("wire-uart") || type.equals("wire-wire")) {
					JOptionPane.showMessageDialog(null, "������������·��");
					ret = 0;
					return ret;
				}
			} else {
				if (type.equals("wireless-uart") || type.equals("wireless-wire")) {
					JOptionPane.showMessageDialog(null, "��������������·��");
					ret = 0;
					return ret;
				}
				if (ipCheck(text_field_device_wan_gateway.getText()) == false) {
					JOptionPane.showMessageDialog(null, "�������ظ�ʽ����");
					ret = 0;
					return ret;
				}
			}
			if (ipCheck(text_field_device_lan_ipadd.getText()) == false) {
				JOptionPane.showMessageDialog(null, "����IP��ʽ����");
				ret = 0;
				return ret;
			}
			if (ipCheck(text_field_device_lan_netmask.getText()) == false) {
				JOptionPane.showMessageDialog(null, "���������ʽ����");
				ret = 0;
				return ret;
			}
			if (!device_config.get("NET0", "devip0").equals(text_field_device_wan_ipadd.getText())
					|| !device_config.get("NET0", "netmask0").equals(text_field_device_wan_netmask.getText())
					|| !device_config.get("NET0", "defaultgw0").equals(text_field_device_wan_gateway.getText())
					|| !device_config.get("NET1", "devip1").equals(text_field_device_lan_ipadd.getText())
					|| !device_config.get("NET1", "netmask1").equals(text_field_device_lan_netmask.getText())) {
				isChangeNet = true;
			} else {
				isChangeNet = false;
			}

			// ����
			device_config.set("NET0", "devip0", text_field_device_wan_ipadd.getText());
			device_config.set("NET0", "netmask0", text_field_device_wan_netmask.getText());
			device_config.set("NET0", "defaultgw0", text_field_device_wan_gateway.getText());
			device_config.set("NET1", "devip1", text_field_device_lan_ipadd.getText());
			device_config.set("NET1", "netmask1", text_field_device_lan_netmask.getText());
			// device_config.set("NET1", "defaultgw1",
			// text_field_device_lan_gateway.getText());

			// DIAL0
			device_config.set("DIAL0", "apn0", text_field_device_apn0.getText());
			device_config.set("DIAL0", "code0", text_field_device_code0.getText());
			device_config.set("DIAL0", "name0", text_field_device_name0.getText());
			device_config.set("DIAL0", "pwd0", text_field_device_pwd0.getText());
			device_config.set("DIAL0", "pin0", text_field_device_pin0.getText());

			// DIAL1
			device_config.set("DIAL1", "apn1", text_field_device_apn1.getText());
			device_config.set("DIAL1", "code1", text_field_device_code1.getText());
			device_config.set("DIAL1", "name1", text_field_device_name1.getText());
			device_config.set("DIAL1", "pwd1", text_field_device_pwd1.getText());
			device_config.set("DIAL1", "pin1", text_field_device_pin1.getText());

			// �������
			if (box_device_isenc1.getSelectedItem() == "��") {
				device_config.set("policy1", "isenc1", "1");
			} else if (box_device_isenc1.getSelectedItem() == "��") {
				device_config.set("policy1", "isenc1", "0");
			} else {
				device_config.set("policy1", "isenc1", "0");
			}
			device_config.set("policy1", "protect_dtuip1", text_field_device_protect_dtuip1.getText());
			device_config.set("policy1", "mainip1", text_field_device_mainip1.getText());
			device_config.set("policy1", "protect_mainip1", text_field_device_protect_mainip1.getText());

			// ҵ�񴮿�
			if (box_device_rate.getSelectedItem() == "δ֪") {
				device_config.set("serial", "rate", "9600");
			} else {
				device_config.set("serial", "rate", (String) box_device_rate.getSelectedItem());
			}

			if (box_device_databit.getSelectedItem() == "δ֪") {
				device_config.set("serial", "databit", "8");
			} else {
				device_config.set("serial", "databit", (String) box_device_databit.getSelectedItem());
			}

			if (box_device_verifybit.getSelectedItem() == "δ֪") {
				device_config.set("serial", "verifybit", "N");
			} else if (box_device_verifybit.getSelectedItem() == "żУ��") {
				device_config.set("serial", "verifybit", "E");
			} else if (box_device_verifybit.getSelectedItem() == "��У��") {
				device_config.set("serial", "verifybit", "O");
			}

			if (box_device_stopbit.getSelectedItem() == "δ֪") {
				device_config.set("serial", "stopbit", "1");
			} else {
				device_config.set("serial", "stopbit", (String) box_device_stopbit.getSelectedItem());
			}

			if (box_device_conbit.getSelectedItem() == "δ֪") {
				device_config.set("serial", "conbit", "Xoff");
			} else if (box_device_conbit.getSelectedItem() == "������") {
				device_config.set("serial", "conbit", "Xoff");
			} else if (box_device_conbit.getSelectedItem() == "Ӳ����") {
				device_config.set("serial", "conbit", "Xon");
			}

			// 101
			device_config.set("101tcp", "servers", text_field_device_tcp101_servers.getText());
			if (box_device_tcp101_localserver.getSelectedItem() == "δ֪") {
				device_config.set("101tcp", "localserver", "0");
			} else if (box_device_tcp101_localserver.getSelectedItem() == "��") {
				device_config.set("101tcp", "localserver", "1");
			} else if (box_device_tcp101_localserver.getSelectedItem() == "��") {
				device_config.set("101tcp", "localserver", "0");
			}

			// �����ļ�
			device_config.save(file_ini_name);
			JOptionPane.showMessageDialog(null, "���ñ���ɹ���");

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return ret;
	}

	// �ύ����
	public void tool_every_save_button_listener() throws Exception {
		tool_every_save_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// ���浽PC��ini
					if (tool_every_save_to_ini() == 0) {
						return;
					}
					// ��ȡPC��ini�ļ����豸
					serialPort.sendComm(g_data_process.send_cmd_config_upload());
					if(isChangeNet){
						 int n = JOptionPane.showConfirmDialog(null, "������������仯�������豸��", "", JOptionPane.YES_NO_OPTION);
						 if (n == 0) {
							 serialPort.sendComm(g_data_process.send_cmd_run_shell("reboot"));
							/// ������̴߳�����ʾ
								Thread thread = new Thread() {
									public void run() {
										// ��ȡ�豸����
										try {
				
											Thread.sleep(100);
											serialPort.closeNet();
											
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								};
								thread.start();
							
							 
						 }
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	// ��ȡ�豸���ò���
	public void tool_every_updata_button_listener() throws Exception {
		tool_every_updata_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// ��ȡ�豸����
					serialPort.sendComm(g_data_process.send_cmd_config_request());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	// ����ϵͳ
	public void tooi_ui_reboot_system() {
		tool_system_reboot_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// �� 0;�� 1
					int n = JOptionPane.showConfirmDialog(null, "ȷ������ϵͳ��", "", JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						rebootSuccess = false;
						g_data_process.g_tool_ui_all.loading("��������");
						cat_version();
					}

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

	public void cat_version() {
		/// ������̴߳�����ʾ
		Thread thread = new Thread() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					serialPort.sendComm(g_data_process.send_cmd_run_shell("reboot"));
					Thread.sleep(24000);
					while (true) {
						if (rebootSuccess)
							break;
						serialPort.sendComm(
								g_data_process.send_cmd_run_shell_2(g_data_process.CMD_REBOOT, "cat /etc/version"));
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

	// ������Ϣ����ʾ�ڽ���
	public void tool_ui_panel_every_text_update(tool_device_info device_config) {
		new Thread(new Runnable() {
			public void run() {
				// �豸������Ϣ
				text_field_device_name.setText(device_config.device_name);
				// text_field_device_type.setText(device_config.device_type);
				text_field_device_location.setText(device_config.device_location);
				text_field_device_software_version.setText(device_config.device_software_vesion);
				// {"","����-����", "����-����","����-����","����-����"};
				if (device_config.device_type.equals("wireless-uart")) {
					box_device_work_mode.setSelectedIndex(1);
				} else if (device_config.device_type.equals("wire-uart")) {
					box_device_work_mode.setSelectedIndex(2);
				} else if (device_config.device_type.equals("wireless-wire")) {
					box_device_work_mode.setSelectedIndex(3);
				} else if (device_config.device_type.equals("wire-wire")) {
					box_device_work_mode.setSelectedIndex(4);
				} else {
					box_device_work_mode.setSelectedIndex(0);
				}

				// eth
				text_field_device_wan_ipadd.setText(device_config.device_wan_ip);
				text_field_device_wan_netmask.setText(device_config.device_wan_netmask);
				text_field_device_wan_gateway.setText(device_config.device_wan_gateway);
				text_field_device_lan_ipadd.setText(device_config.device_lan_ip);
				text_field_device_lan_netmask.setText(device_config.device_lan_netmask);
				// text_field_device_lan_gateway.setText(device_config.device_lan_gateway);

				// apn0
				text_field_device_apn0.setText(device_config.device_apn0);
				text_field_device_code0.setText(device_config.device_code0);
				text_field_device_name0.setText(device_config.device_name0);
				text_field_device_pwd0.setText(device_config.device_pwd0);
				text_field_device_pin0.setText(device_config.device_pin0);

				// apn1
				text_field_device_apn1.setText(device_config.device_apn1);
				text_field_device_code1.setText(device_config.device_code1);
				text_field_device_name1.setText(device_config.device_name1);
				text_field_device_pwd1.setText(device_config.device_pwd1);
				text_field_device_pin1.setText(device_config.device_pin1);

				// �������
				if (device_config.device_isenc1.equals("1")) {
					box_device_isenc1.setSelectedIndex(1);
				} else if (device_config.device_isenc1.equals("0")) {
					box_device_isenc1.setSelectedIndex(2);
				} else {
					box_device_isenc1.setSelectedIndex(0);
				}
				text_field_device_protect_dtuip1.setText(device_config.device_protect_dtuip1);
				text_field_device_mainip1.setText(device_config.device_mainip1);
				text_field_device_protect_mainip1.setText(device_config.device_protect_mainip1);

				// 101
				System.out.println("101");
				System.out.println("set 101: " + device_config.device_tcp101_servers);
				System.out.println("set 101: " + device_config.device_tcp101_localserver);
				text_field_device_tcp101_servers.setText(device_config.device_tcp101_servers);
				if (device_config.device_tcp101_localserver.equals("1")) {
					box_device_tcp101_localserver.setSelectedIndex(1);
				} else if (device_config.device_tcp101_localserver.equals("0")) {
					box_device_tcp101_localserver.setSelectedIndex(2);
				} else {
					box_device_tcp101_localserver.setSelectedIndex(2);
				}

				// ҵ�񴮿�
				// String rate[] = {"δ֪","1200", "4800", "9600", "19200",
				// "38400", "57600", "115200"};
				// 1 2 3 4 5 6 7
				System.out.println("ҵ�񴮿�");
				if (device_config.device_rate.equals("1200")) {
					box_device_rate.setSelectedIndex(1);
				} else if (device_config.device_rate.equals("4800")) {
					box_device_rate.setSelectedIndex(2);
				} else if (device_config.device_rate.equals("9600")) {
					box_device_rate.setSelectedIndex(3);
				} else if (device_config.device_rate.equals("19200")) {
					box_device_rate.setSelectedIndex(4);
				} else if (device_config.device_rate.equals("38400")) {
					box_device_rate.setSelectedIndex(5);
				} else if (device_config.device_rate.equals("57600")) {
					box_device_rate.setSelectedIndex(6);
				} else if (device_config.device_rate.equals("115200")) {
					box_device_rate.setSelectedIndex(7);
				} else {
					box_device_rate.setSelectedIndex(0);
				}

				System.out.println("device_databit");
				if (device_config.device_databit.equals("7")) {
					box_device_databit.setSelectedIndex(1);
				} else if (device_config.device_databit.equals("8")) {
					box_device_databit.setSelectedIndex(2);
				} else {
					box_device_databit.setSelectedIndex(0);
				}
				System.out.println("device_verifybit");
				if (device_config.device_verifybit.equals("O")) {
					box_device_verifybit.setSelectedIndex(1);
				} else if (device_config.device_verifybit.equals("E")) {
					box_device_verifybit.setSelectedIndex(2);
				} else if (device_config.device_verifybit.equals("N")) {
					box_device_verifybit.setSelectedIndex(3);
				} else {
					box_device_verifybit.setSelectedIndex(0);
				}

				System.out.println("device_stopbit");
				if (device_config.device_stopbit.equals("1")) {
					box_device_stopbit.setSelectedIndex(1);
				} else if (device_config.device_stopbit.equals("2")) {
					box_device_stopbit.setSelectedIndex(2);
				} else {
					box_device_stopbit.setSelectedIndex(0);
				}

				if (device_config.device_conbit != null) {
					if (device_config.device_conbit.equals("Xon")) {
						box_device_conbit.setSelectedIndex(1);
					} else if (device_config.device_conbit.equals("Xoff")) {
						box_device_conbit.setSelectedIndex(2);
					} else {
						box_device_conbit.setSelectedIndex(2);
					}
				} else {
					box_device_conbit.setSelectedIndex(2);
				}
				g_data_process.g_tool_ui_all.Panel_log.tool_log_set("������Ϣ��ˢ��");
				// ҵ�񴮿� end
			}

			public Runnable start() {
				// TODO Auto-generated method stub
				run();
				return null;
			}
		}.start());
	}

}
