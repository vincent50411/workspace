package com.huntkey.multDimesions.service.http.edm;

import com.huntkey.multDimesions.dto.HttpRequestParamDTO;
import com.huntkey.multDimesions.dto.HttpResultDTO;
import com.huntkey.multDimesions.service.http.base.HttpRequestBaseService;
import org.springframework.stereotype.Service;

/**
 * Created by liuwens on 2017/8/8.
 */
@Service(value ="EDMRequestServiceImpl")
public class EDMRequestServiceImpl extends HttpRequestBaseService
{
    @Override
    public HttpResultDTO httpGet(HttpRequestParamDTO param) {
        return null;
    }

    @Override
    public HttpResultDTO httpDelete(HttpRequestParamDTO param)
    {
        return null;
    }

    @Override
    public HttpResultDTO httpPut(HttpRequestParamDTO param) {
        return null;
    }
}
