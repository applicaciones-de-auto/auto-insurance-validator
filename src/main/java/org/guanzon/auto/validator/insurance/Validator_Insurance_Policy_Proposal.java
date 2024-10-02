/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.validator.insurance;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.model.insurance.Model_Insurance_Policy_Proposal;

/**
 *
 * @author Arsiela
 */
public class Validator_Insurance_Policy_Proposal implements ValidatorInterface {
    GRider poGRider;
    String psMessage;
    
    Model_Insurance_Policy_Proposal poEntity;

    Validator_Insurance_Policy_Proposal(Object foValue){
        poEntity = (Model_Insurance_Policy_Proposal) foValue;
    }

    @Override
    public void setGRider(GRider foValue) {
        poGRider = foValue;
    }

    @Override
    public boolean isEntryOkay() {
        /**
         * POLICY PROPOSAL TYPE
         * 0 = TPL
         * 1 = COMPREHENSIVE
         * 2 = BOTH / TPL AND COMPREHENSIVE
         */
        
        if(poEntity.getTransNo()== null) {
            psMessage = "Transaction No is not set.";
            return false;
        } else {
            if (poEntity.getTransNo().isEmpty()){
                psMessage = "Transaction No is not set.";
                return false;
            }
        }
        if(poEntity.getReferNo()== null) {
            psMessage = "Reference No is not set.";
            return false;
        } else {
            if (poEntity.getReferNo().isEmpty()){
                psMessage = "Reference No is not set.";
                return false;
            }
        }
        if(poEntity.getClientID()== null) {
            psMessage = "Client is not set.";
            return false;
        } else {
            if (poEntity.getClientID().isEmpty()){
                psMessage = "Client is not set.";
                return false;
            }
        }
        if(poEntity.getSerialID()== null) {
            psMessage = "Vehicle is not set.";
            return false;
        } else {
            if (poEntity.getSerialID().isEmpty()){
                psMessage = "Vehicle is not set.";
                return false;
            }
        }
        if(poEntity.getBrInsID()== null) {
            psMessage = "Insurance is not set.";
            return false;
        } else {
            if (poEntity.getBrInsID().isEmpty()){
                psMessage = "Insurance is not set.";
                return false;
            }
        }
        if(poEntity.getInsTypID()== null) {
            psMessage = "Insurance Type is not set.";
            return false;
        } else {
            if (poEntity.getInsTypID().isEmpty()){
                psMessage = "Insurance Type is not set.";
                return false;
            }
        }
        if(poEntity.getIsNew()== null) {
            psMessage = "Proposal Type is not set.";
            return false;
        } else {
            if (poEntity.getIsNew().isEmpty()){
                psMessage = "Insurance Type is not set.";
                return false;
            }
        }
        if(poEntity.getIsNew()== null) {
            psMessage = "Proposal Type is not set.";
            return false;
        } else {
            if (poEntity.getIsNew().isEmpty()){
                psMessage = "Insurance Type is not set.";
                return false;
            }
        }
        if(poEntity.getInsTypID().equals("1") || poEntity.getInsTypID().equals("2")) { //Comprehensive or Both
            if(poEntity.getODTCRate()== null) {
                psMessage = "ODT Rate is not set.";
                return false;
            } else {
                if (poEntity.getODTCRate() <= 0.00){
                    psMessage = "Invalid ODT Rate.";
                    return false;
                }
            }
            if (poEntity.getODTCAmt().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getODTCAmt() == null){
                psMessage = "Invalid ODT coverage amount.";
                return false;
            }
            if (poEntity.getODTCPrem().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getODTCPrem() == null){
                psMessage = "Invalid ODT premium amount.";
                return false;
            }
        }
        if (poEntity.getAONCAmt().compareTo(new BigDecimal("0.00")) > 0){
            if(poEntity.getAONCPayM()== null) {
                psMessage = "AON Payment mode is not set.";
                return false;
            } else {
                if (poEntity.getAONCPayM().isEmpty()){
                    psMessage = "AON Payment mode is not set.";
                    return false;
                }
            }
            
            if(poEntity.getAONCPayM().equals("cha")){
                if(poEntity.getAONCRate()== null) {
                    psMessage = "AON Rate is not set.";
                    return false;
                } else {
                    if (poEntity.getAONCRate() <= 0.00){
                        psMessage = "Invalid AON Rate.";
                        return false;
                    }
                }
                if (poEntity.getAONCPrem().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getAONCPrem() == null){
                    psMessage = "Invalid AON premium amount.";
                    return false;
                }
            }
        }
        
        /*BODILY INJURY*/
        if (poEntity.getBdyCAmt().compareTo(new BigDecimal("0.00")) > 0){
            if (poEntity.getBdyCPrem().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getBdyCPrem() == null){
                psMessage = "Invalid BI premium amount.";
                return false;
            }
        }
        if (poEntity.getBdyCPrem().compareTo(new BigDecimal("0.00")) > 0){
            if (poEntity.getBdyCAmt().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getBdyCAmt() == null){
                psMessage = "Invalid BI coverage amount.";
                return false;
            }
        }
        /*PROPERTY DAMAGE*/
        if (poEntity.getPrDCAmt().compareTo(new BigDecimal("0.00")) > 0){
            if (poEntity.getPrDCPrem().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getPrDCPrem() == null){
                psMessage = "Invalid PD premium amount.";
                return false;
            }
        }
        if (poEntity.getPrDCPrem().compareTo(new BigDecimal("0.00")) > 0){
            if (poEntity.getPrDCAmt().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getPrDCAmt() == null){
                psMessage = "Invalid PD coverage amount.";
                return false;
            }
        }
        /*PASSENGER ACCIDENT*/
        if (poEntity.getPAcCAmt().compareTo(new BigDecimal("0.00")) > 0){
            if (poEntity.getPAcCPrem().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getPAcCPrem() == null){
                psMessage = "Invalid PA premium amount.";
                return false;
            }
        }
        if (poEntity.getPAcCPrem().compareTo(new BigDecimal("0.00")) > 0){
            if (poEntity.getPAcCAmt().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getPAcCAmt() == null){
                psMessage = "Invalid PA coverage amount.";
                return false;
            }
        }
        
        //TPL or Both
        if(poEntity.getInsTypID().equals("0") || poEntity.getInsTypID().equals("2")) { 
            if (poEntity.getTPLPrem().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getTPLPrem() == null){
                psMessage = "Invalid TPL premium amount.";
                return false;
            }
            if (poEntity.getTPLAmt().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getTPLAmt() == null){
                psMessage = "Invalid TPL coverage amount.";
                return false;
            }
        }
        
        if(poEntity.getInsTypID().equals("2")){ //Comprehensive
            if (poEntity.getTPLPrem().compareTo(new BigDecimal("0.00")) > 0 || poEntity.getTPLAmt().compareTo(new BigDecimal("0.00")) > 0){
                psMessage = "Please choose BOTH as POLICY TYPE if you want to include TPL.";
                return false;
            }
        }
        
        if(poEntity.getInsTypID().equals("0")){ //TPL
            if (poEntity.getAONCAmt().compareTo(new BigDecimal("0.00")) > 0 || poEntity.getAONCPrem().compareTo(new BigDecimal("0.00")) > 0
                || poEntity.getBdyCAmt().compareTo(new BigDecimal("0.00")) > 0 || poEntity.getBdyCPrem().compareTo(new BigDecimal("0.00")) > 0 
                || poEntity.getODTCAmt().compareTo(new BigDecimal("0.00")) > 0 || poEntity.getODTCPrem().compareTo(new BigDecimal("0.00")) > 0   
                || poEntity.getPAcCAmt().compareTo(new BigDecimal("0.00")) > 0 || poEntity.getPAcCPrem().compareTo(new BigDecimal("0.00")) > 0    
                || poEntity.getPrDCAmt().compareTo(new BigDecimal("0.00")) > 0 || poEntity.getPrDCPrem().compareTo(new BigDecimal("0.00")) > 0      
                ){
                psMessage = "You chose TPL only, please choose other policy type to include other coverages.";
                return false;
            }
        }
        
        if(poEntity.getTaxRate() == null) {
            psMessage = "Tax Rate is not set.";
            return false;
        } else {
            if (poEntity.getTaxRate() <= 0.00){
                psMessage = "Invalid Tax Rate.";
                return false;
            }
        }
        if(poEntity.getTaxAmt()== null) {
            psMessage = "Tax Amount is not set.";
            return false;
        } else {
             if (poEntity.getTaxAmt().compareTo(new BigDecimal("0.00")) <= 0){
                psMessage = "Invalid Tax Amount.";
                return false;
            }
        }
        
        if (poEntity.getTotalAmt().compareTo(new BigDecimal("0.00")) <= 0 || poEntity.getTotalAmt() == null){
            psMessage = "Invalid Total Amount.";
            return false;
        }
        try {
            // Do not allow multiple proposal per vehicle with the same insurance company
            // Shall alow multiple proposals per vehicle, per policy type as long as rates and or coverages/premiums  or insurance company are not alike
            String lsID = "";
            String lsDesc = "";
            String lsSQL = poEntity.makeSelectSQL();
            lsSQL = MiscUtil.addCondition(lsSQL, " cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
                                                    + " AND sTransNox <> " + SQLUtil.toSQL(poEntity.getTransNo()) 
                                                    + " AND (nODTCRate = " + SQLUtil.toSQL(poEntity.getODTCRate()) 
                                                    + " OR nAONCRate = " + SQLUtil.toSQL(poEntity.getAONCRate()) 
                                                    + " OR nODTCAmtx = " + SQLUtil.toSQL(poEntity.getODTCAmt()) 
                                                    + " OR nODTCPrem = " + SQLUtil.toSQL(poEntity.getODTCPrem()) 
                                                    + " OR nAONCAmtx = " + SQLUtil.toSQL(poEntity.getAONCAmt()) 
                                                    + " OR nAONCPrem = " + SQLUtil.toSQL(poEntity.getAONCPrem()) 
                                                    + " OR nBdyCAmtx = " + SQLUtil.toSQL(poEntity.getBdyCAmt()) 
                                                    + " OR nBdyCPrem = " + SQLUtil.toSQL(poEntity.getBdyCPrem()) 
                                                    + " OR nPrDCAmtx = " + SQLUtil.toSQL(poEntity.getPrDCAmt()) 
                                                    + " OR nPrDCPrem = " + SQLUtil.toSQL(poEntity.getPrDCPrem()) 
                                                    + " OR nPAcCAmtx = " + SQLUtil.toSQL(poEntity.getPAcCAmt()) 
                                                    + " OR nPAcCPrem = " + SQLUtil.toSQL(poEntity.getPAcCPrem()) 
                                                    + " OR nTPLAmtxx = " + SQLUtil.toSQL(poEntity.getTPLAmt()) 
                                                    + " OR nTPLPremx = " + SQLUtil.toSQL(poEntity.getTPLPrem()) 
                                                    + " OR sBrInsIDx = " + SQLUtil.toSQL(poEntity.getBrInsID()) 
                                                    + " ) AND sSerialID = " + SQLUtil.toSQL(poEntity.getSerialID()) 
                                            );
            System.out.println("EXISTING POLICY PROPOSAL CHECK: " + lsSQL);
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            if (MiscUtil.RecordCount(loRS) > 0){
                while(loRS.next()){
                    lsID = loRS.getString("sReferNox");
                    lsDesc = xsDateShort(loRS.getDate("dTransact"));
                }

                MiscUtil.close(loRS);

                psMessage = "Found an existing policy proposal with the same details."
                            + "\n\n<Proposal No:" + lsID + ">"
                            + "\n<Proposal Date:" + lsDesc + ">"
                            + "\n\nSave aborted.";
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Validator_Insurance_Policy_Proposal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    
    @Override
    public String getMessage() {
        return psMessage;
    }
    
    private static String xsDateShort(Date fdValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(fdValue);
        return date;
    }

    private static String xsDateShort(String fsValue) throws org.json.simple.parser.ParseException, java.text.ParseException {
        SimpleDateFormat fromUser = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String lsResult = "";
        lsResult = myFormat.format(fromUser.parse(fsValue));
        return lsResult;
    }
    
    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }
    
    
}
