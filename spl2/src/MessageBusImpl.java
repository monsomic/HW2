import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private Vector<Vector<Message>> qlist; // list of all queues
	//private Vector<Object[]> qmap; //hashmap (microservice name (String),index in qlist (int))
	private ConcurrentHashMap<String,Integer> qmap;

	private Vector<Vector<String>> messageTypeList; // for each type of message a vector of names
	//private Vector<Object[]> eventTypeMap; // hashmap (event type (Event.class),index in eventTypeList (int)) .getClass??
	private ConcurrentHashMap<Class<? extends Message>,Integer> messageTypeMap; //

	//private Vector<Object[]> futureMap; // hashmap (some Event (event<T>),its future (future<T>)
	private ConcurrentHashMap<Event,Future> futureMap;

	private static MessageBusImpl instance =null;


	private MessageBusImpl(){
		qlist = new Vector<Vector<Message>>();
		qmap= new ConcurrentHashMap<String,Integer>();
		messageTypeList = new Vector<Vector<String>>(); //Vector<String> will act as a queue for round robin implementation
		messageTypeMap = new ConcurrentHashMap<Class<? extends Message>,Integer>();
		futureMap = new ConcurrentHashMap<Event,Future>();
	}

	public static synchronized MessageBusImpl getInstance() { // not sure if needed synchronized
		if (instance == null)
			instance = new MessageBusImpl();
		return instance;
	}
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {    //check if the type exists
		// when subscribing send event.class
		Integer currIndex= messageTypeMap.get(type);
		if(currIndex==null){ // make new vector for this specific type and add the name of m to the vector
			messageTypeList.add(new Vector<String>());
			int lastIndex= messageTypeList.size()-1;
			messageTypeList.elementAt(lastIndex).add(m.getName());
			messageTypeMap.put(type,lastIndex);
		}
		else{
			messageTypeList.elementAt(currIndex).add(m.getName()); // put the name in the existing event slot
		}

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }
	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future f = futureMap.get(e);
		f.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	
	@Override
	public synchronized  <T> Future<T>  sendEvent(Event<T> e) { //enque to the right queue (round robin) , create future object , put in futuremap

		Integer ind= messageTypeMap.get(e.getClass());
		if(ind!=null && !messageTypeList.elementAt(ind).isEmpty()){ //this event class is in map & there are microsevises subscribed to it
			String name= messageTypeList.elementAt(ind).elementAt(0);
			int qindex= qmap.get(name);
			qlist.elementAt(qindex).add(e); // push the event to the right queue
			messageTypeList.elementAt(ind).remove(0); // the first name is pushed to the end (round robin)
			messageTypeList.elementAt(ind).add(name);
		}
		else{
			return null;
		}
		Future<T> f= new Future<>();
		futureMap.put(e,f);
		notifyAll();
		return f;
	}

	@Override
	public void register(MicroService m) {    // add to qlist, add name to qmap
		qlist.add(new Vector<Message>());
		qmap.put(m.getName(),qlist.size()-1);
	}

	@Override
	public void unregister(MicroService m) {
		// find the index of the name in qmap, save and delete
		// go to the index in qlist and delete the queue
		// delete from eventTypeList (pass on all the vectors and find name.
		// think if it changes the location of the queues in qlist??
		Integer index= qmap.get(m.getName());
		qmap.remove(m.getName());
		qlist.removeElementAt(index);
		for(String i:qmap.keySet()){ // if removeElement pushes all the bigger indexes one place left
			Integer currentIndex=qmap.get(i);
			if(currentIndex>index)
				qmap.replace(i,currentIndex-1);
		}
		for(Vector<String> v:messageTypeList){
			for(String s: v){
				if(s.equals(m.getName()))
					v.remove(s); // check if removes correctly
			}
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

			int index = qmap.get(m.getName());
			if( qlist.elementAt(index).isEmpty())
				return null;
			Message toReturn = qlist.elementAt(index).firstElement();
			qlist.elementAt(index).remove(0);
			return toReturn;
	}

	private void restart(){ // to clear everything from messegebus
		instance= new MessageBusImpl();
	}
}
