package com.ngntu10.dto.ws;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WsRequestBody {
    String from;

    String to;

    String content;

    String type;

    long date;
}