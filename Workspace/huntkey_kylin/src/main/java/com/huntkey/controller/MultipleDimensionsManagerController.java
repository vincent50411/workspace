package com.huntkey.controller;

import com.huntkey.config.ApplicationLocalProperties;
import com.huntkey.dto.HttpResultDto;
import com.huntkey.dto.KylinServerInfoDto;
import com.huntkey.dto.KylinProjectRequestDto;
import com.huntkey.service.MultipleDimensionsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuwens on 2017/8/7.
 */
@RestController
@RequestMapping("/huntkeyKylin")
public class MultipleDimensionsManagerController
{

    @Autowired
    @Qualifier(value = "MultipleDimensionsManagerServiceImpl")
    private MultipleDimensionsManagerService multipleDimensionsManagerService;

    @Autowired
    private ApplicationLocalProperties applicationProperties;

    /**
     * 单元测试用
     * @return
     */
    @RequestMapping("/test")
    public KylinServerInfoDto test()
    {
        return applicationProperties.getKylin();
    }


    @RequestMapping(value = "/project", method = RequestMethod.POST)
    public HttpResultDto saveKylinProject(@RequestBody KylinProjectRequestDto projectRequestDto)
    {
        HttpResultDto httpResultDto = new HttpResultDto();

        if (StringUtils.isEmpty(projectRequestDto.getFormerProjectName()))
        {
            httpResultDto.setResultStatus(false);
            httpResultDto.setResultMessage("A project name must be given to create a project");
        }

        httpResultDto = multipleDimensionsManagerService.saveKylinProject(projectRequestDto.getFormerProjectName(), projectRequestDto.getProjectDescData());

        return httpResultDto;
    }




}
