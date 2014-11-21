package barnes.matt.tallpinesrally;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import android.content.Context;
//import android.util.Log;

public class XMLParser {

    Context context;

    public XMLParser()
    {

    }

    public XMLParser(Context _context)
    {
        context = _context;
    }

    public String getRSSFromUrl(String url) {
        String xml = null;

        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            //HttpPost httpPost = new HttpPost(url);
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }

    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }


    public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            //clean the BOM or any other junk out from the start of the xml
            if (!xml.startsWith("<"))
            {
                xml = xml.substring(xml.indexOf("<"), xml.length());
            }

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            //Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            //Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            //Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public int getIntValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        int value = 0;
        //if (this.getElementValue(n.item(0)).equals(""))
        //{
        try{
            value += Integer.parseInt(this.getElementValue(n.item(0)));
        }
        catch (Exception e)
        {

        }
        //}
        //else
        //{
        return value;
        //}
    }

    public int getImage(Element item, String str){

        NodeList n = item.getElementsByTagName(str);
        int value = 0;
        //if (this.getElementValue(n.item(0)).equals(""))
        //{
        value += context.getResources().getIdentifier(this.getElementValue(n.item(0)), null, context.getPackageName());
        //}
        //else
        //{
        return value;
        //}

    }

    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
    public String convertStreamToString(InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
}


//
//
////All static variables
//static final String URL = "http://api.androidhive.info/pizza/?format=xml";
////XML node keys
//static final String KEY_ITEM = "item"; // parent node
//static final String KEY_NAME = "name";
//static final String KEY_COST = "cost";
//static final String KEY_DESC = "description";
//
//XMLParser parser = new XMLParser();
//String xml = parser.getXmlFromUrl(URL); // getting XML
//Document doc = parser.getDomElement(xml); // getting DOM element
//
//NodeList nl = doc.getElementsByTagName(KEY_ITEM);
//
////looping through all item nodes <item>
//for (int i = 0; i < nl.getLength(); i++) {
// String name = parser.getValue(e, KEY_NAME); // name child value
// String cost = parser.getValue(e, KEY_COST); // cost child value
// String description = parser.getValue(e, KEY_DESC); // description child value
//}
//Parsing XML data and updating into ListView
//In my previous tutorial Android ListView Tutorial i explained how to create listview and updating with list data. Below i am implementing same listview but the list data i am updating is from parsed xml. This ListView has multiple sub text like name, cost and description.
//
//public class AndroidXMLParsingActivity extends ListActivity {
//
// // All static variables
// static final String URL = "http://api.androidhive.info/pizza/?format=xml";
// // XML node keys
// static final String KEY_ITEM = "item"; // parent node
// static final String KEY_ID = "id";
// static final String KEY_NAME = "name";
// static final String KEY_COST = "cost";
// static final String KEY_DESC = "description";
//
// @Override
// public void onCreate(Bundle savedInstanceState) {
//     super.onCreate(savedInstanceState);
//     setContentView(R.layout.main);
//
//     ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
//
//     XMLParser parser = new XMLParser();
//     String xml = parser.getXmlFromUrl(URL); // getting XML
//     Document doc = parser.getDomElement(xml); // getting DOM element
//
//     NodeList nl = doc.getElementsByTagName(KEY_ITEM);
//     // looping through all item nodes <item>
//     for (int i = 0; i < nl.getLength(); i++) {
//         // creating new HashMap
//         HashMap<String, String> map = new HashMap<String, String>();
//         Element e = (Element) nl.item(i);
//         // adding each child node to HashMap key => value
//         map.put(KEY_ID, parser.getValue(e, KEY_ID));
//         map.put(KEY_NAME, parser.getValue(e, KEY_NAME));
//         map.put(KEY_COST, "Rs." + parser.getValue(e, KEY_COST));
//         map.put(KEY_DESC, parser.getValue(e, KEY_DESC));
//
//         // adding HashList to ArrayList
//         menuItems.add(map);
//     }
//
//     // Adding menuItems to ListView
//     ListAdapter adapter = new SimpleAdapter(this, menuItems,
//             R.layout.list_item,
//             new String[] { KEY_NAME, KEY_DESC, KEY_COST }, new int[] {
//                     R.id.name, R.id.desciption, R.id.cost });
//
//     setListAdapter(adapter);
//
//     // selecting single ListView item
//     ListView lv = getListView();
//             // listening to single listitem click
//     lv.setOnItemClickListener(new OnItemClickListener() {
//
//         @Override
//         public void onItemClick(AdapterView<?> parent, View view,
//                 int position, long id) {
//             // getting values from selected ListItem
//             String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
//             String cost = ((TextView) view.findViewById(R.id.cost)).getText().toString();
//             String description = ((TextView) view.findViewById(R.id.desciption)).getText().toString();
//
//             // Starting new intent
//             Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
//             in.putExtra(KEY_NAME, name);
//             in.putExtra(KEY_COST, cost);
//             in.putExtra(KEY_DESC, description);
//             startActivity(in);
//
//         }
//     });
// }
//}