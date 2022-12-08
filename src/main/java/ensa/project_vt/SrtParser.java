package ensa.project_vt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SrtParser {
    private FileReader fr;
    private ArrayList<Caption> captions;
    private double videoLength;

    public ArrayList<Caption> getCaptions() {
        return captions;
    }

    public void setCaptions(ArrayList<Caption> captions) {
        this.captions = captions;
    }

    public double getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(double videoLength) {
        this.videoLength = videoLength;
    }

    @Override
    public String toString() {
        return "SrtParser{" +
                "fr=" + fr +
                ", captions=" + captions +
                ", videoLength=" + videoLength +
                '}';
    }

    public SrtParser(String filePath, double videoLength)  {
        fr=null;
        BufferedReader br = null;
        try
        {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss,SSS");
        String line;
        ArrayList<Caption> captions1 = new ArrayList<Caption>();
        ArrayList<String> arr = new ArrayList<String>();
        try
        {
            while((line=br.readLine())!=null)
            {
                if(!line.equals("")) arr.add(line);
                else
                {
                    Caption c = new Caption();
                    String startStamp = arr.get(1).substring(0,12);
                    String endStamp = arr.get(1).substring(17,29);

                    c.setStart(sdf.parse(startStamp).getTime());
                    c.setEnd(sdf.parse(endStamp).getTime());
                    String text="";
                    for (int i = 2; i < arr.size(); i++) {
                        text+=" "+arr.get(i);
                    }
                    c.setText(text);
                    System.out.println(c);
                    captions1.add(c);
                    arr = new ArrayList<String>();
                }
            }

        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        setCaptions(captions1);

    }
}
