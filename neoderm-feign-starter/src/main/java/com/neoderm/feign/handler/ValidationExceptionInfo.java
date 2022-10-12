package com.neoderm.feign.handler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationExceptionInfo implements Serializable {

    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("error")
    private String error;
    @JsonProperty("errors")
    private List<ErrorsDTO> errors;
    @JsonProperty("message")
    private String message;
    @JsonProperty("path")
    private String path;
    @JsonProperty("respCode")
    private Integer respCode;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorsDTO implements Serializable {
        @JsonProperty("codes")
        private List<String> codes;
        @JsonProperty("arguments")
        private List<ArgumentsDTO> arguments;
        @JsonProperty("defaultMessage")
        private String defaultMessage;
        @JsonProperty("objectName")
        private String objectName;
        @JsonProperty("field")
        private String field;
        @JsonProperty("rejectedValue")
        private String rejectedValue;
        @JsonProperty("bindingFailure")
        private Boolean bindingFailure;
        @JsonProperty("code")
        private String code;

        @Data
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ArgumentsDTO implements Serializable {
            @JsonProperty("codes")
            private List<String> codes;
            @JsonProperty("arguments")
            private Object arguments;
            @JsonProperty("defaultMessage")
            private String defaultMessage;
            @JsonProperty("code")
            private String code;
        }
    }
}
