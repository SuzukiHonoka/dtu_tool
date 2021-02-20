package com.nj.dtu;

/**
 * 串口必要参数接收类
 */
public class ParamConfig {

    private String serialNumber;// 串口号
    private int baudRate;        // 波特率
    private int checkoutBit;    // 校验位
    private int dataBit;        // 数据位
    private int stopBit;        // 停止位
    
    public ParamConfig() {}
        
    /**
     * 构造方法
     * @param serialNumber    串口号
     * @param baudRate        波特率
     * @param checkoutBit    校验位
     * @param dataBit        数据位
     * @param stopBit        停止位
     */
    public ParamConfig(String serialNumber, int baudRate, int checkoutBit, int dataBit, int stopBit) {
        this.serialNumber = serialNumber;
        this.baudRate = baudRate;
        this.checkoutBit = checkoutBit;
        this.dataBit = dataBit;
        this.stopBit = stopBit;
    }
    public String getSerialNumber(){
    	return this.serialNumber;
    }
    public int getBaudRate(){
    	return this.baudRate;
    }
    public int getCheckoutBit(){
    	return this.checkoutBit;
    }
    public int getDataBit(){
    	return this.dataBit;
    }
    public int getStopBit(){
    	return this.stopBit;
    }
}