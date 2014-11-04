% A simple Prolog test file:

father(ken,mark).
father(ken,ron).
father(ron,anthony).

grandfather(X,Z) :-
    father(X,Y), father(Y,Z).
