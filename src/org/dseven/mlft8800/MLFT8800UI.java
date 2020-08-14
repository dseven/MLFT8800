/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dseven.mlft8800;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import org.dseven.mlutils.MLlogUtils;

/**
 *
 * @author imacdonn
 */
public class MLFT8800UI extends javax.swing.JFrame
                        implements MLFT8800CB {
    
    MLFT8800RC rc;
    Properties appProps;
    String subclassName, comPort;

    /**
     * Creates new form MLFT8800UI
     */
    public MLFT8800UI() {
        initComponents();
        keyPadJFrame.pack();
        subclassName = this.getClass().getSimpleName();
        loadProps();
        rc = new MLFT8800RC(this, comPort);
        rc.start();
        setLeftVol();
        setLeftSql();
        setRightVol();
        setRightSql();
    }
    
    private void shutdown() {   
        poweroff();
        storeProps();
    }

    private void poweroff() {
        if (rc!=null) {
            rc.shutdown();
        }
    }
       
    private void storeProps() {
        try {
            appProps.setProperty("comport", comPort);
            
            Point mainPos = getLocation();
            appProps.setProperty("mainwindowposition.x", ""+mainPos.x);
            appProps.setProperty("mainwindowposition.y", ""+mainPos.y);
            
            Point kpPos = keyPadJFrame.getLocation();
            appProps.setProperty("keypadwindowposition.x", ""+kpPos.x);
            appProps.setProperty("keypadwindowposition.y", ""+kpPos.y);
            
            appProps.setProperty("keypadwindowvisible", keyPadJFrame.isVisible()?"true":"false");
            
            appProps.setProperty("leftvolume", ""+leftVolSlider.getValue());
            appProps.setProperty("leftsquelch", ""+leftSqlSlider.getValue());
            
            appProps.setProperty("rightvolume", ""+rightVolSlider.getValue());
            appProps.setProperty("rightsquelch", ""+rightSqlSlider.getValue());
            
            appProps.setProperty("leftmuted", leftMuteToggleButton.isSelected()?"true":"false");
            appProps.setProperty("rightmuted", rightMuteToggleButton.isSelected()?"true":"false");
            
            FileOutputStream out = new FileOutputStream(subclassName+".properties");
            appProps.store(out, subclassName+" properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadProps() {
        try {
            appProps = new Properties();
            FileInputStream in = new FileInputStream(subclassName+".properties");
            appProps.load(in);
            in.close();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        comPort = appProps.getProperty("comport", "COM1");
        
        int mainposx = Integer.parseInt(appProps.getProperty("mainwindowposition.x", "0"));
        int mainposy = Integer.parseInt(appProps.getProperty("mainwindowposition.y", "0"));
        
        int kpposx = Integer.parseInt(appProps.getProperty("keypadwindowposition.x", "0"));
        int kpposy = Integer.parseInt(appProps.getProperty("keypadwindowposition.y", "0"));
        
        int leftVol = Integer.parseInt(appProps.getProperty("leftvolume", "256"));
        if (leftVol<128) leftVolSlider.setValue(leftVol);
        int leftSql = Integer.parseInt(appProps.getProperty("leftsquelch", "256"));
        if (leftSql<128) leftSqlSlider.setValue(leftSql);
        
        leftMuteToggleButton.setSelected(appProps.getProperty("leftmuted", "false").equals("true"));
        rightMuteToggleButton.setSelected(appProps.getProperty("rightmuted", "false").equals("true"));
        
        int rightVol = Integer.parseInt(appProps.getProperty("rightvolume", "256"));
        if (rightVol<128) rightVolSlider.setValue(rightVol);
        int rightSql = Integer.parseInt(appProps.getProperty("rightsquelch", "256"));
        if (rightSql<128) rightSqlSlider.setValue(rightSql);
        
        boolean keyPadVisible = appProps.getProperty("keypadwindowvisible", "false").equals("true");
        
        setLocationRelativeTo(null);
        setLocation(mainposx, mainposy);
        
        keyPadJFrame.setLocationRelativeTo(null);
        keyPadJFrame.setLocation(kpposx, kpposy);
        keyPadJFrame.setVisible(keyPadVisible);
    }
    
    private void setLeftVol() {                                           
        if (rc==null) return;
        rc.setLeftVol(leftMuteToggleButton.isSelected()?0:leftVolSlider.getValue());
    }                                          

    private void setLeftSql() {                                           
        if (rc==null) return;
        rc.setLeftSql(leftSqlSlider.getValue());
    }                                          

    private void setRightVol() {                                            
        if (rc==null) return;
        rc.setRightVol(rightMuteToggleButton.isSelected()?0:rightVolSlider.getValue());
    }                                           

    private void setRightSql() {                                            
        if (rc==null) return;
        rc.setRightSql(rightSqlSlider.getValue());
    } 
    
    @Override
    public void updateDisplay(
            char cL0, char cL1, char cL2, boolean cLDash,
            char cR0, char cR1, char cR2, boolean cRDash,
            char fL0, char fL1, char fL2, char fL3, char fL4, char fL5, boolean fLDp, boolean fLHalf,
            char fR0, char fR1, char fR2, char fR3, char fR4, char fR5, boolean fRDp, boolean fRHalf,
            boolean leftMain, boolean rightMain,
            boolean leftTx, boolean rightTx,
            boolean leftSkip, boolean rightSkip,
            boolean leftPms, boolean rightPms,
            boolean leftEnc, boolean rightEnc,
            boolean leftDec, boolean rightDec,
            boolean leftPlus, boolean rightPlus,
            boolean leftMinus, boolean rightMinus,
            boolean leftMt, boolean rightMt,
            boolean leftBusy, boolean rightBusy,
            boolean leftMute, boolean rightMute,
            boolean leftDcs, boolean rightDcs,
            boolean left9600, boolean right9600,
            boolean leftMid, boolean rightMid,
            boolean leftLow, boolean rightLow,
            boolean leftMeter1, boolean leftMeter2, boolean leftMeter3,
            boolean leftMeter4, boolean leftMeter5, boolean leftMeter6,
            boolean leftMeter7, boolean leftMeter8, boolean leftMeter9,
            boolean rightMeter1, boolean rightMeter2, boolean rightMeter3,
            boolean rightMeter4, boolean rightMeter5, boolean rightMeter6,
            boolean rightMeter7, boolean rightMeter8, boolean rightMeter9,
            boolean set, boolean lock, boolean apo, boolean key2
    ) {
        cL0Label.setText(String.valueOf(cL0));
        cL1Label.setText(String.valueOf(cL1));
        cL2Label.setText(String.valueOf(cL2));
        cLDashLabel.setText(cLDash?"-":" ");
        
        cR0Label.setText(String.valueOf(cR0));
        cR1Label.setText(String.valueOf(cR1));
        cR2Label.setText(String.valueOf(cR2));
        cRDashLabel.setText(cRDash?"-":" ");
        
        fL0Label.setText(String.valueOf(fL0));
        fL1Label.setText(String.valueOf(fL1));
        fL2Label.setText(String.valueOf(fL2));
        fL3Label.setText(String.valueOf(fL3));
        fL4Label.setText(String.valueOf(fL4));
        fL5Label.setText(String.valueOf(fL5));
        fLDpLabel.setText(fLDp?".":" ");
        fLHalfDotLabel.setText(fLHalf?".":" ");
        fLHalf5Label.setText(fLHalf?"5":" ");
        
        fR0Label.setText(String.valueOf(fR0));
        fR1Label.setText(String.valueOf(fR1));
        fR2Label.setText(String.valueOf(fR2));
        fR3Label.setText(String.valueOf(fR3));
        fR4Label.setText(String.valueOf(fR4));
        fR5Label.setText(String.valueOf(fR5));
        fRDpLabel.setText(fRDp?".":" ");
        fRHalfDotLabel.setText(fRHalf?".":" ");
        fRHalf5Label.setText(fRHalf?"5":" ");
        
        leftBusyLabel.setVisible(leftBusy);
        rightBusyLabel.setVisible(rightBusy);
        leftMainLabel.setVisible(leftMain);
        rightMainLabel.setVisible(rightMain);
        leftTxLabel.setVisible(leftTx);
        rightTxLabel.setVisible(rightTx);
        leftSkipLabel.setVisible(leftSkip);
        rightSkipLabel.setVisible(rightSkip);
        leftMainLabel.setVisible(leftMain);
        rightMainLabel.setVisible(rightMain);
        leftPmsLabel.setVisible(leftPms);
        rightPmsLabel.setVisible(rightPms);
        leftEncLabel.setVisible(leftEnc);
        rightEncLabel.setVisible(rightEnc);
        leftDecLabel.setVisible(leftDec);
        rightDecLabel.setVisible(rightDec);
        leftPlusLabel.setVisible(leftPlus);
        rightPlusLabel.setVisible(rightPlus);
        leftMinusLabel.setVisible(leftMinus);
        rightMinusLabel.setVisible(rightMinus);
        leftMeter1Label.setVisible(leftMeter1);
        leftMeter2Label.setVisible(leftMeter2);
        leftMeter3Label.setVisible(leftMeter3);
        leftMeter4Label.setVisible(leftMeter4);
        leftMeter5Label.setVisible(leftMeter5);
        leftMeter6Label.setVisible(leftMeter6);
        leftMeter7Label.setVisible(leftMeter7);
        leftMeter8Label.setVisible(leftMeter8);
        leftMeter9Label.setVisible(leftMeter9);
        rightMeter1Label.setVisible(rightMeter1);
        rightMeter2Label.setVisible(rightMeter2);
        rightMeter3Label.setVisible(rightMeter1);
        rightMeter4Label.setVisible(rightMeter4);
        rightMeter5Label.setVisible(rightMeter5);
        rightMeter6Label.setVisible(rightMeter6);
        rightMeter7Label.setVisible(rightMeter7);
        rightMeter8Label.setVisible(rightMeter8);
        rightMeter9Label.setVisible(rightMeter9);
        leftMtLabel.setVisible(leftMt);
        rightMtLabel.setVisible(rightMt);
        leftMuteLabel.setVisible(leftMute);
        rightMuteLabel.setVisible(rightMute);
        leftDcsLabel.setVisible(leftDcs);
        rightDcsLabel.setVisible(rightDcs);
        left9600Label.setVisible(left9600);
        right9600Label.setVisible(right9600);
        leftLowLabel.setVisible(leftLow);
        rightLowLabel.setVisible(rightLow);
        leftMidLabel.setVisible(leftMid);
        rightMidLabel.setVisible(rightMid);
        apoLabel.setVisible(apo);
        lockLabel.setVisible(lock);
        setLabel.setVisible(set);
        key2Label.setVisible(key2);
        
        if (key2) {
            rightLowButton.setText("MHz");
            rightVMButton.setText("REV");
            rightHmButton.setText("TON");
            rightScnButton.setText("SUB");
        } else {
            rightLowButton.setText("LOW");
            rightVMButton.setText("V/M");
            rightHmButton.setText("HM");
            rightScnButton.setText("SCN");
        }
    }
    
    @Override
    public void displayTimeout() {
        updateDisplay(
                '*', '*', '*', true, '*', '*', '*', true,
                '*', '*', '*', '*', '*', '*', true, true,
                '*', '*', '*', '*', '*', '*', true, true,
                true, true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true, true, true,
                true, true
        );
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        keyPadJFrame = new javax.swing.JFrame();
        keyPadPanel = new javax.swing.JPanel();
        kp1Button = new javax.swing.JButton();
        kp2Button = new javax.swing.JButton();
        kp3Button = new javax.swing.JButton();
        kpAButton = new javax.swing.JButton();
        kp4Button = new javax.swing.JButton();
        kp5Button = new javax.swing.JButton();
        kp6Button = new javax.swing.JButton();
        kpBButton = new javax.swing.JButton();
        kp7Button = new javax.swing.JButton();
        kp8Button = new javax.swing.JButton();
        kp9Button = new javax.swing.JButton();
        kpCButton = new javax.swing.JButton();
        kpstarButton = new javax.swing.JButton();
        kp0Button = new javax.swing.JButton();
        kppoundButton = new javax.swing.JButton();
        kpDButton = new javax.swing.JButton();
        kpP1Button = new javax.swing.JButton();
        kpP2Button = new javax.swing.JButton();
        kpP3Button = new javax.swing.JButton();
        kpP4Button = new javax.swing.JButton();
        displayPanel = new javax.swing.JPanel();
        leftChannelPanel = new javax.swing.JPanel();
        cL2Label = new javax.swing.JLabel();
        cLDashLabel = new javax.swing.JLabel();
        cL1Label = new javax.swing.JLabel();
        cL0Label = new javax.swing.JLabel();
        leftUpperIconPanel = new javax.swing.JPanel();
        leftPmsLabel = new javax.swing.JLabel();
        leftSkipLabel = new javax.swing.JLabel();
        leftMinusLabel = new javax.swing.JLabel();
        leftPlusLabel = new javax.swing.JLabel();
        leftEncLabel = new javax.swing.JLabel();
        leftDecLabel = new javax.swing.JLabel();
        leftTxLabel = new javax.swing.JLabel();
        leftMainLabel = new javax.swing.JLabel();
        rightChannelPanel = new javax.swing.JPanel();
        cR2Label = new javax.swing.JLabel();
        cRDashLabel = new javax.swing.JLabel();
        cR1Label = new javax.swing.JLabel();
        cR0Label = new javax.swing.JLabel();
        rightUpperIconPanel = new javax.swing.JPanel();
        rightPmsLabel = new javax.swing.JLabel();
        rightSkipLabel = new javax.swing.JLabel();
        rightMinusLabel = new javax.swing.JLabel();
        rightPlusLabel = new javax.swing.JLabel();
        rightEncLabel = new javax.swing.JLabel();
        rightDecLabel = new javax.swing.JLabel();
        rightTxLabel = new javax.swing.JLabel();
        rightMainLabel = new javax.swing.JLabel();
        leftVfoPanel = new javax.swing.JPanel();
        fL5Label = new javax.swing.JLabel();
        fL4Label = new javax.swing.JLabel();
        fL3Label = new javax.swing.JLabel();
        fLDpLabel = new javax.swing.JLabel();
        fL2Label = new javax.swing.JLabel();
        fL1Label = new javax.swing.JLabel();
        fL0Label = new javax.swing.JLabel();
        fLHalfDotLabel = new javax.swing.JLabel();
        fLHalf5Label = new javax.swing.JLabel();
        middleIconsPanel = new javax.swing.JPanel();
        apoLabel = new javax.swing.JLabel();
        lockLabel = new javax.swing.JLabel();
        setLabel = new javax.swing.JLabel();
        key2Label = new javax.swing.JLabel();
        rightVfoPanel = new javax.swing.JPanel();
        fR5Label = new javax.swing.JLabel();
        fR4Label = new javax.swing.JLabel();
        fR3Label = new javax.swing.JLabel();
        fRDpLabel = new javax.swing.JLabel();
        fR2Label = new javax.swing.JLabel();
        fR1Label = new javax.swing.JLabel();
        fR0Label = new javax.swing.JLabel();
        fRHalfDotLabel = new javax.swing.JLabel();
        fRHalf5Label = new javax.swing.JLabel();
        leftBusyLabel = new javax.swing.JLabel();
        leftMeterPanel = new javax.swing.JPanel();
        leftMeter1Label = new javax.swing.JLabel();
        leftMeter2Label = new javax.swing.JLabel();
        leftMeter3Label = new javax.swing.JLabel();
        leftMeter4Label = new javax.swing.JLabel();
        leftMeter5Label = new javax.swing.JLabel();
        leftMeter6Label = new javax.swing.JLabel();
        leftMeter7Label = new javax.swing.JLabel();
        leftMeter8Label = new javax.swing.JLabel();
        leftMeter9Label = new javax.swing.JLabel();
        rightBusyLabel = new javax.swing.JLabel();
        rightMeterPanel = new javax.swing.JPanel();
        rightMeter1Label = new javax.swing.JLabel();
        rightMeter2Label = new javax.swing.JLabel();
        rightMeter3Label = new javax.swing.JLabel();
        rightMeter4Label = new javax.swing.JLabel();
        rightMeter5Label = new javax.swing.JLabel();
        rightMeter6Label = new javax.swing.JLabel();
        rightMeter7Label = new javax.swing.JLabel();
        rightMeter8Label = new javax.swing.JLabel();
        rightMeter9Label = new javax.swing.JLabel();
        leftLowerIconPanel = new javax.swing.JPanel();
        leftMtLabel = new javax.swing.JLabel();
        leftMuteLabel = new javax.swing.JLabel();
        leftDcsLabel = new javax.swing.JLabel();
        left9600Label = new javax.swing.JLabel();
        leftLowLabel = new javax.swing.JLabel();
        leftMidLabel = new javax.swing.JLabel();
        rightLowerIconPanel = new javax.swing.JPanel();
        rightMtLabel = new javax.swing.JLabel();
        rightMuteLabel = new javax.swing.JLabel();
        rightDcsLabel = new javax.swing.JLabel();
        right9600Label = new javax.swing.JLabel();
        rightLowLabel = new javax.swing.JLabel();
        rightMidLabel = new javax.swing.JLabel();
        buttonRowPanel = new javax.swing.JPanel();
        leftButtonRowPanel = new javax.swing.JPanel();
        leftLowButton = new javax.swing.JButton();
        leftVMButton = new javax.swing.JButton();
        leftHmButton = new javax.swing.JButton();
        leftScnButton = new javax.swing.JButton();
        setButton = new javax.swing.JButton();
        rightButtonRowPanel = new javax.swing.JPanel();
        rightLowButton = new javax.swing.JButton();
        rightVMButton = new javax.swing.JButton();
        rightHmButton = new javax.swing.JButton();
        rightScnButton = new javax.swing.JButton();
        leftSideButtonPanel = new javax.swing.JPanel();
        leftMainButton = new javax.swing.JButton();
        hyper1Button = new javax.swing.JButton();
        hyper2Button = new javax.swing.JButton();
        hyper3Button = new javax.swing.JButton();
        rightSideButtonPanel = new javax.swing.JPanel();
        rightMainButton = new javax.swing.JButton();
        hyper4Button = new javax.swing.JButton();
        hyper5Button = new javax.swing.JButton();
        hyper6Button = new javax.swing.JButton();
        sliderPanel = new javax.swing.JPanel();
        leftEncUpButton = new javax.swing.JButton();
        leftEncDownButton = new javax.swing.JButton();
        leftVolSlider = new javax.swing.JSlider();
        leftSqlSlider = new javax.swing.JSlider();
        rightVolSlider = new javax.swing.JSlider();
        rightSqlSlider = new javax.swing.JSlider();
        rightEncUpButton = new javax.swing.JButton();
        rightEncDownButton = new javax.swing.JButton();
        extraButtonPanel = new javax.swing.JPanel();
        leftMuteToggleButton = new javax.swing.JToggleButton();
        pttButton = new javax.swing.JButton();
        wiresButton = new javax.swing.JButton();
        keyPadButton = new javax.swing.JButton();
        rightMuteToggleButton = new javax.swing.JToggleButton();

        keyPadJFrame.setTitle("FT-8800 Key Pad");
        keyPadJFrame.setResizable(false);
        keyPadJFrame.getContentPane().setLayout(new java.awt.GridBagLayout());

        keyPadPanel.setLayout(new java.awt.GridBagLayout());

        kp1Button.setText("1");
        kp1Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kp1ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kp1ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kp1Button, gridBagConstraints);

        kp2Button.setText("2");
        kp2Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kp2ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kp2ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kp2Button, gridBagConstraints);

        kp3Button.setText("3");
        kp3Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kp3ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kp3ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kp3Button, gridBagConstraints);

        kpAButton.setText("A");
        kpAButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kpAButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kpAButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kpAButton, gridBagConstraints);

        kp4Button.setText("4");
        kp4Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kp4ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kp4ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kp4Button, gridBagConstraints);

        kp5Button.setText("5");
        kp5Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kp5ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kp5ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kp5Button, gridBagConstraints);

        kp6Button.setText("6");
        kp6Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kp6ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kp6ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kp6Button, gridBagConstraints);

        kpBButton.setText("B");
        kpBButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kpBButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kpBButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kpBButton, gridBagConstraints);

        kp7Button.setText("7");
        kp7Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kp7ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kp7ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kp7Button, gridBagConstraints);

        kp8Button.setText("8");
        kp8Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kp8ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kp8ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kp8Button, gridBagConstraints);

        kp9Button.setText("9");
        kp9Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kp9ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kp9ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kp9Button, gridBagConstraints);

        kpCButton.setText("C");
        kpCButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kpCButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kpCButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kpCButton, gridBagConstraints);

        kpstarButton.setText("*");
        kpstarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kpstarButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kpstarButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kpstarButton, gridBagConstraints);

        kp0Button.setText("0");
        kp0Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kp0ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kp0ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kp0Button, gridBagConstraints);

        kppoundButton.setText("#");
        kppoundButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kppoundButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kppoundButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kppoundButton, gridBagConstraints);

        kpDButton.setText("D");
        kpDButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kpDButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kpDButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kpDButton, gridBagConstraints);

        kpP1Button.setText("P1");
        kpP1Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kpP1ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kpP1ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kpP1Button, gridBagConstraints);

        kpP2Button.setText("P2");
        kpP2Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kpP2ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kpP2ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kpP2Button, gridBagConstraints);

        kpP3Button.setText("P3");
        kpP3Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kpP3ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kpP3ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kpP3Button, gridBagConstraints);

        kpP4Button.setText("P4");
        kpP4Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kpP4ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                kpP4ButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadPanel.add(kpP4Button, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        keyPadJFrame.getContentPane().add(keyPadPanel, gridBagConstraints);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FT-8800R");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        displayPanel.setBackground(new java.awt.Color(204, 153, 0));
        displayPanel.setLayout(new java.awt.GridBagLayout());

        leftChannelPanel.setBackground(new java.awt.Color(204, 153, 0));
        leftChannelPanel.setLayout(new java.awt.GridBagLayout());

        cL2Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cL2Label.setText("*");
        leftChannelPanel.add(cL2Label, new java.awt.GridBagConstraints());

        cLDashLabel.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cLDashLabel.setText("-");
        leftChannelPanel.add(cLDashLabel, new java.awt.GridBagConstraints());

        cL1Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cL1Label.setText("*");
        leftChannelPanel.add(cL1Label, new java.awt.GridBagConstraints());

        cL0Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cL0Label.setText("*");
        leftChannelPanel.add(cL0Label, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        displayPanel.add(leftChannelPanel, gridBagConstraints);

        leftUpperIconPanel.setBackground(new java.awt.Color(204, 153, 0));
        leftUpperIconPanel.setLayout(new java.awt.GridLayout(2, 4));

        leftPmsLabel.setText("<");
        leftUpperIconPanel.add(leftPmsLabel);

        leftSkipLabel.setText("SKIP");
        leftUpperIconPanel.add(leftSkipLabel);

        leftMinusLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        leftMinusLabel.setText("-");
        leftUpperIconPanel.add(leftMinusLabel);

        leftPlusLabel.setText("+");
        leftUpperIconPanel.add(leftPlusLabel);

        leftEncLabel.setText("ENC");
        leftUpperIconPanel.add(leftEncLabel);

        leftDecLabel.setText("DEC");
        leftUpperIconPanel.add(leftDecLabel);

        leftTxLabel.setBackground(java.awt.Color.black);
        leftTxLabel.setForeground(new java.awt.Color(204, 153, 0));
        leftTxLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        leftTxLabel.setText("TX");
        leftTxLabel.setOpaque(true);
        leftUpperIconPanel.add(leftTxLabel);

        leftMainLabel.setBackground(java.awt.Color.black);
        leftMainLabel.setForeground(new java.awt.Color(204, 153, 0));
        leftMainLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        leftMainLabel.setText("Main");
        leftMainLabel.setOpaque(true);
        leftUpperIconPanel.add(leftMainLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        displayPanel.add(leftUpperIconPanel, gridBagConstraints);

        rightChannelPanel.setBackground(new java.awt.Color(204, 153, 0));
        rightChannelPanel.setLayout(new java.awt.GridBagLayout());

        cR2Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cR2Label.setText("*");
        rightChannelPanel.add(cR2Label, new java.awt.GridBagConstraints());

        cRDashLabel.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cRDashLabel.setText("-");
        rightChannelPanel.add(cRDashLabel, new java.awt.GridBagConstraints());

        cR1Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cR1Label.setText("*");
        rightChannelPanel.add(cR1Label, new java.awt.GridBagConstraints());

        cR0Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        cR0Label.setText("*");
        rightChannelPanel.add(cR0Label, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        displayPanel.add(rightChannelPanel, gridBagConstraints);

        rightUpperIconPanel.setBackground(new java.awt.Color(204, 153, 0));
        rightUpperIconPanel.setLayout(new java.awt.GridLayout(2, 4));

        rightPmsLabel.setText("<");
        rightUpperIconPanel.add(rightPmsLabel);

        rightSkipLabel.setText("SKIP");
        rightUpperIconPanel.add(rightSkipLabel);

        rightMinusLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        rightMinusLabel.setText("-");
        rightUpperIconPanel.add(rightMinusLabel);

        rightPlusLabel.setText("+");
        rightUpperIconPanel.add(rightPlusLabel);

        rightEncLabel.setText("ENC");
        rightUpperIconPanel.add(rightEncLabel);

        rightDecLabel.setText("DEC");
        rightUpperIconPanel.add(rightDecLabel);

        rightTxLabel.setBackground(java.awt.Color.black);
        rightTxLabel.setForeground(new java.awt.Color(204, 153, 0));
        rightTxLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rightTxLabel.setText("TX");
        rightTxLabel.setOpaque(true);
        rightUpperIconPanel.add(rightTxLabel);

        rightMainLabel.setBackground(java.awt.Color.black);
        rightMainLabel.setForeground(new java.awt.Color(204, 153, 0));
        rightMainLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rightMainLabel.setText("Main");
        rightMainLabel.setOpaque(true);
        rightUpperIconPanel.add(rightMainLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        displayPanel.add(rightUpperIconPanel, gridBagConstraints);

        leftVfoPanel.setBackground(new java.awt.Color(204, 153, 0));
        leftVfoPanel.setLayout(new java.awt.GridBagLayout());

        fL5Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fL5Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        leftVfoPanel.add(fL5Label, gridBagConstraints);

        fL4Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fL4Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        leftVfoPanel.add(fL4Label, gridBagConstraints);

        fL3Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fL3Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        leftVfoPanel.add(fL3Label, gridBagConstraints);

        fLDpLabel.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fLDpLabel.setText(".");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        leftVfoPanel.add(fLDpLabel, gridBagConstraints);

        fL2Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fL2Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        leftVfoPanel.add(fL2Label, gridBagConstraints);

        fL1Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fL1Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        leftVfoPanel.add(fL1Label, gridBagConstraints);

        fL0Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fL0Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        leftVfoPanel.add(fL0Label, gridBagConstraints);

        fLHalfDotLabel.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        fLHalfDotLabel.setText(".");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        leftVfoPanel.add(fLHalfDotLabel, gridBagConstraints);

        fLHalf5Label.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        fLHalf5Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        leftVfoPanel.add(fLHalf5Label, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        displayPanel.add(leftVfoPanel, gridBagConstraints);

        middleIconsPanel.setBackground(new java.awt.Color(204, 153, 0));
        middleIconsPanel.setLayout(new java.awt.GridLayout(4, 1));

        apoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        apoLabel.setText("APO");
        middleIconsPanel.add(apoLabel);

        lockLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lockLabel.setText("LOCK");
        middleIconsPanel.add(lockLabel);

        setLabel.setBackground(java.awt.Color.black);
        setLabel.setForeground(new java.awt.Color(204, 153, 0));
        setLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        setLabel.setText("SET");
        setLabel.setOpaque(true);
        middleIconsPanel.add(setLabel);

        key2Label.setBackground(java.awt.Color.black);
        key2Label.setForeground(new java.awt.Color(204, 153, 0));
        key2Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        key2Label.setText("KEY2");
        key2Label.setOpaque(true);
        middleIconsPanel.add(key2Label);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        displayPanel.add(middleIconsPanel, gridBagConstraints);

        rightVfoPanel.setBackground(new java.awt.Color(204, 153, 0));
        rightVfoPanel.setLayout(new java.awt.GridBagLayout());

        fR5Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fR5Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        rightVfoPanel.add(fR5Label, gridBagConstraints);

        fR4Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fR4Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        rightVfoPanel.add(fR4Label, gridBagConstraints);

        fR3Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fR3Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        rightVfoPanel.add(fR3Label, gridBagConstraints);

        fRDpLabel.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fRDpLabel.setText(".");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        rightVfoPanel.add(fRDpLabel, gridBagConstraints);

        fR2Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fR2Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        rightVfoPanel.add(fR2Label, gridBagConstraints);

        fR1Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fR1Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        rightVfoPanel.add(fR1Label, gridBagConstraints);

        fR0Label.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        fR0Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        rightVfoPanel.add(fR0Label, gridBagConstraints);

        fRHalfDotLabel.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        fRHalfDotLabel.setText(".");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        rightVfoPanel.add(fRHalfDotLabel, gridBagConstraints);

        fRHalf5Label.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        fRHalf5Label.setText("*");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        rightVfoPanel.add(fRHalf5Label, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        displayPanel.add(rightVfoPanel, gridBagConstraints);

        leftBusyLabel.setBackground(java.awt.Color.black);
        leftBusyLabel.setForeground(new java.awt.Color(204, 153, 0));
        leftBusyLabel.setText("BUSY");
        leftBusyLabel.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        displayPanel.add(leftBusyLabel, gridBagConstraints);

        leftMeterPanel.setBackground(new java.awt.Color(204, 153, 0));
        leftMeterPanel.setLayout(new java.awt.GridLayout(1, 9));

        leftMeter1Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        leftMeter1Label.setText("-");
        leftMeterPanel.add(leftMeter1Label);

        leftMeter2Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        leftMeter2Label.setText("-");
        leftMeterPanel.add(leftMeter2Label);

        leftMeter3Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        leftMeter3Label.setText("-");
        leftMeterPanel.add(leftMeter3Label);

        leftMeter4Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        leftMeter4Label.setText("=");
        leftMeterPanel.add(leftMeter4Label);

        leftMeter5Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        leftMeter5Label.setText("-");
        leftMeterPanel.add(leftMeter5Label);

        leftMeter6Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        leftMeter6Label.setText("-");
        leftMeterPanel.add(leftMeter6Label);

        leftMeter7Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        leftMeter7Label.setText("-");
        leftMeterPanel.add(leftMeter7Label);

        leftMeter8Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        leftMeter8Label.setText("-");
        leftMeterPanel.add(leftMeter8Label);

        leftMeter9Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        leftMeter9Label.setText("=");
        leftMeterPanel.add(leftMeter9Label);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        displayPanel.add(leftMeterPanel, gridBagConstraints);

        rightBusyLabel.setBackground(java.awt.Color.black);
        rightBusyLabel.setForeground(new java.awt.Color(204, 153, 0));
        rightBusyLabel.setText("BUSY");
        rightBusyLabel.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        displayPanel.add(rightBusyLabel, gridBagConstraints);

        rightMeterPanel.setBackground(new java.awt.Color(204, 153, 0));
        rightMeterPanel.setLayout(new java.awt.GridLayout(1, 9));

        rightMeter1Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        rightMeter1Label.setText("-");
        rightMeterPanel.add(rightMeter1Label);

        rightMeter2Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        rightMeter2Label.setText("-");
        rightMeterPanel.add(rightMeter2Label);

        rightMeter3Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        rightMeter3Label.setText("-");
        rightMeterPanel.add(rightMeter3Label);

        rightMeter4Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        rightMeter4Label.setText("=");
        rightMeterPanel.add(rightMeter4Label);

        rightMeter5Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        rightMeter5Label.setText("-");
        rightMeterPanel.add(rightMeter5Label);

        rightMeter6Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        rightMeter6Label.setText("-");
        rightMeterPanel.add(rightMeter6Label);

        rightMeter7Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        rightMeter7Label.setText("-");
        rightMeterPanel.add(rightMeter7Label);

        rightMeter8Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        rightMeter8Label.setText("-");
        rightMeterPanel.add(rightMeter8Label);

        rightMeter9Label.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        rightMeter9Label.setText("=");
        rightMeterPanel.add(rightMeter9Label);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        displayPanel.add(rightMeterPanel, gridBagConstraints);

        leftLowerIconPanel.setBackground(new java.awt.Color(204, 153, 0));
        leftLowerIconPanel.setLayout(new java.awt.GridLayout(1, 6));

        leftMtLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        leftMtLabel.setText("MT");
        leftLowerIconPanel.add(leftMtLabel);

        leftMuteLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        leftMuteLabel.setText("MUTE");
        leftLowerIconPanel.add(leftMuteLabel);

        leftDcsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        leftDcsLabel.setText("DCS");
        leftLowerIconPanel.add(leftDcsLabel);

        left9600Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        left9600Label.setText("9600");
        leftLowerIconPanel.add(left9600Label);

        leftLowLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        leftLowLabel.setText("LOW");
        leftLowerIconPanel.add(leftLowLabel);

        leftMidLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        leftMidLabel.setText("MID");
        leftLowerIconPanel.add(leftMidLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        displayPanel.add(leftLowerIconPanel, gridBagConstraints);

        rightLowerIconPanel.setBackground(new java.awt.Color(204, 153, 0));
        rightLowerIconPanel.setLayout(new java.awt.GridLayout(1, 6));

        rightMtLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rightMtLabel.setText("MT");
        rightLowerIconPanel.add(rightMtLabel);

        rightMuteLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rightMuteLabel.setText("MUTE");
        rightLowerIconPanel.add(rightMuteLabel);

        rightDcsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rightDcsLabel.setText("DCS");
        rightLowerIconPanel.add(rightDcsLabel);

        right9600Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        right9600Label.setText("9600");
        rightLowerIconPanel.add(right9600Label);

        rightLowLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rightLowLabel.setText("LOW");
        rightLowerIconPanel.add(rightLowLabel);

        rightMidLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rightMidLabel.setText("MID");
        rightLowerIconPanel.add(rightMidLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        displayPanel.add(rightLowerIconPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        getContentPane().add(displayPanel, gridBagConstraints);

        buttonRowPanel.setLayout(new java.awt.GridBagLayout());

        leftButtonRowPanel.setLayout(new java.awt.GridLayout(1, 4));

        leftLowButton.setText("LOW");
        leftLowButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        leftLowButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                leftLowButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                leftLowButtonMouseReleased(evt);
            }
        });
        leftButtonRowPanel.add(leftLowButton);

        leftVMButton.setText("V/M");
        leftVMButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        leftVMButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                leftVMButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                leftVMButtonMouseReleased(evt);
            }
        });
        leftButtonRowPanel.add(leftVMButton);

        leftHmButton.setText("HM");
        leftHmButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        leftHmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                leftHmButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                leftHmButtonMouseReleased(evt);
            }
        });
        leftButtonRowPanel.add(leftHmButton);

        leftScnButton.setText("SCN");
        leftScnButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        leftScnButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                leftScnButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                leftScnButtonMouseReleased(evt);
            }
        });
        leftButtonRowPanel.add(leftScnButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        buttonRowPanel.add(leftButtonRowPanel, gridBagConstraints);

        setButton.setText(" ");
        setButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        setButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                setButtonMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        buttonRowPanel.add(setButton, gridBagConstraints);

        rightButtonRowPanel.setLayout(new java.awt.GridLayout(1, 4));

        rightLowButton.setText("LOW");
        rightLowButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        rightLowButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rightLowButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rightLowButtonMouseReleased(evt);
            }
        });
        rightButtonRowPanel.add(rightLowButton);

        rightVMButton.setText("V/M");
        rightVMButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        rightVMButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rightVMButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rightVMButtonMouseReleased(evt);
            }
        });
        rightButtonRowPanel.add(rightVMButton);

        rightHmButton.setText("HM");
        rightHmButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        rightHmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rightHmButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rightHmButtonMouseReleased(evt);
            }
        });
        rightButtonRowPanel.add(rightHmButton);

        rightScnButton.setText("SCN");
        rightScnButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        rightScnButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rightScnButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rightScnButtonMouseReleased(evt);
            }
        });
        rightButtonRowPanel.add(rightScnButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        buttonRowPanel.add(rightButtonRowPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(buttonRowPanel, gridBagConstraints);

        leftSideButtonPanel.setLayout(new java.awt.GridLayout(4, 1));

        leftMainButton.setText(" ");
        leftMainButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                leftMainButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                leftMainButtonMouseReleased(evt);
            }
        });
        leftSideButtonPanel.add(leftMainButton);

        hyper1Button.setText("1");
        hyper1Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hyper1ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hyper1ButtonMouseReleased(evt);
            }
        });
        leftSideButtonPanel.add(hyper1Button);

        hyper2Button.setText("2");
        hyper2Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hyper2ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hyper2ButtonMouseReleased(evt);
            }
        });
        leftSideButtonPanel.add(hyper2Button);

        hyper3Button.setText("3");
        hyper3Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hyper3ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hyper3ButtonMouseReleased(evt);
            }
        });
        leftSideButtonPanel.add(hyper3Button);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        getContentPane().add(leftSideButtonPanel, gridBagConstraints);

        rightSideButtonPanel.setLayout(new java.awt.GridLayout(4, 1));

        rightMainButton.setText(" ");
        rightMainButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rightMainButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                rightMainButtonMouseReleased(evt);
            }
        });
        rightSideButtonPanel.add(rightMainButton);

        hyper4Button.setText("4");
        hyper4Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hyper4ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hyper4ButtonMouseReleased(evt);
            }
        });
        rightSideButtonPanel.add(hyper4Button);

        hyper5Button.setText("5");
        hyper5Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hyper5ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hyper5ButtonMouseReleased(evt);
            }
        });
        rightSideButtonPanel.add(hyper5Button);

        hyper6Button.setText("6");
        hyper6Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hyper6ButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hyper6ButtonMouseReleased(evt);
            }
        });
        rightSideButtonPanel.add(hyper6Button);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        getContentPane().add(rightSideButtonPanel, gridBagConstraints);

        sliderPanel.setLayout(new java.awt.GridBagLayout());

        leftEncUpButton.setText("+");
        leftEncUpButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        leftEncUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftEncUpButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        sliderPanel.add(leftEncUpButton, gridBagConstraints);

        leftEncDownButton.setText("-");
        leftEncDownButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        leftEncDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftEncDownButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        sliderPanel.add(leftEncDownButton, gridBagConstraints);

        leftVolSlider.setMaximum(127);
        leftVolSlider.setValue(60);
        leftVolSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                leftVolSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        sliderPanel.add(leftVolSlider, gridBagConstraints);

        leftSqlSlider.setMaximum(127);
        leftSqlSlider.setValue(80);
        leftSqlSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                leftSqlSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        sliderPanel.add(leftSqlSlider, gridBagConstraints);

        rightVolSlider.setMaximum(127);
        rightVolSlider.setValue(60);
        rightVolSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rightVolSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        sliderPanel.add(rightVolSlider, gridBagConstraints);

        rightSqlSlider.setMaximum(127);
        rightSqlSlider.setToolTipText("");
        rightSqlSlider.setValue(80);
        rightSqlSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rightSqlSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        sliderPanel.add(rightSqlSlider, gridBagConstraints);

        rightEncUpButton.setText("+");
        rightEncUpButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        rightEncUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightEncUpButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        sliderPanel.add(rightEncUpButton, gridBagConstraints);

        rightEncDownButton.setText("-");
        rightEncDownButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        rightEncDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightEncDownButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        sliderPanel.add(rightEncDownButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(sliderPanel, gridBagConstraints);

        extraButtonPanel.setLayout(new java.awt.GridLayout(1, 0));

        leftMuteToggleButton.setText("Mute");
        leftMuteToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftMuteToggleButtonActionPerformed(evt);
            }
        });
        extraButtonPanel.add(leftMuteToggleButton);

        pttButton.setText("PTT");
        pttButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pttButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pttButtonMouseReleased(evt);
            }
        });
        extraButtonPanel.add(pttButton);

        wiresButton.setText("WIRES");
        wiresButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                wiresButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                wiresButtonMouseReleased(evt);
            }
        });
        extraButtonPanel.add(wiresButton);

        keyPadButton.setText("Key Pad");
        keyPadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keyPadButtonActionPerformed(evt);
            }
        });
        extraButtonPanel.add(keyPadButton);

        rightMuteToggleButton.setText("Mute");
        rightMuteToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightMuteToggleButtonActionPerformed(evt);
            }
        });
        extraButtonPanel.add(rightMuteToggleButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        getContentPane().add(extraButtonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void leftEncDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftEncDownButtonActionPerformed
        rc.leftEncoderCCW();
    }//GEN-LAST:event_leftEncDownButtonActionPerformed

    private void leftEncUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftEncUpButtonActionPerformed
        rc.leftEncoderCW();
    }//GEN-LAST:event_leftEncUpButtonActionPerformed

    private void rightEncDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightEncDownButtonActionPerformed
        rc.rightEncoderCCW();
    }//GEN-LAST:event_rightEncDownButtonActionPerformed

    private void rightEncUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightEncUpButtonActionPerformed
        rc.rightEncoderCW();
    }//GEN-LAST:event_rightEncUpButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        shutdown();
    }//GEN-LAST:event_formWindowClosing

    private void leftVolSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_leftVolSliderStateChanged
        setLeftVol();
    }//GEN-LAST:event_leftVolSliderStateChanged

    private void leftSqlSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_leftSqlSliderStateChanged
        setLeftSql();
    }//GEN-LAST:event_leftSqlSliderStateChanged

    private void rightSqlSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rightSqlSliderStateChanged
        setRightSql();
    }//GEN-LAST:event_rightSqlSliderStateChanged

    private void rightVolSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rightVolSliderStateChanged
        setRightVol();
    }//GEN-LAST:event_rightVolSliderStateChanged

    private void leftMuteToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftMuteToggleButtonActionPerformed
        setLeftVol();
    }//GEN-LAST:event_leftMuteToggleButtonActionPerformed

    private void pttButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pttButtonMousePressed
        rc.pttOn();
    }//GEN-LAST:event_pttButtonMousePressed

    private void pttButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pttButtonMouseReleased
        rc.pttOff();
    }//GEN-LAST:event_pttButtonMouseReleased

    private void kpP1ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpP1ButtonMousePressed
        rc.kpButton('P');
    }//GEN-LAST:event_kpP1ButtonMousePressed

    private void kpP2ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpP2ButtonMousePressed
        rc.kpButton('Q');
    }//GEN-LAST:event_kpP2ButtonMousePressed

    private void kpP3ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpP3ButtonMousePressed
        rc.kpButton('R');
    }//GEN-LAST:event_kpP3ButtonMousePressed

    private void kpP4ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpP4ButtonMousePressed
        rc.kpButton('S');
    }//GEN-LAST:event_kpP4ButtonMousePressed

    private void kp1ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp1ButtonMousePressed
        rc.kpButton('1');
    }//GEN-LAST:event_kp1ButtonMousePressed

    private void kp2ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp2ButtonMousePressed
        rc.kpButton('2');
    }//GEN-LAST:event_kp2ButtonMousePressed

    private void kp3ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp3ButtonMousePressed
        rc.kpButton('3');
    }//GEN-LAST:event_kp3ButtonMousePressed

    private void kpAButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpAButtonMousePressed
        rc.kpButton('A');
    }//GEN-LAST:event_kpAButtonMousePressed

    private void kp4ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp4ButtonMousePressed
        rc.kpButton('4');
    }//GEN-LAST:event_kp4ButtonMousePressed

    private void kp5ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp5ButtonMousePressed
        rc.kpButton('5');
    }//GEN-LAST:event_kp5ButtonMousePressed

    private void kp6ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp6ButtonMousePressed
        rc.kpButton('6');
    }//GEN-LAST:event_kp6ButtonMousePressed

    private void kpBButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpBButtonMousePressed
        rc.kpButton('B');
    }//GEN-LAST:event_kpBButtonMousePressed

    private void kp7ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp7ButtonMousePressed
        rc.kpButton('7');
    }//GEN-LAST:event_kp7ButtonMousePressed

    private void kp8ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp8ButtonMousePressed
        rc.kpButton('8');
    }//GEN-LAST:event_kp8ButtonMousePressed

    private void kp9ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp9ButtonMousePressed
        rc.kpButton('9');
    }//GEN-LAST:event_kp9ButtonMousePressed

    private void kpCButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpCButtonMousePressed
        rc.kpButton('C');
    }//GEN-LAST:event_kpCButtonMousePressed

    private void kpstarButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpstarButtonMousePressed
        rc.kpButton('*');
    }//GEN-LAST:event_kpstarButtonMousePressed

    private void kp0ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp0ButtonMousePressed
        rc.kpButton('0');
    }//GEN-LAST:event_kp0ButtonMousePressed

    private void kppoundButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kppoundButtonMousePressed
        rc.kpButton('#');
    }//GEN-LAST:event_kppoundButtonMousePressed

    private void kpDButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpDButtonMousePressed
        rc.kpButton('D');
    }//GEN-LAST:event_kpDButtonMousePressed

    private void kp1ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp1ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kp1ButtonMouseReleased

    private void kp2ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp2ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kp2ButtonMouseReleased

    private void kp3ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp3ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kp3ButtonMouseReleased

    private void kpAButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpAButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kpAButtonMouseReleased

    private void kp4ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp4ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kp4ButtonMouseReleased

    private void kp5ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp5ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kp5ButtonMouseReleased

    private void kp6ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp6ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kp6ButtonMouseReleased

    private void kpBButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpBButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kpBButtonMouseReleased

    private void kp7ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp7ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kp7ButtonMouseReleased

    private void kp8ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp8ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kp8ButtonMouseReleased

    private void kp9ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp9ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kp9ButtonMouseReleased

    private void kpCButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpCButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kpCButtonMouseReleased

    private void kpstarButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpstarButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kpstarButtonMouseReleased

    private void kp0ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kp0ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kp0ButtonMouseReleased

    private void kppoundButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kppoundButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kppoundButtonMouseReleased

    private void kpDButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpDButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kpDButtonMouseReleased

    private void kpP1ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpP1ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kpP1ButtonMouseReleased

    private void kpP2ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpP2ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kpP2ButtonMouseReleased

    private void kpP3ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpP3ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kpP3ButtonMouseReleased

    private void kpP4ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kpP4ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_kpP4ButtonMouseReleased

    private void leftLowButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftLowButtonMousePressed
        rc.pushButton("leftLow");
    }//GEN-LAST:event_leftLowButtonMousePressed

    private void leftVMButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftVMButtonMousePressed
        rc.pushButton("leftVm");
    }//GEN-LAST:event_leftVMButtonMousePressed

    private void leftHmButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftHmButtonMousePressed
        rc.pushButton("leftHm");
    }//GEN-LAST:event_leftHmButtonMousePressed

    private void leftScnButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftScnButtonMousePressed
        rc.pushButton("leftScn");
    }//GEN-LAST:event_leftScnButtonMousePressed

    private void setButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setButtonMousePressed
        rc.pushButton("set");
    }//GEN-LAST:event_setButtonMousePressed

    private void rightLowButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightLowButtonMousePressed
        rc.pushButton("rightLow");
    }//GEN-LAST:event_rightLowButtonMousePressed

    private void rightVMButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightVMButtonMousePressed
        rc.pushButton("rightVm");
    }//GEN-LAST:event_rightVMButtonMousePressed

    private void rightHmButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightHmButtonMousePressed
        rc.pushButton("rightHm");
    }//GEN-LAST:event_rightHmButtonMousePressed

    private void rightScnButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightScnButtonMousePressed
        rc.pushButton("rightScn");
    }//GEN-LAST:event_rightScnButtonMousePressed

    private void leftMainButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftMainButtonMousePressed
        rc.pushButton("leftEnc");
    }//GEN-LAST:event_leftMainButtonMousePressed

    private void rightMainButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightMainButtonMousePressed
        rc.pushButton("rightEnc");
    }//GEN-LAST:event_rightMainButtonMousePressed

    private void leftMainButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftMainButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_leftMainButtonMouseReleased

    private void rightMainButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightMainButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_rightMainButtonMouseReleased

    private void leftLowButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftLowButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_leftLowButtonMouseReleased

    private void leftVMButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftVMButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_leftVMButtonMouseReleased

    private void leftHmButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftHmButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_leftHmButtonMouseReleased

    private void leftScnButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftScnButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_leftScnButtonMouseReleased

    private void setButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_setButtonMouseReleased

    private void rightLowButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightLowButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_rightLowButtonMouseReleased

    private void rightVMButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightVMButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_rightVMButtonMouseReleased

    private void rightHmButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightHmButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_rightHmButtonMouseReleased

    private void rightScnButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightScnButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_rightScnButtonMouseReleased

    private void hyper1ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper1ButtonMousePressed
        rc.hyperButton(1);
    }//GEN-LAST:event_hyper1ButtonMousePressed

    private void hyper2ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper2ButtonMousePressed
        rc.hyperButton(2);
    }//GEN-LAST:event_hyper2ButtonMousePressed

    private void hyper3ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper3ButtonMousePressed
        rc.hyperButton(3);
    }//GEN-LAST:event_hyper3ButtonMousePressed

    private void hyper4ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper4ButtonMousePressed
        rc.hyperButton(4);
    }//GEN-LAST:event_hyper4ButtonMousePressed

    private void hyper5ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper5ButtonMousePressed
        rc.hyperButton(5);
    }//GEN-LAST:event_hyper5ButtonMousePressed

    private void hyper6ButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper6ButtonMousePressed
        rc.hyperButton(6);
    }//GEN-LAST:event_hyper6ButtonMousePressed

    private void hyper1ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper1ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_hyper1ButtonMouseReleased

    private void hyper2ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper2ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_hyper2ButtonMouseReleased

    private void hyper3ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper3ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_hyper3ButtonMouseReleased

    private void hyper4ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper4ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_hyper4ButtonMouseReleased

    private void hyper5ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper5ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_hyper5ButtonMouseReleased

    private void hyper6ButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hyper6ButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_hyper6ButtonMouseReleased

    private void wiresButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wiresButtonMousePressed
        rc.pushButton("leftVol");
    }//GEN-LAST:event_wiresButtonMousePressed

    private void wiresButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wiresButtonMouseReleased
        rc.resetButtons();
    }//GEN-LAST:event_wiresButtonMouseReleased

    private void keyPadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keyPadButtonActionPerformed
        keyPadJFrame.setVisible(keyPadJFrame.isVisible()?false:true);
    }//GEN-LAST:event_keyPadButtonActionPerformed

    private void rightMuteToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightMuteToggleButtonActionPerformed
        setRightVol();
    }//GEN-LAST:event_rightMuteToggleButtonActionPerformed
     
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MLFT8800UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MLFT8800UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MLFT8800UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MLFT8800UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MLFT8800UI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel apoLabel;
    private javax.swing.JPanel buttonRowPanel;
    private javax.swing.JLabel cL0Label;
    private javax.swing.JLabel cL1Label;
    private javax.swing.JLabel cL2Label;
    private javax.swing.JLabel cLDashLabel;
    private javax.swing.JLabel cR0Label;
    private javax.swing.JLabel cR1Label;
    private javax.swing.JLabel cR2Label;
    private javax.swing.JLabel cRDashLabel;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JPanel extraButtonPanel;
    private javax.swing.JLabel fL0Label;
    private javax.swing.JLabel fL1Label;
    private javax.swing.JLabel fL2Label;
    private javax.swing.JLabel fL3Label;
    private javax.swing.JLabel fL4Label;
    private javax.swing.JLabel fL5Label;
    private javax.swing.JLabel fLDpLabel;
    private javax.swing.JLabel fLHalf5Label;
    private javax.swing.JLabel fLHalfDotLabel;
    private javax.swing.JLabel fR0Label;
    private javax.swing.JLabel fR1Label;
    private javax.swing.JLabel fR2Label;
    private javax.swing.JLabel fR3Label;
    private javax.swing.JLabel fR4Label;
    private javax.swing.JLabel fR5Label;
    private javax.swing.JLabel fRDpLabel;
    private javax.swing.JLabel fRHalf5Label;
    private javax.swing.JLabel fRHalfDotLabel;
    private javax.swing.JButton hyper1Button;
    private javax.swing.JButton hyper2Button;
    private javax.swing.JButton hyper3Button;
    private javax.swing.JButton hyper4Button;
    private javax.swing.JButton hyper5Button;
    private javax.swing.JButton hyper6Button;
    private javax.swing.JLabel key2Label;
    private javax.swing.JButton keyPadButton;
    private javax.swing.JFrame keyPadJFrame;
    private javax.swing.JPanel keyPadPanel;
    private javax.swing.JButton kp0Button;
    private javax.swing.JButton kp1Button;
    private javax.swing.JButton kp2Button;
    private javax.swing.JButton kp3Button;
    private javax.swing.JButton kp4Button;
    private javax.swing.JButton kp5Button;
    private javax.swing.JButton kp6Button;
    private javax.swing.JButton kp7Button;
    private javax.swing.JButton kp8Button;
    private javax.swing.JButton kp9Button;
    private javax.swing.JButton kpAButton;
    private javax.swing.JButton kpBButton;
    private javax.swing.JButton kpCButton;
    private javax.swing.JButton kpDButton;
    private javax.swing.JButton kpP1Button;
    private javax.swing.JButton kpP2Button;
    private javax.swing.JButton kpP3Button;
    private javax.swing.JButton kpP4Button;
    private javax.swing.JButton kppoundButton;
    private javax.swing.JButton kpstarButton;
    private javax.swing.JLabel left9600Label;
    private javax.swing.JLabel leftBusyLabel;
    private javax.swing.JPanel leftButtonRowPanel;
    private javax.swing.JPanel leftChannelPanel;
    private javax.swing.JLabel leftDcsLabel;
    private javax.swing.JLabel leftDecLabel;
    private javax.swing.JButton leftEncDownButton;
    private javax.swing.JLabel leftEncLabel;
    private javax.swing.JButton leftEncUpButton;
    private javax.swing.JButton leftHmButton;
    private javax.swing.JButton leftLowButton;
    private javax.swing.JLabel leftLowLabel;
    private javax.swing.JPanel leftLowerIconPanel;
    private javax.swing.JButton leftMainButton;
    private javax.swing.JLabel leftMainLabel;
    private javax.swing.JLabel leftMeter1Label;
    private javax.swing.JLabel leftMeter2Label;
    private javax.swing.JLabel leftMeter3Label;
    private javax.swing.JLabel leftMeter4Label;
    private javax.swing.JLabel leftMeter5Label;
    private javax.swing.JLabel leftMeter6Label;
    private javax.swing.JLabel leftMeter7Label;
    private javax.swing.JLabel leftMeter8Label;
    private javax.swing.JLabel leftMeter9Label;
    private javax.swing.JPanel leftMeterPanel;
    private javax.swing.JLabel leftMidLabel;
    private javax.swing.JLabel leftMinusLabel;
    private javax.swing.JLabel leftMtLabel;
    private javax.swing.JLabel leftMuteLabel;
    private javax.swing.JToggleButton leftMuteToggleButton;
    private javax.swing.JLabel leftPlusLabel;
    private javax.swing.JLabel leftPmsLabel;
    private javax.swing.JButton leftScnButton;
    private javax.swing.JPanel leftSideButtonPanel;
    private javax.swing.JLabel leftSkipLabel;
    private javax.swing.JSlider leftSqlSlider;
    private javax.swing.JLabel leftTxLabel;
    private javax.swing.JPanel leftUpperIconPanel;
    private javax.swing.JButton leftVMButton;
    private javax.swing.JPanel leftVfoPanel;
    private javax.swing.JSlider leftVolSlider;
    private javax.swing.JLabel lockLabel;
    private javax.swing.JPanel middleIconsPanel;
    private javax.swing.JButton pttButton;
    private javax.swing.JLabel right9600Label;
    private javax.swing.JLabel rightBusyLabel;
    private javax.swing.JPanel rightButtonRowPanel;
    private javax.swing.JPanel rightChannelPanel;
    private javax.swing.JLabel rightDcsLabel;
    private javax.swing.JLabel rightDecLabel;
    private javax.swing.JButton rightEncDownButton;
    private javax.swing.JLabel rightEncLabel;
    private javax.swing.JButton rightEncUpButton;
    private javax.swing.JButton rightHmButton;
    private javax.swing.JButton rightLowButton;
    private javax.swing.JLabel rightLowLabel;
    private javax.swing.JPanel rightLowerIconPanel;
    private javax.swing.JButton rightMainButton;
    private javax.swing.JLabel rightMainLabel;
    private javax.swing.JLabel rightMeter1Label;
    private javax.swing.JLabel rightMeter2Label;
    private javax.swing.JLabel rightMeter3Label;
    private javax.swing.JLabel rightMeter4Label;
    private javax.swing.JLabel rightMeter5Label;
    private javax.swing.JLabel rightMeter6Label;
    private javax.swing.JLabel rightMeter7Label;
    private javax.swing.JLabel rightMeter8Label;
    private javax.swing.JLabel rightMeter9Label;
    private javax.swing.JPanel rightMeterPanel;
    private javax.swing.JLabel rightMidLabel;
    private javax.swing.JLabel rightMinusLabel;
    private javax.swing.JLabel rightMtLabel;
    private javax.swing.JLabel rightMuteLabel;
    private javax.swing.JToggleButton rightMuteToggleButton;
    private javax.swing.JLabel rightPlusLabel;
    private javax.swing.JLabel rightPmsLabel;
    private javax.swing.JButton rightScnButton;
    private javax.swing.JPanel rightSideButtonPanel;
    private javax.swing.JLabel rightSkipLabel;
    private javax.swing.JSlider rightSqlSlider;
    private javax.swing.JLabel rightTxLabel;
    private javax.swing.JPanel rightUpperIconPanel;
    private javax.swing.JButton rightVMButton;
    private javax.swing.JPanel rightVfoPanel;
    private javax.swing.JSlider rightVolSlider;
    private javax.swing.JButton setButton;
    private javax.swing.JLabel setLabel;
    private javax.swing.JPanel sliderPanel;
    private javax.swing.JButton wiresButton;
    // End of variables declaration//GEN-END:variables
}
