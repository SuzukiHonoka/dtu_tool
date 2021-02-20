package com.nj.dtu;

/**
 * ���ڱ�Ҫ����������
 */
public class ParamConfig {

    private String serialNumber;// ���ں�
    private int baudRate;        // ������
    private int checkoutBit;    // У��λ
    private int dataBit;        // ����λ
    private int stopBit;        // ֹͣλ
    
    public ParamConfig() {}
        
    /**
     * ���췽��
     * @param serialNumber    ���ں�
     * @param baudRate        ������
     * @param checkoutBit    У��λ
     * @param dataBit        ����λ
     * @param stopBit        ֹͣλ
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