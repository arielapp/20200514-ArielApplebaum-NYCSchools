package com.example.a20200514_arielapplebaum_nycschools.bll;

public class School {


    public enum Fields {
        DBN("dbn"), NAME("school_name"), PHONE("phone_number"), EMAIL("school_email"),
        CRITICALREADING("sat_critical_reading_avg_score"), MATH("sat_math_avg_score"), WRITING("sat_writing_avg_score");

        public String getValue() {
            return Value;
        }

        private String Value;

        Fields(String value) {
            Value = value;
        }
    }

    private String dbn;
    private String Name;
    private String phone;
    private String email;
    private String SATCriticalReading;
    private String SATMath;
    private String SATWriting;

    public School(String dbn) {
        this.dbn = dbn;
    }

    public String getName() {
        return Name;
    }

    public String getDbn() {
        return dbn;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }



    public String getSATCriticalReading() {
        return SATCriticalReading;
    }

    public String getSATMath() {
        return SATMath;
    }

    public String getSATWriting() {
        return SATWriting;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSATCriticalReading(String SATCriticalReading) {
        this.SATCriticalReading = SATCriticalReading;
    }

    public void setSATMath(String SATMath) {
        this.SATMath = SATMath;
    }

    public void setSATWriting(String SATWriting) {
        this.SATWriting = SATWriting;
    }


}
