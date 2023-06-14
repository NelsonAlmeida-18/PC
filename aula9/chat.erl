-module(chat).
-export([start/1, stop/1]).

start(Port) -> spawn(fun() -> server(Port) end).
stop(Server) -> Server ! stop.

server(Port)->
  {ok, LSock} = gen_tcp:listen(Port, [{packet, line}, {reuseaddr, true}]),
  Room = spawn(fun() -> room([]) end),
  RM = spawn(fun() -> room_manager(#{}) end),
  spawn(fun() -> acceptor(LSock, Room) end),
  receive stop ->ok end.

acceptor(LSock, Room) -> 
  {ok, Sock} = gen_tcp:accept(LSock),
  spawn(fun() -> acceptor(LSock, Room) end),
  Room ! {enter , self()},
  user(Sock, Room).

room(Pids) -> 
  receive
    {enter, Pid} ->
      io:format("User_joined~n", []),
      room([Pid|Pids]);
    {line, Data} = Msg -> 
      io:format("Received_~p_~n", [Data]),
      [Pid ! Msg || Pid <- Pids],
      room(Pids);
    {leave, Pid} -> 
      io:format("User_left~n", []),
      room(Pids -- [Pid])
    end.

user(Sock,Room)->
  receive
    {line, Data}->
      gen_tcp:send(Sock, Data),
      user(Sock, Room);
    {tcp, _ , Data}->
      Room ! {line, Data},
      user(Sock,Room);
    {tcp_closed, _} ->
      Room ! {leave, self()};
    {tcp_error, _,_}->
      Room ! {leave, self()}
  end.

get_room(RM, Name) ->
  
  
room_manager(Rooms)->
  receive
    {["/room "|Name]} ->
      case (maps:get(Name, Rooms)) of
           {ok, Pid} -> Pid;
           {error} -> 
                      Pid = spawn(fun() -> room([]) end),
                      maps:put(Name, Pid, Rooms),
                      Pid;
      end
    room_manager(Rooms);
  end.
