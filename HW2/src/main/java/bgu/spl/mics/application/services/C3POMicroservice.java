package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
	
    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize() {

    }

    protected void writeDiary() {
        diary.setC3POTerminate(System.currentTimeMillis());
    }
}
