/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dseven.mlft8800;

/**
 *
 * @author imacdonn
 */
public interface MLFT8800CB {
    void updateDisplay(
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
    );
    
    void displayTimeout();
}
