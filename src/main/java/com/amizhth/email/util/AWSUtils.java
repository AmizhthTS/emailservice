package com.amizhth.email.util;

import java.io.InputStream;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amizhth.aws.AWSS3Utils;
import com.amizhth.email.dao.AWSConfigDao;
import com.amizhth.email.entitymodel.AWSConfigModel;

@Component
public class AWSUtils {
	
	
	@Autowired
	private com.amizhth.email.dao.AWSConfigDao awsConfigDao;

	
/*	public String saveImage(InputStream inputStream, String filename,String imageFormat,int tenantid,String module) {
		try
		{
			AWSConfigModel awsConfigModel = awsConfigDao.findByTenantid(tenantid);
			
			filename = TenantContext.getCurrentTenant() + module + new Random().nextInt(9999) + "-" + filename + imageFormat;
			
			boolean status = new AWSS3Utils().saveImage(inputStream, awsConfigModel.getAwsaccesskeyid(),
					awsConfigModel.getAwssecretkeyid(), awsConfigModel.getAwsregion(), awsConfigModel.getBucketname(), filename);
			
			if (status) 
				return filename;

			else
				throw new Exception("Image not uploaded");
		}
		catch(Exception ex)
		{
			System.out.println("Exception"+ex.getMessage());
			
			return "Image not uploaded";
		}

	} */

	public String saveFile(InputStream inputStream, String filename,String fileFormat,int tenantid,String module) {
		try 
		{
			AWSConfigModel awsConfigModel = awsConfigDao.findByTenantid(tenantid);
			String fileType = null ;
		//	 filename = TenantContext.getCurrentTenant() + module + new Random().nextInt(9999) +  "-" + filename +"."+ fileFormat;
			 
			 filename = tenantid + module + new Random().nextInt(9999) +  "-" + filename +"."+ fileFormat;
			 
			 if(fileFormat.equalsIgnoreCase("html"))
				 fileType = "text/html";
			 else if(fileFormat.equalsIgnoreCase("png"))
				 fileType = "image/png";
			 else if(fileFormat.equalsIgnoreCase("jpg"))
				 fileType = "image/jpg";
			 else if(fileFormat.equalsIgnoreCase("jpeg"))
				 fileType = "image/jpeg";
			 
			 System.out.println(fileType);
			boolean status = new AWSS3Utils().saveFile(inputStream, awsConfigModel.getAwsaccesskeyid(),
					awsConfigModel.getAwssecretkeyid(), awsConfigModel.getAwsregion(), awsConfigModel.getBucketname(), filename , fileType );

			if (status) 
				return filename;
			else
				throw new Exception(UtilConstants.FILENOTUPLOAD);
		}
		catch(Exception ex)
		{
			System.out.println("Exception"+ex.getMessage());

			return "";
		}

	}

}
