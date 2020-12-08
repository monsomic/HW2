package test.java.bgu.spl.mics;

import main.java.bgu.spl.mics.Future;
import main.java.bgu.spl.mics.application.passiveObjects.Ewok;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.Test;


import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    private Ewok e;
    private Future future;

    @BeforeEach
     void setUp(){
        future = new Future<>();
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() { e = new Ewok(1); }

    @org.junit.jupiter.api.Test
    void acquire() {
        e.acquire();
        assertTrue(!e.available);
    }

    @org.junit.jupiter.api.Test
    void release() {
        e.release();
        assertTrue(e.available);
    }
}