package com.huntkey.main;



import java.io.*;
import java.net.URI;

/**
 * Created by liuwens on 2017/9/5.
 */
public class HDFSDemoApplication
{
    /*

    public static void main(String[] args)
    {

        createNewFile();

    }

    private static void createNewFile()
    {
        String dst = "hdfs://192.168.13.40:9000/user/root/test/test22.txt";


        try {
            Configuration conf = new Configuration();

            FileSystem fs = FileSystem.get(URI.create(dst), conf);

            fs.createNewFile(new Path(dst));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static void copyFileToHDFS()
    {
        String localSrc = "D:\\Test\\words.txt";
        String dst = "hdfs://192.168.13.40:9000/user/root/test/test1.txt";
        InputStream in = null;
        try {
            Configuration conf = new Configuration();

            in = new BufferedInputStream(new FileInputStream(localSrc));

            FileSystem fs = FileSystem.get(URI.create(dst), conf);
            OutputStream out = fs.create(new Path(dst), new Progressable()
            {
                public void progress()
                {
                    System.out.print(".");
                }
            });

            IOUtils.copyBytes(in, out, 4096, true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    */



}
