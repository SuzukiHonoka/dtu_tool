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
    //����ʵ��
    tool_uart serialPort;
    //���ݽ���ʵ��
    tool_data_process g_data_process;
    tool_device_info g_device_info;
    LoadingPanel glasspane = new LoadingPanel();

    public tool_ui(tool_uart serialPort, tool_data_process data_process, tool_device_info device_info) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            frame.setTitle("TOOL���ù���");
            Image logo = new ImageIcon(tool_ui.class.getResource("logo.png")).getImage();  
            frame.setIconImage(logo);
            frame.setSize(1000, 720);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //�رպ��˳�
            frame.setLocationRelativeTo(null);//��������Ļ�м���ʾ
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
            //�������β˵�
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
        //�Լ��˵�
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("���ܵ���");
        DefaultMutableTreeNode paramConfig = new DefaultMutableTreeNode("��������");
        DefaultMutableTreeNode searchStatus = new DefaultMutableTreeNode("״̬��ѯ");
//        DefaultMutableTreeNode sysSetup = new DefaultMutableTreeNode("ϵͳ����");
       // DefaultMutableTreeNode certConfig = new DefaultMutableTreeNode("֤������");
        DefaultMutableTreeNode certConfig2 = new DefaultMutableTreeNode("֤������");
        DefaultMutableTreeNode communicationTest = new DefaultMutableTreeNode("ͨ�Ų���");
        DefaultMutableTreeNode deivceUpgrade = new DefaultMutableTreeNode("�豸����");
        DefaultMutableTreeNode configImportExport = new DefaultMutableTreeNode("���õ��뵼��");
        DefaultMutableTreeNode other = new DefaultMutableTreeNode("����");

        // �����˵�
        DefaultMutableTreeNode deviceInfo = new DefaultMutableTreeNode("�豸��Ϣ");
        DefaultMutableTreeNode principalCardDial = new DefaultMutableTreeNode("��������");
        DefaultMutableTreeNode viceCardDial = new DefaultMutableTreeNode("��������");
        DefaultMutableTreeNode tunnelParam = new DefaultMutableTreeNode("�������");
        DefaultMutableTreeNode internetAccessParam = new DefaultMutableTreeNode("���ڲ���");
        DefaultMutableTreeNode businessCom = new DefaultMutableTreeNode("ҵ�񴮿�");
        DefaultMutableTreeNode tcp = new DefaultMutableTreeNode("TCP����");
        
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
       
        // Ĭ��չ����
        expandTree(tree, new TreePath(root));
        tree.setSelectionPath(new TreePath(deviceInfo));
        treePanel = new JPanel();
        treePanel.add(tree);
        treePanel.setBorder(BorderFactory.createTitledBorder("���ܵ���"));
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
                    if ("��������".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_every.principalCardDial( serialPort, g_data_process));
                    } else if ("�������".equals(selectionNode.toString())) {
                        centerPanel .add(Panel_every.tunnelParamPanel( serialPort, g_data_process));
                    } else if ("���ڲ���".equals(selectionNode.toString())) {
                        centerPanel .add(Panel_every.internetAccessPanel( serialPort, g_data_process));
                    } else if ("��������".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_every.viceCardDialPanel(serialPort, g_data_process));
                    } else if ("ҵ�񴮿�".equals(selectionNode.toString())) {
                        centerPanel .add( Panel_every.businessSerialPortPanel( serialPort, g_data_process));
                    } else if ("TCP����".equals(selectionNode.toString())) {
                        centerPanel .add(Panel_every.otherPanel( serialPort, g_data_process));
                    } else if ("״̬��ѯ".equals(selectionNode.toString())) {
                        centerPanel .add(Panel_status_check.checkStatusPanel());
                    }
//                    else if ("֤������".equals(selectionNode.toString())) {
//                        centerPanel.add(Panel_x509.certConfigPanel());
//                    }
                    else if ("֤������".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_certificate.certConfigPanel());
                    }  
                    else if ("ͨ�Ų���".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_status_check.communicationTestPanel());
                    } else if ("�豸����".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_system.deviceUpgradePanel());
                    } else if ("���õ��뵼��".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_system.configImportExportPanel());
                    }else if ("����".equals(selectionNode.toString())) {
                        centerPanel.add(Panel_other.otherPanel());
                    }else{
                    	 centerPanel.add( Panel_every.deviceInfoPanelInit(serialPort, g_data_process));
                    }
                    centerPanel.validate(); 
                    centerPanel.repaint(); 
                }
            }
        });
      ///������̴߳�����ʾ  Ԥ���� 
        Thread thread = new Thread(){
         public void run()
         {
        	 //��ȡ�豸����
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
                expandTree(tree, path);//չ���ڵ�ݹ�
            }
        }
        tree.expandPath(parent);//չ���ø��ڵ�������ӽڵ�
    }

}
