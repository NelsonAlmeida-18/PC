-module(guiao7).
-export([create/0, enqueue/2, dequeue/1, createPQ/0, enqueuePQ/3, dequeuePQ/1]).

create() -> [].

enqueue([], A) -> [A];
enqueue([H|T], A) -> [H | enqueue(T,A)] .

dequeue([]) -> empty;
dequeue([H|_]) -> [H].

createPQ() -> [].

enqueuePQ([], I, P) -> [{I,P}];
enqueuePQ([{I1, P1} | T], I, P) ->
    if P1 > P -> [{I,P} | [{I1, P1} | T]];
       true -> [{I1, P1} | enqueuePQ(T, I, P)]
    end.

dequeuePQ([]) -> empty;
dequeuePQ([_ | T]) -> [T].
