-module(myqueue).
-export([create/0, enqueue/2, dequeue/1, reverse/1, reverseAux/2]).

reverse([])->[];
reverse(L)->reverseAux(L,[]).
%reverse([Head | Tail]) -> [reverse(Tail) | Head]. 
reverseAux([],Acc)->Acc;
reverseAux([H|T], Acc)-> reverseAux(T, [H|Acc]).

create() -> [].

enqueue({In, Out}, Item) -> {[Item|In], Out}.

dequeue({[],[]}) -> empty;
dequeue({In, [Item | T]}) -> {{In,T}, Item};
dequeue({In, []}) -> dequeue({[], reverse(In)}).
