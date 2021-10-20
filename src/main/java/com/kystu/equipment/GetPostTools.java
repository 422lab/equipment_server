package com.kystu.equipment;

import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.JsonParser;
import com.mysql.cj.xdevapi.JsonString;

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
            JsonString string = (JsonString) json.get(key);
            if (string == null) {
                return null;
            } else {
                return string.getString();
            }
        } else {
            return req.getParameter(key);
        }
    }

}
