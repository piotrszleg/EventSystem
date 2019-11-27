import java.util.function.Consumer;

public class Tests {
    static class MouseClick {
        double x;
        double y;
        public MouseClick(double x, double y){
            this.x=x;
            this.y=y;
        }
    }
    public static void main(String[] args){
        EventSystem es=new EventSystem();
        Consumer<MouseClick> onClick=(MouseClick e)->System.out.println(e.x);
        es.subscribe(MouseClick.class, onClick);
        es.emit(new MouseClick(2.6, 5));
        es.unsubscribe(MouseClick.class, onClick);
    }
}