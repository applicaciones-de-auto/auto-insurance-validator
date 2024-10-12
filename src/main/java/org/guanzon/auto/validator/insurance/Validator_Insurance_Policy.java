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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.auto.model.insurance.Model_Insurance_Policy;

/**
 *
 * @author Arsiela
 */
public class Validator_Insurance_Policy implements ValidatorInterface {
    GRider poGRider;
    String psMessage;
    
    Model_Insurance_Policy poEntity;

    Validator_Insurance_Policy(Object foValue){
        poEntity = (Model_Insurance_Policy) foValue;
    }

    @Override
    public void setGRider(GRider foValue) {
        poGRider = foValue;
    }

    @Override
    public boolean isEntryOkay() {
        if(poEntity.getTransNo()== null) {
            psMessage = "Transaction No is not set.";
            return false;
        } else {
            if (poEntity.getTransNo().isEmpty()){
                psMessage = "Transaction No is not set.";
                return false;
            }
        }
        if(poEntity.getPolicyNo()== null) {
            psMessage = "Policy No is not set.";
            return false;
        } else {
            if (poEntity.getPolicyNo().isEmpty()){
                psMessage = "Policy No is not set.";
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
        
        String lsdate = "1900-01-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateOld = null;
        try {
            // Parse the formatted date string into a Date object
            dateOld = sdf.parse(lsdate);
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }

        java.util.Date date = (java.util.Date) poEntity.getValue("dValidFrm");
        System.out.println(date);

        if(date == null){
            psMessage = "Invalid valid from Date.";
            return false;
        } else {
            if("1900-01-01".equals(xsDateShort(date))){
                psMessage = "Invalid valid from Date.";
                return false;
            }
        }

        date = (java.util.Date) poEntity.getValue("dValidTru");
        if(date == null){
            psMessage = "Invalid valid to Date.";
            return false;
        } else {
            if("1900-01-01".equals(xsDateShort(date))){
                psMessage = "Invalid valid to Date.";
                return false;
            }
        }

        LocalDate ldteFrom = strToDate(xsDateShort((java.util.Date) poEntity.getValue("dValidFrm")));
        LocalDate ldteThru =  strToDate(xsDateShort((java.util.Date) poEntity.getValue("dValidTru")));
        Period age = Period.between(ldteFrom, ldteThru);
        if(age.getDays() < 0){
            psMessage = "Invalid policy application validity Date.";
            return false;
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
        BigDecimal ldblGrossAmt = poEntity.getGrossAmt().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblNetTotal = poEntity.getNetTotal().setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal ldblCommissn = poEntity.getCommissn().setScale(2, BigDecimal.ROUND_HALF_UP);
        
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
        
        if(poEntity.getVATRate()== null) {
            psMessage = "VAT Rate is not set.";
            return false;
        } else {
            if (poEntity.getVATRate() <= 0.00){
                psMessage = "Invalid VAT Rate.";
                return false;
            }
        }
        if(poEntity.getVATAmt()== null) {
            psMessage = "VAT Amount is not set.";
            return false;
        } else {
             if (poEntity.getVATAmt().compareTo(new BigDecimal("0.00")) <= 0){
                psMessage = "Invalid VAT Amount.";
                return false;
            }
        }
        
        if(poEntity.getLGUTaxRt()== null) {
            psMessage = "LGU Tax Rate is not set.";
            return false;
        } else {
            if (poEntity.getLGUTaxRt() <= 0.00){
                psMessage = "Invalid LGU Tax Rate.";
                return false;
            }
        }
        if(poEntity.getLGUTaxAm()== null) {
            psMessage = "LGU Tax Amount is not set.";
            return false;
        } else {
             if (poEntity.getLGUTaxAm().compareTo(new BigDecimal("0.00")) <= 0){
                psMessage = "Invalid LGU Tax Amount.";
                return false;
            }
        }
        
        if(poEntity.getDocRate()== null) {
            psMessage = "Documentary stamp Rate is not set.";
            return false;
        } else {
            if (poEntity.getDocRate() <= 0.00){
                psMessage = "Invalid Documentary stamp Rate.";
                return false;
            }
        }
        if(poEntity.getDocAmt()== null) {
            psMessage = "Documentary stamp Amount is not set.";
            return false;
        } else {
             if (poEntity.getDocAmt().compareTo(new BigDecimal("0.00")) <= 0){
                psMessage = "Invalid Documentary stamp Amount.";
                return false;
            }
        }
        
        if (ldblGrossAmt.compareTo(new BigDecimal("0.00")) <= 0 || ldblGrossAmt == null){
            psMessage = "Invalid Gross Amount.";
            return false;
        }
        
        if (ldblNetTotal.compareTo(new BigDecimal("0.00")) <= 0 || ldblNetTotal == null){
            psMessage = "Invalid Net Total Amount.";
            return false;
        }
        
        if (ldblCommissn.compareTo(new BigDecimal("0.00")) <= 0 || ldblCommissn == null){
            psMessage = "Invalid Commission Amount.";
            return false;
        }
        try {
            //Do not allow multiple application for insurance application
            String lsID = "";
            String lsDesc = "";
            String lsSQL = poEntity.makeSelectSQL();
            lsSQL = MiscUtil.addCondition(lsSQL, " cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
                                                    + " AND sTransNox <> " + SQLUtil.toSQL(poEntity.getTransNo()) 
                                                    + " AND sReferNox = " + SQLUtil.toSQL(poEntity.getReferNo())   
                                                    );
            System.out.println("EXISTING POLICY CHECK: " + lsSQL);
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            if (MiscUtil.RecordCount(loRS) > 0){
                while(loRS.next()){
                    lsID = loRS.getString("sTransNox");
                    lsDesc = xsDateShort(loRS.getDate("dTransact"));
                }

                MiscUtil.close(loRS);

                psMessage = "Found an existing policy for policy application."
                            + "\n\n<Policy No:" + lsID + ">"
                            + "\n<Policy Date:" + lsDesc + ">"
                            + "\n\nSave aborted.";
                return false;
            }
            
            //check for same existing policy number but with different unit, do not allow if found any	
            lsID = "";
            lsDesc = "";
            lsSQL = poEntity.getSQL();
            lsSQL = MiscUtil.addCondition(lsSQL, " a.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
                                                    + " AND a.sPolicyNo = " + SQLUtil.toSQL(poEntity.getPolicyNo()) 
                                                    + " AND c.sSerialID <> " + SQLUtil.toSQL(poEntity.getSerialID())   
                                                    );
            System.out.println("EXISTING POLICY WITH DIFFERENT VEHICLE CHECK: " + lsSQL);
            loRS = poGRider.executeQuery(lsSQL);
            if (MiscUtil.RecordCount(loRS) > 0){
                while(loRS.next()){
                    lsID = loRS.getString("sPolicyNo");
                    lsDesc = xsDateShort(loRS.getDate("dTransact"));
                }

                MiscUtil.close(loRS);

                psMessage = "Found exisiting policy number."
                            + "\n\n<Policy No:" + lsID + ">"
                            + "\n<Policy Date:" + lsDesc + ">"
                            + "\n\nSave aborted.";
                return false;
            }
            //check for same existing policy number but with different unit, do not allow if found any
            lsID = "";
            lsDesc = "";
            lsSQL = poEntity.getSQL();
            lsSQL = MiscUtil.addCondition(lsSQL, " a.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
                                                    + " AND a.sPolicyNo = " + SQLUtil.toSQL(poEntity.getPolicyNo()) 
                                                    + " AND c.sSerialID <> " + SQLUtil.toSQL(poEntity.getSerialID()) 
                                                    + " AND a.sTransNox <> " + SQLUtil.toSQL(poEntity.getTransNo())  
                                                    );
            System.out.println("EXISTING POLICY WITH DIFFERENT VEHICLE and DIFFERENT POLICY CHECK: " + lsSQL);
            loRS = poGRider.executeQuery(lsSQL);
            if (MiscUtil.RecordCount(loRS) > 0){
                while(loRS.next()){
                    lsID = loRS.getString("sPolicyNo");
                    lsDesc = xsDateShort(loRS.getDate("dTransact"));
                }

                MiscUtil.close(loRS);

                psMessage = "Found exisiting policy."
                            + "\n\n<Policy No:" + lsID + ">"
                            + "\n<Policy Date:" + lsDesc + ">"
                            + "\n\nSave aborted.";
                return false;
            }
            
            //check for same existing policy number with same unit, allow but date of validity should be lesser than the new policy user is trying to input; prompt user if found one
//            lsID = "";
//            lsDesc = "";
//            lsSQL = poEntity.makeSelectSQL();
//            lsSQL = MiscUtil.addCondition(lsSQL, " cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
//                                                    + " AND sPolicyNo = " + SQLUtil.toSQL(poEntity.getPolicyNo()) 
//                                                    + " AND sSerialID = " + SQLUtil.toSQL(poEntity.getSerialID())  
//                                                    + " AND (DATE(dValidTru) = " + SQLUtil.toSQL(poEntity.getValidFrmDte())
//                                                    + " ) ORDER BY dValidTru DESC LIMIT 1"
//                                                    );
//            System.out.println("EXISTING POLICY WITH DIFFERENT VEHICLE CHECK: " + lsSQL);
//            loRS = poGRider.executeQuery(lsSQL);
//            if (MiscUtil.RecordCount(loRS) > 0){
//                while(loRS.next()){
//                    lsID = loRS.getString("sPolicyNo");
//                    lsDesc = xsDateShort(loRS.getDate("dTransact"));
//                }
//
//                MiscUtil.close(loRS);
//
//                psMessage = "Found exisiting policy number with similar to one of those previously recorded Policy for this unit. Are your sure this is correct?"
//                            + "\n\n<Policy No:" + lsID + ">"
//                            + "\n<Policy Date:" + lsDesc + ">"
//                            + "\n\nSave aborted.";
//                return false;
//            }

            //check if issued date has been used already at wpe tables TODO
            
        } catch (SQLException ex) {
            Logger.getLogger(Validator_Insurance_Policy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }

    
    @Override
    public String getMessage() {
        return psMessage;
    }
    
    private static String xsDateShort(java.util.Date fdValue) {
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
