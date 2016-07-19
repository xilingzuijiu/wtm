package com.weitaomi.systemconfig.util;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class AjaxUtil {

    private AjaxUtil() {
    }

    public static void rendText(HttpServletResponse response, String content)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(content);
    }

    public static void rendJson(HttpServletResponse response, String message) throws IOException {
        JSONObject json = new JSONObject();
        json.put("message", message);
        rendText(response, json.toString());
    }
}
