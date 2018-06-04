package bindong.utils.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import java.io.*;
import java.util.List;

/**
 * Created by xubanxian on 17-6-15.
 * more comments,less complains.
 */
public class AwsS3Helper {
    /**
     * @param args
     */
    public static void main(String[] args) {
		/*
		 * long startTime = System.nanoTime(); long endTime = System.nanoTime();
		 * if(endTime - startTime > 0) {System.out.println(">");} else
		 * System.out.println("<");;
		 */
        /**
         * ns（nanosecond）：纳秒， 时间单位。一秒的10亿分之一，即等于10的负9次方秒。常用作
         * 内存读写速度的单位，其前面数字越小则表示速度越快。 1纳秒=1000 皮秒 1纳秒 =0.001 微秒 1纳秒=0.000001 毫秒
         * 1纳秒=0.00000 0001秒
         *
         * java的System.currentTimeMillis()和System.nanoTime()有什么区别
         *
         * java中System.nanoTime()返回的是纳秒，nanoTime而返回的可能是任意时间，甚至可能是负数……按照API的说明，nanoTime主要的用途是衡量一个时间段，比如说一段代码执行所
         * 用的时间，获取数据库连接所用的时间，网络访问所用的时间等。另外，nanoTime提供了纳秒级别的精度，但实际上获得的值可能没有精确到纳秒。
         *
         * 但总的来说，这两个函数的用途是完全不一样的！。
         *
         * java中System.currentTimeMillis()返回的毫秒，这个毫秒其实就是自1970年1月1日0时起的毫秒数，Date()其实就是相当于Date(System.currentTimeMillis());因为Date类还有构造Date(long
         * date)，用来计算long秒与1970年1月1日之间的毫秒差。。
         */
		/* System.out.println(System.currentTimeMillis()); */
        System.out.println("------------------main() start--------------------------");
		/*
		 * 通过本地的配置文件，生成credentials文件
		 */
        AWSCredentials awsCredentials = null;
        awsCredentials = new ProfileCredentialsProvider("default").getCredentials();
        // 生成amazons3Client object
        AmazonS3 s3 = new AmazonS3Client(awsCredentials);
        Region jp = Region.getRegion(Regions.AP_NORTHEAST_1);
        s3.setRegion(jp);
        //System.out.println(s3.getRegionName());
        //Owner s3owner = s3.getS3AccountOwner();
        //System.out.println("owner: " + s3owner);
        System.out.println("===========================================================");
        System.out.println("Getting Started with Amazon S3");
        System.out.println("===========================================================");
        //列出现有的bucket
        //listBucket(s3);
        //生成随机的bucketname
        //String bucketName = "xu-test" + UUID.randomUUID();
        String bucketName = "xu-testee6c45b5-5b5f-4745-a795-ea70a485d686";
        //key
        String key = "XuObjectKey";
        //本地文件目录
        String filePath = "/home/xxh/tempfile/dd";
        //文件对像
        File file = new File(filePath);
        //创建bucket
        /*try {
            System.out.println("Create bucket: " + bucketName);
            s3.createBucket(bucketName);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("create Bucket error.......");
            e.printStackTrace();
        }*/
        //再次列出所有的bucket
        //listBucket(s3);
        //上传文件对象并加密
        if (s3.doesBucketExist(bucketName)) {
            try {
                PutObjectRequest putRequest = new PutObjectRequest(bucketName,key,file);
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
                putRequest.setMetadata(objectMetadata);
                PutObjectResult response = s3.putObject(putRequest);
                System.out.println("Uploaded object encrytion status is: "  + response.getSSEAlgorithm());
                //s3.putObject(bucketName, key, file);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        //列出所有的object
        /*try {
            listObject(s3, bucketName, key);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }*/
        try {
            getObject(s3, bucketName, key);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //测试： 新建bucket、object后先不删除，观察其他没有权限的用户能否看到object的效果
		/*//删除刚才上传的对象
		//System.out.println("Deleting an object\n");
		//s3.deleteObject(bucketName, key);
		//删除刚才新建的bucket
		//System.out.println("Deleting bucket " + bucketName + "\n");
        //s3.deleteBucket(bucketName);
*/
        System.out.println("-------------------main()end------------------------");
    }

    private static void listBucket(AmazonS3 s3) {
        System.out.println("List all bucket...");
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(" - " + bucket.getName());
        }
    }

    private static void listObject(AmazonS3 s3, String bucketName, String key) {
        System.out.println("list all object in the bucket...");
        ObjectListing objectList = s3.listObjects(bucketName);
        List<S3ObjectSummary> objectListSums = objectList.getObjectSummaries();
        for (S3ObjectSummary s3ObjectSummary : objectListSums) {
            GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest(bucketName, key);
            ObjectMetadata metadata = s3.getObjectMetadata(getObjectMetadataRequest);
            System.out.println(" - " + s3ObjectSummary.getKey() + "  " + "(size = " + s3ObjectSummary.getSize() + ")");
            System.out.println("Encryption algorithm used:"+metadata.getSSEAlgorithm());
            System.out.println();
        }
        System.out.println();
    }

    private static void getObject(AmazonS3 s3, String bucketName, String key) throws IOException {
        System.out.println("Downloading the object: " + key);
        S3Object object = s3.getObject(bucketName, key);
        System.out.println("download ok-----------------");
        //System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());
        //displayTextInputStream(object.getObjectContent());
    }

    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;
            System.out.println("    " + line);
        }
        System.out.println();
    }

}
