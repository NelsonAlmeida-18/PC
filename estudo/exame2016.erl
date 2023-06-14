-module(exame2016).
-export([start/0, vota/1, espera/3, mostraCandidatos/0]).

start() -> register(?MODULE,  spawn(fun() -> eleicao([]) end)).

vota(Candidato) ->
    ?MODULE ! {vota, Candidato}.

espera(C1, C2, C3) ->
    ?MODULE ! {espera, C1, C2, C3}.

mostraCandidatos() ->
    ?MODULE ! {mostra_candidatos}.

eleicao(Urna) ->
    receive
        {vota, Candidato} -> 
            Res = vota_aux(Urna, Candidato),
            eleicao(Res);
        {espera, Candidato1, Candidato2, Candidato3} ->
            case sequencia(Candidato1, Candidato2, Candidato3, Urna) of
                true -> 
                    io:format("Sequencia alcancada~n"),
                    eleicao(Urna);
                wait -> eleicao(Urna)
            end;
        {mostra_candidatos} ->
            mostraCandidatos(Urna)
    end.

vota_aux([], Candidato) -> [{Candidato, 1}];
vota_aux([{C, V} | T], Candidato) ->
    case C =:= Candidato of
        true  -> [{C, V + 1} | T];
        false -> [{C, V} | vota_aux(T, Candidato)]
    end.

sequencia(_, _, _, []) -> wait;
sequencia(C1, C2, C3, Candidatos) ->
    VotosC1 = votos(C1, Candidatos),
    VotosC2 = votos(C2, Candidatos),
    VotosC3 = votos(C3, Candidatos),
    if
        VotosC3 > VotosC1, VotosC3 > VotosC2 -> true;
        true -> wait
    end.

votos(_, []) -> -1;
votos(C, [{Candidato, V} | T]) ->
    case C =:= Candidato of
        true  -> V;
        false -> votos(C, T)
    end.

mostraCandidatos([]) -> ok;
mostraCandidatos([{C,V}|T]) ->
    io:format("Candidato: ~p Votos: ~p~n", [C,V]),
    mostraCandidatos(T).
