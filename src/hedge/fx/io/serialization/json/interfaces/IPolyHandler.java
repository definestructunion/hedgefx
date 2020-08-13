package hedge.fx.io.serialization.json.interfaces;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An interface to avoid ugly boilerplate for polymorphic de/serialization using {@link com.fasterxml.jackson Jackson}
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="@class")
public interface IPolyHandler {
}
