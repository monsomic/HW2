package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.application.messages.AttackEvent;
import main.java.bgu.spl.mics.application.messages.BombDestroyerEvent;
import main.java.bgu.spl.mics.application.messages.DeactivationEvent;
import main.java.bgu.spl.mics.application.passiveObjects.Ewoks;
import main.java.bgu.spl.mics.MicroService;


import java.util.List;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {

    private long duration;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration=duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(DeactivationEvent.class,(DeactivationEvent d)->{
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            complete(d,true);
        });
    }

    protected void writeDiary() {
        diary.setR2D2Terminate(System.currentTimeMillis());
    }
}
