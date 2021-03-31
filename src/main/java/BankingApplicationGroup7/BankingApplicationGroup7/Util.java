package BankingApplicationGroup7.BankingApplicationGroup7;

import javax.servlet.http.HttpServletRequest;

public class Util {

    public static String getSiteURL(HttpServletRequest httpServletRequest) {
        String url = httpServletRequest.getRequestURL().toString();

        return url.replace(httpServletRequest.getServletPath(), "");
    }

}
