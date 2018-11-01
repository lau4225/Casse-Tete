import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;

public class Main extends Application {

    public static void main(String[] args) { launch(args); }
    public static ArrayList<ImageView> tabImage = new ArrayList<>();
    public static ArrayList<Image> tabPieces = new ArrayList<>();
    public static GridPane root = new GridPane();


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Casse-Tête");
        primaryStage.setHeight(616);
        primaryStage.setWidth(616);
        primaryStage.setResizable(false);

        /*
        Créer les images de type Piece
        L'attribut position sert à comparer à la fin
        la position dans le tableau avec l'attribut position
        */

        Image img0 = new Piece("volcan0.jpg", 0);
        Image img1 = new Piece("volcan1.jpg", 1);
        Image img2 = new Piece("volcan2.jpg", 2);
        Image img3 = new Piece("volcan3.jpg", 3);
        Image img4 = new Piece("volcan4.jpg", 4);
        Image img5 = new Piece("volcan5.jpg", 5);
        Image img6 = new Piece("volcan6.jpg", 6);
        Image img7 = new Piece("volcan7.jpg", 7);
        Image img8 = new Piece("volcan8.jpg", 8);

       /*
       On ajoute les Images dans un tableau
       pour pouvoir utiliser ma méthode shuffle
        */

        tabPieces.add(img0);
        tabPieces.add(img1);
        tabPieces.add(img2);
        tabPieces.add(img3);
        tabPieces.add(img4);
        tabPieces.add(img5);
        tabPieces.add(img6);
        tabPieces.add(img7);
        tabPieces.add(img8);

        Collections.shuffle(tabPieces);

        /*
        On crée un autre tableau d'ImageViews pour ne pas
        affecter les ImageViews et seulement changer les Images
        lors du Drag and Drop
         */

        for (int i = 0; i<9; i++){
            tabImage.add(new ImageView(tabPieces.get(i)));
        }

        /*
        Créer un GridPane pour pouvoir bien disposer les
        images en fonction de leur position dans le
        tableau d'images
         */

        root.add(tabImage.get(0), 0, 0);
        root.add(tabImage.get(1), 1, 0);
        root.add(tabImage.get(2), 2, 0);
        root.add(tabImage.get(3), 0, 1);
        root.add(tabImage.get(4), 1, 1);
        root.add(tabImage.get(5), 2, 1);
        root.add(tabImage.get(6), 0, 2);
        root.add(tabImage.get(7), 1, 2);
        root.add(tabImage.get(8), 2, 2);

        // Espacements pour faire plus beau

        root.setHgap(4);
        root.setVgap(4);
        root.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(root);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        //Fonctionnement: COLONNE puis LIGNE
        // MELANGER  "Ctrl + M"

        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.M) { melanger(); }
        });

        //DRAG AND DROP
        for (int i = 0; i < 9; i++) {
            int finalI = i;                                                //1I = DEPART
            /*
            Détecter si une ImageView est drag par la souris
             */
            tabImage.get(i).setOnDragDetected((MouseEvent event) -> {
                // mettre l'image choisi dans le Dragboard (meme que dans le content)
                Dragboard dragboard = tabImage.get(finalI).startDragAndDrop(TransferMode.MOVE);
                ClipboardContent contenu = new ClipboardContent();
                contenu.putImage(tabImage.get(finalI).getImage());               //1I = DEPART
                dragboard.setContent(contenu);
            });
            tabImage.get(i).setOnDragDone(event -> { //quand tout est terminé

                boolean trouve = true;

                for (int j = 0; j<9; j++){
                    Piece piece = (Piece) tabImage.get(j).getImage();
                    if (piece.getPos() != j){
                        trouve = false;
                    }
                }
                if (trouve){
                    root.setHgap(0); root.setVgap(0);

                    Alert felicitation = new Alert(Alert.AlertType.CONFIRMATION);
                    felicitation.setTitle("Casse-Tête Réussi!");
                    felicitation.setHeaderText("Félicitation!! :D");
                    felicitation.setContentText("Voulez-vous rejouer?");
                    ButtonType reponse = felicitation.showAndWait().get();

                    if (reponse == ButtonType.OK){
                        root.setHgap(4);root.setVgap(4);
                        melanger();
                    } else if (reponse == ButtonType.CANCEL){
                        felicitation.close(); primaryStage.close();event.consume();}
                }});
            //source et destination même mode de transfert


            int finalI1 = i;                                  //1I1 = FIN
            tabImage.get(i).setOnDragOver(event -> { //pour accepter modes de transfert
                event.acceptTransferModes(TransferMode.MOVE);
            });


            tabImage.get(i).setOnDragDropped(event -> {               //1I1 = FIN
                Image temp = ((ImageView)event.getGestureSource()).getImage(); //image du depart
                //set image du depart a image fin
                ((ImageView)event.getGestureSource()).setImage(tabImage.get(finalI1).getImage());
                //set image fin a image depart
                tabImage.get(finalI1).setImage(temp);
                event.setDropCompleted(true);

            });
        }
    }

    public void melanger (){
        root.getChildren().clear();
        Collections.shuffle(tabPieces);

        for (int i = 0; i<9; i++){
            tabImage.get(i).setImage(tabPieces.get(i));
        }
        root.add(tabImage.get(0), 0, 0);
        root.add(tabImage.get(1), 1, 0);
        root.add(tabImage.get(2), 2, 0);
        root.add(tabImage.get(3), 0, 1);
        root.add(tabImage.get(4), 1, 1);
        root.add(tabImage.get(5), 2, 1);
        root.add(tabImage.get(6), 0, 2);
        root.add(tabImage.get(7), 1, 2);
        root.add(tabImage.get(8), 2, 2);

    }

}
