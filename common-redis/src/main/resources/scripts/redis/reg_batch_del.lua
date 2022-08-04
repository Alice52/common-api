-- TODO: counter
local keys = {};
local cursor = "0"

local result = redis.call("SCAN", ARGV[1], "match", ARGV[2], "count", ARGV[3])
cursor = result[1];
keys = result[2];
for i, key in ipairs(keys) do
    redis.call("DEL", key);
end

return cursor;