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

    // obejście limitacji lambdy w Java'ie
    static boolean eventHappened;
    static boolean secondEventHappened;

    // sprawdzenie czy wyemitowanie Eventu powoduje wywołanie funkcji która na niego
    static void callingCallback(){
        eventHappened=false;
        EventReceiver<Event> callback=(Event event)->eventHappened=true;
        EventTarget target=new EventTarget();

        target.subscribe(Event.class, callback);
        target.emit(new Event(), false);
        assert(eventHappened);
    }

    static void unsubscribing() {
        eventHappened=false;
        EventReceiver<Event> callback=(Event event)->eventHappened=true;
        EventTarget target=new EventTarget();

        target.subscribe(Event.class, callback);
        target.unsubscribe(Event.class, callback);
        target.emit(new Event(), false);
        assert(!eventHappened);
    }

    static void unsubscribeException() {
        boolean exceptionHappened=false;
        EventReceiver<Event> callback=(Event event)->eventHappened=true;
        EventTarget target=new EventTarget();
        try {
            target.unsubscribe(Event.class, callback);
        } catch(EventTarget.NoSuchEventListener e){
            exceptionHappened=true;
        }
        assert(exceptionHappened);
    }
    
    // sprawdzenie czy wyemitowanie Event'u innego typu nie spowoduje wywołania funkcji oczekującej na Event
    static void differentReceiverType(){
        eventHappened=false;
        int key=64;
        EventReceiver<KeyPress> callback=(KeyPress event)->{eventHappened=true;};
        EventTarget target=new EventTarget();
        target.subscribe(KeyPress.class, callback);
        target.emit(new MouseClick(0, 0), false);
        assert(!eventHappened);
    }

    // przekazywanie danych o wydarzeniu poprzez parametr funkcji
    static void passingData(){
        int key=64;
        EventReceiver<KeyPress> callback=(KeyPress event)->{assert(event.key==key);};
        EventTarget target=new EventTarget();
        target.subscribe(KeyPress.class, callback);
        target.emit(new KeyPress(key), false);
    }

    // drugi parameter funkcji emit EventReceiver'a mówi o tym czy rodzic EventReceiver'a także powinien otrzymać Event
    static void bubbling(){
        eventHappened=false;
        EventReceiver<Event> callback=(Event event)->eventHappened=true;

        EventTarget parent=new EventTarget();
        EventTarget child=new EventTarget();
        child.setParent(parent);
        
        parent.subscribe(Event.class, callback);
        child.emit(new Event(), true);
        assert(eventHappened);
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
    }

    // metoda stopBubbling Event'u zatrzymuje jego wysłanie do obiektu rodzica
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
    }

    // metoda cancel Event'u zatrzymuje wywoływanie pozostałych funkcji do niego przypisanych
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
    }

    public static void main(String[] args){
        callingCallback();
        unsubscribing();
        unsubscribeException();
        differentReceiverType();
        passingData();
        bubbling();
        dontBubble();
        stopBubbling();
        multipleCallbacks();
        cancelling();
        System.out.println("Testy udane.");
    }
}