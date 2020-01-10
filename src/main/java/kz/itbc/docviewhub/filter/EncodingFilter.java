package kz.itbc.docviewhub.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kz.itbc.docviewhub.constant.AppConstant.UTF_8_CHARSET;

public class EncodingFilter extends HttpFilter {
    private static final String encoding = UTF_8_CHARSET;

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws
            IOException, ServletException {
        req.setCharacterEncoding(encoding);
        chain.doFilter(req, res);
    }
}
