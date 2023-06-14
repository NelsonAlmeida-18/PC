-module(guiao9).
-export([start/1]).

start(Port) -> register(guiao9, spawn(fun() -> server(Port) end)).

server(Port) -> 
    {ok, LSock} = gen_tcp:listen(Port, [binary, {packet, line}, {reuseaddr, true}]),
    RoomPid = spawn(fun() -> rooms([]) end),
    spawn(fun() -> acceptor(LSock, RoomPid) end),
    receive stop -> ok end.

acceptor(LSock, RoomPid) ->
    {ok, ASock} = gen_tcp:accept(LSock),
    spawn(fun() -> acceptor(LSock, RoomPid) end),
    %RoomPid ! {enter, self()},
    handler(ASock, RoomPid) ! {enter, self()}.

handler(ASock, RoomPid) ->
    receive 
        {enter, Pid} ->
            RoomPid ! {join_room, Pid},
            io:format("User has joined a channel~n"),
            handler(ASock, RoomPid)
    end.

rooms(Salas) ->
    receive 
        {message, Message, RoomNumber} ->
            rooms(message_room(Salas, Message, RoomNumber));
        {join_room, Pid} ->
            rooms(join_room(Salas, Pid))
    end.

join_room([], Pid) -> [{0, [Pid]}];
join_room([{RN, Room}|T], Pid) -> [{RN, [Pid|Room]}|T].

message_room([], _, _) -> no_rooms;
message_room([{RN, Room}|T], Message, RoomNumber) ->
    case RN =:= RoomNumber of
        true -> send_message(Room, Message);
        false -> message_room(T, Message, RoomNumber)
    end.

send_message([], _) -> ok;
send_message([Pid|Pids], Message) ->
    Pid ! Message,
    send_message(Pids, Message).
