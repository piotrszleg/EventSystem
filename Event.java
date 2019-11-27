public class Event {
    EventTarget target;
    boolean bubbling;
    boolean cancelled;
    protected void setTarget(EventTarget value){
        target=value;
    }
    protected void setBubbling(boolean value){
        bubbling=value;
    }
    public void stopBubbling(){
        bubbling=false;
    }
    public void cancel(){
        cancelled=true;
    }
    protected boolean isCancelled(){
        return cancelled;
    }
    protected boolean isBubbling(){
        return bubbling;
    }
}