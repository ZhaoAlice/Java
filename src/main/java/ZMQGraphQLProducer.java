//import java.nio.charset.StandardCharsets;
//import java.util.Properties;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.alibaba.fastjson.JSONObject;
//import com.ztesoft.mq.client.api.MQClientFactory;
//import com.ztesoft.mq.client.api.common.PropertyKey;
//import com.ztesoft.mq.client.api.common.exception.MQClientApiException;
//import com.ztesoft.mq.client.api.model.MQMessage;
//import com.ztesoft.mq.client.api.model.ProduceResult;
//import com.ztesoft.mq.client.api.model.ProduceResultStatus;
//import com.ztesoft.mq.client.api.producer.Producer;
//import com.ztesoft.mq.client.impl.MQClientFactoryImpl;
//import com.ztesoft.zmq.domain.ManExpertDiagnosisResult;
//
//public class ZMQGraphQLProducer {
//
//    private static final Logger logger = LoggerFactory.getLogger(ZMQGraphQLProducer.class);
//
//    public static String sendOrderMessage() throws MQClientApiException {
//        Producer producer = null;
//        try {
//            // 创建生产者工厂
//            MQClientFactory factory = new MQClientFactoryImpl();
//            // 生产者属性信息
//            Properties properties = new Properties();
//            properties.put(PropertyKey.Producer_Id, "diagnosis-test");
//            properties.put(PropertyKey.Producer_Send_Timeout, "10000000");
//            properties.put(PropertyKey.Namesrv_Addr, "10.10.180.59:9876;10.10.180.85:9876");
//
//            //// 创建生产者
//            producer = factory.createProducer(properties);
//            //// 启动生产者
//            producer.start();
//
//            ManExpertDiagnosisResult result1 = new ManExpertDiagnosisResult();
//            result1.setResultId(1232123454556L);
//            result1.setVersion(1);
//
//            MQMessage mqMessage = new MQMessage("NORMAL", null,
//                result1.getResultId().toString(), JSONObject.toJSONString(result1).getBytes(StandardCharsets.UTF_8));
//            mqMessage.setDelayTimeLevel(2);
//            ProduceResult result = producer.send(mqMessage);
//            if (result == null || !ProduceResultStatus.SEND_OK.equals(result.getStatus())) {
//                logger.error("send mq update table error, content:{}, send result:{}",
//                    JSONObject.toJSONString(result1), JSONObject.toJSONString(result));
//                throw new RuntimeException();
//            }
//            return result.getMessageId();
//        } catch (MQClientApiException e) {
//            logger.error("mq producer error: {}", e);
//        }
//        finally {
//            if (producer != null) {
//                producer.shutdown();
//            }
//        }
//        return null;
//}
//
//    public static void main(String[] args) throws MQClientApiException {
//        sendOrderMessage();
//    }
//}
