package top.hubby.test.custom.redis.constants;

/**
 * @author zack <br>
 * @create 2022-05-23 15:10 <br>
 * @project project-cloud-custom <br>
 */
public interface RedisStreamConstants {

    String DEFAULT_MESSAGE_KEY = "msg-key";

    /*
    Testing queue relative
    */
    String TESTING_STREAM_KEY = "testing_stream_key";
    String TESTING_STREAM_CONSUMER_GROUP = "NewMemberFollowerPointGroup";
    String TESTING_STREAM_CONSUMER_GROUP_CONSUMER_NAME = "NewMemberFollowerConsumerName";
}
