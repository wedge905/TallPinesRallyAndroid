/* Copyright 2013 Google Inc.
   Licensed under Apache 2.0: http://www.apache.org/licenses/LICENSE-2.0.html */

package barnes.matt.tallpinesrally.MapHelpers;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.util.Property;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerAnimation {
    public static void animateMarkerTo(Marker marker, LatLng finalPosition, final LatLngInterpolator latLngInterpolator, int duration) {
        if (duration > 10000)
            duration = 10000;

        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                return latLngInterpolator.interpolate(fraction, startValue, endValue);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        animator.setDuration(duration);
        animator.start();
    }
}