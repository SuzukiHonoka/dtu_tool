package com.nj.dtu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.util.Enumeration;

public class tool_ui extends tool_ui_base {
    JFrame frame = new JFrame();
    Container container = frame.getContentPane();
    JPanel centerPanel;
    JPanel systemLogPanel, globalBtnPanel;
    JTree tree;
    JPanel treePanel;

    tool_ui_config_every Panel_every;
    tool_ui_load Panel_load;
    tool_ui_status_check Panel_status_check;
    tool_ui_system Panel_system;
    tool_ui_log Panel_log;
  //  tool_ui_x509 Panel_x509;
    tool_ui_certificate Panel_certificate;
    tool_other Panel_other;
    tool_ui_login Panel_login;
    tool_ui_set_user Panel_set;
    //串口实例
    tool_uart serialPort;
    //数据交互实例
    tool_data_process g_data_process;
    tool_device_info g_device_info;
    LoadingPanel glasspane = new LoadingPanel();

    public tool_ui(tool_uart serialPort, tool_data_process data_process, tool_device_info device_info) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            frame.setTitle("TOOL配置工具");
            Image logo = new ImageIcon(tool_ui.class.getResource("logo.png")).getImage();  
            frame.setIconImage(logo);
            frame.setSize(1000, 720);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //关闭后退出
            frame.setLocationRelativeTo(null);//窗口在屏幕中间显示
            this.serialPort = serialPort;
            this.g_data_process = data_process;
            g_device_info = device_info;

            Panel_every = new tool_ui_config_every();
            Panel_load = new tool_ui_load(this.serialPort, this.g_data_process);
            Panel_status_check = new tool_ui_status_check(this.serialPort, this.g_data_process, g_device_info);
            Panel_system = new tool_ui_system(this.serialPort, this.g_data_process, g_device_info);
            Panel_log = new tool_ui_log(this.serialPort, this.g_data_process, g_device_info);
          //  Panel_x509 = new tool_ui_x509(this.serialPort, this.g_data_process, g_device_info);
            Panel_certificate = new tool_ui_certificate(this.serialPort, this.g_data_process, g_device_info);
            Panel_other = new tool_other(this.serialPort, this.g_data_process, g_device_info);
            Panel_login = new tool_ui_login(this);
            Panel_set = new tool_ui_set_user(this);
            centerPanel = new JPanel();
            Panel_set.tool_ui_login_init();
            //创建树形菜单
            this.createTreeMenu();

            centerPanel.setPreferredSize(new Dimension(600, 300));
            container.add(Panel_load.tool_ui_load_init(), BorderLayout.NORTH);
            container.add(Panel_login.tool_ui_login_init(), BorderLayout.CENTER);  
            Panel_every.globalBtnPanel(globalBtnPanel);
            Panel_log.systemLogPanel(systemLogPanel);
//            container.add(treePanel, BorderLayout.WEST);
//            container.add(centerPanel, BorderLayout.CENTER);       
//            container.add(Panel_every.globalBtnPanel(globalBtnPanel), BorderLayout.EAST);
 //           container.add(Panel_log.systemLogPanel(systemLogPanel), BorderLayout.SOUTH);
          
