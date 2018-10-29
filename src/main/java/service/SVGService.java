package service;


import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.keys.PaintKey;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SVGService {

    public SVGService() {
 }

    public void startConvert(Color color)throws TranscoderException,IOException {

        ByteArrayOutputStream resultByteStream = new ByteArrayOutputStream();
//https://upload.wikimedia.org/wikipedia/commons/0/02/SVG_logo.svg
        TranscoderInput transcoderInput = new TranscoderInput("http://demo2.bucons.com/ycolor/img/kapsch_logo.svg");
        TranscoderOutput transcoderOutput = new TranscoderOutput(resultByteStream);

        PNGTranscoder pngTranscoder = new PNGTranscoder();


        pngTranscoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR,
                color);

        pngTranscoder.transcode(transcoderInput,transcoderOutput);

        BufferedImage bim = ImageIO.read(new ByteArrayInputStream(resultByteStream.toByteArray()));

        File outputfile = new File("image.png");

        ImageIO.write(bim, "png", outputfile);


        resultByteStream.flush();

        System.out.println("Test done");


    }
}
