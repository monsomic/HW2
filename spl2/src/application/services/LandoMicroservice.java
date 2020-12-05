package application.services;

import bgu.spl.mics.MicroService;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration=duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyerEvent.class,(BombDestroyerEvent b)->{
            this.sleep(duration);
            complete(b,true);
        });
    }
}
