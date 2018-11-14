package view;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.FilenameUtils;
import service.SVGService;

import javax.imageio.ImageIO;
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

    @FXML
    ImageView imagePreview,okLogo;

    private SVGService svgService;
    private File svgFile;
    private java.awt.Color awtColor;
    final FileChooser fileChooser = new FileChooser();


public Controller(){}

@FXML
public void initialize(){
    checkBG();
    Double transitionDuration = 500.00;
    okLogo.setImage(new Image("icon_file.png"));
    okLogo.setFitHeight(15);
    okLogo.setOpacity(0);
    ScaleTransition scale = new ScaleTransition(Duration.millis(transitionDuration),okLogo);
    scale.setFromX(1.0f);
    scale.setFromY(1.0f);
    scale.setToX(1.5f);
    scale.setToY(1.5f);
    FadeTransition fade = new FadeTransition(Duration.millis(transitionDuration),okLogo);
    fade.setFromValue(1);
    fade.setToValue(0);
    RotateTransition rotate = new RotateTransition(Duration.millis(transitionDuration-100),okLogo);
    rotate.setFromAngle(0);
    rotate.setToAngle(60);
    TranslateTransition translate = new TranslateTransition(Duration.millis(transitionDuration),okLogo);
    translate.setFromX(-20);
    translate.setToX(40);
    ParallelTransition parallel = new ParallelTransition(scale,fade,rotate,translate);

    submitButton.setDisable(true);

    String writerNames[] = ImageIO.getWriterFormatNames();

    // Define ImageType Combobox
    ObservableList<String> imageTypes = FXCollections.observableArrayList();
    for(String imagetype:writerNames){imageTypes.add(imagetype);}

    imageType.setItems(imageTypes);
    imageType.setOnMouseEntered(event -> {
        imageType.getStyleClass().add("hovered");
    });
    imageType.setOnMouseExited(event -> {
        imageType.getStyleClass().remove("hovered");
    });

    colorPicker.setOnMouseEntered(event -> {
        colorPicker.getStyleClass().add("hovered");
    });

    colorPicker.setOnMouseExited(event -> {
        colorPicker.getStyleClass().remove("hovered");
    });

    svgService = new SVGService(this);

    colorPicker.setValue(Color.BLUE);

    awtColor = new java.awt.Color((float)colorPicker.getValue().getRed(),(float)colorPicker.getValue().getGreen(),(float)colorPicker.getValue().getBlue(),(float)colorPicker.getValue().getOpacity());

    colorPicker.setOnAction((event)-> {
         {
            System.out.println("New Color"+colorPicker.getValue());
            awtColor = new java.awt.Color((float)colorPicker.getValue().getRed(),(float)colorPicker.getValue().getGreen(),(float)colorPicker.getValue().getBlue(),(float)colorPicker.getValue().getOpacity());
            //awtColor = new java.awt.Color(0,0,0,0);
            refreshImagePreview();
         }
    });

    submitButton.setOnAction((event)->{
        {
            new Thread(()->{
                submitButton.setDisable(true);
                System.out.println("Start");
                if(svgFile!=null)
                {
                    Platform.runLater(()->{
                        try{svgService.startConvert(bgColor.isSelected()?awtColor:null,svgFile,imageType.getSelectionModel().getSelectedItem().toString());}
                        catch (Exception t){System.out.println("Error Converting the SVG-File"+ t);}
                        submitButton.setDisable(false);
                        parallel.play();
                            }
                    );
  }
            }).start();
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
        svgFile = fileChooser.showOpenDialog(selectFile.getScene().getWindow());
        if (svgFile != null) {
            System.out.println("Filename:"+FilenameUtils.getExtension(svgFile.getName()));

            if(!FilenameUtils.getExtension(svgFile.getName()).equals("svg")){
                svgFile=null;
                filename.setText("invalid file!");
                submitButton.setDisable(true);
            }
            else {submitButton.setDisable(false);
            filename.setText(svgFile.getName());
            refreshImagePreview();

            }
        }

    }


    public void exitController() {
        //pdfService.stop();
        //logger.info(LocalDateTime.now().format(formatter)+" | "+"exitController");
    }

    public Button getSubmitButton(){
    return submitButton;
    }

    public void refreshImagePreview(){
        if(awtColor!=null && svgFile!=null){
        try{ Image image = SwingFXUtils.toFXImage(svgService.convertIt(bgColor.isSelected()?awtColor:null, svgFile), null);
            imagePreview.setImage(image);
        }
        catch(Exception e){System.out.println("Error Preview");} }
    }

    @FXML
    protected void checkBG(){
    colorPicker.setVisible(bgColor.isSelected());
    refreshImagePreview();
    }

}
