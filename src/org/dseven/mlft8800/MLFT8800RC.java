/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dseven.mlft8800;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.copyOf;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dseven.mlutils.MLlogUtils;
import purejavacomm.*;

/**
 *
 * @author imacdonn
 */
public class MLFT8800RC {
    private MLFT8800CB cb;
    private SerialPort serialPort;
    private InputStream is;
    private OutputStream os;
    private Controller controller;
    private DisplayReader displayReader;
    private byte[] ba;
    char[] cMap, fMap;
    private long lastDisplayUpdate = 0;
    private boolean displayTimedOut;
    
    public MLFT8800RC(MLFT8800CB cb, String comPort) {
        this.cb = cb;
        
        try {
            Enumeration portList = CommPortIdentifier.getPortIdentifiers();
            CommPortIdentifier portIdentifier = null;

            while (portList.hasMoreElements()) {
                CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();

                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    if (portId.getName().equals(comPort)) {
                        portIdentifier = portId;
                    }
                }
            }
            
            if (portIdentifier == null) {
                MLlogUtils.errMsg("Error: Failed to find serial port named [" + comPort + "]");
                return;
            }
            
            if (portIdentifier.isCurrentlyOwned()) {
                MLlogUtils.errMsg("Error: " + comPort + " is currently in use");
            } else {
                CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

                if ( commPort instanceof SerialPort ) {
                    serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(19200,
                                                   SerialPort.DATABITS_8,
                                                   SerialPort.STOPBITS_1,
                                                   SerialPort.PARITY_NONE);
                    is = serialPort.getInputStream();
                    os = serialPort.getOutputStream();
                }
            }
        } catch (Exception e) {
            MLlogUtils.errMsg("Problem opening port " + comPort);
            e.printStackTrace();
        }
        
        cMap = new char[128];
        for (int i=0; i<128; i++) cMap[i] = '?';
        cMap[0] = ' ';
        cMap[63] = '0';
        cMap[6] = '1';
        cMap[91] = '2';
        cMap[79] = '3';
        cMap[102] = '4';
        cMap[109] = '5';
        cMap[125] = '6';
        cMap[7] = '7';
        cMap[127] = '8';
        cMap[111] = '9';
        
        cMap[124] = 'b';
        cMap[88] = 'c';
        cMap[116] = 'h';
        cMap[4] = 'i';
        cMap[84] = 'n';
        cMap[120] = 't';
        cMap[28] = 'u';
        
        cMap[118] = 'H';
        cMap[56] = 'L';
        cMap[115] = 'P';
        cMap[62] = 'U';
        
        fMap = new char[8192];
        for (int i=0; i<8192; i++) fMap[i] = '?';
        fMap[0] = ' ';
        fMap[6199] = 'A';
        fMap[791] = 'B';
        fMap[6416] = 'C';
        fMap[789] = 'D';
        fMap[6450] = 'E';
        fMap[6194] = 'F';
        fMap[6419] = 'G';
        fMap[6183] = 'H';
        fMap[784] = 'I';
        fMap[2309] = 'J';
        fMap[6312] = 'K';
        fMap[6400] = 'L';
        fMap[6221] = 'M';
        fMap[6341] = 'N';
        fMap[6421] = 'O';
        fMap[6198] = 'P';
        fMap[6549] = 'Q';
        fMap[6326] = 'R';
        fMap[4528] = 'S';
        fMap[528] = 'T';
        fMap[6405] = 'U';
        fMap[7176] = 'V';
        fMap[7301] = 'W';
        fMap[1224] = 'X';
        fMap[4391] = 'Y';
        fMap[1304] = 'Z';
        
        fMap[7453] = '0';
        fMap[5] = '1';
        fMap[2358] = '2';
        fMap[311] = '3';
        fMap[4135] = '4';
        fMap[4403] = '5';
        fMap[6451] = '6';
        fMap[21] = '7';
        fMap[6455] = '8';
        fMap[4407] = '9';
        
        fMap[32] = '-';
        fMap[34] = '-';
        fMap[546] = '+';
        fMap[1032] = '/';
        
