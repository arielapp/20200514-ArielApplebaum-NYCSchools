package com.example.a20200514_arielapplebaum_nycschools;

import com.example.a20200514_arielapplebaum_nycschools.bll.School;
import com.example.a20200514_arielapplebaum_nycschools.pl.MainActivity;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class SchoolDetailsTest {

    @Test
    public void SchoolNameExist() {

        ArrayList<School> TestedSchools = new ArrayList<>();


        MainActivity TestedClass = new MainActivity();

        TestedClass.AttachData(TestedSchools);

        assertNotNull(TestedClass.getSchoolList());


    }

}
