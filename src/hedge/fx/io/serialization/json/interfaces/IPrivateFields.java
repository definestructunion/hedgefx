package hedge.fx.io.serialization.json.interfaces;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * An interface to avoid ugly boilerplate for private field JSON serialization using {@link com.fasterxml.jackson.annotation.JsonAutoDetect JsonAutoDetect}
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public interface IPrivateFields {
}
