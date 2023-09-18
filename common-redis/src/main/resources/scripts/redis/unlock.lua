-- there bugs about biz timeout, but this lock is re-entry
local key = KEYS[1];
local threadId = ARGV[1];

if (redis.call("hexists", key, threadId) == 0) then
    return nil;
end ;

local count = redis.call("hincrby", key, threadId, -1);
if (count == 0) then
    redis.call('DEL', key);
    return nil;
end ;
