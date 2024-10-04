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
import org.guanzon.auto.model.insurance.Model_Insurance_Policy_Application;
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
        
        Double ldblODTCRate = poEntity.getODTCRate();
        Double ldblAONCRate = poEntity.getAONCRate();
        BigDecimal ldblODTCAmt = poEntity.getODTCAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblODTCPrem = poEntity.getODTCPrem().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblAONCAmt = poEntity.getAONCAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblAONCPrem = poEntity.getAONCPrem().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblBdyCAmt = poEntity.getBdyCAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblBdyCPrem = poEntity.getBdyCPrem().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblPrDCAmt = poEntity.getPrDCAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblPrDCPrem = poEntity.getPrDCPrem().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblPAcCAmt = poEntity.getPAcCAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblPAcCPrem = poEntity.getPAcCPrem().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblTPLAmt = poEntity.getTPLAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblTPLPrem = poEntity.getTPLPrem().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblTotalAmt = poEntity.getTotalAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
        
        if(poEntity.getInsTypID().equals("c") || poEntity.getInsTypID().equals("b")) { //Comprehensive or Both
            if(ldblODTCRate == null) {
                psMessage = "ODT Rate is not set.";
                return false;
            } else {
                if (ldblODTCRate <= 0.00){
                    psMessage = "Invalid ODT Rate.";
                    return false;
                }
            }
            if (ldblODTCAmt.compareTo(new BigDecimal("0.00")) <= 0 || ldblODTCAmt == null){
                psMessage = "Invalid ODT coverage amount.";
                return false;
            }
            if (ldblODTCPrem.compareTo(new BigDecimal("0.00")) <= 0 || ldblODTCPrem == null){
                psMessage = "Invalid ODT premium amount.";
                return false;
            }
        }
        if (ldblAONCAmt.compareTo(new BigDecimal("0.00")) > 0){
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
                if(ldblAONCRate== null) {
                    psMessage = "AON Rate is not set.";
                    return false;
                } else {
                    if (ldblAONCRate <= 0.00){
                        psMessage = "Invalid AON Rate.";
                        return false;
                    }
                }
                if (ldblAONCPrem.compareTo(new BigDecimal("0.00")) <= 0 || ldblAONCPrem == null){
                    psMessage = "Invalid AON premium amount.";
                    return false;
                }
            }
        }
        
        /*BODILY INJURY*/
        if (ldblBdyCAmt.compareTo(new BigDecimal("0.00")) > 0){
            if (ldblBdyCPrem.compareTo(new BigDecimal("0.00")) <= 0 || ldblBdyCPrem == null){
                psMessage = "Invalid BI premium amount.";
                return false;
            }
        }
        if (ldblBdyCPrem.compareTo(new BigDecimal("0.00")) > 0){
            if (ldblBdyCAmt.compareTo(new BigDecimal("0.00")) <= 0 || ldblBdyCAmt == null){
                psMessage = "Invalid BI coverage amount.";
                return false;
            }
        }
        /*PROPERTY DAMAGE*/
        if (ldblPrDCAmt.compareTo(new BigDecimal("0.00")) > 0){
            if (ldblPrDCPrem.compareTo(new BigDecimal("0.00")) <= 0 || ldblPrDCPrem == null){
                psMessage = "Invalid PD premium amount.";
                return false;
            }
        }
        if (ldblPrDCPrem.compareTo(new BigDecimal("0.00")) > 0){
            if (ldblPrDCAmt.compareTo(new BigDecimal("0.00")) <= 0 || ldblPrDCAmt == null){
                psMessage = "Invalid PD coverage amount.";
                return false;
            }
        }
        /*PASSENGER ACCIDENT*/
        if (ldblPAcCAmt.compareTo(new BigDecimal("0.00")) > 0){
            if (ldblPAcCPrem.compareTo(new BigDecimal("0.00")) <= 0 || ldblPAcCPrem == null){
                psMessage = "Invalid PA premium amount.";
                return false;
            }
        }
        if (ldblPAcCPrem.compareTo(new BigDecimal("0.00")) > 0){
            if (ldblPAcCAmt.compareTo(new BigDecimal("0.00")) <= 0 || ldblPAcCAmt == null){
                psMessage = "Invalid PA coverage amount.";
                return false;
            }
        }
        
        //TPL or Both
        if(poEntity.getInsTypID().equals("y") || poEntity.getInsTypID().equals("b")) { 
            if (ldblTPLPrem.compareTo(new BigDecimal("0.00")) <= 0 || ldblTPLPrem == null){
                psMessage = "Invalid TPL premium amount.";
                return false;
            }
            if (ldblTPLAmt.compareTo(new BigDecimal("0.00")) <= 0 || ldblTPLAmt == null){
                psMessage = "Invalid TPL coverage amount.";
                return false;
            }
        }
        
        if(poEntity.getInsTypID().equals("c")){ //Comprehensive
            if (ldblTPLPrem.compareTo(new BigDecimal("0.00")) > 0 || ldblTPLAmt.compareTo(new BigDecimal("0.00")) > 0){
                psMessage = "Please choose BOTH as POLICY TYPE if you want to include TPL.";
                return false;
            }
        }
        
        if(poEntity.getInsTypID().equals("y")){ //TPL
            if (ldblAONCAmt.compareTo(new BigDecimal("0.00")) > 0 || ldblAONCPrem.compareTo(new BigDecimal("0.00")) > 0
                || ldblBdyCAmt.compareTo(new BigDecimal("0.00")) > 0 || ldblBdyCPrem.compareTo(new BigDecimal("0.00")) > 0 
                || ldblODTCAmt.compareTo(new BigDecimal("0.00")) > 0 || ldblODTCPrem.compareTo(new BigDecimal("0.00")) > 0   
                || ldblPAcCAmt.compareTo(new BigDecimal("0.00")) > 0 || ldblPAcCPrem.compareTo(new BigDecimal("0.00")) > 0    
                || ldblPrDCAmt.compareTo(new BigDecimal("0.00")) > 0 || ldblPrDCPrem.compareTo(new BigDecimal("0.00")) > 0      
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
        
        if (ldblTotalAmt.compareTo(new BigDecimal("0.00")) <= 0 || ldblTotalAmt == null){
            psMessage = "Invalid Total Amount.";
            return false;
        }
        
        try {
            
            String lsID = "";
            String lsDesc = "";
            String lsSQL = "";
            
            //Cancellation
            if (poEntity.getTranStat().equals(TransactionStatus.STATE_CANCELLED)){
                if(poEntity.getInsAppNo() != null){
                    if(!poEntity.getInsAppNo().trim().isEmpty()){
                        psMessage = "Policy Proposal already linked thru Application No. " +poEntity.getInsAppNo()+ "\n\nCancellation Aborted.";
                        return false;
                    }
                }
                
                Model_Insurance_Policy_Application loEntity = new Model_Insurance_Policy_Application(poGRider);
                lsSQL =  MiscUtil.addCondition(loEntity.makeSelectSQL(), " cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
                                                                        + " AND sReferNox = " + SQLUtil.toSQL(poEntity.getTransNo()) );
                System.out.println("EXISTING POLICY APPLICATION CHECK: " + lsSQL);
                ResultSet loRS = poGRider.executeQuery(lsSQL);
                if (MiscUtil.RecordCount(loRS) > 0){
                    while(loRS.next()){
                        lsID = loRS.getString("sReferNox");
                        lsDesc = xsDateShort(loRS.getDate("dTransact"));
                    }

                    MiscUtil.close(loRS);

                    psMessage = "Found an existing policy application."
                                + "\n\n<Application No:" + lsID + ">"
                                + "\n<Application Date:" + lsDesc + ">"
                                + "\n\nCancellation aborted.";
                    return false;
                }
                
            }

            // Do not allow multiple proposal per vehicle with the same insurance company
            // Shall alow multiple proposals per vehicle, per policy type as long as rates and or coverages/premiums  or insurance company are not alike
            lsID = "";
            lsDesc = "";
            lsSQL = poEntity.makeSelectSQL();
            lsSQL = MiscUtil.addCondition(lsSQL, " cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
                                                    + " AND sTransNox <> " + SQLUtil.toSQL(poEntity.getTransNo()) 
                                                    + " AND (nODTCRate = " + SQLUtil.toSQL(ldblODTCRate) 
                                                    + " OR nAONCRate = " + SQLUtil.toSQL(ldblAONCRate) 
                                                    + " OR nODTCAmtx = " + SQLUtil.toSQL(ldblODTCAmt) 
                                                    + " OR nODTCPrem = " + SQLUtil.toSQL(ldblODTCPrem) 
                                                    + " OR nAONCAmtx = " + SQLUtil.toSQL(ldblAONCAmt) 
                                                    + " OR nAONCPrem = " + SQLUtil.toSQL(ldblAONCPrem) 
                                                    + " OR nBdyCAmtx = " + SQLUtil.toSQL(ldblBdyCAmt) 
                                                    + " OR nBdyCPrem = " + SQLUtil.toSQL(ldblBdyCPrem) 
                                                    + " OR nPrDCAmtx = " + SQLUtil.toSQL(ldblPrDCAmt) 
                                                    + " OR nPrDCPrem = " + SQLUtil.toSQL(ldblPrDCPrem) 
                                                    + " OR nPAcCAmtx = " + SQLUtil.toSQL(ldblPAcCAmt) 
                                                    + " OR nPAcCPrem = " + SQLUtil.toSQL(ldblPAcCPrem) 
                                                    + " OR nTPLAmtxx = " + SQLUtil.toSQL(ldblTPLAmt) 
                                                    + " OR nTPLPremx = " + SQLUtil.toSQL(ldblTPLPrem) 
                                                    + " OR sBrInsIDx = " + SQLUtil.toSQL(poEntity.getBrInsID()) 
                                                    + " ) AND sInsTypID = " + SQLUtil.toSQL(poEntity.getInsTypID()) 
                                                    + " AND cIsNewxxx = " + SQLUtil.toSQL(poEntity.getIsNew()) 
                                                    + " AND sSerialID = " + SQLUtil.toSQL(poEntity.getSerialID()) 
                    
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
