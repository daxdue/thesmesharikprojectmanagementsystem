package com.smeshariks.pms.utils;

public class CostValidator implements Validator {

    private String checkData;
    private Double checkDouble;

    public CostValidator(String checkData) {
        this.checkData = checkData;
    }

    public CostValidator(Double checkDouble) {
        this.checkDouble = checkDouble;
    }

    public boolean isValid() {

        /*try {
            Double checkValue = Double.parseDouble(checkData);
            if(checkValue > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

         */

        if(checkDouble > 0) {
            return true;
        }
        return false;
    }
}
