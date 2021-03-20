package org.me.gcu.mpdearthquake;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EarthquakeListViewModel extends ViewModel {
    private MutableLiveData<ArrayList<EarthquakeItem>> earthquakes;
    private ExecutorService threadExecutor;
    private String url = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    public LiveData<ArrayList<EarthquakeItem>> getEarthquakes() {
        if (earthquakes == null) {
            earthquakes = new MutableLiveData<>();
            loadEarthquakes();
        }
        return earthquakes;
    }

    private void loadEarthquakes() {
        try {
            threadExecutor = Executors.newSingleThreadExecutor();
            ArrayList<EarthquakeItem> items = parseEarthquakes(url).get();
            earthquakes.setValue(items);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public Future<ArrayList<EarthquakeItem>> parseEarthquakes(String url) {
        return threadExecutor.submit(() -> {
            try {
                Log.e("EarthquakeListViewModel", "Parsing");
                URL XMLUrl = new URL(url);
                URLConnection conn = XMLUrl.openConnection();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String data = "";
                String line;
                while ((line = buffer.readLine()) != null) {
                    data = data + line;
                }
                buffer.close();

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
            } catch(IOException | XmlPullParserException err) {
                Log.e("XMLParser", "Parser Error: " + err.toString());
                throw new Exception(err);
            }
        });
    }
}
