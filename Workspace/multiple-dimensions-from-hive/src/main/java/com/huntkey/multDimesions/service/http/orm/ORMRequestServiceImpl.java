package com.huntkey.multDimesions.service.http.orm;

import com.huntkey.multDimesions.dto.HttpRequestParamDTO;
import com.huntkey.multDimesions.dto.HttpResultDTO;
import com.huntkey.multDimesions.service.http.base.HttpRequestBaseService;
import org.springframework.stereotype.Service;


/**
 * Created by liuwens on 2017/8/14.
 */
@Service(value ="ORMRequestServiceImpl")
public class ORMRequestServiceImpl extends HttpRequestBaseService
{

    @Override
    public HttpResultDTO httpGet(HttpRequestParamDTO param) {
        return null;
    }

    @Override
    public HttpResultDTO httpDelete(HttpRequestParamDTO param) {
        return null;
    }

    @Override
    public HttpResultDTO httpPut(HttpRequestParamDTO param) {
        return null;
    }
}
