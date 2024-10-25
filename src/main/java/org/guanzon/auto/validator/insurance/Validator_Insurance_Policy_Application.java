/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.validator.insurance;

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
import org.guanzon.auto.model.insurance.Model_Insurance_Policy_Application;

/**
 *
 * @author Arsiela
 */
public class Validator_Insurance_Policy_Application implements ValidatorInterface {
    GRider poGRider;
    String psMessage;
    
    Model_Insurance_Policy_Application poEntity;

    Validator_Insurance_Policy_Application(Object foValue){
        poEntity = (Model_Insurance_Policy_Application) foValue;
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
        if(poEntity.getReferNo()== null) {
            psMessage = "Reference No is not set.";
            return false;
        } else {
            if (poEntity.getReferNo().isEmpty()){
                psMessage = "Reference No is not set.";
                return false;
            }
        }
        if(poEntity.getEmployID()== null) {
            psMessage = "Insurance Coordinator is not set.";
            return false;
        } else {
            if (poEntity.getEmployID().isEmpty()){
                psMessage = "Insurance Coordinator is not set.";
                return false;
            }
        }
        if(poEntity.getFinType()== null) {
            psMessage = "Finance type is not set.";
            return false;
        } else {
            if (poEntity.getFinType().isEmpty()){
                psMessage = "Finance type is not set.";
                return false;
            }
        }
        
//        if (poEntity.getFinType().toLowerCase().equals("f") || poEntity.getFinType().toLowerCase().equals("po")){
        if (!poEntity.getFinType().toLowerCase().equals("0")){
            if(poEntity.getBrBankID()== null) {
                psMessage = "Bank is not set.";
                return false;
            } else {
                if (poEntity.getBrBankID().isEmpty()){
                    psMessage = "Bank is not set.";
                    return false;
                }
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
        
        try {
            
            String lsID = "";
            String lsDesc = "";
            String lsSQL = "";
            
            //Cancellation
            if (poEntity.getTranStat().equals(TransactionStatus.STATE_CANCELLED)){
                if(poEntity.getPolicyNo()!= null){
                    if(!poEntity.getPolicyNo().trim().isEmpty()){
                        psMessage = "Policy Application already linked thru Policy No. " +poEntity.getPolicyNo()+ "\n\nCancellation Aborted.";
                        return false;
                    }
                }
                
                Model_Insurance_Policy loEntity = new Model_Insurance_Policy(poGRider);
                lsSQL =  MiscUtil.addCondition(loEntity.makeSelectSQL(), " cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
                                                                        + " AND sReferNox = " + SQLUtil.toSQL(poEntity.getTransNo()) );
                System.out.println("EXISTING POLICY CHECK: " + lsSQL);
                ResultSet loRS = poGRider.executeQuery(lsSQL);
                if (MiscUtil.RecordCount(loRS) > 0){
                    while(loRS.next()){
                        lsID = loRS.getString("sPolicyNo");
                        lsDesc = xsDateShort(loRS.getDate("dTransact"));
                    }

                    MiscUtil.close(loRS);

                    psMessage = "Found an existing policy."
                                + "\n\n<Policy No:" + lsID + ">"
                                + "\n<Policy Date:" + lsDesc + ">"
                                + "\n\nCancellation aborted.";
                    return false;
                }
                
                //Check when linked to accounting forms
                //PAYMENT
                //check if linked with CC
                //check if linked with Receipt
                lsID = "";
                lsDesc = "";
                String lsType = "";
                lsSQL = " SELECT "                                             
                        + "   a.sTransNox "                                     
                        + " , a.sReferNox "                                     
                        + " , a.sSourceCD "                                     
                        + " , a.sSourceNo "                                     
                        + " , a.sTranType "                                     
                        + " , b.sReferNox AS sSINoxxxx "                        
                        + " , b.dTransact "                      
                        + " , b.cTranStat  "                                  
                        + " FROM si_master_source a "                           
                        + " LEFT JOIN si_master b ON b.sTransNox = a.sTransNox ";
                lsSQL = MiscUtil.addCondition(lsSQL, " b.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
                                                    + " AND a.sReferNox = " + SQLUtil.toSQL(poEntity.getTransNo()) 
                                                    );
                System.out.println("EXISTING PAYMENT CHECK: " + lsSQL);
                loRS = poGRider.executeQuery(lsSQL);

                if (MiscUtil.RecordCount(loRS) > 0){
                    while(loRS.next()){
                        lsID = loRS.getString("sSINoxxxx");
                        lsType = loRS.getString("sTranType"); //TODO
                        lsDesc = xsDateShort(loRS.getDate("dTransact"));
                    }

                    MiscUtil.close(loRS);

                    psMessage = "Found an existing payment."
                                + "\n\n<Invoice No:" + lsID + ">"
                                + "\n<Invoice Date:" + lsDesc + ">"
                                + "\n<Invoice Type:" + lsType + ">"
                                + "\n\nCancellation aborted.";
                    return false;
                }
            }
            
            //Do not allow multiple application for insrance proposal
            lsID = "";
            lsDesc = "";
            lsSQL = poEntity.makeSelectSQL();
            lsSQL = MiscUtil.addCondition(lsSQL, " cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED) 
                                                    + " AND sTransNox <> " + SQLUtil.toSQL(poEntity.getTransNo()) 
                                                    + " AND sReferNox = " + SQLUtil.toSQL(poEntity.getReferNo())   
                                                    );
            System.out.println("EXISTING POLICY APPLICATION CHECK: " + lsSQL);
            ResultSet loRS = poGRider.executeQuery(lsSQL);
            if (MiscUtil.RecordCount(loRS) > 0){
                while(loRS.next()){
                    lsID = loRS.getString("sTransNox");
                    lsDesc = xsDateShort(loRS.getDate("dTransact"));
                }

                MiscUtil.close(loRS);

                psMessage = "Found an existing policy application for policy proposal."
                            + "\n\n<Application No:" + lsID + ">"
                            + "\n<Application Date:" + lsDesc + ">"
                            + "\n\nSave aborted.";
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Validator_Insurance_Policy_Application.class.getName()).log(Level.SEVERE, null, ex);
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
