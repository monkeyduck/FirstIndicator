package com.caikang.com.tester;

import jxl.*;
import jxl.write.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader  
{   
    /***读取Excel*/  
    public static List<String[]> readExcel(String filePath)   
    {   
    	List<String[]> list = new ArrayList<String[]>();
        try  
        {   
            InputStream is = new FileInputStream(filePath);   
            Workbook rwb = Workbook.getWorkbook(is);   
            //Sheet的下标是从0开始   
            //获取第一张Sheet表   
            //Sheet rst = rwb.getSheet("2016-05-25");   
            Sheet rst = rwb.getSheet("Sheet1");   
            //获取Sheet表中所包含的总列数   
            int rsColumns = rst.getColumns();   
            //获取Sheet表中所包含的总行数   
            int rsRows = rst.getRows();   
            //获取指定单元格的对象引用      
            for (int i = 2; i <= 2851; i++) { 
            	String[] str = new String[3];
            	str[0] = rst.getCell(6,i).getContents();
    			str[1] = rst.getCell(4,i).getContents();
            	str[2] = rst.getCell(11,i).getContents();
            	//System.out.println(str[0] + " " + str[1] + " " + str[2]);
            	list.add(str);
            }         
            rwb.close();        	
        	
        }   
        catch(Exception e)   
        {   
            e.printStackTrace();   
        }   
        return list;
    }   
}