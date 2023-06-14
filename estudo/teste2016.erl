-module(teste2016).
-export([start/0, sinaliza/1, espera/4]).

start() ->
    register(?MODULE, spawn(fun() -> esperador(0, 0, 0, 0) end)).

esperador(Processo1, Sinal1, Processo2, Sinal2) ->
    receive
        {sinal, Tipo} ->
            if Tipo =:= Processo1 -> esperador(Processo1, Sinal1+1, Processo2, Sinal2);
               Tipo =:= Processo2 -> esperador(Processo1, Sinal1, Processo2, Sinal2+1);
               true -> esperador(Processo1, Sinal1, Processo2, Sinal2)
            end;
        {espera, Tipo1, N1, Tipo2, N2} ->
            if Sinal1 >= N1, Sinal2 >= N2 ->
                esperador(Processo1, Sinal1-N1, Processo2, Sinal2-N2);
               true -> esperador(Processo1, Sinal1, Processo2, Sinal2)
            end
    end.

sinaliza(Tipo) ->
    ?MODULE ! {sinal, Tipo}.

espera(Tipo1, N1, Tipo2, N2) ->
    ?MODULE ! {espera, Tipo1, N1, Tipo2, N2}.
