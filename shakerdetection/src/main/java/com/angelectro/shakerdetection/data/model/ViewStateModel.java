package com.angelectro.shakerdetection.data.model;



public class ViewStateModel {
    private boolean checkedSlack;
    private boolean checkedJira;

    public boolean isCheckedSlack() {
        return checkedSlack;
    }

    public void setCheckedSlack(boolean checkedSlack) {
        this.checkedSlack = checkedSlack;
    }

    public boolean isCheckedJira() {
        return checkedJira;
    }

    public void setCheckedJira(boolean checkedJira) {
        this.checkedJira = checkedJira;
    }
}
