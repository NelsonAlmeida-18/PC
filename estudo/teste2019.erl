-module(teste2019).
-export([start/0]).

start() ->
    spawn(fun() -> jogo([]) end).

jogo(Players) ->
    receive
        {participa, PID} ->
            case length(Players) of
                3 ->
                    spawn(fun() -> init_partida([Players | [PID]]) end),
                    jogo([]);
                _ ->
                    jogo(Players ++ [PID])
            end
    end.

init_partida(Players) ->
    Secret = rand:uniform(100),
    NumTentativas = 0,
    Tempo = false,
    Game_Won = false,
    Partida = spawn(fun() -> partida(Secret, NumTentativas, Tempo, Game_Won) end),
    sendPartidaToPlayers(Players, Partida),
    timer(Partida).

timer(PID) ->
    timer:sleep(60000),
    PID ! tempo.

partida(Secret, NumTentativas, Tempo, Game_Won) ->
    receive
        {adivinha, Valor, PID} ->
            case NumTentativas of
                99 ->
                    PID ! tentativas,
                    partida(Secret, NumTentativas, Tempo, Game_Won);
                _ ->
                    case Tempo of
                        true ->
                            PID ! tempo,
                            partida(Secret, NumTentativas, Tempo, Game_Won);
                        _ ->
                            case Game_Won of
                                true ->
                                    PID ! perdeu,
                                    partida(Secret, NumTentativas, Tempo, Game_Won);
                                _ ->
                                    case Valor of
                                        Secret ->
                                            PID ! ganhou,
                                            partida(Secret, NumTentativas, Tempo, true);
                                        _ ->
                                            case Valor < Secret of
                                                true ->
                                                    PID ! maior,
                                                    partida(Secret, NumTentativas + 1, Tempo, Game_Won);
                                                _ ->
                                                    PID ! menor,
                                                    partida(Secret, NumTentativas + 1, Tempo, Game_Won)
                                            end
                                    end
                            end
                    end
            end;
        tempo ->
            partida(Secret, NumTentativas, true, Game_Won)
    end.

sendPartidaToPlayers([], _Pid) -> ok;
sendPartidaToPlayers([P | T], Pid) ->
    P ! Pid,
    sendPartidaToPlayers(T, Pid).