            frame.setVisible(true);
            frame.setResizable(false);
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    try {
                        serialPort.closeSerialPort();
                        serialPort.closeNet();
                        System.out.println("uart close !!");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        glasspane.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
        frame.setGlassPane(glasspane);
        
      
    }
    public void showLoginSuccess(){
      stop();
      container.removeAll();
      try {
		container.add(Panel_load.tool_ui_load_init(), BorderLayout.NORTH);
		container.add(treePanel, BorderLayout.WEST);
	    container.add(centerPanel, BorderLayout.CENTER);       
	    container.add(Panel_every.globalBtnPanel(globalBtnPanel), BorderLayout.EAST);
	    container.add(Panel_log.systemLogPanel(systemLogPanel), BorderLayout.SOUTH);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      container.validate();
      container.repaint();
    }
    public void showLoginSuccessSuper(){
        container.removeAll();
        try {
  		container.add(Panel_load.tool_ui_load_init(), BorderLayout.NORTH);
  	    container.add(Panel_set.tool_ui_login_init(), BorderLayout.CENTER);       
  	    container.add(Panel_log.systemLogPanel(systemLogPanel), BorderLayout.SOUTH);
  	    
  	} catch (Exception e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
        container.validate();
        container.repaint();
      }
    public void loading(String tip){
        glasspane.setText(tip);
        glasspane.start();    
    }
    public void stop(){
    	glasspane.stop();
    }
    private void createTreeMenu() {
        //以及菜单
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("功能导航");
        DefaultMutableTreeNode paramConfig = new DefaultMutableTreeNode("参数配置");
        DefaultMutableTreeNode searchStatus = new DefaultMutableTreeNode("状态查询");
//        DefaultMutableTreeNode sysSetup = new DefaultMutableTreeNode("系统设置");
       // DefaultMutableTreeNode certConfig = new DefaultMutableTreeNode("证书配置");
        DefaultMutableTreeNode certConfig2 = new DefaultMutableTreeNode("证书配置");
        DefaultMutableTreeNode communicationTest = new DefaultMutableTreeNode("通信测试");
        DefaultMutableTreeNode deivceUpgrade = new DefaultMutableTreeNode("设备升级");
        DefaultMutableTreeNode configImportExport = new DefaultMutableTreeNode("配置导入导出");
        DefaultMutableTreeNode other = new DefaultMutableTreeNode("其他");

        // 二级菜单
        DefaultMutableTreeNode deviceInfo = new DefaultMutableTreeNode("设备信息");
        DefaultMutableTreeNode principalCardDial = new DefaultMutableTreeNode("主卡拨号");
        DefaultMutableTreeNode viceCardDial = new DefaultMutableTreeNode("副卡拨号");
        DefaultMutableTreeNode tunnelParam = new DefaultMutableTreeNode("隧道参数");
        DefaultMutableTreeNode internetAccessParam = new DefaultMutableTreeNode("网口参数");
        DefaultMutableTreeNode businessCom = new DefaultMutableTreeNode("业务串口");
        DefaultMutableTreeNode tcp = new DefaultMutableTreeNode("TCP参数");
        
        root.add(paramConfig);
        root.add(searchStatus);
        root.add(communicationTest);
       // root.add(certConfig);
        root.add(certConfig2);
        root.add(deivceUpgrade);
        root.add(configImportExport);
        root.add(other);
    

        paramConfig.add(deviceInfo);
        paramConfig.add(principalCardDial);
        paramConfig.add(viceCardDial);
        paramConfig.add(tunnelParam);
        paramConfig.add(internetAccessParam);
        paramConfig.add(businessCom);
        paramConfig.add(tcp);

        TreeModel treeModel = new DefaultTreeModel(root);
        tree = new JTree();
        tree.setModel(treeModel);
        tree.setRootVisible(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 0));
       
        // 默认展开树
        expandTree(tree, new TreePath(root));
        tree.setSelectionPath(new TreePath(deviceInfo));
        treePanel = new JPanel();
        treePanel.add(tree);
        treePanel.setBorder(BorderFactory.createTitledBorder("功能导航"));
        treePanel.setPreferredSize(new Dimension(300, 300));
        treePanel.setLayout(new GridLayout(0, 1));
        //JScrollPane scrollPane = new JScrollPane(treePanel);
      
        tree.setSelectionRow(1);
        centerPanel.add( Panel_every.deviceInfoPanelInit(serialPort, g_data_process));
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (!tree.isSelectionEmpty()) {
                    DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode) tree
                            .getLastSelectedPathComponent();
                    centerPanel.removeAll();
                    if ("主卡拨号".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_every.principalCardDial( serialPort, g_data_process));
                    } else if ("隧道参数".equals(selectionNode.toString())) {
                        centerPanel .add(Panel_every.tunnelParamPanel( serialPort, g_data_process));
                    } else if ("网口参数".equals(selectionNode.toString())) {
                        centerPanel .add(Panel_every.internetAccessPanel( serialPort, g_data_process));
                    } else if ("副卡拨号".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_every.viceCardDialPanel(serialPort, g_data_process));
                    } else if ("业务串口".equals(selectionNode.toString())) {
                        centerPanel .add( Panel_every.businessSerialPortPanel( serialPort, g_data_process));
                    } else if ("TCP参数".equals(selectionNode.toString())) {
                        centerPanel .add(Panel_every.otherPanel( serialPort, g_data_process));
                    } else if ("状态查询".equals(selectionNode.toString())) {
                        centerPanel .add(Panel_status_check.checkStatusPanel());
                    }
//                    else if ("证书配置".equals(selectionNode.toString())) {
//                        centerPanel.add(Panel_x509.certConfigPanel());
//                    }
                    else if ("证书配置".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_certificate.certConfigPanel());
                    }  
                    else if ("通信测试".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_status_check.communicationTestPanel());
                    } else if ("设备升级".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_system.deviceUpgradePanel());
                    } else if ("配置导入导出".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_system.configImportExportPanel());
                    }else if ("其他".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_other.otherPanel());
                    }else{
                    	 centerPanel.add( Panel_every.deviceInfoPanelInit(serialPort, g_data_process));
                    }
                    centerPanel.validate(); 
                    centerPanel.repaint(); 
                }
            }
        });
      ///单起个线程处理显示  预加载 
        Thread thread = new Thread(){
         public void run()
         {
        	 //获取设备参数
             try {
            	  Panel_every.principalCardDial( serialPort, g_data_process);
                  Panel_every.tunnelParamPanel( serialPort, g_data_process);
                  Panel_every.internetAccessPanel( serialPort, g_data_process);
                  Panel_every.viceCardDialPanel(serialPort, g_data_process);
                  Panel_every.businessSerialPortPanel( serialPort, g_data_process);
                  Panel_every.otherPanel( serialPort, g_data_process);
                  Panel_status_check.checkStatusPanel();
                //  Panel_x509.certConfigPanel();
                  Panel_certificate.certConfigPanel();
                  Panel_status_check.communicationTestPanel();
                  Panel_system.deviceUpgradePanel();
                  Panel_system.configImportExportPanel();
                  Panel_other.otherPanel();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                  
          }                                   
        };
        thread.start();
                                       
      
    }

    private static void expandTree(JTree tree, TreePath parent) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandTree(tree, path);//展开节点递归
            }
        }
        tree.expandPath(parent);//展开该父节点下面的子节点
    }

}
