package com.nj.dtu;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class tool_main {
	public static void main(String[] args) throws Exception {
		loadLIB(".");
		setUi();
		show();
	}

	public static void loadLIB(String path){
		File dir = new File(path);
		File[] files = dir.listFiles((dir1, name) -> name.endsWith(".dll"));
		if (files != null) {
			for (File file: files){
				System.load(file.getAbsolutePath());
			}
		}
	}

	public static void show() {
		// 全局的设备信息
		tool_device_info dtu_device_info = new tool_device_info();

		// 数据处理类
		tool_data_process dtu_tool_data_process = new tool_data_process();

		// 串口类，包含接收数据响应
		tool_uart dtu_tool_uart = new tool_uart(dtu_tool_data_process);
		// 全局ui
		tool_ui dtu_tool_ui = new tool_ui(dtu_tool_uart, dtu_tool_data_process, dtu_device_info);

		dtu_tool_data_process.data_init(dtu_tool_uart, dtu_device_info, dtu_tool_ui);
		
		OpSqliteDB.init();
		 new Thread() {
				public void run() {
					usbUtils.isExits();
					while (true){
						usbUtils.isExits();
						if(usbUtils.usbExit&&!usbUtils.usbExitLastTime)
							  JOptionPane.showMessageDialog(null, "密钥插入");
						if(!usbUtils.usbExit&&usbUtils.usbExitLastTime){
							  JOptionPane.showMessageDialog(null, "密钥拔出");
						      System.exit(0);
						}
						      
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}.start();
		
	}

	public static void setUi() {
		Font font = new Font("隶书", Font.PLAIN, 20);
		Font font2 = new Font("仿宋", Font.PLAIN, 16);
		Font font3 = new Font("仿宋", Font.PLAIN, 14);
		UIManager.put("TitledBorder.font", font);
		UIManager.put("Label.font", font2);
		UIManager.put("ComboBox.font", font2);
		UIManager.put("ComboBoxItem.font", font2);
		UIManager.put("Button.font", font2);
		UIManager.put("RadioButton.font",font2);
		UIManager.put("OptionPane.buttonFont", font2);
		UIManager.put("OptionPane.messageFont", font2);
		UIManager.put("TabbedPane.font", font2);
		UIManager.put("TextField.font", font2);
		UIManager.put("TextArea.font", font3);
		

		// UIManager.put("Button.font",font);
		// UIManager.put("ToggleButton.font",font);
		// UIManager.put("RadioButton.font",font);
		// UIManager.put("CheckBox.font",font);
		// UIManager.put("ColorChooser.font",font);
		// UIManager.put("ToggleButton.font",font);
		// UIManager.put("ComboBox.font",font);
		// UIManager.put("ComboBoxItem.font",font);
		// UIManager.put("InternalFrame.titleFont",font);
		// UIManager.put("Label.font",font);
		// UIManager.put("List.font",font);
		// UIManager.put("MenuBar.font",font);
		// UIManager.put("Menu.font",font);
		// UIManager.put("MenuItem.font",font);
		// UIManager.put("RadioButtonMenuItem.font",font);
		// UIManager.put("CheckBoxMenuItem.font",font);
		// UIManager.put("PopupMenu.font",font);
		// UIManager.put("OptionPane.font",font);
		// UIManager.put("Panel.font",font);
		// UIManager.put("ProgressBar.font",font);
		// UIManager.put("ScrollPane.font",font);
		// UIManager.put("Viewport",font);
		// UIManager.put("TabbedPane.font",font);
		// UIManager.put("TableHeader.font",font);
		// UIManager.put("TextField.font",font);
		// UIManager.put("PasswordFiled.font",font);
		// UIManager.put("TextArea.font",font);
		// UIManager.put("TextPane.font",font);
		// UIManager.put("EditorPane.font",font);
		// UIManager.put("TitledBorder.font",font);
		// UIManager.put("ToolBar.font",font);
		// UIManager.put("ToolTip.font",font);
		// UIManager.put("Tree.font",font);
	}
}