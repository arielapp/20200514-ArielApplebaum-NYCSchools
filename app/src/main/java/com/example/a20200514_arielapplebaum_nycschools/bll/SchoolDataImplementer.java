package com.example.a20200514_arielapplebaum_nycschools.bll;

import java.util.ArrayList;

public interface SchoolDataImplementer {

    void AttachData(ArrayList<School> schools);

    void HandleError(String error);

}