        this.ba = new byte[] {
            (byte)0x80,         // 0 left enc count - MSB must be 1 for sync
            (byte)0x00,         // 1 right enc count
            (byte)0x7F,         // 2 ptt 7F=off, 1B=on
            (byte)0x7F,         // 3 right sql 7F=open
            (byte)0x30,         // 4 right vol 7F=max
            (byte)0x7F,         // 5 mic button maxtric (rows)
            (byte)0x7F,         // 6 left sql
            (byte)0x30,         // 7 left vol
            (byte)0x7F,         // 8 mic button maxtric (cols)
            (byte)0x7F,         // 9 right buttons
            (byte)0x7F,         // 10 left buttons
            (byte)0x00,         // 11 knob buttons + middle button
            (byte)0x00,         // 12 hypermem key            
        };
                    
        this.controller = new Controller();
        this.displayReader = new DisplayReader();
    }
    
    public void start() {
//        MLlogUtils.dbgMsg("rc start");
        this.controller.alive = true;
        this.controller.start();
        this.displayReader.alive = true;
        this.displayReader.start();
    }
    
    public void shutdown() {
        controller.alive = false;
        displayReader.alive = false;
//        MLlogUtils.dbgMsg("rc shutdown");
        try {
            do {
                Thread.sleep(500);
//                if (!(controller.dead))
//                    MLlogUtils.dbgMsg("controller says: I'm not dead yet!");
//                if (!displayReader.dead)
//                    MLlogUtils.dbgMsg("displayReader says: I'm not dead yet!");
            } while (!(controller.dead));
//            is.close();
//            os.close();
//            serialPort.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void resetButtons() {
        this.ba[5] = 0x7F;
        this.ba[8] = 0x7F;
        this.ba[9] = 0x7F;
        this.ba[10] = 0x7F;
        this.ba[11] = 0x00;
        this.ba[12] = 0x00;
    }
    
    public void kpButton(char button) {
        if (button == '1' || button == '2' || button == '3' || button == 'A') {
            this.ba[5] = (byte)0;            
        } else if (button == '4' || button == '5' || button == '6' || button == 'B') {
            this.ba[5] = (byte)0x1A;
        } else if (button == '7' || button == '8' || button == '9' || button == 'C') {
            this.ba[5] = (byte)0x32;
        } else if (button == '*' || button == '0' || button == '#' || button == 'D') {
            this.ba[5] = (byte)0x4C;
        } else if (button == 'P' || button == 'Q' || button == 'R' || button == 'S') {
            this.ba[5] = (byte)0x64;
        }
        if (button == '1' || button == '4' || button == '7' || button == '*' || button == 'P') {
            this.ba[8] = (byte)0x1B;            
        } else if (button == '2' || button == '5' || button == '8' || button == '0' || button == 'Q') {
            this.ba[8] = (byte)0x33;
        } else if (button == '3' || button == '6' || button == '9' || button == '#' || button == 'R') {
            this.ba[8] = (byte)0x4C;
        } else if (button == 'A' || button == 'B' || button == 'C' || button == 'D' || button == 'S') {
            this.ba[8] = (byte)0x67;
        }
    }
    
    public void pushButton(String what) {
        if ("leftScn".equals(what)) {
            this.ba[9] = (byte)0x00;
        } else if ("leftHm".equals(what)) {
            this.ba[9] = (byte)0x20;
        } else if ("leftVm".equals(what)) {
            this.ba[9] = (byte)0x40;
        } else if ("leftLow".equals(what)) {
            this.ba[9] = (byte)0x60;
            
        } else if ("rightLow".equals(what)) {
            this.ba[10] = (byte)0x00;
        } else if ("rightVm".equals(what)) {
            this.ba[10] = (byte)0x20;
        } else if ("rightHm".equals(what)) {
            this.ba[10] = (byte)0x40;
        } else if ("rightScn".equals(what)) {
            this.ba[10] = (byte)0x60;
        
        } else if ("rightEnc".equals(what)) {
            this.ba[11] = (byte)0x01;
        } else if ("leftEnc".equals(what)) {
            this.ba[11] = (byte)0x02;
        } else if ("set".equals(what)) {
            this.ba[11] = (byte)0x04;
        } else if ("leftVol".equals(what)) {
            this.ba[11] = (byte)0x08;
        }
    }
    
    public void hyperButton(int n) {
        this.ba[12] |= (1<<(n-1));
    }
    
    public void pttOn() {
        this.ba[2] = 0x1B;
    }
    
    public void pttOff() {
        this.ba[2] = 0x7F;
    }
    
    public void setLeftVol(int newVal) {
        this.ba[6] = (byte)(newVal);
    }
    
    public void setLeftSql(int newVal) {
        this.ba[7] = (byte)(127 - newVal);
    }
    
    public void setRightVol(int newVal) {
        this.ba[4] = (byte)(newVal);
    }
    
    public void setRightSql(int newVal) {
        this.ba[3] = (byte)(127 - newVal);
    }
    
    public void leftEncoderCW() {
        ba[0] = (byte)0x81;
    }
    
    public void leftEncoderCCW() {
        // First bit is sync for packet. Remainder is two-compliment of -1
        ba[0] = (byte)0xFF;
    }
    
    public void rightEncoderCW() {
        ba[1] = (byte)0x01;
    }
    
    public void rightEncoderCCW() {
        ba[1] = (byte)0x7F;
    }
    
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    private char cLookup(int b0, int b1, int b2, int b3,
                         int b4, int b5, int b6) {
        int idx = (b0 & 0x1);
        idx |= (b1 & 0x1) << 1;
        idx |= (b2 & 0x1) << 2;
        idx |= (b3 & 0x1) << 3;
        idx |= (b4 & 0x1) << 4;
        idx |= (b5 & 0x1) << 5;
        idx |= (b6 & 0x1) << 6;
        return(cMap[idx]);
    }
    
//    private char[] fBitmap(int a, int b, int c, int d, int e, int f, int g, int h,
//                         int i, int j, int k, int l, int m) {
//        char[] x = new char[13];
//        x[12] = (a & 0x1)>0 ? '1' : '0';
//        x[11] = (b & 0x1)>0 ? '1' : '0';
//        x[10] = (c & 0x1)>0 ? '1' : '0';
//        x[9] = (d & 0x1)>0 ? '1' : '0';
//        x[8] = (e & 0x1)>0 ? '1' : '0';
//        x[7] = (f & 0x1)>0 ? '1' : '0';
//        x[6] = (g & 0x1)>0 ? '1' : '0';
//        x[5] = (h & 0x1)>0 ? '1' : '0';
//        x[4] = (i & 0x1)>0 ? '1' : '0';
//        x[3] = (j & 0x1)>0 ? '1' : '0';
//        x[2] = (k & 0x1)>0 ? '1' : '0';
//        x[1] = (l & 0x1)>0 ? '1' : '0';
//        x[0] = (m & 0x1)>0 ? '1' : '0';
//        MLlogUtils.dbgMsg("idx " + String.valueOf(x));
//        return x;
//    }
    
    private char fLookup(int a, int b, int c, int d, int e, int f, int g, int h,
                         int i, int j, int k, int l, int m) {
        int idx = (a & 0x1);
        idx |= (b & 0x1) << 1;
        idx |= (c & 0x1) << 2;
        idx |= (d & 0x1) << 3;
        idx |= (e & 0x1) << 4;
        idx |= (f & 0x1) << 5;
        idx |= (g & 0x1) << 6;
        idx |= (h & 0x1) << 7;
        idx |= (i & 0x1) << 8;
        idx |= (j & 0x1) << 9;
        idx |= (k & 0x1) << 10;
        idx |= (l & 0x1) << 11;
        idx |= (m & 0x1) << 12;
        return(fMap[idx]);
    }
    
//    private String prevHex = "";
    private void processDisplayPacket(byte[] packet) {
//        String newHex = bytesToHex(packet);
//        if (!newHex.equals(prevHex)) {
//            MLlogUtils.dbgMsg(newHex);
//            prevHex = newHex;
//        }
        
        char cL0 = cLookup(
                packet[37]>>6,
                packet[37]>>4,
                packet[37]>>3,
                packet[38]>>2,
                packet[38]>>0,
                packet[38]>>1,
                packet[37]>>5
        );
        
        char cL1 = cLookup(
                packet[39]>>4,
                packet[38]>>5,
                packet[38]>>4,
                packet[40]>>0,
                packet[39]>>5,
                packet[39]>>6,
                packet[39]>>3
        ); 
                   
        char cL2 = cLookup(
                packet[40]>>5,
                packet[40]>>3,
                packet[40]>>2,
                packet[38]>>3,
                packet[40]>>6,
                packet[41]>>0,
                packet[40]>>4
        );
        
        boolean cLDash = ((packet[40] & 1<<1) != 0);

        char cR0 = cLookup(
                packet[33]>>4,
                packet[33]>>2,
                packet[33]>>1,
                packet[34]>>0,
                packet[33]>>5,
                packet[33]>>6,
                packet[33]>>3
        );
        
        char cR1 = cLookup(
                packet[34]>>5,
                packet[34]>>3,
                packet[34]>>2,
                packet[35]>>1,
                packet[34]>>6,
                packet[35]>>0,
                packet[34]>>4
        );
        
        char cR2 = cLookup(
                packet[35]>>6,
                packet[35]>>4,
                packet[35]>>3,
                packet[34]>>1,
                packet[36]>>0,
                packet[36]>>1,
                packet[35]>>5
        );
        
        
        boolean cRDash = ((packet[35] & 1<<2) != 0);
       
        char fL0 = fLookup(
                packet[0]>>5,
                packet[0]>>6,
                packet[1]>>0,
                packet[1]>>1,
                packet[1]>>2,
                packet[1]>>3,
                packet[1]>>4,
                packet[1]>>6, 
                packet[2]>>0,
                packet[2]>>1,
                packet[2]>>3,
                packet[2]>>4,
                packet[2]>>5
        );
        
        char fL1 = fLookup(
                packet[3]>>0,
                packet[3]>>1,
                packet[3]>>2,
                packet[3]>>3,
                packet[3]>>4,
                packet[3]>>5,
                packet[3]>>6,
                packet[4]>>1, 
                packet[4]>>2,
                packet[4]>>3,
                packet[4]>>5,
                packet[4]>>6,
                packet[5]>>0
        );
        
        char fL2 = fLookup(
                packet[5]>>2,
                packet[5]>>3,
                packet[5]>>4,
                packet[5]>>5,
                packet[5]>>6,
                packet[6]>>0,
                packet[6]>>1,
                packet[6]>>3, 
                packet[6]>>4,
                packet[6]>>5,
                packet[7]>>0,
                packet[7]>>1,
                packet[7]>>2
        );
        
        char fL3 = fLookup(
                packet[8]>>3,
                packet[8]>>4,
                packet[8]>>5,
                packet[8]>>6,
                packet[9]>>0,
                packet[9]>>1,
                packet[9]>>2,
                packet[9]>>4, 
                packet[9]>>5,
                packet[9]>>6,
                packet[10]>>1,
                packet[10]>>2,
                packet[10]>>3
        );
        
        char fL4 = fLookup(
                packet[10]>>5,
                packet[10]>>6,
                packet[11]>>0,
                packet[11]>>1,
                packet[11]>>2,
                packet[11]>>3,
                packet[11]>>4,
                packet[11]>>6, 
                packet[12]>>0,
                packet[12]>>1,
                packet[12]>>3,
                packet[12]>>4,
                packet[12]>>5
        );
        
        char fL5 = fLookup(
                packet[13]>>0,
                packet[13]>>1,
                packet[13]>>2,
                packet[13]>>3,
                packet[13]>>4,
                packet[13]>>5,
                packet[13]>>6,
                packet[14]>>1, 
                packet[14]>>2,
                packet[14]>>3,
                packet[14]>>5,
                packet[14]>>6,
                packet[15]>>0
        );
        
        boolean fLDp = ((packet[8] & 1<<0) > 0);
        boolean fLHalf = ((packet[0] & 1<<3) > 0);
        
        char fR0 = fLookup(
                packet[16]>>5,
                packet[16]>>6,
                packet[17]>>0,
                packet[17]>>1,
                packet[17]>>2,
                packet[17]>>3,
                packet[17]>>4,
                packet[17]>>6, 
                packet[18]>>0,
                packet[18]>>1,
                packet[18]>>3,
                packet[18]>>4,
                packet[18]>>5
        );
        
        char fR1 = fLookup(
                packet[19]>>0,
                packet[19]>>1,
                packet[19]>>2,
                packet[19]>>3,
                packet[19]>>4,
                packet[19]>>5,
                packet[19]>>6,
                packet[20]>>1, 
                packet[20]>>2,
                packet[20]>>3,
                packet[20]>>5,
                packet[20]>>6,
                packet[21]>>0
        );
        
        char fR2 = fLookup(
                packet[21]>>2,
                packet[21]>>3,
                packet[21]>>4,
                packet[21]>>5,
                packet[21]>>6,
                packet[22]>>0,
                packet[22]>>1,
                packet[22]>>3, 
                packet[22]>>4,
                packet[22]>>5,
                packet[23]>>0,
                packet[23]>>1,
                packet[23]>>2
        );
        
        char fR3 = fLookup(
                packet[24]>>3,
                packet[24]>>4,
                packet[24]>>5,
                packet[24]>>6,
                packet[25]>>0,
                packet[25]>>1,
                packet[25]>>2,
                packet[25]>>4, 
                packet[25]>>5,
                packet[25]>>6,
                packet[26]>>1,
                packet[26]>>2,
                packet[26]>>3
        );
        
        char fR4 = fLookup(
                packet[26]>>5,
                packet[26]>>6,
                packet[27]>>0,
                packet[27]>>1,
                packet[27]>>2,
                packet[27]>>3,
                packet[27]>>4,
                packet[27]>>6, 
                packet[28]>>0,
                packet[28]>>1,
                packet[28]>>3,
                packet[28]>>4,
                packet[28]>>5
        );
        
        char fR5 = fLookup(
                packet[29]>>0,
                packet[29]>>1,
                packet[29]>>2,
                packet[29]>>3,
                packet[29]>>4,
                packet[29]>>5,
                packet[29]>>6,
                packet[30]>>1, 
                packet[30]>>2,
                packet[30]>>3,
                packet[30]>>5,
                packet[30]>>6,
                packet[31]>>0
        );
        
        boolean fRDp = ((packet[24] & 1<<0) > 0);
        boolean fRHalf = ((packet[16] & 1<<3) > 0);
               
        boolean apo = ((packet[0] & 1<<1) > 0);
        boolean set = ((packet[30] & 1<<0) > 0);
        boolean lock = ((packet[30] & 1<<4) > 0);
        boolean key2 = ((packet[28] & 1<<6) > 0);
        
        boolean leftLow = ((packet[1] & 1<<5) > 0);
        boolean rightLow = ((packet[17] & 1<<5) > 0);
        
        boolean leftMid = ((packet[0] & 1<<2) > 0);
        boolean rightMid = ((packet[16] & 1<<2) > 0);
        
        boolean left9600 = ((packet[4] & 1<<0) > 0);
        boolean right9600 = ((packet[20] & 1<<0) > 0);
        
        boolean leftDcs = ((packet[9] & 1<<3) > 0);
        boolean rightDcs = ((packet[22] & 1<<2) > 0);
        
        boolean leftMute = ((packet[11] & 1<<5) > 0);
        boolean rightMute = ((packet[25] & 1<<3) > 0);
        
        boolean leftBusy = ((packet[12] & 1<<2) > 0);
        boolean rightBusy = ((packet[28] & 1<<2) > 0);
        
        boolean leftMt = ((packet[12] & 1<<6) > 0);
        boolean rightMt = ((packet[27] & 1<<5) > 0);

        boolean leftPlus = ((packet[36] & 1<<4) > 0);
        boolean rightPlus = ((packet[32] & 1<<2) > 0);
        
        boolean leftMinus = ((packet[36] & 1<<5) > 0);
        boolean rightMinus = ((packet[32] & 1<<3) > 0);
        
        boolean leftEnc = ((packet[36] & 1<<6) > 0);
        boolean rightEnc = ((packet[32] & 1<<4) > 0);
        
        boolean leftDec = ((packet[37] & 1<<0) > 0);
        boolean rightDec = ((packet[32] & 1<<5) > 0);
        
        boolean leftPms = ((packet[37] & 1<<1) > 0);
        boolean rightPms = ((packet[32] & 1<<6) > 0);
        
        boolean leftSkip = ((packet[37] & 1<<2) > 0);
        boolean rightSkip = ((packet[33] & 1<<0) > 0);
        
        boolean leftTx = ((packet[36] & 1<<3) > 0);
        boolean rightTx = ((packet[32] & 1<<1) > 0);
      
        boolean leftMain = ((packet[36] & 1<<2) != 0);
        boolean rightMain = ((packet[32] & 1<<0) != 0);
        
        boolean leftMeter9 = ((packet[0] & 1<<4) != 0);
        boolean leftMeter8 = ((packet[2] & 1<<2) != 0);
        boolean leftMeter7 = ((packet[2] & 1<<6) != 0);
        boolean leftMeter6 = ((packet[4] & 1<<4) != 0);
        boolean leftMeter5 = ((packet[5] & 1<<1) != 0);
        boolean leftMeter4 = ((packet[6] & 1<<6) != 0);
        boolean leftMeter3 = ((packet[8] & 1<<2) != 0);
        boolean leftMeter2 = ((packet[10] & 1<<0) != 0);
        boolean leftMeter1 = ((packet[10] & 1<<4) != 0);
        
        boolean rightMeter9 = ((packet[16] & 1<<4) != 0);
        boolean rightMeter8 = ((packet[18] & 1<<2) != 0);
        boolean rightMeter7 = ((packet[18] & 1<<6) != 0);
        boolean rightMeter6 = ((packet[20] & 1<<4) != 0);
        boolean rightMeter5 = ((packet[21] & 1<<1) != 0);
        boolean rightMeter4 = ((packet[22] & 1<<6) != 0);
        boolean rightMeter3 = ((packet[24] & 1<<2) != 0);
        boolean rightMeter2 = ((packet[26] & 1<<0) != 0);
        boolean rightMeter1 = ((packet[26] & 1<<4) != 0);
        
        cb.updateDisplay(cL0, cL1, cL2, cLDash,
                cR0, cR1, cR2, cRDash,
                fL0, fL1, fL2, fL3, fL4, fL5, fLDp, fLHalf,
                fR0, fR1, fR2, fR3, fR4, fR5, fRDp, fRHalf,
                leftMain, rightMain,
                leftTx, rightTx,
                leftSkip, rightSkip,
                leftPms, rightPms,
                leftEnc, rightEnc,
                leftDec, rightDec,
                leftPlus, rightPlus,
                leftMinus, rightMinus,
                leftMt, rightMt,
                leftBusy, rightBusy,
                leftMute, rightMute,
                leftDcs, rightDcs,
                left9600, right9600,
                leftMid, rightMid,
                leftLow, rightLow,
                leftMeter1, leftMeter2, leftMeter3,
                leftMeter4, leftMeter5, leftMeter6,
                leftMeter7, leftMeter8, leftMeter9,
                rightMeter1, rightMeter2, rightMeter3,
                rightMeter4, rightMeter5, rightMeter6,
                rightMeter7, rightMeter8, rightMeter9,
                set, lock, apo, key2
        );
                
        lastDisplayUpdate = System.currentTimeMillis();
        displayTimedOut = false;
    }
    
    private class Controller extends Thread {
        private boolean alive = true, dead = false;
        public int count_to_reset = -1;
        
        @Override
        public void run() {
            while (this.alive) {
                try {
                    Thread.sleep(20);
                    os.write(ba);
                    // reset encoder counts
                    ba[0] = (byte)0x80;
                    ba[1] = (byte)0x0;
                    if (count_to_reset == 0) {
                        resetButtons();
                        count_to_reset = -1;
                    } else if (count_to_reset > 0)
                        count_to_reset--;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if ((System.currentTimeMillis() - lastDisplayUpdate) > 1000 &&
                        displayTimedOut == false) {
//                    MLlogUtils.dbgMsg("Display timeout");
                    cb.displayTimeout();
                    displayTimedOut = true;
                }
            }
            dead = true;
        }
    }
    
    private class DisplayReader extends Thread {
        private boolean alive = true, dead=false;
        
        public void run() {
            byte[] buf = new byte[42];
            int b = 0;
            int idx = 0; 
            while (this.alive) {
                try {
                    do {
                        b = is.read();
//                        MLlogUtils.dbgMsg("Got " + b);
                    } while (b < 128);
                    buf[idx++] = (byte)b;
                    do {
                        idx += is.read(buf, idx, 42-idx);
                    } while (idx < 42);
//                    MLlogUtils.dbgMsg("packet");
                    processDisplayPacket(copyOf(buf, 42));
                    idx = 0;
                } catch (Exception e) {
                    if (alive)
                        e.printStackTrace();
                }
            }
            dead = true;
        }
    }
}
