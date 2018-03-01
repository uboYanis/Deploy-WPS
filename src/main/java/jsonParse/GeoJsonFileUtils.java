package jsonParse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GeoJsonFileUtils {

	private static final String POINT = "Point";
	private static final String MULTIPOINT = "MultiPoint";
	private static final String LINESTRING = "LineString";
	private static final String MULTILINESTRING = "MultiLineString";
	private static final String POLYGON = "Polygon";
	private static final String MULTIPOLYGON = "MultiPolygon";

	public static String GeoJsonToString(File jsonFile)
			throws FileNotFoundException, IOException, ParseException, NullPointerException {

		JSONParser jsonParser = new JSONParser();
		String data = jsonParser.parse(new FileReader(jsonFile)).toString();

		return data;
	}

	public static boolean isGeometryData(File jsonFile) throws FileNotFoundException, IOException {
		try {
			String data = GeoJsonToString(jsonFile);
			JSONObject jsonData = new JSONObject(data);
			String type = jsonData.get("type").toString();
			return type.matches(POINT + "|" + MULTIPOINT + "|" + LINESTRING + "|" + MULTILINESTRING + "|" + POLYGON
					+ "|" + MULTIPOLYGON);
		} catch (ParseException e) {
			return false;
		} catch (JSONException e) {
			return false;
		}
	}

	public static boolean isFeatureCollectionData(File jsonFile) throws FileNotFoundException, IOException {

		try {
			String data = GeoJsonToString(jsonFile);
			JSONObject jsonData = new JSONObject(data);
			return jsonData.get("type").equals("FeatureCollection");
		} catch (ParseException e) {
			return false;
		} catch (JSONException e) {
			return false;
		}

	}
}