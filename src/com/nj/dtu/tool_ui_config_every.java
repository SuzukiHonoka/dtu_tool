package com.nj.dtu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;

public class tool_ui_config_every extends tool_ui_base {

	// 设备基本信息
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
	// 隧道参数
	JComboBox box_device_isenc1;
	JTextField text_field_device_protect_dtuip1;
	JTextField text_field_device_mainip1;
	JTextField text_field_device_protect_mainip1;
	// 业务串口
	JComboBox box_device_rate;
	JComboBox box_device_databit;
	JComboBox box_device_verifybit;
	JComboBox box_device_stopbit;
	JComboBox box_device_conbit;
	// 其他
	// tcp101
	JTextField text_field_device_tcp101_servers;
	JComboBox box_device_tcp101_localserver;

	// 按钮
	JButton tool_every_updata_button;
	JButton tool_every_save_button;
	JButton tool_system_reboot_button;

	// 串口实例
	tool_uart serialPort;
	// 数据交互实例
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
	 * 设备信息模块
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
		devicePanel.setBorder(BorderFactory.createTitledBorder("设备信息"));

		// 标签起始位置
		int x_lable_location = 40;
		int y_lable_location = 40;
		// 文本框起始位置
		int x_text_location = 120;
		int y_text_location = 70;

		// 步长
		int x_step = 20;
		int y_step = 50;

		// 文本框长度和宽度
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// 标签框长度和宽度
		int x_lable_length = 80;

		// ************* 设备基本信息 **********************
		// devicePanel.add(super.buildJLabel("设备信息:", x_lable_location,
		// y_lable_location, x_lable_length * 2, y_text_heigth));

