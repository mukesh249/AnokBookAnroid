package com.anokbook.Models;

public class WalletHistoryModel {

    String history_id,history_title,history_amount,history_date,histroy_time,payment_id;

    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    public String getHistory_title() {
        return history_title;
    }

    public void setHistory_title(String history_title) {
        this.history_title = history_title;
    }

    public String getHistory_amount() {
        return history_amount;
    }

    public void setHistory_amount(String history_amount) {
        this.history_amount = history_amount;
    }

    public String getHistory_date() {
        return history_date;
    }

    public void setHistory_date(String history_date) {
        this.history_date = history_date;
    }

    public String getHistroy_time() {
        return histroy_time;
    }

    public void setHistroy_time(String histroy_time) {
        this.histroy_time = histroy_time;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
}
