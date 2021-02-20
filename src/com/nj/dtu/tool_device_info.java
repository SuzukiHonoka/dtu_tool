package com.nj.dtu;

public class tool_device_info extends tool_ui_base {
    //设备基本信息
    public String device_name = "";
    public String device_type = "";
    public String device_location = "";
    public String device_software_vesion = "";
    public String device_name_dian = "";
    public String location_dian = "";
    public String name_wang = "";
    public String rx = "";
    public String tx = "";
    public String time = "";

    //设备eth
    public String device_wan_ip = "";
    public String device_wan_netmask = "";
    public String device_wan_gateway = "";
    public String device_lan_ip = "";
    public String device_lan_netmask = "";
    public String device_lan_gateway = "";

    //设备sim
    public String device_apn0 = "";
    public String device_code0 = "";
    public String device_name0 = "";
    public String device_pwd0 = "";
    public String device_pin0 = "";
    public String device_apn1 = "";
    public String device_code1 = "";
    public String device_name1 = "";
    public String device_pwd1 = "";
    public String device_pin1 = "";

    public String device_isenc1 = "";
    public String device_protect_dtuip1 = "";
    public String device_mainip1 = "";
    public String device_protect_mainip1 = "";

    public String device_rate = "";
    public String device_databit = "";
    public String device_verifybit = "";
    public String device_stopbit = "";
    public String device_conbit = "";

    public String device_tcp101_servers = "";
    public String device_tcp101_localserver = "";

    public String device_config_download_config_file;
    public String device_config_download_config_file_path;

    public String device_config_upload_config_file_path;

    public String device_x509_req_file_path;
    public String device_x509_crt_file_path;

    public String device_status_wan_rx = "";
    public String device_status_wan_tx = "";
    public String device_status_run_time = "";

    public String ping_info;

    public String device_softversion;
    // **************************     状态信息           ************************************/


    public void tool_device_info_get() {
        IniEditor device_config = new IniEditor();
        try {
            device_config.load(file_ini_name);
            //device
            device_name = device_config.get("device", "name_device");
            device_type = device_config.get("device", "type_device");
            device_location = device_config.get("device", "location_device");
            device_software_vesion = device_config.get("device", "version_device");
            System.out.println("device_name: " + device_name);
            System.out.println("device_type: " + device_type);
            System.out.println("device_location: " + device_location);
            System.out.println("device_software_vesion: " + device_software_vesion);

            //网口
            device_wan_ip = device_config.get("NET0", "devip0");
            device_wan_netmask = device_config.get("NET0", "netmask0");
            device_wan_gateway = device_config.get("NET0", "defaultgw0");
            device_lan_ip = device_config.get("NET1", "devip1");
            device_lan_netmask = device_config.get("NET1", "netmask1");
            device_lan_gateway = device_config.get("NET1", "defaultgw1");
            System.out.println("device_wan_ip: " + device_wan_ip);
            System.out.println("device_wan_netmask: " + device_wan_netmask);
            System.out.println("device_wan_gateway: " + device_wan_gateway);
            System.out.println("device_lan_ip: " + device_lan_ip);
            System.out.println("device_lan_netmask: " + device_lan_netmask);
            System.out.println("device_lan_gateway: " + device_lan_gateway);

            //DIAL0
            device_apn0 = device_config.get("DIAL0", "apn0");
            device_code0 = device_config.get("DIAL0", "code0");
            device_name0 = device_config.get("DIAL0", "name0");
            device_pwd0 = device_config.get("DIAL0", "pwd0");
            device_pin0 = device_config.get("DIAL0", "pin0");
            System.out.println("device_apn0: " + device_apn0);
            System.out.println("device_code0: " + device_code0);
            System.out.println("device_name0: " + device_name0);
            System.out.println("device_pwd0: " + device_pwd0);
            System.out.println("device_pin0: " + device_pin0);

            //DIAL1
            device_apn1 = device_config.get("DIAL1", "apn1");
            device_code1 = device_config.get("DIAL1", "code1");
            device_name1 = device_config.get("DIAL1", "name1");
            device_pwd1 = device_config.get("DIAL1", "pwd1");
            device_pin1 = device_config.get("DIAL1", "pin1");
            System.out.println("device_apn1: " + device_apn1);
            System.out.println("device_code1: " + device_code1);
            System.out.println("device_name1: " + device_name1);
            System.out.println("device_pwd1: " + device_pwd1);
            System.out.println("device_pin1: " + device_pin1);

            //隧道参数
            device_isenc1 = device_config.get("policy1", "isenc1");
            device_protect_dtuip1 = device_config.get("policy1", "protect_dtuip1");
            device_mainip1 = device_config.get("policy1", "mainip1");
            device_protect_mainip1 = device_config.get("policy1", "protect_mainip1");

            System.out.println("device_isenc1: " + device_isenc1);
            System.out.println("device_protect_dtuip1: " + device_protect_dtuip1);
            System.out.println("device_mainip1: " + device_mainip1);
            System.out.println("device_protect_mainip1: " + device_protect_mainip1);

            //业务串口
            device_rate = device_config.get("serial", "rate");
            device_databit = device_config.get("serial", "databit");
            device_verifybit = device_config.get("serial", "verifybit");
            device_stopbit = device_config.get("serial", "stopbit");
            device_conbit = device_config.get("serial", "conbit");
            System.out.println("device_conbit: " + device_conbit);

            // 101
            device_tcp101_servers = device_config.get("101tcp", "servers");
            device_tcp101_localserver = device_config.get("101tcp", "localserver");
            System.out.println("device_tcp101_servers: " + device_tcp101_servers);
            System.out.println("device_tcp101_localserver: " + device_tcp101_localserver);
        } catch (Exception e) {
            System.out.println("读取出错 " + e);
        }
    }

    //
    public void tool_device_status_set(String wan_rx, String wan_tx, String run_time) {
        device_status_wan_rx = wan_rx;
        device_status_wan_tx = wan_tx;
        device_status_run_time = run_time;
    }

    //讲配置信息保存到
    public void tool_device_info_set(tool_ui_config_every tool_user_set_config) {
        IniEditor device_config = new IniEditor();
        try {
            //device_config.load(file_ini_name);

        } catch (Exception e) {
            System.out.println("写出错 " + e);
        }
    }

    //保存ping信息
    public void tool_device_ping_info(String ping_ret) {
        ping_info = ping_info + ping_ret;
    }

    //保存ping信息
    public void tool_device_ping_info_clear() {
        ping_info = "";
    }

    //保存版本信息
    public void tool_device_softversion_info(String str) {
        device_softversion = str.substring(0, 4);
    }
}
