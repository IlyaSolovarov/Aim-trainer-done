public class Circle {
    public int x,y;
    public int r;
    public volatile boolean isShot;
    public int index;

    public Circle(int x, int y, int r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public boolean checkShot(int mouse_x, int mouse_y) {
        if ((x - mouse_x) * (x - mouse_x) + (y - mouse_y) * (y - mouse_y) <= r * r)
            return true;
        return false;
    }
}
