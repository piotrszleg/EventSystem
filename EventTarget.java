import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class EventTarget {
    private Map<Class<?>, ArrayList<EventReceiver<?>>> listeners=new HashMap<Class<?>, ArrayList<EventReceiver<?>>>();
    private EventTarget parent;

    public EventTarget getParent(){
        return parent;
    }
    public void setParent(EventTarget value){
        parent=value;
    }
    public final <T extends Event> void emit(T event, boolean bubbling){
        event.setTarget(this);
        event.setBubbling(bubbling);
        apply(event);
    }
    @SuppressWarnings("unchecked")
    final <T extends Event> void apply(T event) {
        for(Map.Entry<Class<?>, ArrayList<EventReceiver<?>>> e : listeners.entrySet()){
            if(e.getKey().isInstance(event)){
                for(EventReceiver<?> callback : e.getValue()){
                    // the signature of subscribe method already ensures
                    // that all callbacks assigned to the event are of correct EventReceiver type
                    ((EventReceiver<T>)callback).receive(event);
                    if(event.isCancelled()){
                        return;
                    }
                }
            }
        }
        if(event.isBubbling()){
            if(parent!=null){
                parent.apply(event);
            }
        }
    }
    public final <T extends Event> void subscribe(Class<T> event, EventReceiver<T> callback){
        if(listeners.containsKey(event)){
            listeners.get(event).add(callback);
        } else {
            ArrayList<EventReceiver<?>> callbackList=new ArrayList<EventReceiver<?>>();
            callbackList.add(callback);
            listeners.put(event, callbackList);
        }
    }
    public final <T extends Event> void unsubscribe(Class<T> event, EventReceiver<T> callback){
        ArrayList<EventReceiver<?>> callbacksList=listeners.get(event);
        if(callbacksList!=null){
            callbacksList.remove(callback);
            if(callbacksList.size()==0){
                listeners.remove(event);
            }
        }
    }
}