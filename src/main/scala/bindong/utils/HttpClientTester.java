package bindong.utils;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xubanxian on 17-7-10.
 * more comments,less complains.
 */
public class HttpClientTester {
    public static void main(String[] args){
        new HttpClientTester().post();
    }
    public void post() {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://localhost:9090/dlo3dScan/measurementItems/uploadMeasurementItems");
        // 创建参数队列
        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
        formparams.add(new BasicNameValuePair("sign", "e5fc1081abf31be94349f948f5cbe89a"));
        formparams.add(new BasicNameValuePair("data", "2017-07-07 12:16:45||TEST021_00463||epoque||{customer_name:,customer_age:0,customer_sex:1,customer_native_place:,customer_height:0,customer_phone_number:,customer_email:,customer_weixin:,advice:,}||74||{foot_length_original: { left:237.655, right:234.954},foot_length: { left:240, right:235},first_metatarsophalangeal_convex_length: { left:216, right:212},fifth_metatarsophalangeal_convex_length: { left:187, right:183},first_metatarsophalangeal_joint_length: { left:174, right:171},fifth_metatarsophalangeal_joint_length: { left:152, right:149},tarsal_bone_length: { left:133, right:130},waist_length: { left:98, right:96},heel_heart_length: { left:43, right:42},waist_girth_length: { left:109, right:106},back_girth_length: { left:82, right:81},metatarsophalangeal_length: { left:202, right:198},heel_up_edge_distance: { left:14.7488, right:14.3202},first_metatarsophalangeal_convex_width: { left:33.8622, right:34.7062},fifth_metatarsophalangeal_convex_width: { left:44.7116, right:46.8519},first_metatarsophalangeal_joint_width: { left:40.0934, right:37.2078},fifth_metatarsophalangeal_joint_width: { left:48.6759, right:51.2321},base_width: { left:88.7693, right:88.4399},metatarsophalangeal_width: { left:70.5417, right:68.6505},waist_width: { left:38.371, right:40.0016},heel_heart_width: { left:62.914, right:61.7074},first_metatarsophalangeal_convex_footprint_width: { left:32.1365, right:33.8747},fifth_metatarsophalangeal_convex_footprint_width: { left:43.4539, right:45.1525},first_metatarsophalangeal_joint_footprint_width: { left:36.7173, right:34.9915},fifth_metatarsophalangeal_joint_footprint_width: { left:47.7363, right:49.6236},waist_footprint_width: { left:35.9492, right:37.5497},first_metatarsophalangeal_convex_edge_width: { left:1.72565, right:0.831463},fifth_metatarsophalangeal_convex_edge_width: { left:1.25771, right:1.69936},first_metatarsophalangeal_joint_edge_width: { left:3.37606, right:2.21638},fifth_metatarsophalangeal_joint_edge_width: { left:0.939629, right:1.60851},waist_edge_width: { left:2.42173, right:2.45183},heel_heart_inside_edge_width: { left:3.6869, right:4.24852},heel_heart_outside_edge_width: { left:3.46997, right:3.89984},foot_arch_param: { left:0.498346, right:0.489303},thumb_top_height: { left:14.5085, right:14.4051},first_metatarsophalangeal_joint_height: { left:36.4059, right:38.1245},tarsal_bone_height: { left:51.3987, right:52.9115},lateral_malleolus_height: { left:39.2811, right:39.6084},metatarsophalangeal_girth: { left:158.559, right:157.402},plantar_girth: { left:217.049, right:219.738},waist_girth: { left:223.24, right:230.943},tarsal_girth: { left:220.492, right:223.653},back_girth: { left:231.24, right:233.035},pocket_heel_girth: { left:307.454, right:303.96},ankle_girth: { left:208.944, right:207.145},calf_height: { left:0, right:0},below_knee_height: { left:0, right:0},calf_girth: { left:0, right:0},below_knee_girth: { left:0, right:0},arch_top_height: { left:0, right:0},arch_top_width: { left:0, right:0},heel_inside_convex_length: { left:0, right:0},heel_outside_convex_length: { left:0, right:0},heel_convex_width: { left:0, right:0},heel_inside_convex_width: { left:0, right:0},heel_outside_convex_width: { left:0, right:0},heel_gap: { left:4.61098, right:5.77577},heel_gap_tolerance: { left:2.30549, right:2.88788},heel_inside_convex_edge_width: { left:0, right:0},heel_outside_convex_edge_width: { left:0, right:0},boat_curve_height: { left:67.7613, right:69.0161},ankle_height: { left:100, right:101},kyphosis_height: { left:15.2214, right:15.5587},back_arch_gap_54: { left:11.6539, right:10.2659},back_arch_gap_70: { left:14.7493, right:14.0128},back_arch_gap_85: { left:17.1005, right:16.8112},back_arch_gap_90: { left:17.4666, right:17.3159},angle_between_heel_line_and_axis_line: { left:3.23168, right:5.37698},foot_inoutside_turn: { left:0, right:0},foot_inoutside_rotate: { left:0, right:1499314590},first_metatarsophalangeal_length: { left:236.993, right:234.852},second_metatarsophalangeal_length: { left:233.255, right:233.321},third_metatarsophalangeal_length: { left:223.355, right:221.763},shoe_type: { left:0, right:0.5}}||YB20170706121630Ot1ROCRPRqapfk3U||{LEFT_FOOT_TYPE:2,LEFT_FOOT_INOUTSIDE_TURN:0,LEFT_FOOT_INOUTSIDE_ROTATE:0,RIGHT_FOOT_TYPE:3,RIGHT_FOOT_INOUTSIDE_TURN:0,RIGHT_FOOT_INOUTSIDE_ROTATE:1499314590}"));
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                StatusLine statusLine = response.getStatusLine();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("Response content: " + response);
                    System.out.println("--------------------------------------");
                }
                if (statusLine != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response statusLine: " + statusLine);
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
