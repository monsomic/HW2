import java.sql.Array;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private Vector<Queue<Message>> qlist; // list of all queues
	private Vector[] eventTypeList; // for each type pf message a vector of names
	private Vector<Vector> eventTypeMap;
	private Vector<Object[]> qmap; //hashmap (MessageType,index in qlist)

	public MessageBusImpl(){
		eventTypeList = new Vector[3];
		/*for(int i=0;i<3;i++){
			eventTypeList[i]=new Vector<String>(0);
		}*/
		qlist = new Vector(0);

		qmap =new Vector(0);
	}
	
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {    // add Q from "all"(qlist[0][i]) to the correct event type
		
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }
	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		
        return null;
	}

	@Override
	public void register(MicroService m) {    // add to qlist, add name to qmap
		qlist.addElement(new LinkedList<Message>());
		Object[] toAdd=new Object[]{m.getName(),qlist.size()-1}; //might be problematic
		qmap.addElement(toAdd);
	}

	@Override
	public void unregister(MicroService m) {
		
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		
		return null;
	}
}
