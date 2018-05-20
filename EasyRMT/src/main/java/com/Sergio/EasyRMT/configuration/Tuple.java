package com.Sergio.EasyRMT.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tuple<X, Y> {
    private X key;
    private Y content;

    public Tuple(X key, Y content) {
        this.key = key;
        this.content = content;
    }
}
