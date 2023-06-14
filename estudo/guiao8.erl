-module(guiao8).
-export([start/0, create_account/2, close_account/2, login/2]).

start() ->
    register(guiao8, spawn(fun() -> loop([]) end)).

create_account(Username, Password) ->
    guiao8 ! {create_account, Username, Password, self()},
    receive
        Result -> Result
    end.
close_account(Username, Password) ->
    guiao8 ! {close_account, Username, Password, self()},
    receive Result -> Result end.

login(Username, Password) ->
    guiao8 ! {login, Username, Password, self()},
    receive Result -> Result end.


loop(DB) ->
    receive
        {create_account, Username, Password, From} ->
            case create_account_aux(DB, Username, Password) of
                user_exists ->
                    From ! user_exists,
                    loop(DB);
                NewDB ->
                    From ! ok,
                    loop(NewDB)
            end;
        {close_account, Username, Password, From} ->
            case close_account_aux(DB, Username, Password) of
                invalid -> 
                    From ! invalid,
                    loop(DB);
                NewDB ->
                    From ! ok,
                    loop(NewDB)
            end;

        {login, Username, Password, From} ->
            case login_aux(DB, Username, Password) of
                invalid->
                    From ! invalid,
                    loop(DB);
                NewDB ->
                    From ! ok,
                    loop(NewDB)
            end
    end.

create_account_aux([], Username, Password) -> [{Username, Password, false}];
create_account_aux([{U, P, S} | T], Username, Password) ->
    if
        Username =:= U, Password =:= P ->
            user_exists;
        true ->
            [{U, P, S} | create_account_aux(T, Username, Password)]
    end.

close_account_aux([], _, _) -> invalid;
close_account_aux([{U,P,S}| T], Username, Password) ->
    if Username=:=U, Password=:=P -> [T];
    true -> 
        case close_account_aux(T, Username, Password) of
            invalid -> invalid;
            L -> [{U,P,S} | L]
        end
    end.

login_aux([],_, _) -> invalid;
login_aux([{U,P,S}|T], Username, Password) ->
    if Username=:=U, Password=:=P->[{U,P,true}|T];
    true -> 
        case login_aux(T, Username, Password) of
            invalid -> invalid;
            DB -> 
                [{U,P,S}|DB]
        end
    end.
