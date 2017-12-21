package com.google.zxing.client.android.result;

import com.google.zxing.client.result.GeoParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.example.user.lip.R;

import android.app.Activity;

/**
 * Handles geographic coordinates (typically encoded as geo: URLs).
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class GeoResultHandler extends ResultHandler {
  private static final int[] buttons = {
      R.string.button_show_map,
      R.string.button_get_directions
  };

  public GeoResultHandler(Activity activity, ParsedResult result) {
    super(activity, result);
  }

  @Override
  public int getButtonCount() {
    return buttons.length;
  }

  @Override
  public int getButtonText(int index) {
    return buttons[index];
  }

  @Override
  public void handleButtonPress(int index) {
    GeoParsedResult geoResult = (GeoParsedResult) getResult();
    switch (index) {
      case 0:
        openMap(geoResult.getGeoURI());
        break;
      case 1:
        getDirections(geoResult.getLatitude(), geoResult.getLongitude());
        break;
    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_geo;
  }
}
