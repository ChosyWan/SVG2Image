package view;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.FilenameUtils;
import service.SVGService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    GridPane gridPane;

    @FXML
    CheckBox bgColor;

    @FXML
    ComboBox imageType;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Button submitButton,selectFile;

    @FXML
    Text filename;

    private java.awt.Color awtColor;
    final FileChooser fileChooser = new FileChooser();

public Controller(){}

@FXML
public void initialize(){

    submitButton.setDisable(true);

    // Define ImageType Combobox
    ObservableList<String> imageTypes = FXCollections.observableArrayList(
            "jpg","gif","tif"
    );

    imageType.setItems(imageTypes);
    imageType.setOnMouseEntered(event -> {
        //imageType.getStyleClass().add("hovered");
    });
    imageType.setOnMouseExited(event -> {
        //imageType.getStyleClass().remove("hovered");
    });

    colorPicker.setOnMouseEntered(event -> {
        colorPicker.getStyleClass().add("hovered");
    });

    colorPicker.setOnMouseExited(event -> {
        colorPicker.getStyleClass().remove("hovered");
    });

    SVGService svgService = new SVGService();

    colorPicker.setValue(Color.BLUE);

    awtColor = new java.awt.Color((float)colorPicker.getValue().getRed(),(float)colorPicker.getValue().getGreen(),(float)colorPicker.getValue().getBlue(),(float)colorPicker.getValue().getOpacity());

    colorPicker.setOnAction((event)-> {
         {
            System.out.println("New Color"+colorPicker.getValue());
            awtColor = new java.awt.Color((float)colorPicker.getValue().getRed(),(float)colorPicker.getValue().getGreen(),(float)colorPicker.getValue().getBlue(),(float)colorPicker.getValue().getOpacity());
            //awtColor = new java.awt.Color(0,0,0,0);
         }
    });

    submitButton.setOnAction((event)->{
        {
            System.out.println("Start");
            try{svgService.startConvert(awtColor);}
            catch (Exception t){System.out.println("Error Converting the SVG-File"+t);};
        }
    });

    imageType.setOnAction((event -> {

    }));


}

    @FXML void updateImageType(){

        //System.out.println("test");

    }



    @FXML
    protected void showFileChooser() {
        fileChooser.setTitle("Open svg");
        //fileChooser.setInitialDirectory(new File(pdfService.getSourcePath()));
        File file = fileChooser.showOpenDialog(selectFile.getScene().getWindow());
        if (file != null) {
            if(!FilenameUtils.getExtension(file.getName()).equals("svg")){
                file=null;
                filename.setText("invalid file!");
                submitButton.setDisable(true);
            }
            else {submitButton.setDisable(false);
            filename.setText(file.getName());}
        }

    }


    public void exitController() {
        //pdfService.stop();
        //logger.info(LocalDateTime.now().format(formatter)+" | "+"exitController");
    }


}
