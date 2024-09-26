/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.validator.insurance;

/**
 *
 * @author Arsiela
 */
public class ValidatorFactory {
    public enum TYPE{
        Policy_Proposal
    }
    
    public static ValidatorInterface make(ValidatorFactory.TYPE foType, Object foValue){
        switch (foType) {
            case Policy_Proposal:
                return new Validator_Insurance_Policy_Proposal(foValue);
            default:
                return null;
        }
    }
    
}
