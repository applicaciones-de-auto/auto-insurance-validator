/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.validator.insurance;

import org.guanzon.appdriver.base.GRider;

/**
 *
 * @author Arsiela
 */
public interface ValidatorInterface {
    public void setGRider(GRider foValue);
    public boolean isEntryOkay();
    public String getMessage();
}
