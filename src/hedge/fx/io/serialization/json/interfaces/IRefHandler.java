package hedge.fx.io.serialization.json.interfaces;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * An interface to avoid ugly boilerplate for reference handling using {@link com.fasterxml.jackson Jackson}
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public interface IRefHandler {
}