package com.ooyyh.top.entity;

import lombok.Data;

@Data
public class Labor {
    String id;
    String companyAddress;
    String city;
    String workDate;
    String salary;
    String isInsurance;
    String isSponsored;
    public Labor(String id, String companyAddress, String city, String workDate, String salary, String isInsurance, String isSponsored) {
        this.id = id;
        this.companyAddress = companyAddress;
        this.city = city;
        this.workDate = workDate;
        this.salary = salary;
        this.isInsurance = isInsurance;
        this.isSponsored = isSponsored;
    }
}
