package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import org.apache.batik.transcoder.TranscoderException;
import service.SVGService;

import java.io.IOException;

public class Controller {


    @FXML
    ColorPicker colorPicker;

    @FXML
    Button submitButton;

    private java.awt.Color awtColor;

public Controller(){}

@FXML
public void initialize(){

    System.out.println("Test");

   SVGService svgService = new SVGService();

    colorPicker.setValue(Color.BLUE);
    colorPicker.setOnAction((event)-> {
         {
            System.out.println("New Color"+colorPicker.getValue());
            awtColor = new java.awt.Color((float)colorPicker.getValue().getRed(),(float)colorPicker.getValue().getGreen(),(float)colorPicker.getValue().getBlue(),(float)colorPicker.getValue().getOpacity());
         }
    });

    submitButton.setOnAction((event)->{
        {
            System.out.println("Start");
            try{svgService.startConvert(awtColor);}
            catch (Exception t){System.out.println("Oida na!"+t);};
        }
    });

}


}
