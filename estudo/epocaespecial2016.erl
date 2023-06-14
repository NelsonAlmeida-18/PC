-module(epocaespecial2016).
-export([start/1, inicioTravessiaIda/0, inicioTravessiaVolta/0, fimTravessia/0]).

start(MaxWeight) -> 
    register(?MODULE, spawn(fun() -> ponteManager([], [], MaxWeight) end)),
    register(gestor, spawn(fun() -> gestorEsperas([], []) end)).

inicioTravessiaIda() -> gestor ! {ida, self()}.
inicioTravessiaVolta() -> gestor ! {volta, self()}.
fimTravessia() -> gestor ! {fim, self()}.

gestorEsperas(WaitingIr, WaitingVir) ->
    {WaitingIrNew, WaitingVirNew} = sender(WaitingIr, WaitingVir),
    gestorEsperas(WaitingIrNew, WaitingVirNew).


sender([],[]) -> {[],[]};
sender([C|T], []) ->
    case ?MODULE ! {ida, C} of 
        wait -> {[C|T], []}
    end;
sender(WaitingIr, [C|T]) ->
    case ?MODULE ! {volta, C} of 
        wait -> {WaitingIr, [C|T]}
    end.

ponteManager(VisitantesIda, VisitantesVolta, MaxWeight) ->
    receive 
        {ida, PID} -> 
            Size = length(VisitantesIda) + length(VisitantesVolta),
            if  
                Size < MaxWeight ->
                    ponteManager([PID | VisitantesIda], VisitantesVolta, MaxWeight);
                true -> wait
            end;
        {volta, PID} ->
            Size = length(VisitantesIda) + length(VisitantesVolta),
            if 
                Size < MaxWeight ->
                    ponteManager(VisitantesIda, [PID | VisitantesVolta], MaxWeight);
                true -> wait
            end;
        {fim, PID} ->
            ponteManager(VisitantesIda, lists:delete(PID, VisitantesVolta), MaxWeight)
    end.
