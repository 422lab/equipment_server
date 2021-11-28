package com.kystu.equipment;

import com.mysql.cj.xdevapi.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class GetPostTools {
    public GetPostTools(HttpServletRequest req) throws IOException {
        this.req = req;
        if (req.getMethod().equals("POST")) {
            json = JsonParser.parseDoc(Tools.read(req.getReader()));
        }
    }

    private final HttpServletRequest req;

    private DbDoc json;

    public String getParameter(String key) {
        if (json != null) {
            JsonValue value = json.get(key);
            if (value == null) {
                return null;
            } else if (value instanceof JsonString) {
                return ((JsonString) value).getString();
            } else {
                return value.toFormattedString();
            }
        } else {
            return req.getParameter(key);
        }
    }

}
