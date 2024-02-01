package gui;

import core.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class FXMLController implements Initializable {

    Moteur moteur ;

    @FXML
    private Button btn_start;
    @FXML
    private Button btn_new_automate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            URL rulesPath = Main.class.getClassLoader().getResource("rules/Major.json");
            this.moteur = new Moteur(Objects.requireNonNull(rulesPath).getPath(), 100);
            this.moteur.randomizeGrid();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        btn_start.setOnAction(event -> System.out.println("Let's start the game!"));
        btn_new_automate.setOnAction(event -> System.out.println("NEW AUTOMATE!") );

    }

}
