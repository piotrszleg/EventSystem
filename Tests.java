public class Tests {
    static class MouseClick extends Event {
        double x;
        double y;
        public MouseClick(double x, double y){
            this.x=x;
            this.y=y;
        }
    }
    static class KeyPress extends Event {
        int key;
        public KeyPress(int key){
            this.key=key;
        }
    }

    // workaround Java's lambda limitations
    static boolean eventHappened;
    static boolean secondEventHappened;

    // check if emiting the event causes the callback function being called
    static void callingCallback(){
        eventHappened=false;
        EventReceiver<Event> callback=(Event event)->eventHappened=true;
        EventTarget target=new EventTarget();

        target.subscribe(Event.class, callback);
        target.emit(new Event(), false);
        assert(eventHappened);
        target.unsubscribe(Event.class, callback);
    }
    
    // check if emiting an event of different type than expected doesn't cause the callback function being called
    static void differentReceiverType(){
        eventHappened=false;
        int key=64;
        EventReceiver<KeyPress> callback=(KeyPress event)->{eventHappened=true;};
        EventTarget target=new EventTarget();
        target.subscribe(KeyPress.class, callback);
        target.emit(new MouseClick(0, 0), false);
        assert(!eventHappened);
        target.unsubscribe(KeyPress.class, callback);
    }

    // check if data enclosed in Event object is passed correctly to the callback function
    static void passingData(){
        int key=64;
        EventReceiver<KeyPress> callback=(KeyPress event)->{assert(event.key==key);};
        EventTarget target=new EventTarget();
        target.subscribe(KeyPress.class, callback);
        target.emit(new KeyPress(key), false);
        target.unsubscribe(KeyPress.class, callback);
    }

    // in this event system each event target can have its parent
    // if second parameter to emit is true the event will go up from child to its parent
    static void bubbling(){
        eventHappened=false;
        EventReceiver<Event> callback=(Event event)->eventHappened=true;

        EventTarget parent=new EventTarget();
        EventTarget child=new EventTarget();
        child.setParent(parent);
        
        parent.subscribe(Event.class, callback);
        child.emit(new Event(), true);
        assert(eventHappened);
        parent.unsubscribe(Event.class, callback);
    }
    static void dontBubble(){
        eventHappened=false;
        EventReceiver<Event> callback=(Event event)->eventHappened=true;

        EventTarget parent=new EventTarget();
        EventTarget child=new EventTarget();
        child.setParent(parent);
        
        parent.subscribe(Event.class, callback);
        child.emit(new Event(), false);
        assert(!eventHappened);
        parent.unsubscribe(Event.class, callback);
    }

    // each Event has a stopBubbling method which allows the callback function to stop its propagation to the parent object
    static void stopBubbling(){
        eventHappened=false;
        EventReceiver<Event> stopBubbling=(Event event)->event.stopBubbling();
        EventReceiver<Event> callback=(Event event)->eventHappened=true;

        EventTarget parent=new EventTarget();
        EventTarget child=new EventTarget();
        child.setParent(parent);
        
        child.subscribe(Event.class, stopBubbling);
        parent.subscribe(Event.class, callback);
        child.emit(new Event(), true);
        assert(!eventHappened);
        parent.unsubscribe(Event.class, callback);
        child.unsubscribe(Event.class, stopBubbling);
    }

    static void multipleCallbacks(){
        eventHappened=false;
        secondEventHappened=false;

        EventReceiver<Event> callback1=(Event event)->eventHappened=true;
        EventReceiver<Event> callback2=(Event event)->secondEventHappened=true;

        EventTarget target=new EventTarget();

        target.subscribe(Event.class, callback1);
        target.subscribe(Event.class, callback2);
        target.emit(new Event(), false);
        assert(eventHappened);
        assert(secondEventHappened);
        target.unsubscribe(Event.class, callback1);
        target.unsubscribe(Event.class, callback2);
    }

    // cancel method of Event should stop its bubbling 
    // and stop the rest of callback functions assigned to this Event on this EventTarget from being called
    static void cancelling(){
        eventHappened=false;
        EventReceiver<Event> cancel=(Event event)->event.cancel();
        EventReceiver<Event> callback=(Event event)->eventHappened=true;

        EventTarget parent=new EventTarget();
        EventTarget child=new EventTarget();
        child.setParent(parent);
        
        child.subscribe(Event.class, cancel);
        child.subscribe(Event.class, callback);
        parent.subscribe(Event.class, callback);
        child.emit(new Event(), true);
        assert(!eventHappened);
        child.unsubscribe(Event.class, cancel);
        child.unsubscribe(Event.class, callback);
        parent.unsubscribe(Event.class, callback);
    }

    public static void main(String[] args){
        callingCallback();
        differentReceiverType();
        passingData();
        bubbling();
        dontBubble();
        stopBubbling();
        multipleCallbacks();
        cancelling();
        System.out.println("All tests passed.");
    }
}