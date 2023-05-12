package common.logging.converter;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Iterator;

/**
 * @author T04856 <br>
 * @create 2023-05-12 9:35 AM <br>
 * @project project-cloud-custom <br>
 */
public class ErrorMarker implements Marker {

    public static final String NUM_1 = "1";
    public static final String NUM_2 = "2";
    public static final String NUM_3 = "3";
    public static final String NUM_4 = "4";
    public static final String INFRASTRUCTURE_ERROR = "Infrastructure Error";
    public static final String SYSTEM_ERROR = "System Error";
    public static final String BUSINESS_ERROR = "Business Error";
    public static final String SLIGHT_ERROR = "Slight Error";
    public static final String BUSINESS_ALARM = "Business Alarm";

    public static final ErrorMarker HIGH = new ErrorMarker(MarkerFactory.getMarker(NUM_1), INFRASTRUCTURE_ERROR);
    public static final ErrorMarker MEDIUM = new ErrorMarker(MarkerFactory.getMarker(NUM_2), SYSTEM_ERROR);
    public static final ErrorMarker LOW = new ErrorMarker(MarkerFactory.getMarker(NUM_3), BUSINESS_ERROR);
    public static final ErrorMarker SLIGHT = new ErrorMarker(MarkerFactory.getMarker(NUM_4), SLIGHT_ERROR);
    public static final ErrorMarker EMPTY = new ErrorMarker(MarkerFactory.getMarker(""), "");

    private final Marker baseMarker;
    private final String errorType;

    public ErrorMarker(Marker baseMarker, String errorType) {
        this.baseMarker = baseMarker;
        this.errorType = errorType;
    }

    @Override
    public String getName() {
        return this.baseMarker.getName();
    }

    @Override
    public void add(Marker reference) {
        this.baseMarker.add(reference);
    }

    @Override
    public boolean remove(Marker reference) {
        return this.baseMarker.remove(reference);
    }

    @Override
    public boolean hasChildren() {
        return this.baseMarker.hasChildren();
    }

    @Override
    public boolean hasReferences() {
        return this.baseMarker.hasReferences();
    }

    @Override
    public Iterator<Marker> iterator() {
        return this.baseMarker.iterator();
    }

    @Override
    public boolean contains(Marker other) {
        return this.baseMarker.contains(other);
    }

    @Override
    public boolean contains(String name) {
        return this.baseMarker.contains(name);
    }

    public String getErrorType() {
        return this.errorType;
    }
}
