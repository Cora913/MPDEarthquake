/**
 * @author Andreea-Cora Tibuc-Boboc
 * @student_id S1703130
 */

package org.me.gcu.mpdearthquake.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import org.me.gcu.mpdearthquake.models.EarthquakeItem;
import org.me.gcu.mpdearthquake.R;

public class EarthquakeItemUI {

    @BindingAdapter({"label", "value"})
    public static void setFieldData(View view, String label, String value) {
        ((TextView) view.findViewById(R.id.labelText)).setText(label);
        ((TextView) view.findViewById(R.id.valueText)).setText(value);
    }

    public static void setLocationField(View view, EarthquakeItem earthquake) {
        TextView locationDataText = view.findViewById(R.id.locationText);
        String locationValue = earthquake.getLocation();
        locationDataText.setText(locationValue);
    }

    public static void setLatitudeField(View view, Context activity, EarthquakeItem earthquake) {
        View latitudeDataView = view.findViewById(R.id.latitude);
        String latitudeLabel = activity.getString(R.string.earthquake_lat_label);
        String latitudeValue = String.valueOf(earthquake.getLat());
        setFieldData(latitudeDataView, latitudeLabel, latitudeValue);
    }

    public static void setLongitudeField(View view, Context activity, EarthquakeItem earthquake) {
        View longitudeDataView = view.findViewById(R.id.longitude);
        String longitudeLabel = activity.getString(R.string.earthquake_long_label);
        String longitudeValue = String.valueOf(earthquake.getLon());
        setFieldData(longitudeDataView, longitudeLabel, longitudeValue);
    }

    public static void setDepthField(View view, Context activity, EarthquakeItem earthquake) {
        View depthDataView = view.findViewById(R.id.depth);
        String depthLabel = activity.getString(R.string.earthquake_depth_label);
        String depthValue = activity.getString(R.string.earthquake_depth_value, earthquake.getDepth());
        setFieldData(depthDataView, depthLabel, depthValue);
    }

    public static void setMagnitudeField(View view, Context activity, EarthquakeItem earthquake) {
        View magnitudeDataView = view.findViewById(R.id.magnitude);
        String magnitudeLabel = activity.getString(R.string.earthquake_magnitude_label);
        String magnitudeValue = activity.getString(R.string.earthquake_magnitude_value, earthquake.getMagnitude());
        setFieldData(magnitudeDataView, magnitudeLabel, magnitudeValue);
    }

    public static void setDateField(View view, Context activity, EarthquakeItem earthquake) {
        View dateDataView = view.findViewById(R.id.date);
        String dateLabel = activity.getString(R.string.earthquake_date_label);
        String dateValue = earthquake.getDateToString();
        setFieldData(dateDataView, dateLabel, dateValue);
    }

    public static void setCategoryField(View view, Context activity, EarthquakeItem earthquake) {
        View categoryDataView = view.findViewById(R.id.category);
        String categoryLabel = activity.getString(R.string.earthquake_category_label);
        String categoryValue = earthquake.getCategory();
        setFieldData(categoryDataView, categoryLabel, categoryValue);
    }

    public static void setBackground(View view, EarthquakeItem earthquake) {
        int red = 0x33ff6347;
        int yellow = 0x33ffcc00;
        int green = 0x3333cc33;

        View container = view.findViewById(R.id.earthquakeItem);
        GradientDrawable background = (GradientDrawable) container.getBackground();
        if (earthquake.getMagnitude() < 1.0) {
            background.setColor(green);
            background.setStroke(4, Color.argb(255, Color.red(green), Color.green(green), Color.blue(green)));
        }
        else if (earthquake.getMagnitude() >= 1.0 && earthquake.getMagnitude() < 3.0) {
            background.setColor(yellow);
            background.setStroke(4, Color.argb(255, Color.red(yellow), Color.green(yellow), Color.blue(yellow)));
        }
        else {
            background.setColor(red);
            background.setStroke(1, Color.argb(255, Color.red(red), Color.green(red), Color.blue(red)));
        }
    }
}
