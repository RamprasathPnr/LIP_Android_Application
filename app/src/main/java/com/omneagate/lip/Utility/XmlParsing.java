package com.omneagate.lip.Utility;

import android.util.Log;


import com.omneagate.lip.Model.AadhaarSeedingDto;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by user1 on 20/5/16.
 */
public class XmlParsing {

    AadhaarSeedingDto beneMemberUpdate;

    public AadhaarSeedingDto getXmlParsingValue(String xmlData) {
        try
        {
            String xmlRecords = xmlData;
            Log.e("members aadhar", "xmlRecords..." + xmlRecords);
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlRecords));
            Document dom = db.parse(is);
            NodeList l = dom.getElementsByTagName("PrintLetterBarcodeData");
            for (int j=0; j<l.getLength(); ++j) {
                Node prop = l.item(j);
                NamedNodeMap attr = prop.getAttributes();
                if (null != attr) {
                    //Node nodeUid = attr.getNamedItem("uid");
                    String uid = "", name = "", gender = "", yob = "", co = "", house = "", street = "", lm = "", loc = "", vtc = "", po = "", dist = "", subdist = "",
                            state = "", pc= "", dob = "";

                    try { uid = attr.getNamedItem("uid").getNodeValue(); } catch(Exception e) {}

                    try { name= attr.getNamedItem("name").getNodeValue(); } catch(Exception e) {}

                    try { gender= attr.getNamedItem("gender").getNodeValue(); } catch(Exception e) {}

                    try { yob = attr.getNamedItem("yob").getNodeValue(); } catch(Exception e) {}

                    try { co = attr.getNamedItem("co").getNodeValue(); } catch(Exception e) {}

                    try { house= attr.getNamedItem("house").getNodeValue(); } catch(Exception e) {}

                    try { street= attr.getNamedItem("street").getNodeValue(); } catch(Exception e) {}

                    try { lm= attr.getNamedItem("lm").getNodeValue(); } catch(Exception e) {}

                    try { loc= attr.getNamedItem("loc").getNodeValue(); } catch(Exception e) {}

                    try { vtc = attr.getNamedItem("vtc").getNodeValue(); } catch(Exception e) {}

                    try { po= attr.getNamedItem("po").getNodeValue(); } catch(Exception e) {}

                    try { dist= attr.getNamedItem("dist").getNodeValue(); } catch(Exception e) {}

                    try { subdist= attr.getNamedItem("subdist").getNodeValue(); } catch(Exception e) {}

                    try { state = attr.getNamedItem("state").getNodeValue(); } catch(Exception e) {}

                    try { pc = attr.getNamedItem("pc").getNodeValue(); } catch(Exception e) {}

                    try { dob = attr.getNamedItem("dob").getNodeValue(); } catch(Exception e) {}
                    beneMemberUpdate = new AadhaarSeedingDto();
                //  beneMemberUpdate.setAadhaarNum(Long.parseLong(uid));
                    beneMemberUpdate.setUid(uid);
                    beneMemberUpdate.setName(name);
                    beneMemberUpdate.setGender(gender.charAt(0));
                    beneMemberUpdate.setCo(co);
                    beneMemberUpdate.setHouse(house);
                    beneMemberUpdate.setStreet(street);
                    beneMemberUpdate.setLm(lm);
                    beneMemberUpdate.setLoc(loc);
                    beneMemberUpdate.setVtc(vtc);
                    beneMemberUpdate.setPo(po);
                    beneMemberUpdate.setDist(dist);
                    beneMemberUpdate.setSubdist(subdist);
                    beneMemberUpdate.setState(state);
                    beneMemberUpdate.setPc(pc);


                    beneMemberUpdate.setChannel("PUBLIC_APP");
                    if(!yob.equalsIgnoreCase("")) {
                        beneMemberUpdate.setYob(Long.parseLong(yob));

                    }
                    /*if(!dob.equalsIgnoreCase("")) {
                        SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date1 = sourceFormat.parse(dob);
                        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        String dateStr = targetFormat.format(date1);
                        Date date2 = targetFormat.parse(dateStr);
                        beneMemberUpdate.setDob(date2.getTime());

                    }
*/

                    try {
                        if (!dob.equalsIgnoreCase("")) {
                            SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date date1 = sourceFormat.parse(dob);
                            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                            String dateStr = targetFormat.format(date1);
                            Date date2 = targetFormat.parse(dateStr);
                            beneMemberUpdate.setDob(date2.getTime());
                        }
                    }
                    catch(Exception e) {
                        try {
                            if (!dob.equalsIgnoreCase("")) {
                                SimpleDateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
                                Date date1 = sourceFormat.parse(dob);
                                SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                String dateStr = targetFormat.format(date1);
                                Date date2 = targetFormat.parse(dateStr);
                                beneMemberUpdate.setDob(date2.getTime());
                            }
                        }
                        catch(Exception e2) {}
                    }

                    Log.e("UIDValue", uid);
                    Log.e("name", name);
                    Log.e("gender", gender);
                    Log.e("yob", yob);
                    Log.e("co", co);
                    Log.e("house", house);
                    Log.e("street", street);
                    Log.e("lm", lm);
                    Log.e("loc", loc);
                    Log.e("vtc", vtc);
                    Log.e("po", po);
                    Log.e("dist", dist);
                    Log.e("subdist", subdist);
                    Log.e("state", state);
                    Log.e("pc", pc);
                    Log.e("dob", dob);

                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("UIDValue : ").append(uid).append("\n");
                    stringBuffer.append("Name     : ").append(name).append("\n");
                    stringBuffer.append("Gender   : ").append(gender).append("\n");
                    stringBuffer.append("Yob      : ").append(yob).append("\n");
                    stringBuffer.append("Co       : ").append(co).append("\n");
                    stringBuffer.append("House    : ").append(house).append("\n");
                    stringBuffer.append("Street   : ").append(street).append("\n");
                    stringBuffer.append("Lm       : ").append(lm).append("\n");
                    stringBuffer.append("LOC      : ").append(loc).append("\n");
                    stringBuffer.append("VTC      : ").append(vtc).append("\n");
                    stringBuffer.append("PO       : ").append(po).append("\n");
                    stringBuffer.append("Dist     : ").append(dist).append("\n");
                    stringBuffer.append("SubDist  : ").append(subdist).append("\n");
                    stringBuffer.append("State    : ").append(state).append("\n");
                    stringBuffer.append("Pc       : ").append(pc).append("\n");
                    stringBuffer.append("Dob       : ").append(dob).append("\n");

                    Log.e("Xml Records", xmlRecords);
                    Log.e(">>>>>>>>>>", ">>>>>>>>>");
                    Log.e("Details", stringBuffer.toString());
                }
            }
        }
        catch (Exception e)
        {
            Log.e("Exception", e.toString());
        }

        return beneMemberUpdate;
    }


    public AadhaarSeedingDto getStringParsing(String text) {
        try{
            String uid = "", name = "", gender = "", yob = "", co = "", house = "", street = "", lm = "", loc = "", vtc = "", po = "", dist = "", subdist = "",
                    state = "", pc= "", dob = "";
            String[] strArr = text.split(",");
            for(int i=0;i<strArr.length;i++) {
                try {
                    Log.e("mara", "strArr contents" + strArr[i].toString());
                    String element = strArr[i].toString();
                    String[] strArr2 = element.split(":");

                    if(strArr2[0].equalsIgnoreCase(" aadhaar no")) {
                        uid = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" Name")) {
                        name = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" Gender")) {
                        gender = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" YOB")) {
                        yob = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" co")) {
                        co = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" house")) {
                        house = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" street")) {
                        street = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" lmark")) {
                        lm = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" loc")) {
                        loc = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" vtc")) {
                        vtc = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" po")) {
                        po = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" dist")) {
                        dist = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" subdist")) {
                        subdist = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" state")) {
                        state = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" pc")) {
                        pc = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase(" DOB")) {
                        dob = strArr2[1];
                    }

                    if(strArr2[0].equalsIgnoreCase("aadhaar no")) {
                        uid = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("Name")) {
                        name = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("Gender")) {
                        gender = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("YOB")) {
                        yob = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("co")) {
                        co = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("house")) {
                        house = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("street")) {
                        street = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("lmark")) {
                        lm = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("loc")) {
                        loc = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("vtc")) {
                        vtc = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("po")) {
                        po = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("dist")) {
                        dist = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("subdist")) {
                        subdist = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("state")) {
                        state = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("pc")) {
                        pc = strArr2[1];
                    }
                    if(strArr2[0].equalsIgnoreCase("DOB")) {
                        dob = strArr2[1];
                    }
                }
                catch(Exception e) {
                    Log.e("mara", "string exception" + e);
                }
            }

            beneMemberUpdate = new AadhaarSeedingDto();
            beneMemberUpdate.setUid(uid);
            beneMemberUpdate.setName(name);
            beneMemberUpdate.setGender(gender.charAt(0));
            beneMemberUpdate.setCo(co);
            beneMemberUpdate.setHouse(house);
            beneMemberUpdate.setStreet(street);
            beneMemberUpdate.setLm(lm);
            beneMemberUpdate.setLoc(loc);
            beneMemberUpdate.setVtc(vtc);
            beneMemberUpdate.setPo(po);
            beneMemberUpdate.setDist(dist);
            beneMemberUpdate.setSubdist(subdist);
            beneMemberUpdate.setState(state);
            beneMemberUpdate.setPc(pc);
            beneMemberUpdate.setChannel("PUBLIC_APP");
            if(!yob.equalsIgnoreCase("")) {
                beneMemberUpdate.setYob(Long.parseLong(yob));
            }
            try {
                if (!dob.equalsIgnoreCase("")) {
                    SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date1 = sourceFormat.parse(dob);
                    SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    String dateStr = targetFormat.format(date1);
                    Date date2 = targetFormat.parse(dateStr);
                    beneMemberUpdate.setDob(date2.getTime());
                }
            }
            catch(Exception e) {
                try {
                    if (!dob.equalsIgnoreCase("")) {
                        SimpleDateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date date1 = sourceFormat.parse(dob);
                        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        String dateStr = targetFormat.format(date1);
                        Date date2 = targetFormat.parse(dateStr);
                        beneMemberUpdate.setDob(date2.getTime());
                    }
                }
                catch(Exception e2) {}
            }

            Log.e("UIDValue", uid);
            Log.e("name", name);
            Log.e("gender", gender);
            Log.e("yob", yob);
            Log.e("co", co);
            Log.e("house", house);
            Log.e("street", street);
            Log.e("lm", lm);
            Log.e("loc", loc);
            Log.e("vtc", vtc);
            Log.e("po", po);
            Log.e("dist", dist);
            Log.e("subdist", subdist);
            Log.e("state", state);
            Log.e("pc", pc);
            Log.e("dob", dob);

        }catch (Exception e){
            Log.e("Exception  Aadhaar Seeding String format", e.toString());
        }
      return beneMemberUpdate;
    }

}
