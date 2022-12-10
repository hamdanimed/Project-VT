package ensa.project_vt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SrtParser {
    private FileReader fr;
    private HashMap<Integer,Caption> captions;
    private double videoLength;





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

    public HashMap<Integer, Caption> getCaptions() {
        return captions;
    }

    public void setCaptions(HashMap<Integer, Caption> captions) {
        this.captions = captions;
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
        HashMap<Integer,Caption> captions1 = new HashMap<>();
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
                    c.setId(Integer.parseInt(arr.get(0)));
                    c.setStart(sdf.parse(startStamp).getTime());
                    c.setEnd(sdf.parse(endStamp).getTime());
                    String text="";
                    for (int i = 2; i < arr.size(); i++) {
                        text+=" "+arr.get(i);
                    }
                    c.setText(text);
                    captions1.put(c.getId(),c);
                    arr = new ArrayList<String>();
                }
            }
            setCaptions(captions1);

        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        for (int i = 0; i < captions1.size(); i++) {
            System.out.println(captions1.get(i+1));
        }

    }
    public HashMap<Integer,Caption> find(double time)
    {
        Caption nextCaption=null;
        Caption caption=null;
        HashMap<Integer,Caption> result = new HashMap<>();
        for (int i = 0 ; i < captions.size(); i++) {
            if(time>=captions.get(i+1).getStart())
            {
                nextCaption =  captions.get(i+2);
                if(time<=captions.get(i+1).getEnd()){ caption = captions.get(i+1);break;}
            }
        }
        if(nextCaption==null) return null;
        if(caption==null) result.put(0,nextCaption);
        else result.put(1,caption);
        return result;
    }

}
