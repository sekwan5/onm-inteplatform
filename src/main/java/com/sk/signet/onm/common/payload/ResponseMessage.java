package com.sk.signet.onm.common.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseMessage {
    public String responseType;
    public String responseCode;
    public String responseLogcd;
    public String responseTitle;
    public String responseBasc;
    public String responseDtal;
}