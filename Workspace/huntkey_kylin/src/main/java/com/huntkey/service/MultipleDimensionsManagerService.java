package com.huntkey.service;

import com.huntkey.dto.HttpResultDto;

/**
 * Created by liuwens on 2017/8/7.
 */
public interface MultipleDimensionsManagerService
{

    boolean kylinAuthorization();

    HttpResultDto saveKylinProject(String formerProjectName, String projectDescData);

}
