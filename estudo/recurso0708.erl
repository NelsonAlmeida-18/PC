-module(recurso0708).
-export([start/2, obtem_posto/1, fim_atendimento/1]).

start(NumServicos, NumPostos) ->
    register(escalonador, spawn(fun() -> escalonador(NumServicos, NumPostos, [], []) end)).

obtem_posto(Servico) ->
    escalonador ! {obtem_posto, Servico, self()},
    receive
        {ok, Posto} ->
            io:fwrite("Posto obtido ~p~n", [Posto])
    end.

fim_atendimento(Posto) ->
    escalonador ! {fim_atendimento, Posto}.

escalonador(NumServicos, NumPostos, PostosOcupados, FilaEspera) ->
    receive
        {obtem_posto, Servico, Pid} ->
            {PostosOcupadosUpdated, FilaEsperaUpdated} = obtemPosto(NumPostos, PostosOcupados, FilaEspera, Pid),
            escalonador(NumServicos, NumPostos, PostosOcupadosUpdated, FilaEsperaUpdated);

        {fim_atendimento, Posto} ->
            {PostosOcupadosUpdated, FilaEsperaUpdated} = fimAtendimento(Posto, PostosOcupados, FilaEspera),
            escalonador(NumServicos, NumPostos, PostosOcupadosUpdated, FilaEsperaUpdated)
    end.

obtemPosto(NumPostos, PostosOcupados, FilaEspera, Pid) ->
    case length(PostosOcupados) < NumPostos of
        true ->
            {PostosOcupados ++ [{Pid, Pid}], FilaEspera};
        false ->
            {PostosOcupados, FilaEspera ++ [{Pid, Pid}]}
    end.

fimAtendimento(Posto, PostosOcupados, FilaEspera) ->
    PostosOcupadosUpdated = lists:keydelete(Posto, 1, PostosOcupados),
    {FilaEsperaUpdated, Atendido} = case FilaEspera of
        [] ->
            {FilaEspera, undefined};
        [Head | Rest] ->
            {Rest, Head}
    end,
    case Atendido of
        undefined ->
            {PostosOcupadosUpdated, FilaEsperaUpdated};
        {Pid, Servico} ->
            {PostosOcupadosUpdated ++ [{Pid, Servico}], FilaEsperaUpdated}
    end.
