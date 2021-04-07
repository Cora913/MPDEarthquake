package org.me.gcu.mpdearthquake.xml;

import android.util.Log;

import org.me.gcu.mpdearthquake.models.EarthquakeItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class XMLParser {
    private ExecutorService urlReadThread;
    private ExecutorService xmlParseThread;

    public XMLParser() {
        this.urlReadThread = Executors.newSingleThreadExecutor();
        this.xmlParseThread = Executors.newSingleThreadExecutor();
    }

    public Future<String> readURL(String url) {
        return urlReadThread.submit(() -> {
            try {
                Log.e("XMLParser", String.format("Reading URL: %s", url));

                URL XMLUrl = new URL(url);
                URLConnection conn = XMLUrl.openConnection();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String data = "";
                String line;
                while ((line = buffer.readLine()) != null) {
                    data = data + line;
                }
                buffer.close();
                return data;

            } catch(IOException err) {
                Log.e("XMLParser", "Could not read URL content: " + err.toString());
                throw new Exception(err);
            }
        });
    }

    public Future<ArrayList<EarthquakeItem>> parseXML(String data) {
        return xmlParseThread.submit(() -> {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                String XMLdata = data.replace("null", "");
                xpp.setInput(new StringReader(XMLdata));
                int eventType;

                ArrayList<EarthquakeItem> items = new ArrayList<>();
                EarthquakeItem earthquake = null;

                while ((eventType = xpp.next()) != XmlPullParser.END_DOCUMENT) {
                    String tagName = xpp.getName();
                    if (eventType == XmlPullParser.END_TAG && tagName.equalsIgnoreCase("item")) {
                        items.add(earthquake);
                        earthquake = null;
                    }

                    if (eventType != XmlPullParser.START_TAG) {
                        continue;
                    }

                    if (tagName.equalsIgnoreCase ("item")) {
                        earthquake = new EarthquakeItem();
                        Log.e("XMLParser", "\t\tItem found");
                    }

                    if (earthquake != null) {
                        if (tagName.equalsIgnoreCase("title")) {
                            String text = xpp.nextText();
                            earthquake.setTitle(text);
                            Log.e("XMLParser", "Title found: " + text);
                        } else if (tagName.equalsIgnoreCase("description")) {
                            String text = xpp.nextText();
                            earthquake.setDescription(text);
                            Log.e("XMLParser", "Description found: " + text);

                            String[] parsedDescription = text.split(" ; ");
                            Map<String, String> kv = new HashMap<>();
                            for (String item : parsedDescription) {
                                String[] parsedItem = item.split(":");
                                kv.put(parsedItem[0], parsedItem[1].trim());
                            }
                            earthquake.setLocation(kv.get("Location"));
                            String[] parsedMagnitude = kv.get("Magnitude").trim().split(" ");
                            earthquake.setMagnitude(Float.parseFloat(parsedMagnitude[0]));
                            String[] parsedDepth = kv.get("Depth").trim().split(" ");
                            earthquake.setDepth(Float.parseFloat(parsedDepth[0]));
                        } else if (tagName.equalsIgnoreCase("link")) {
                            String text = xpp.nextText();
                            earthquake.setLink(text);
                            Log.e("XMLParser", "Link found: " + text);
                        } else if (tagName.equalsIgnoreCase("pubDate")) {
                            String text = xpp.nextText();
                            earthquake.setDate(text);
                            Log.e("XMLParser", "Date found: " + text);
                        } else if (tagName.equalsIgnoreCase("category")) {
                            String text = xpp.nextText();
                            earthquake.setCategory(text);
                            Log.e("XMLParser", "Category found: " + text);
                        } else if (tagName.equalsIgnoreCase("lat")) {
                            String text = xpp.nextText();
                            earthquake.setLat(Float.parseFloat(text));
                            Log.e("XMLParser", "Lat found: " + text);
                        } else if (tagName.equalsIgnoreCase("long")) {
                            String text = xpp.nextText();
                            earthquake.setLon(Float.parseFloat(text));
                            Log.e("XMLParser", "Long found: " + text);
                        }
                    }
                }
                return items;
            } catch(XmlPullParserException err) {
                Log.e("XMLParser", "XML could not be parsed: " + err.toString());
                throw new Exception(err);
            }
        });
    }
}
