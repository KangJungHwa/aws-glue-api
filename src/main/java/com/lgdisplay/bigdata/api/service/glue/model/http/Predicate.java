package com.lgdisplay.bigdata.api.service.glue.model.http;

        import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
        import com.fasterxml.jackson.annotation.JsonProperty;
        import lombok.*;

        import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Predicate {
    @JsonProperty("Conditions")
    List<Condition> conditions;

    @JsonProperty("Logical")
    String logical;
}
