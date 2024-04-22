package com.mois.transactionservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class TestClass {

    @Test
    public void TestMe(){
        System.out.println("Print me lol");
    }
}