		// y_lable_location += y_step;
		devicePanel.add(buildJLabel("设备名称", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_name = buildJTextField("", "device_name", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		devicePanel.add(text_field_device_name);

		y_lable_location += y_step;
		y_text_location += y_step;
		devicePanel.add(buildJLabel("设备类型", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String isenc1[] = { "未知", "无线-串口型", "有线-串口型", "无线-网口型", "有线-网口型" };
		box_device_work_mode = buildJComboBox("", "device_type", isenc1, text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		devicePanel.add(box_device_work_mode);
		box_device_work_mode.setEditable(false);

		y_lable_location += y_step;
		y_text_location += y_step;
		devicePanel.add(buildJLabel("安装位置", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_location = buildJTextField("", "device_location", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		devicePanel.add(text_field_device_location);

		y_lable_location += y_step;
		y_text_location += y_step;
		devicePanel.add(buildJLabel("软件版本", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_software_version = buildJTextField("", "device_software_version", text_columns,
				x_text_location, y_lable_location, x_text_length, y_text_heigth);
		devicePanel.add(text_field_device_software_version);
		text_field_device_software_version.setEditable(false);
		return devicePanel;
	}

	/**
	 * 主卡拨号模块
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
		pcdPanel.setBorder(BorderFactory.createTitledBorder("主卡拨号"));

		// 标签起始位置
		int x_lable_location = 40;
		int y_lable_location = 40;
		// 文本框起始位置
		int x_text_location = 120;
		int y_text_location = 70;

		// 步长
		int x_step = 20;
		int y_step = 50;

		// 文本框长度和宽度
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// 标签框长度和宽度
		int x_lable_length = 80;

		// pcdPanel.add(buildJLabel("主卡拨号：", x_lable_location, y_lable_location,
		// x_lable_length * 2, y_text_heigth));

		// y_lable_location += y_step;
		pcdPanel.add(buildJLabel("APN", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_apn0 = buildJTextField("", "apn", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		pcdPanel.add(text_field_device_apn0);

		y_lable_location += y_step;
		y_text_location += y_step;
		pcdPanel.add(buildJLabel("用户名", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_name0 = buildJTextField("", "用户名", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		pcdPanel.add(text_field_device_name0);

		y_lable_location += y_step;
		y_text_location += y_step;
		pcdPanel.add(buildJLabel("密码", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_pwd0 = buildJTextField("", "密码", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		pcdPanel.add(text_field_device_pwd0);

		y_lable_location += y_step;
		y_text_location += y_step;
		pcdPanel.add(buildJLabel("PIN码", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_pin0 = buildJTextField("", "pin码", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		pcdPanel.add(text_field_device_pin0);

		y_lable_location += y_step;
		y_text_location += y_step;
		pcdPanel.add(buildJLabel("网络制式", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_code0 = buildJTextField("", "网络制式", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		pcdPanel.add(text_field_device_code0);
		return pcdPanel;
	}

	/**
	 * 隧道参数模块
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
		tunnelParamPanel.setBorder(BorderFactory.createTitledBorder("隧道参数"));

		// 标签起始位置
		int x_lable_location = 40;
		int y_lable_location = 40;
		// 文本框起始位置
		int x_text_location = 160;
		int y_text_location = 70;

		// 步长
		int x_step = 20;
		int y_step = 50;

		// 文本框长度和宽度
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// 标签框长度和宽度
		int x_lable_length = 120;

		// tunnelParamPanel.add(buildJLabel("隧道参数:", x_lable_location,
		// y_lable_location, x_lable_length * 2, y_text_heigth));

		// y_lable_location += y_step;
		tunnelParamPanel.add(buildJLabel("是否加密", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String isenc1[] = { "未知", "是", "否" };
		box_device_isenc1 = buildJComboBox("", "moshi", isenc1, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		tunnelParamPanel.add(box_device_isenc1);

		y_lable_location += y_step;
		y_text_location += y_step;
		tunnelParamPanel
				.add(buildJLabel("受保护的终端IP", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_protect_dtuip1 = buildJTextField("", "保护的终端IP", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		tunnelParamPanel.add(text_field_device_protect_dtuip1);

		y_lable_location += y_step;
		y_text_location += y_step;
		tunnelParamPanel.add(buildJLabel("主站IP", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_mainip1 = buildJTextField("", "主站IP", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		tunnelParamPanel.add(text_field_device_mainip1);

		y_lable_location += y_step;
		y_text_location += y_step;
		tunnelParamPanel
				.add(buildJLabel("受保护的主站IP", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_protect_mainip1 = buildJTextField("", "保护的", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		tunnelParamPanel.add(text_field_device_protect_mainip1);

		return tunnelParamPanel;
	}

	/**
	 * 网口参数模块
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
		internetAccessPanel.setBorder(BorderFactory.createTitledBorder("网口参数"));

		// 标签起始位置
		int x_lable_location = 40;
		int y_lable_location = 40;
		// 文本框起始位置
		int x_text_location = 120;
		int y_text_location = 70;

		// 步长
		int x_step = 20;
		int y_step = 50;

		// 文本框长度和宽度
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// 标签框长度和宽度
		int x_lable_length = 80;

		// internetAccessPanel.add(buildJLabel("网口参数: ", x_lable_location,
		// y_lable_location, x_lable_length * 2, y_text_heigth));
		// y_lable_location += y_step;
		internetAccessPanel.add(buildJLabel("外网IP", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_wan_ipadd = buildJTextField("", "device_wan_ipadd", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		internetAccessPanel.add(text_field_device_wan_ipadd);

		y_lable_location += y_step;
		y_text_location += y_step;
		internetAccessPanel.add(buildJLabel("外网掩码", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_wan_netmask = buildJTextField("", "device_wan_netmask", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		internetAccessPanel.add(text_field_device_wan_netmask);

		y_lable_location += y_step;
		y_text_location += y_step;
		internetAccessPanel.add(buildJLabel("外网网关", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_wan_gateway = buildJTextField("", "device_wan_gateway", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		internetAccessPanel.add(text_field_device_wan_gateway);

		y_lable_location += y_step;
		y_text_location += y_step;
		internetAccessPanel.add(buildJLabel("内网IP", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_lan_ipadd = buildJTextField("", "device_lan_ipadd", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		internetAccessPanel.add(text_field_device_lan_ipadd);

		y_lable_location += y_step;
		y_text_location += y_step;
		internetAccessPanel.add(buildJLabel("内网掩码", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_lan_netmask = buildJTextField("", "device_lan_netmask", text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		internetAccessPanel.add(text_field_device_lan_netmask);

		// y_lable_location += y_step;
		// y_text_location += y_step;
		// internetAccessPanel.add(buildJLabel("内网网关", x_lable_location,
		// y_lable_location, x_lable_length, y_text_heigth));
		// text_field_device_lan_gateway = buildJTextField("",
		// "device_lan_gateway", text_columns, x_text_location,
		// y_lable_location, x_text_length, y_text_heigth);
		// internetAccessPanel.add(text_field_device_lan_gateway);

		return internetAccessPanel;
	}

	/**
	 * 副卡拨号模块
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
		viceCardDialPanel.setBorder(BorderFactory.createTitledBorder("副卡拨号"));

		// 标签起始位置
		int x_lable_location = 40;
		int y_lable_location = 40;
		// 文本框起始位置
		int x_text_location = 120;
		int y_text_location = 70;

		// 步长
		int x_step = 20;
		int y_step = 50;

		// 文本框长度和宽度
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// 标签框长度和宽度
		int x_lable_length = 80;

		// viceCardDialPanel.add(buildJLabel("副卡拨号:", x_lable_location,
		// y_lable_location, x_lable_length * 2, y_text_heigth));

		// y_lable_location += y_step;
		viceCardDialPanel.add(buildJLabel("APN", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_apn1 = buildJTextField("", "apn", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		viceCardDialPanel.add(text_field_device_apn1);

		y_lable_location += y_step;
		y_text_location += y_step;
		viceCardDialPanel.add(buildJLabel("用户名", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_name1 = buildJTextField("", "用户名", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		viceCardDialPanel.add(text_field_device_name1);

		y_lable_location += y_step;
		y_text_location += y_step;
		viceCardDialPanel.add(buildJLabel("密码", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_pwd1 = buildJTextField("", "密码", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		viceCardDialPanel.add(text_field_device_pwd1);

		y_lable_location += y_step;
		y_text_location += y_step;
		viceCardDialPanel.add(buildJLabel("PIN码", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_pin1 = buildJTextField("", "pin码", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		viceCardDialPanel.add(text_field_device_pin1);

		y_lable_location += y_step;
		y_text_location += y_step;
		viceCardDialPanel.add(buildJLabel("网络制式", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_code1 = buildJTextField("", "网络制式", text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		viceCardDialPanel.add(text_field_device_code1);

		return viceCardDialPanel;
	}

	/**
	 * 业务串口
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
		businessSerialPortPanel.setBorder(BorderFactory.createTitledBorder("业务串口"));

		// 标签起始位置
		int x_lable_location = 40;
		int y_lable_location = 40;
		// 文本框起始位置
		int x_text_location = 120;
		int y_text_location = 70;

		// 步长
		int x_step = 20;
		int y_step = 50;

		// 文本框长度和宽度
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// 标签框长度和宽度
		int x_lable_length = 80;

		// businessSerialPortPanel.add(buildJLabel("业务串口:", x_lable_location,
		// y_lable_location, x_lable_length * 2, y_text_heigth));

		// y_lable_location += y_step;
		businessSerialPortPanel
				.add(buildJLabel("波特率", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String rate[] = { "未知", "1200", "4800", "9600", "19200", "38400", "57600", "115200" };
		box_device_rate = buildJComboBox("", "rate", rate, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		businessSerialPortPanel.add(box_device_rate);

		y_lable_location += y_step;
		y_text_location += y_step;
		businessSerialPortPanel
				.add(buildJLabel("数据位", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String data_bit[] = { "未知", "7", "8" };
		box_device_databit = buildJComboBox("", "data_bit", data_bit, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		businessSerialPortPanel.add(box_device_databit);

		y_lable_location += y_step;
		y_text_location += y_step;
		businessSerialPortPanel
				.add(buildJLabel("校验位", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String verfy_bit[] = { "未知", "奇校验", "偶校验", "无校验" };
		box_device_verifybit = buildJComboBox("", "data_bit", verfy_bit, text_columns, x_text_location,
				y_lable_location, x_text_length, y_text_heigth);
		businessSerialPortPanel.add(box_device_verifybit);

		y_lable_location += y_step;
		y_text_location += y_step;
		businessSerialPortPanel
				.add(buildJLabel("停止位", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String stop_bit[] = { "未知", "1", "2" };
		box_device_stopbit = buildJComboBox("", "stop_bit", stop_bit, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		businessSerialPortPanel.add(box_device_stopbit);

		y_lable_location += y_step;
		y_text_location += y_step;
		businessSerialPortPanel
				.add(buildJLabel("流控位", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String con_bit[] = { "未知", "硬流控", "无流控" };
		box_device_conbit = buildJComboBox("", "con_bit", con_bit, text_columns, x_text_location, y_lable_location,
				x_text_length, y_text_heigth);
		businessSerialPortPanel.add(box_device_conbit);

		return businessSerialPortPanel;
	}

	/**
	 * 其他
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
		otherPanel.setBorder(BorderFactory.createTitledBorder("其他"));

		// 标签起始位置
		int x_lable_location = 40;
		int y_lable_location = 40;
		// 文本框起始位置
		int x_text_location = 140;
		int y_text_location = 70;

		// 步长
		int x_step = 20;
		int y_step = 50;

		// 文本框长度和宽度
		int x_text_length = 185;
		int y_text_heigth = 25;
		int text_columns = 20;

		// 标签框长度和宽度
		int x_lable_length = 80;

		// otherPanel.add(buildJLabel("其他:", x_lable_location, y_lable_location,
		// x_lable_length * 2, y_text_heigth));
		// y_lable_location += y_step;
		otherPanel.add(buildJLabel("101服务器", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		text_field_device_tcp101_servers = buildJTextField("", "text_field_device_tcp101_servers", text_columns,
				x_text_location, y_lable_location, x_text_length, y_text_heigth);
		otherPanel.add(text_field_device_tcp101_servers);

		y_lable_location += y_step;
		y_text_location += y_step;
		otherPanel.add(buildJLabel("本地服务器", x_lable_location, y_lable_location, x_lable_length, y_text_heigth));
		String tcp_101_local[] = { "未知", "是", "否" };
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

		// 添加按钮
		tool_every_updata_button = super.buildJButton("获取配置", 10, 10, 100, 30);
		tool_every_save_button = super.buildJButton("配置下发", 10, 80, 100, 30);
		// 重启
		tool_system_reboot_button = super.buildJButton("系统重启", 10, 170, 100, 30);

		// 添加监听事件
		tool_every_updata_button_listener();
		tool_every_save_button_listener();
		tooi_ui_reboot_system();
		globalBtnPanel.add(tool_every_updata_button);
		globalBtnPanel.add(tool_every_save_button);
		globalBtnPanel.add(tool_system_reboot_button);

		return globalBtnPanel;
	}

	// 保存text的设置到ini文件
	public int tool_every_save_to_ini() {
		int ret = 1;
		try {
			// 获取设备参数
			IniEditor device_config = new IniEditor();
			device_config.load(file_ini_name);

			// 基本信息
			device_config.set("device", "name_device", text_field_device_name.getText());
			// device_config.set("device","type_device",text_field_device_type.getText());
			String type = (String) box_device_work_mode.getSelectedItem();
			if (type.equals("未知")) {
				JOptionPane.showMessageDialog(null, "请选择设备工作模式");
				ret = 0;
				return ret;
			} else {

				if (type.equals("无线-串口型")) {
					type = "wireless-uart";
				} else if (type.equals("有线-串口型")) {
					type = "wire-uart";
				} else if (type.equals("无线-网口型")) {
					type = "wireless-wire";
				} else if (type.equals("有线-网口型")) {
					type = "wire-wire";
				}

				device_config.set("device", "type_device", type);
			}

			device_config.set("device", "location_device", text_field_device_location.getText());
			device_config.set("device", "version_device", text_field_device_software_version.getText());

			if (ipCheck(text_field_device_wan_ipadd.getText()) == false) {
				JOptionPane.showMessageDialog(null, "外网IP格式错误");
				ret = 0;
				return ret;
			}
			if (ipCheck(text_field_device_wan_netmask.getText()) == false) {
				JOptionPane.showMessageDialog(null, "外网掩码格式错误");
				ret = 0;
				return ret;
			}
			// 路由类型
			if (text_field_device_wan_gateway.getText().equals("")) {
				if (type.equals("wire-uart") || type.equals("wire-wire")) {
					JOptionPane.showMessageDialog(null, "请设置外网口路由");
					ret = 0;
					return ret;
				}
			} else {
				if (type.equals("wireless-uart") || type.equals("wireless-wire")) {
					JOptionPane.showMessageDialog(null, "请勿设置外网口路由");
					ret = 0;
					return ret;
				}
				if (ipCheck(text_field_device_wan_gateway.getText()) == false) {
					JOptionPane.showMessageDialog(null, "外网网关格式错误");
					ret = 0;
					return ret;
				}
			}
			if (ipCheck(text_field_device_lan_ipadd.getText()) == false) {
				JOptionPane.showMessageDialog(null, "内网IP格式错误");
				ret = 0;
				return ret;
			}
			if (ipCheck(text_field_device_lan_netmask.getText()) == false) {
				JOptionPane.showMessageDialog(null, "内网掩码格式错误");
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

			// 网口
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

			// 隧道参数
			if (box_device_isenc1.getSelectedItem() == "是") {
				device_config.set("policy1", "isenc1", "1");
			} else if (box_device_isenc1.getSelectedItem() == "否") {
				device_config.set("policy1", "isenc1", "0");
			} else {
				device_config.set("policy1", "isenc1", "0");
			}
			device_config.set("policy1", "protect_dtuip1", text_field_device_protect_dtuip1.getText());
			device_config.set("policy1", "mainip1", text_field_device_mainip1.getText());
			device_config.set("policy1", "protect_mainip1", text_field_device_protect_mainip1.getText());

			// 业务串口
			if (box_device_rate.getSelectedItem() == "未知") {
				device_config.set("serial", "rate", "9600");
			} else {
				device_config.set("serial", "rate", (String) box_device_rate.getSelectedItem());
			}

			if (box_device_databit.getSelectedItem() == "未知") {
				device_config.set("serial", "databit", "8");
			} else {
				device_config.set("serial", "databit", (String) box_device_databit.getSelectedItem());
			}

			if (box_device_verifybit.getSelectedItem() == "未知") {
				device_config.set("serial", "verifybit", "N");
			} else if (box_device_verifybit.getSelectedItem() == "偶校验") {
				device_config.set("serial", "verifybit", "E");
			} else if (box_device_verifybit.getSelectedItem() == "奇校验") {
				device_config.set("serial", "verifybit", "O");
			}

			if (box_device_stopbit.getSelectedItem() == "未知") {
				device_config.set("serial", "stopbit", "1");
			} else {
				device_config.set("serial", "stopbit", (String) box_device_stopbit.getSelectedItem());
			}

			if (box_device_conbit.getSelectedItem() == "未知") {
				device_config.set("serial", "conbit", "Xoff");
			} else if (box_device_conbit.getSelectedItem() == "无流控") {
				device_config.set("serial", "conbit", "Xoff");
			} else if (box_device_conbit.getSelectedItem() == "硬流控") {
				device_config.set("serial", "conbit", "Xon");
			}

			// 101
			device_config.set("101tcp", "servers", text_field_device_tcp101_servers.getText());
			if (box_device_tcp101_localserver.getSelectedItem() == "未知") {
				device_config.set("101tcp", "localserver", "0");
			} else if (box_device_tcp101_localserver.getSelectedItem() == "是") {
				device_config.set("101tcp", "localserver", "1");
			} else if (box_device_tcp101_localserver.getSelectedItem() == "否") {
				device_config.set("101tcp", "localserver", "0");
			}

			// 保存文件
			device_config.save(file_ini_name);
			JOptionPane.showMessageDialog(null, "配置保存成功！");

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return ret;
	}

	// 提交配置
	public void tool_every_save_button_listener() throws Exception {
		tool_every_save_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// 保存到PC的ini
					if (tool_every_save_to_ini() == 0) {
						return;
					}
					// 读取PC的ini文件到设备
					serialPort.sendComm(g_data_process.send_cmd_config_upload());
					if(isChangeNet){
						 int n = JOptionPane.showConfirmDialog(null, "网络参数发生变化，重启设备？", "", JOptionPane.YES_NO_OPTION);
						 if (n == 0) {
							 serialPort.sendComm(g_data_process.send_cmd_run_shell("reboot"));
							/// 单起个线程处理显示
								Thread thread = new Thread() {
									public void run() {
										// 获取设备参数
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

	// 获取设备配置参数
	public void tool_every_updata_button_listener() throws Exception {
		tool_every_updata_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// 获取设备参数
					serialPort.sendComm(g_data_process.send_cmd_config_request());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	// 重启系统
	public void tooi_ui_reboot_system() {
		tool_system_reboot_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// 是 0;否 1
					int n = JOptionPane.showConfirmDialog(null, "确认重启系统？", "", JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						rebootSuccess = false;
						g_data_process.g_tool_ui_all.loading("正在重启");
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
		/// 单起个线程处理显示
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

	// 更新信息，显示在界面
	public void tool_ui_panel_every_text_update(tool_device_info device_config) {
		new Thread(new Runnable() {
			public void run() {
				// 设备基本信息
				text_field_device_name.setText(device_config.device_name);
				// text_field_device_type.setText(device_config.device_type);
				text_field_device_location.setText(device_config.device_location);
				text_field_device_software_version.setText(device_config.device_software_vesion);
				// {"","串口-串口", "串口-网口","网口-网口","网口-串口"};
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

				// 隧道参数
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

				// 业务串口
				// String rate[] = {"未知","1200", "4800", "9600", "19200",
				// "38400", "57600", "115200"};
				// 1 2 3 4 5 6 7
				System.out.println("业务串口");
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
				g_data_process.g_tool_ui_all.Panel_log.tool_log_set("配置信息已刷新");
				// 业务串口 end
			}

			public Runnable start() {
				// TODO Auto-generated method stub
				run();
				return null;
			}
		}.start());
	}

}
