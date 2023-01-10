package ensa.project_vt;

import ensa.project_vt.Caption;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SrtParser {
    private FileReader fr;
    private HashMap<Integer, Caption> captions;
    private double videoLength;
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
            System.out.println("can't read file");
            captions = new HashMap<>();
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
                    Caption c = makeCaption(arr,sdf);
                    captions1.put(c.getId(),c);
                    arr = new ArrayList<String>();
                }

            }
            Caption c = makeCaption(arr,sdf);
            captions1.put(c.getId(),c);
            setCaptions(captions1);
            System.out.println(captions1);

        }
        catch (Exception e)
        {
            System.out.println("file format is erroneous");
            captions = new HashMap<>();
        }

    }
    public String format()
    {
        String content="";
        for (int i = 0; i < captions.size(); i++) {
            content+=captions.get(i).getId()+1+"\n";
            content+=timeString(captions.get(i).getStart())+" --> "+timeString(captions.get(i).getEnd())+"\n";
            content+=captions.get(i).getText()+"\n";
            if(i!=captions.size()-1) content+="\n";
        }
        return content;
    }
    public void editCaption(String text,int i)
    {
        System.out.println("i :" +i);
        captions.get(i).setText(text);
    }
    public Caption makeCaption(ArrayList<String> arr,SimpleDateFormat sdf)
    {
        Caption c = new Caption(0);
        String startStamp = arr.get(1).substring(0,12);
        String endStamp = arr.get(1).substring(17,29);

        c.setId(Integer.parseInt(arr.get(0))-1);

        try {
            c.setStart(sdf.parse(startStamp).getTime());
        } catch (ParseException e) {
            System.out.println("can't format start");
        }
        try {
            c.setEnd(sdf.parse(endStamp).getTime());
        } catch (ParseException e) {
            System.out.println("can't format end");
        }
        String text="";
        for (int i = 2; i < arr.size()-1; i++) {
            text+=arr.get(i)+"\n";
        }
        text+=arr.get(arr.size()-1);
        c.setText(text);
        return c;
    }
    public HashMap<Integer,Caption> search(double time,int start,int end)
    {
            HashMap<Integer,Caption> result = new HashMap<>();
            Caption c=null;
            Caption nc=null;
            if(start>end) return null;
            if(start==end)
            {
                result.put(1,captions.get(start));
                return result;
            }
            int m=(start+end)/2;
            c = captions.get(m);
            if(captions.containsKey(m+1)) nc=captions.get(m+1);
            if(c.getStart()<=time && c.getEnd()>=time)
            {
                result.put(1,c);
                return result;
            }
            if(nc==null)
            {
                result.put(0,null);
                return result;
            }
            if(c.getEnd()<time && nc.getStart()>time)
            {
                result.put(0,nc);
                return result;
            }
            if(time>c.getStart()) return search(time,m,end);
            return search(time,start,m);

    }
    public HashMap<Integer,Caption> find(double time)
    {
        Caption nextCaption=captions.get(1);
        Caption caption=null;
        HashMap<Integer,Caption> result = new HashMap<>();
        for (int i = 0 ; i < captions.size(); i++) {
            if(time>=captions.get(i+1).getStart())
            {
                if(captions.containsKey(i+2)) nextCaption =  captions.get(i+2);
                else nextCaption = null;
                if(time<=captions.get(i+1).getEnd()){ caption = captions.get(i+1);break;}
            }
        }
        if(nextCaption==null) return null;
        if(caption==null) result.put(0,nextCaption);
        else result.put(1,caption);
        return result;
    }
    public String timeString(double time)
    {
        String seconds = String.format("%02d",(int) (time/1000) %60);
        String minutes = String.format("%02d",(int) ((time/(1000*60)) %60)) ;
        String hours = String.format("%02d",(int) ((time/(1000*60*60)) %24)) ;
        String millis = String.format("%03d",(int)time%1000);
        return hours+":"+minutes+":"+seconds+","+millis;

    }

}
