import javafx.scene.image.Image;

public class Piece extends Image {
    private int pos;

    public Piece(String url, int pos) {
        super(url);
        this.pos = pos;
    }
    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

}
