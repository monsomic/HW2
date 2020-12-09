package test.java.bgu.spl.mics;

import static org.junit.jupiter.api.Assertions.*;

import main.java.bgu.spl.mics.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class anaAref implements Event<Boolean> {
    private boolean b;

    public anaAref(){
        b=false;
    }
}

class eloimsheishmor implements Broadcast {
    private boolean a;

    public eloimsheishmor(){
        a=false;
    }
}

class stam extends MicroService {
    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public stam(String name) {
        super(name);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void writeDiary() {

    }
}


class MessageBusImplTest {
/*
    private MessageBusImpl MBI;
    private anaAref b;
    private eloimsheishmor a;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        MBI.getInstance();
        b = new anaAref();
        a = new eloimsheishmor();

    }

    @org.junit.jupiter.api.Test
    void complete() {
        // check if the future object has been updated correctly

        Future<Boolean> f = MBI.sendEvent(b);
        MBI.complete(b, true);
        assertTrue(f.get()); //expected true value
    }

    @org.junit.jupiter.api.Test
    void sendBroadcast() {
        //check that all microServices received the broadcast
        MicroService m1 = new stam("orel");
        MicroService m2 = new stam("bobby");
        MicroService m3 = new stam("gaul");
        m1.subscribeBroadcast(eloimsheishmor.class, (callback)->{});
        m2.subscribeBroadcast(eloimsheishmor.class, (callback)->{});
        m3.sendBroadcast(a);
        try {
            assertTrue(MBI.awaitMessage(m1).equals(a));
        }
        catch(InterruptedException nahum){System.out.println("not good");}

        try {
            assertTrue(MBI.awaitMessage(m2).equals(a));
        }
        catch(InterruptedException nahum){System.out.println("not good2");}
    }

    @org.junit.jupiter.api.Test
    void sendEvent() {
        // send a message to the proper queue(check that it goes to the right queue)

        MicroService m1 = new stam("orel");
        MicroService m2 = new stam("bobby");
        m2.subscribeEvent(anaAref.class, (callback)->{});
        Future<Boolean> f = m1.sendEvent(b);
        try {
            assertTrue(MBI.awaitMessage(m2).equals(b));
        }
        catch(InterruptedException taco){System.out.println("not good3");}

    }

 */
}