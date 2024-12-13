package vttp.batch5.ssf.noticeboard.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class NoticeRepository {

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	// TODO: Task 4
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	// 
	/*
	 * Write the redis-cli command that you use in this method in the comment. 
	 * For example if this method deletes a field from a hash, then write the following
	 * redis-cli command 
	 * 	hdel myhashmap a_key
	 *
	 * 
	 * 	set id respJson
	 *
	 */
	public void insertNotices(String id, JsonObject respJson) {
		redisTemplate.opsForValue().set(id, respJson.toString());
	}



	/*
	 * 
	 * 
	 * 
	 * randomkey
	 * 
	 */
	public void getRandomKey() {
		redisTemplate.randomKey();
	}


}
