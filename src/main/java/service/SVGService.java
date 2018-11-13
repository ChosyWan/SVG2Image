package service;


import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;
import org.apache.batik.transcoder.keys.PaintKey;
import view.Controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class SVGService {

    private Controller controller;

    static Integer count  = 0;

    public SVGService(Controller controller){
        this.controller = controller;
    }

    public SVGService() {
 }

    public BufferedImage convertIt(Color color,File svgFile) throws TranscoderException,IOException{

        ByteArrayOutputStream resultByteStream = new ByteArrayOutputStream();

        TranscoderInput transcoderInput = new TranscoderInput(new FileInputStream(svgFile));
        TranscoderOutput transcoderOutput = new TranscoderOutput(resultByteStream);

        PNGTranscoder pngTranscoder = new PNGTranscoder();

        if(color!=null) pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR,color);

        pngTranscoder.transcode(transcoderInput,transcoderOutput);

        BufferedImage bim = ImageIO.read(new ByteArrayInputStream(resultByteStream.toByteArray()));

        return bim;

    }

    public void startConvert(Color color,File svgFile,String filetype)throws TranscoderException,IOException {

        ByteArrayOutputStream resultByteStream = new ByteArrayOutputStream();

        TranscoderInput transcoderInput = new TranscoderInput(new FileInputStream(svgFile));
        TranscoderOutput transcoderOutput = new TranscoderOutput(resultByteStream);

        Transcoder transcoder= new JPEGTranscoder();
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,1f);

        switch(filetype){
            case "PNG":
            case "png":     transcoder= new PNGTranscoder();
                            break;
            case "TIF": transcoder= new TIFFTranscoder();break;

        }

        if(color!=null) {transcoder.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR,color);}
        //else if(filetype!="png") {transcoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR,Color.WHITE);}



        transcoder.transcode(transcoderInput,transcoderOutput);

        BufferedImage bim = ImageIO.read(new ByteArrayInputStream(resultByteStream.toByteArray()));


        //System.out.println("filetype"+filetype);

        try {
            File outputfile = new File("image"+count+"." + filetype);
            ImageIO.write(bim, filetype, outputfile);
            count++;

        }catch(IOException e){System.out.println("Error writing image file:"+e);}

        System.out.println("Test done");
        resultByteStream.flush();
        resultByteStream.close();
        transcoderInput.getInputStream().close();

    }
}
