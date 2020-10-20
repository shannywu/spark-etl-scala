namespace java types

struct TIpDetail {
    1: i32 asnum,
    2: string country,
    3: string host
}

struct TRequestDetail {
    1: string method,
    2: string scheme,
    3: string host,
    4: i32 port,
    5: string path,
    6: map<string,string> query,
    7: string protocol
}

struct TAccessLog {
    1: string remote_ip,
    2: i64 received_at,
    3: string request,
    4: i32 status,
    5: i32 size,
    6: i32 duration,
    7: string referer,
    8: string user_agent,
    9: post_query_detail,
    11: TRequestDetail request_detail,
    12: TIpDetail remote_ip_detail
}
