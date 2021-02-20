package com.nj.dtu;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class tool_ui_base {
	final String file_ini_name = "sys_param.ini";
	final int MAX_split = 1024;
    //创建下拉列表
    public JComboBox buildJComboBox(Object selectedItem, String name, String[] elements, int selectedIndex, int x, int y, int width, int height) {
        DefaultComboBoxModel codeTypeModel = new DefaultComboBoxModel();
        // elements 下拉框中的选项
        for (String element : elements) {
            codeTypeModel.addElement(element);
        }
        JComboBox codeTypeBox = new JComboBox(codeTypeModel);
        codeTypeBox.setName(name);
        // 默认选中的下拉框选项
        codeTypeBox.setSelectedItem(selectedItem);
//        codeTypeBox.setSelectedItem(selectedIndex);
        codeTypeBox.setBounds(x, y, width, height);
        // 添加下拉框事件监听器
        codeTypeBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 选择的下拉框选项
                    System.out.println(e.getItem());
                }
            }
        });
        return codeTypeBox;
    }
    
    //创建文本框
    public JTextField buildJTextField(String defaultValue, String name, int columns, int x, int y, int width, int height) {
        JTextField text = new JTextField(columns);
        text.setText(defaultValue);
        text.setName(name);
        text.setBounds(x, y, width, height);
        return text;
    }
    
    //创建按钮
    public JButton buildJButton(String name, int x, int y, int width, int height) {
        JButton button = new JButton(name);
        button.setBounds(x, y, width, height);
        return button;
    }
    //创建按钮
    public JButton buildJButtonSmall(String name, int x, int y, int width, int height) {
        JButton button = new JButton(name);
        button.setBounds(x, y, width, height);
        Font font2 = new Font("仿宋",Font.PLAIN,14);  
        button.setFont(font2);
        button.setMargin(new Insets(0,0,0,0));
        return button;
    }
    
    //创建TEXT
    public JTextArea buildJJTextArea(int row, int clo,int x, int y, int width, int height) {
    	JTextArea text_area = new JTextArea(row,clo);
    	text_area.setBounds(x, y, width, height);
        return text_area;
    }
    
    //创建滚动条
    public JScrollPane buildJJScrollPane(JTextArea text_area,int x, int y, int width, int height) {
    	JScrollPane scrollpane = new JScrollPane(text_area);
//    	JTextArea text_area = new JTextArea(row,clo);
    	scrollpane.setBounds(x, y, width, height);
        return scrollpane;
    }
    
    //创建标签
    public JLabel buildJLabel(String name, int x, int y, int width, int height) {
        JLabel label = new JLabel(name);
        label.setBounds(x, y, width, height);
        return label;
    }
    
	public void write_str_2_ini_file(final String strBuffer) throws IOException {
	    File fileText = new File(file_ini_name);
	    
		if (!fileText.exists()) {
			fileText.createNewFile();
		}
		
	    try { 
	    	// 向文件写入对象写入信息
	   	    FileWriter fileWriter = new FileWriter(fileText);
	        // 写文件      
	        fileWriter.write(strBuffer);
	        // 关闭
	        fileWriter.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	//文件保存
	public void ParaToFile(final String strBuffer,final String file_name) throws IOException {
	    File fileText = new File(file_name);
		if(!fileText.exists()){
			fileText.createNewFile();
		}
	    try { 
	      // 向文件写入对象写入信息
	      FileWriter fileWriter = new FileWriter(fileText);
	 
	      // 写文件      
	      fileWriter.write(strBuffer);
	      // 关闭
	      fileWriter.close();
	    }
	    catch (IOException e)
	    {
	      //
	      e.printStackTrace();
	    }
	}

    //将byte数组写入文件
	public void createFile(String path, byte[] content) throws IOException {
	
		if(null==path||("").equals(path))
			return;
		FileOutputStream fos = new FileOutputStream(path);
	
		fos.write(content);
		fos.close();
	}

	//读取文件
    public  String readFileContent(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
    	FileInputStream file = new FileInputStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(file));
            String tempStr = new String();
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
                sbf.append("\n");
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
    
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            //stringBuilder.append(i + ":");//序号 2个数字为1组
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
           stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    //读取文件的MD5
	public static String getMD5(String file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
        	MessageDigest MD5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            
            while ((length = fileInputStream.read(buffer)) != -1) {
            	MD5.update(buffer, 0, length);
            }
            return bytesToHexString(MD5.digest()).toUpperCase();
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
            return null;
        } catch (IOException e) {
        	e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            try {
                if (fileInputStream != null)
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return null;
    }
	
	public static String convertStringToHex(String str) {
		 char[] chars = "0123456789ABCDEF".toCharArray();
		 StringBuilder sb = new StringBuilder("");
		 byte[] bs = str.getBytes();
		 int bit;
		 for (int i = 0; i < bs.length; i++) {
		 bit = (bs[i] & 0x0f0) >> 4;
		 sb.append(chars[bit]);
		 bit = bs[i] & 0x0f;
		 sb.append(chars[bit]);
		 }
		 return sb.toString().trim();
   }
	
	public  byte[] intToByteArray4(int value) {
    	return hexToByteArray(HexStr(Integer.toHexString(value)));
    }
	
    public  String HexStr(String tmp) {
    	String xuhaowei="";
    	tmp=tmp.toUpperCase();
    	if(tmp.length()==1) {
    		xuhaowei="000"+tmp;
    	}else if(tmp.length()==2) {
    		xuhaowei="00"+tmp;
    	}else if(tmp.length()==3) {
    		xuhaowei="0"+tmp;
    	}else if(tmp.length()==4) {
    		xuhaowei=tmp;
    	}
    	return xuhaowei;
    }

    
    public byte[] intToByteArray8(int value) {
    	return hexToByteArray(HexStr8(Integer.toHexString(value)));
    }
    
    public String HexStr8(String tmp) {
    	String xuhaowei="";
    	tmp=tmp.toUpperCase();
    	if(tmp.length()==1) {
    		xuhaowei="0000000"+tmp;
    	}else if(tmp.length()==2) {
    		xuhaowei="000000"+tmp;
    	}else if(tmp.length()==3) {
    		xuhaowei="00000"+tmp;
    	}else if(tmp.length()==4) {
    		xuhaowei="0000"+tmp;
    	}else if(tmp.length()==5) {
    		xuhaowei="000"+tmp;
    	}else if(tmp.length()==6) {
    		xuhaowei="00"+tmp;
    	}else if(tmp.length()==7) {
    		xuhaowei="0"+tmp;
    	}else if(tmp.length()==8) {
    		xuhaowei=tmp;
    	}
    	return xuhaowei;
    }
    
	public byte[] checkSumByte(byte[] writerBuffer,int buf_len) {
		short bb = 0;
	    for(int i = 0;i<buf_len;i++) {
	    	bb= (short) ((bb+writerBuffer[i]) & 0xFF);
	    }
	    String result = Integer.toHexString(bb);
	    result = result.toUpperCase();
	    byte [] bytes = hexToByteArray(result);
	    result = Integer.toHexString(0xFF-bytes[0]&0xFF);
	    result = result.toUpperCase();
	    if(result.length()<2) {
	    	result = "0"+result;
	    }
	    byte[] checksum = hexToByteArray(result);
		return checksum;	
	}
	
	//byte 字节校验
	public byte do_check_sum(byte[] buf,int buf_start,int buf_len) {
		int checksum = 0;
		byte ret;
		
	    for (int i = buf_start;i < buf_len;i++) {
	    	checksum += buf[i];
	    }
        
        ret = (byte) (checksum & 0xff);
        System.out.printf("ret 1  %2x\n",ret);
        ret = (byte)(0xff - ret);
        System.out.printf("ret 2  %2x\n",ret);
		return ret;
	}
	
	//将数字转化
	public String num_str_2byte(int num) {
		return String.format("%04x", num);
	}
	
    /**
     * hex字符串转byte数组
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
	public static String strTo16hexstr(String s) {
	    String str = "";
	    for (int i = 0; i < s.length(); i++) {
	        int ch = (int) s.charAt(i);
	        String s4 = Integer.toHexString(ch);
	        str = str + s4;
	    }
	    return str;
	}
	
    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     * */
    public static boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (text.matches(regex)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }
    //匹配  www.baidu.com
    public static boolean url_check(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(www.|[a-zA-Z].)[a-zA-Z0-9\\-\\.]+\\.(com|edu|gov|mil|net|org|biz|info|name|museum|us|ca|uk)*$";
            // 判断ip地址是否与正则表达式匹配
            if (text.matches(regex)) {
                // 返回判断信息
            	System.out.println("text true");
                return true;
            } else {
                // 返回判断信息
            	System.out.println("text false");
                return false;
            }
        }
        return false;
    }
    /**
     * Hex字符串转byte
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }
	
    /**
     * hex字符串转byte数组
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            // 奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            // 偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }
	
	public static String data_checkSum2(String str) {
		byte[] writerBuffer = null;
		writerBuffer = hexToByteArray(str);
		short bb = 0;
	    for(int i = 0;i<writerBuffer.length;i++) {
	    	bb= (short) ((bb+writerBuffer[i])& 0xFF);
	    }
	    String result = Integer.toHexString(bb);
	    result = result.toUpperCase();
	    if(result.length()<2) {
	    	result = "0"+result;
	    }
		return result;
	}
	public static String checkSum(String str) {
		byte[] writerBuffer = null;
		writerBuffer = hexToByteArray(str);
		short bb = 0;
	    for(int i = 0;i<writerBuffer.length;i++) {
	    	bb= (short) ((bb+writerBuffer[i])& 0xFF);
	    }
	    String result = Integer.toHexString(bb);
	    result = result.toUpperCase();
	    byte [] bytes = hexToByteArray(result);
	    result = Integer.toHexString(0xFF-bytes[0]&0xFF);
	    result = result.toUpperCase();
	    if(result.length()<2) {
	    	result = "0"+result;
	    }
		return result;
	}
	
    public String OctHex(int xuhao) {
    	String xuhaowei="";
    	String tmp=Integer.toHexString(xuhao);
    	tmp = tmp.toUpperCase();
    	if(tmp.length()==1) {
    		xuhaowei="000"+tmp;
    	}else if(tmp.length()==2) {
    		xuhaowei="00"+tmp;
    	}else if(tmp.length()==3) {
    		xuhaowei="0"+tmp;
    	}else if(tmp.length()==4) {
    		xuhaowei=tmp;
    	}
    	return xuhaowei;
    }
    
    //16进制字符串转数字 int
	public static int str16_to_int16(String s) {
        int b = Integer.parseInt(s.replaceAll("^0[x|X]", ""), 16);
//        System.out.println("b " + b);
        return b;
	}	
    //讲int 转化为字符串
	public String numToHex16(int b) {
        return String.format("%04x", b);
    }
    //讲int 转化为字符串
	public String numToHex_2_byte(int b) {
        return String.format("%04x", b);
    }
	
    //讲int 转化为字符串
	public String numToHex8(int b) {
        return String.format("%08x", b);
    }
	
    //讲int 转化为字符串
	public String numToHex_4_byte(int b) {
        return String.format("%08x", b);
    }
    public String DecStr(int xuhao) {
    	String xuhaowei="";
    	String tmp=Integer.toString(xuhao);
    	if(tmp.length()==1) {
    		xuhaowei="000"+tmp;
    	}else if(tmp.length()==2) {
    		xuhaowei="00"+tmp;
    	}else if(tmp.length()==3) {
    		xuhaowei="0"+tmp;
    	}else {
    		xuhaowei=tmp;
    	}
    	return xuhaowei;
    }


	
	public static String convertHexToString(String hexStr) throws UnsupportedEncodingException {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes,"gb2312");
	}
	
    //文件追加写
    public static void appendFile(String file_name, String content) throws IOException {
    	File file = new File(file_name);
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, true),"UTF-8");
        out.write(content);
        out.close();
    }
    /** 
     * 获取当前时间 
     *  
     * @param args 
     */ 
    public static String getNowTime() { 
        Calendar cal = Calendar.getInstance(); 
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd"); 
        String lastMonth = dft.format(cal.getTime()); 
        System.out.println(lastMonth);
        return lastMonth; 
    } 
}
