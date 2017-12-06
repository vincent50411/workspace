package com.huntkey.multDimesions;

import com.huntkey.multDimesions.controller.MultipleDimensionsManagerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MultDimesionsApplication  implements CommandLineRunner
{
	private static Logger LOGGER = LoggerFactory.getLogger(MultDimesionsApplication.class);

	@Autowired
	private MultipleDimensionsManagerController multipleDimensionsManagerController;

	public static void main(String[] args) {
		SpringApplication.run(MultDimesionsApplication.class, args);
	}

	/**
	 * 应用程序入口
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception
	{
		/*
		 * 输入参数说明：[操作类型，资源类编码, 属性ID]
		 * 		操作类型：
		 * 			init 初始化kylin流程，包括创建project、load hive table、create new data model、create new cube、build cube、query cube、save cube result
		 * 			build 构建cube，只包括load hive table和从新构建build
		 *
		 *
		 */

		for (String inputParam : args )
		{
			LOGGER.info("--> inputParam:" + inputParam);
		}

		//操作类型：init、build
		String operaterType;

		//资源类编码
		String sourceClassCode;

		//资源属性ID
		String attributeID;

		if(args != null && args.length >= 3)
		{
			operaterType = args[0];
			sourceClassCode = args[1];
			attributeID = args[2];
		}
		else
		{
			LOGGER.info("--> 输入的参数不合法");
			return;
		}

		multipleDimensionsManagerController.init(sourceClassCode, attributeID);


	}




}
