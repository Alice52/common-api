local function isempty(s)
    return s == nil or s == ''
end

if (redis.call("exists", ARGV[1]) == 1) then
    local stock = tonumber(redis.call('get', ARGV[1]));
    local delta;
    -- ARGV[2] 为空时才执行
    if isempty(ARGV[2]) then
        delta = -1;
    else
        delta = ARGV[2];
    end ;

    if (stock > 0) then
        redis.call('incrby', ARGV[1], delta)
        return true;
    end ;

    return false;
else
    return false;
end ;